package com.example.myEzCloudApp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class S3Service {

    private final S3Client s3Client;
    @Value("${s3.bucket.name}")
    private String BUCKET_NAME;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadFile(MultipartFile file) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(file.getOriginalFilename())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    }

    public List<String> listFiles() {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME)
                .build();

        ListObjectsResponse response = s3Client.listObjects(request);
        List<S3Object> objects = response.contents();

        ListIterator<S3Object> listIterator = objects.listIterator();

        List<String> objectNames = new ArrayList<>();

        while(listIterator.hasNext()) {
            S3Object s3Object = listIterator.next();
            objectNames.add(s3Object.key());
            System.out.println("Key: " + s3Object.key());
            System.out.println("Owner: " + s3Object.owner());
            System.out.println("Size: " + s3Object.size());
        }

        return objectNames;
    }

    public byte[] downloadFile(String fileName) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObject(request, ResponseTransformer.toBytes());
        return responseBytes.asByteArray();
    }

    public void deleteFile(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(fileName)
                .build();

        s3Client.deleteObject(request);
    }
}