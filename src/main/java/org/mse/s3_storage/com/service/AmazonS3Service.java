package org.mse.s3_storage.com.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public AmazonS3Service(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

        public String uploadPhoto(String memberId, String subfolder, MultipartFile multipartFile) {
            final String originalFilename = multipartFile.getOriginalFilename();
            final String directory = memberId + (subfolder != null ? "/" + subfolder : "") + "/";
            final String s3Filename = directory + originalFilename;
            final ObjectMetadata metadata = getObjectMetadata(multipartFile);

            try {
                if (!amazonS3Client.doesObjectExist(bucket, directory)) {
                    amazonS3Client.putObject(bucket, directory, "");
                }
                amazonS3Client.putObject(bucket, s3Filename, multipartFile.getInputStream(), metadata);
                return amazonS3Client.getUrl(bucket, s3Filename).toString();
            } catch (Exception e) {
                throw new IllegalArgumentException("S3 picture upload failed");
            }
        }

        public ResponseEntity<byte[]> getObject(String memberId, String subfolder, String storedFileName) throws IOException {
            final String directory = memberId + (subfolder != null ? "/" + subfolder : "") + "/";
            String s3FileName = directory + storedFileName;
            S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, s3FileName));
            S3ObjectInputStream objectInputStream = o.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);
            objectInputStream.close();

            String fileExtension = storedFileName.substring(storedFileName.lastIndexOf(".") + 1);
            String contentType;
            if (fileExtension.equalsIgnoreCase("png")) {
                contentType = "image/png";
            } else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
                contentType = "image/jpeg";
            } else if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xls")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else {
                contentType = "application/octet-stream";
            }

            String fileName = URLEncoder.encode(storedFileName, "UTF-8").replaceAll("\\+", "%20");

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(contentType));
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        }

        public String getPresignedUrl(String memberId, String subfolder, String storedFileName) {
            final String directory = memberId + (subfolder != null ? "/" + subfolder : "") + "/";
            final String s3FileName = directory + storedFileName;

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, s3FileName);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + 3600000));

            URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        }

    private ObjectMetadata getObjectMetadata(MultipartFile multipartFile) {
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public List<String> listFolders() {
        List<String> folders = new ArrayList<>();
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucket).withDelimiter("/");

        ListObjectsV2Result result;
        do {
            result = amazonS3Client.listObjectsV2(request);
            folders.addAll(result.getCommonPrefixes());
            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return folders;
    }

    public List<String> listFiles(String path) {
        List<String> items = new ArrayList<>();
        final String directory = path.endsWith("/") ? path : path + "/";

        // 객체들을 나열하는 요청 생성
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucket).withPrefix(directory).withDelimiter("/");

        // 객체들을 나열하고 파일들과 폴더들을 추출하여 리스트에 추가
        ListObjectsV2Result result;
        do {
            result = amazonS3Client.listObjectsV2(request);

            // 폴더 추가
            for (String commonPrefix : result.getCommonPrefixes()) {
                items.add(commonPrefix.substring(directory.length(), commonPrefix.length() - 1));
            }

            // 파일 추가
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                // 폴더 이름 자체는 포함하지 않도록 필터링
                if (!objectSummary.getKey().equals(directory) && !objectSummary.getKey().endsWith("/")) {
                    items.add(objectSummary.getKey().substring(directory.length()));
                }
            }
            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());

        return items;
    }


    }

