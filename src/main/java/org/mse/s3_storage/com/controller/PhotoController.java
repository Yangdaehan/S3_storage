package org.mse.s3_storage.com.controller;

import java.io.IOException;
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
public class PhotoController {

    private final AmazonS3Service s3Service;

    public PhotoController(AmazonS3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> profilePhotoUploadAndUpdate(
            @RequestPart("profile_photo") MultipartFile multipartFile,
            @RequestPart("memberRequest") @Valid MemberRequest memberRequest) {
        try {
            String memberId = memberRequest.getMemberId();
            final String profilePhotoUrl = s3Service.uploadPhoto(memberId, null, multipartFile);
            return ResponseEntity.ok().body("Photo uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo: " + e.getMessage());
        }
    }

    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> profilePhotosUploadAndUpdate(
            @RequestPart("memeberRequest") @Valid MemberRequest memberRequest,
            @RequestPart("profile_photos") List<MultipartFile> files) {
        try {
            String memberId = memberRequest.getMemberId();
            List<String> profilePhotoUrls = new ArrayList<>(files.size());
            for (MultipartFile file : files) {
                final String profilePhotoUrl = s3Service.uploadPhoto(memberId, null, file);
                profilePhotoUrls.add(profilePhotoUrl);
            }
            return ResponseEntity.ok().body(Collections.singletonList("Photos uploaded successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error uploading photos: " + e.getMessage()));
        }
    }

    @PostMapping("/photo-sub")
    public ResponseEntity<String> profilePhotoUploadAndUpdate(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @Valid @RequestPart("profile_photo") MultipartFile multipartFile) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = subfolderRequest.getSubfolderName();
            final String profilePhotoUrl = s3Service.uploadPhoto(memberId, subfolderName, multipartFile);
            return ResponseEntity.ok().body("Photo uploaded successfully to subfolder: " );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading photo to subfolder: " + e.getMessage());
        }
    }

    @PostMapping("/photos-sub")
    public ResponseEntity<List<String>> profilePhotosUploadAndUpdate(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @Valid @RequestPart("profile_photo") List<MultipartFile> files) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = subfolderRequest.getSubfolderName();
            List<String> profilePhotoUrls = new ArrayList<>(files.size());
            for (MultipartFile file : files) {
                final String profilePhotoUrl = s3Service.uploadPhoto(memberId, subfolderName, file);
                profilePhotoUrls.add(profilePhotoUrl);
            }
            return ResponseEntity.ok().body(
                    Collections.singletonList("Photos uploaded successfully to subfolder"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error uploading photos to subfolder: " + e.getMessage()));
        }
    }

    @GetMapping("/xlsx_download")
    public ResponseEntity<String> xlsx_download(
            @RequestPart("fileName") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest) {
        try {
            String memberId = memberRequest.getMemberId();
            System.out.println(memberId);
            System.out.println(storedFileName);
            String url = s3Service.getPresignedUrl(memberId, null, storedFileName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating presigned URL: " + e.getMessage());
        }
    }

    @GetMapping("/xlsx_download-sub")
    public ResponseEntity<String> xlsx_download(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @RequestPart("fileName") String storedFileName) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = subfolderRequest.getSubfolderName();
            String url = s3Service.getPresignedUrl(memberId, subfolderName, storedFileName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating presigned URL: " + e.getMessage());
        }
    }

    @GetMapping("/image_download")
    public ResponseEntity<byte[]> photoDownload(
            @RequestPart("fileName") String storedFileName,
            @RequestPart("memberRequest") MemberRequest memberRequest) {
        try {
            String memberId = memberRequest.getMemberId();
            return s3Service.getObject(memberId, null, storedFileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/image_download-sub")
    public ResponseEntity<byte[]> photoDownload(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest,
            @RequestPart("fileName") String storedFileName) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = subfolderRequest.getSubfolderName();
            return s3Service.getObject(memberId, subfolderName, storedFileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list")
    public List<String> list() {
        return s3Service.listFolders();
    }

    @GetMapping("/memberList")
    public ResponseEntity<List<String>> listFiles(
            @RequestPart("memberRequest") MemberRequest memberRequest) {
        try {
            String memberId = memberRequest.getMemberId();
            List<String> files = s3Service.listFiles(memberId);
            return ResponseEntity.ok().body(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error listing files: " + e.getMessage()));
        }
    }

    @GetMapping("/subList")
    public ResponseEntity<List<String>> listFilesInSubfolder(
            @RequestPart("memberRequest") MemberRequest memberRequest,
            @RequestPart("subfolderRequest") SubfolderRequest subfolderRequest) {
        try {
            String memberId = memberRequest.getMemberId();
            String subfolderName = subfolderRequest.getSubfolderName();
            String path = memberId + "/" + subfolderName;
            List<String> files = s3Service.listFiles(path);
            return ResponseEntity.ok().body(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error listing files in subfolder: " + e.getMessage()));
        }
    }

    @DeleteMapping("/file_delete")
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


}
