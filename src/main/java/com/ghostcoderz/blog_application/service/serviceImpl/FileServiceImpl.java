package com.ghostcoderz.blog_application.service.serviceImpl;

import com.ghostcoderz.blog_application.service.serviceInterface.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Pull the file name
        String fileName = file.getOriginalFilename();

        System.out.println("Original file name : "+ fileName);

        // Generate random name for the file
        String randomId = UUID.randomUUID().toString();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = randomId.concat(fileExtension);
        System.out.println("new file name : "+ newFileName);

        // Prepare the full path for the file
        String filePath = path + File.separator + newFileName;

        // Create images folder if not there
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }

        // Upload/Copy the file to the folder/path
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return newFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;
        InputStream fis = new FileInputStream(fullPath);

        return fis;

    }

}
