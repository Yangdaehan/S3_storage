package org.mse.s3_storage.com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import org.mse.s3_storage.com.dto.MemberRequest;
import org.mse.s3_storage.com.dto.SubfolderRequest;
import org.mse.s3_storage.com.service.AmazonS3Service;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PhotoController {

    private final AmazonS3Service s3Service;

    public PhotoController(AmazonS3Service s3Service) {
        this.s3Service = s3Service;
    }


    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> profilePhotoUploadAndUpdate(
            @RequestPart("profile_photo") MultipartFile multipartFile,
            @RequestPart("memberRequest") @Valid MemberRequest memberRequest) {

        String memberId = memberRequest.getMemberId();
        final String profilePhotoUrl = s3Service.uploadPhoto(memberId, null, multipartFile);

        return ResponseEntity.ok().body("Photo uploaded successfully: ");
    }

    // memberId 폴더에 여러 파일 저장
    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> profilePhotosUploadAndUpdate(
            @RequestPart("memeberRequest") @Valid MemberRequest memberRequest,
            @RequestPart("profile_photos")
            List<MultipartFile> files) {

        String memberId = memberRequest.getMemberId();
        List<String> profilePhotoUrls = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            final String profilePhotoUrl = s3Service.uploadPhoto(memberId, null, file);
            profilePhotoUrls.add(profilePhotoUrl);
        }
        return ResponseEntity.ok().body(Collections.singletonList("Photos uploaded successfully"));
    }

    // memberId 폴더에 하위 폴더 생성 후 파일 저장
    @PostMapping("/photo-sub")
    public ResponseEntity<String> profilePhotoUploadAndUpdate(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @Valid @RequestPart("profile_photo") MultipartFile multipartFile
    ) {
        String memberId = memberRequest.getMemberId();
        String subfolderName = subfolderRequest.getSubfolderName();
        final String profilePhotoUrl = s3Service.uploadPhoto(memberId, subfolderName, multipartFile);
        return ResponseEntity.ok().body("Photo uploaded successfully to subfolder");
    }

    // memberId 폴더에 하위 폴더 생성 후 파일 저장
    @PostMapping("/photos-sub")
    public ResponseEntity<List<String>> profilePhotosUploadAndUpdate(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @Valid @RequestPart("profile_photo") List<MultipartFile> files) {

        String memberId = memberRequest.getMemberId();
        String subfolderName = subfolderRequest.getSubfolderName();
        List<String> profilePhotoUrls = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            final String profilePhotoUrl = s3Service.uploadPhoto(memberId, subfolderName, file);
            profilePhotoUrls.add(profilePhotoUrl);
        }
        return ResponseEntity.ok().body(
                Collections.singletonList("Photos uploaded successfully to subfolder"));
    }

    // memberId 폴더에 있는 엑셀 다운 받기
    @GetMapping("/xlsx_download")
    public String xlsx_download(
            @RequestPart("Filename") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest
    ) throws IOException {
        String memberId = memberRequest.getMemberId();
        System.out.println(memberId);
        System.out.println(storedFileName);
        return s3Service.getPresignedUrl(memberId, null, storedFileName);
    }

    // memberId 폴더 내 하위 폴더에 있는 엑셀 다운 받기
    @GetMapping("/xlsx_download-sub")
    public String xlsx_download(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @RequestPart("Filename") String storedFileName
    ) throws IOException {

        String memberId = memberRequest.getMemberId();
        String subfolderName = subfolderRequest.getSubfolderName();
        return s3Service.getPresignedUrl(memberId, subfolderName, storedFileName);
    }

    // memberId 폴더에 있는 파일을 바이너리 파일로 전송
    @GetMapping("/image_download")
    public ResponseEntity<byte[]> photoDownload(
            @RequestPart("Filename") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest
    ) throws IOException {

        String memberId = memberRequest.getMemberId();
        return s3Service.getObject(memberId, null, storedFileName);
    }

    // memberId 폴더 내부에 하위 폴더 안에 있는 파일을 바이너리 파일로 전송
    @GetMapping("/image_download-sub")
    public ResponseEntity<byte[]> photoDownload(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @RequestPart("Filename") String storedFileName
    ) throws IOException {
        String memberId = memberRequest.getMemberId();
        String subfolderName = subfolderRequest.getSubfolderName();
        return s3Service.getObject(memberId, subfolderName, storedFileName);
    }

    @GetMapping("/list")
    public List<String> list() {
        return s3Service.listFolders();
    }

    @GetMapping("/memberList")
    public ResponseEntity<List<String>> listFiles(
            @RequestPart("memberRequest") MemberRequest memberRequest
    ) {
        String memberId = memberRequest.getMemberId();
        List<String> files = s3Service.listFiles(memberId);
        return ResponseEntity.ok().body(files);
    }

    @GetMapping("/subList")
    public ResponseEntity<List<String>> listFilesInSubfolder(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest
    ) {
        String memberId = memberRequest.getMemberId();
        String subfolderName = subfolderRequest.getSubfolderName();
        String path = memberId + "/" + subfolderName;
        List<String> files = s3Service.listFiles(path);
        return ResponseEntity.ok().body(files);
    }


}
