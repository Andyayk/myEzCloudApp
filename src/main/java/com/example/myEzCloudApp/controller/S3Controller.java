package com.example.myEzCloudApp.controller;

import com.example.myEzCloudApp.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @Autowired
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        s3Service.uploadFile(file);
    }

    @GetMapping("/list")
    public List<String>  listFiles() {
        return s3Service.listFiles();
    }

    @GetMapping("/download/{fileName}")
    public byte[] downloadFile(@PathVariable String fileName) throws IOException {
        return s3Service.downloadFile(fileName);
    }

    @DeleteMapping("/delete/{fileName}")
    public void deleteFile(@PathVariable String fileName) {
        s3Service.deleteFile(fileName);
    }
}