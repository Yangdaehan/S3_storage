package org.mse.s3_storage.com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import org.mse.s3_storage.com.dto.MemberRequest;
import org.mse.s3_storage.com.dto.SubfolderRequest;
import org.mse.s3_storage.com.service.AmazonS3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class S3Controller {

    private final AmazonS3Service s3Service;

    public S3Controller(AmazonS3Service s3Service) {
        this.s3Service = s3Service;
    }


    // 파일 업로드
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile multipartFile,
            @RequestPart("memberRequest") @Valid MemberRequest memberRequest,

            //하위 폴더가 제공될 경우 하위 폴더에 파일을 업로드
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;
            final String profilePhotoUrl = s3Service.uploadFile(memberId, subfolderName, multipartFile);
            return ResponseEntity.ok().body("File uploaded successfully" + (subfolderName != null ? " to subfolder: " + subfolderName : ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo: " + e.getMessage());
        }
    }

    @PostMapping(value = "/uploadFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadFiles(
            @RequestPart("memberRequest") @Valid MemberRequest memberRequest,
            @RequestPart("file") List<MultipartFile> files,

            //하위 폴더가 제공될 경우 하위 폴더에 파일을 업로드
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;
            List<String> profilePhotoUrls = new ArrayList<>(files.size());
            for (MultipartFile file : files) {
                final String profilePhotoUrl = s3Service.uploadFile(memberId, subfolderName, file);
                profilePhotoUrls.add(profilePhotoUrl);
            }
            String successMessage = "Photos uploaded successfully" + (subfolderName != null ? " to subfolder: " + subfolderName : "");
            return ResponseEntity.ok().body(Collections.singletonList(successMessage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error uploading photos: " + e.getMessage()));
        }
    }


    //파일 다운로드
    @GetMapping("/downloadFile")
    public ResponseEntity<String> downloadFile(
            @RequestPart("fileName") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest,

            //하위 폴더가 제공될 경우 하위 폴더에 있는 파일을 다운로드
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;
            String url = s3Service.getPresignedUrl(memberId, subfolderName, storedFileName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating presigned URL: " + e.getMessage());
        }
    }

    @GetMapping("/downloadBinaryFile")
    public ResponseEntity<byte[]> downloadBinaryFile(
            @RequestPart("fileName") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest,

            //하위 폴더가 제공될 경우 하위 폴더에 있는 파일을 다운로드
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;
            return s3Service.getObject(memberId, subfolderName, storedFileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //조회
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles(
            @RequestPart(value = "memberRequest", required = false) MemberRequest memberRequest,
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            if (memberRequest == null) {
                // memberRequest가 없으면 전체 폴더 목록을 반환
                List<String> folders = s3Service.listFolders();
                return ResponseEntity.ok().body(folders);
            } else {
                String memberId = memberRequest.getMemberId();
                if (subfolderRequest == null) {
                    // subfolderRequest가 없으면 멤버 폴더의 파일 목록을 반환
                    List<String> files = s3Service.listFiles(memberId);
                    return ResponseEntity.ok().body(files);
                } else {
                    // subfolderRequest가 있으면 하위 폴더의 파일 목록을 반환
                    String subfolderName = subfolderRequest.getSubfolderName();
                    String path = memberId + "/" + subfolderName;
                    List<String> files = s3Service.listFiles(path);
                    return ResponseEntity.ok().body(files);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error listing files: " + e.getMessage()));
        }
    }


    //파일 삭제
    @DeleteMapping("/deleteFile")
    public ResponseEntity<String> deleteFile(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest,
            @RequestPart("fileName") String fileName
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;
            s3Service.deleteFile(memberId, subfolderName, fileName);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteFolder")
    public ResponseEntity<String> deleteFolder(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart(value = "subfolderRequest", required = false) SubfolderRequest subfolderRequest
    ) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = (subfolderRequest != null) ? subfolderRequest.getSubfolderName() : null;

            s3Service.deleteFolder(memberId, subfolderName);

            return ResponseEntity.ok("Folder and its contents deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting folder: " + e.getMessage());
        }
    }


}
