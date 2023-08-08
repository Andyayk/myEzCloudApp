package com.example.myEzCloudApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${access.key.id}")
    private String accessKeyId;

    @Value("${access.key.secret}")
    private String accessKeySecret;

    @Value("${s3.region.name}")
    private String s3RegionName;

    @Value("${minio.url}")
    private String minioURL;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, accessKeySecret);

        // If using Minio on EC2
        return S3Client.builder()
                .endpointOverride(URI.create(minioURL)) // Replace with your MinIO server URL and port
                .credentialsProvider(() -> awsCredentials)
                .build();

        // If using AWS S3
//        return S3Client.builder()
//                .region(Region.of(s3RegionName))
//                .credentialsProvider(() -> awsCredentials)
//                .build();
    }
}
