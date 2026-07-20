package com.javaweb.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
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

    public boolean deleteByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return false;
        }

        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId == null || publicId.isBlank()) {
                return false;
            }

            Map<?, ?> result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );
            Object deleteResult = result.get("result");
            return "ok".equals(deleteResult);
        } catch (IOException e) {
            return false;
        }
    }

    private String extractPublicId(String imageUrl) {
        try {
            URI uri = URI.create(imageUrl);
            String[] segments = uri.getPath().split("/");
            int uploadIndex = Arrays.asList(segments).indexOf("upload");
            if (uploadIndex < 0 || uploadIndex + 1 >= segments.length) {
                return null;
            }

            StringBuilder publicId = new StringBuilder();
            for (int i = uploadIndex + 1; i < segments.length; i++) {
                if (i == uploadIndex + 1 && segments[i].matches("v\\d+")) {
                    continue;
                }
                if (publicId.length() > 0) {
                    publicId.append('/');
                }
                publicId.append(segments[i]);
            }

            return publicId.toString();
        } catch (Exception e) {
            return null;
        }
    }
}