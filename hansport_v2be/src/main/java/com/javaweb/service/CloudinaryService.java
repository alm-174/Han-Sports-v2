package com.javaweb.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class CloudinaryService {

    private static final Set<String> ALLOWED_FOLDERS = Set.of("product", "logo", "banner");

    private final Cloudinary cloudinary;

    @Value("${cloudinary.upload-folder-prefix:hansport_v2}")
    private String uploadFolderPrefix;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file, String folder) throws IOException {
        if (folder == null || !ALLOWED_FOLDERS.contains(folder)) {
            throw new IllegalArgumentException("Folder upload không hợp lệ");
        }

        String targetFolder = uploadFolderPrefix + "/" + folder;
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", targetFolder,
                        "resource_type", "auto"
                )
        );

        Object secureUrl = uploadResult.get("secure_url");
        if (secureUrl == null) {
            throw new IOException("Cloudinary upload failed: missing secure_url");
        }

        return secureUrl.toString();
    }
}