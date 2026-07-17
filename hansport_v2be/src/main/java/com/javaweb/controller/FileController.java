package com.javaweb.controller;

import com.javaweb.domain.response.file.ResUploadFileDTO;
import com.javaweb.service.CloudinaryService;
import com.javaweb.util.annotation.ApiMessage;
import com.javaweb.util.error.StorageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final CloudinaryService cloudinaryService;

    public FileController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "files", required = false) List<MultipartFile> files,
                                                   @RequestParam("folder") String folder)
            throws IOException, StorageException {
        //validation
        if (files == null || files.isEmpty()) {
            throw new StorageException("file is empty. Please upload the file");
        }

        List<String> fileNames = new ArrayList<>();

        for(MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

            String extension = "";
            if (fileName != null && fileName.lastIndexOf(".") >= 0) {
                extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            }
            boolean isValid = allowedExtensions.contains(extension);

            if (!isValid) {
                throw new StorageException("Invalid file extension. Only allows " + allowedExtensions.toString());
            }
            String uploadedFile = this.cloudinaryService.upload(file, folder);
            fileNames.add(uploadedFile);
        }

        ResUploadFileDTO res = new ResUploadFileDTO(fileNames, Instant.now());
        return ResponseEntity.ok().body(res);
    }
}
