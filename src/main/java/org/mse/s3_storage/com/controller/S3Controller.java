package org.mse.s3_storage.com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "파일 업로드 api", notes = "파일을 업로드하는 api입니다. 하위폴더가 제공될 경우 해당 하위폴더를 만든 후 파일을 업로드 합니다.")
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
    @ApiOperation( value = "파일 다운로드 API", notes = "파일을 다운로드 받은 API입니다. 하위폴더가 제공될 경우 하위폴더 안의 파일을 다운받습니다.")
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


    @ApiOperation(value = "바이너리 형태로 파일 다운로드", notes = "바이너리 형태로 파일을 다운받습니다. 따라서 이미지 형태의 경우 바로 출력됩니다.")
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
    @ApiOperation(value = "리스트 조회 API", notes = "리스트를 조회하는데 하위 폴더가 제공될 경우 폴더 안의 리스트를 조회합니다.")
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
    @ApiOperation(value = "파일을 삭제하는 API",notes = "해당 파일을 삭제합니다." )
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


    @ApiOperation(value = "폴더를 삭제하는 API", notes = "해당 폴더를 삭제하여 폴더 안 파일까지 모두 삭제하는 기능입니다.")
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
