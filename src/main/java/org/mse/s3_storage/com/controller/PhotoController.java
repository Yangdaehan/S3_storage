package org.mse.s3_storage.com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.mse.s3_storage.com.service.AmazonS3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PhotoController {

    private final AmazonS3Service s3Service;

    public PhotoController(AmazonS3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/{memberId}/photo")
    public ResponseEntity<String> profilePhotoUploadAndUpdate(
            // memberId 값 가져오기
            @PathVariable Long memberId,
            @Valid @RequestPart("profile_photo") MultipartFile multipartFile) {
        final String profilePhotoUrl = s3Service.uploadPhoto(memberId, multipartFile);

        return ResponseEntity.ok().body(profilePhotoUrl);
    }

    @PostMapping("/{memberId}/photos")
    public ResponseEntity<List<String>> profilePhotosUploadAndUpdate(
            @PathVariable Long memberId,
            @RequestPart("profile_photos") List<MultipartFile> files) {
        List<String> profilePhotoUrls = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            final String profilePhotoUrl = s3Service.uploadPhoto(memberId, file);
            profilePhotoUrls.add(profilePhotoUrl);
        }
        return ResponseEntity.ok().body(profilePhotoUrls);
    }


    // 엑셀 다운 받기
    @GetMapping("/{memberId}/{storedFileName}/xlsx_download")
    public String xlsx_download(
            @PathVariable String storedFileName,
            @PathVariable Long memberId
    ) throws IOException {

        return s3Service.getPresignedUrl(memberId, storedFileName);
    }

    // 사진 바이너리 파일로 전송
    @GetMapping("/{memberId}/{storedFileName}/image_download")
    public ResponseEntity<byte[]> photoDownload(
            @PathVariable String storedFileName,
            @PathVariable Long memberId
    ) throws IOException {

        return s3Service.getObject(memberId, storedFileName);
    }

    // 리스트로 반환
    @GetMapping("/list")
    public List<String> list() {
        return s3Service.listFolders();
    }


    // 특정 멤버의 파일 목록 반환
    @GetMapping("/{memberId}/list")
    public ResponseEntity<List<String>> listFiles(@PathVariable Long memberId) {
        List<String> files = s3Service.listFiles(memberId);
        return ResponseEntity.ok().body(files);
    }
}
