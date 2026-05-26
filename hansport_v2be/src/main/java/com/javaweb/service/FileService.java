package com.javaweb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
=======
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
<<<<<<< HEAD
import java.util.Set;
=======
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf

@Service
public class FileService {

<<<<<<< HEAD
    private static final Set<String> ALLOWED_FOLDERS = Set.of("product", "logo");

    @Value("${hansport.upload-file.base-path}")
    private String basePath;

    public void createDirectory(String folder) throws IOException {
        Files.createDirectories(resolveFolder(folder));
    }

    public String store(MultipartFile file, String folder) throws IOException {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
        String safeName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String finalName = System.currentTimeMillis() + "-" + safeName;
        Path path = resolveFile(folder, finalName);
=======
    @Value("${hansport.upload-file.base-uri}")
    private String baseURI;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }

    }

    public String store(MultipartFile file, String folder) throws URISyntaxException,
            IOException {
        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseURI + folder + "/" + finalName);
        Path path = Paths.get(uri);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

<<<<<<< HEAD
    public long getFileLength(String fileName, String folder) {
        Path path = resolveFile(folder, fileName);
        File file = path.toFile();
        if (!file.exists() || file.isDirectory()) {
            return 0;
        }
        return file.length();
    }

    public InputStreamResource getResource(String fileName, String folder)
            throws FileNotFoundException {
        return new InputStreamResource(new FileInputStream(resolveFile(folder, fileName).toFile()));
    }

    private Path resolveFolder(String folder) {
        if (folder == null || !ALLOWED_FOLDERS.contains(folder)) {
            throw new IllegalArgumentException("Folder upload không hợp lệ");
        }
        Path root = Paths.get(basePath).toAbsolutePath().normalize();
        Path folderPath = root.resolve(folder).normalize();
        if (!folderPath.startsWith(root)) {
            throw new IllegalArgumentException("Đường dẫn upload không hợp lệ");
        }
        return folderPath;
    }

    private Path resolveFile(String folder, String fileName) {
        String safeName = StringUtils.cleanPath(fileName == null ? "" : fileName);
        if (safeName.isBlank() || safeName.contains("..") || safeName.contains("/") || safeName.contains("\\")) {
            throw new IllegalArgumentException("Tên file không hợp lệ");
        }
        return resolveFolder(folder).resolve(safeName).normalize();
=======
    public long getFileLength(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());

        // file không tồn tại hoặc file là 1 directory => return 0
        if (!tmpDir.exists() || tmpDir.isDirectory())
            return 0;

        return tmpDir.length();
    }

    public InputStreamResource getResource(String fileName, String folder)
            throws URISyntaxException, FileNotFoundException {

        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File file = new File(path.toString());
        return new InputStreamResource(new FileInputStream(file));
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }
}
