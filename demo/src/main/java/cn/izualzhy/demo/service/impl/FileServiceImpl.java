package cn.izualzhy.demo.service.impl;

import cn.izualzhy.demo.service.FileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file) {
        log.info("upload file: {} contentType: {}", file.getOriginalFilename() , file.getContentType());

        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            log.info("file content: {}", content);
        } catch (Exception e) {
            log.error("error when get file content", e);
        }

        String originalFileName = file.getOriginalFilename();
        // 获取当前时间，格式为 yyyyMMddHHmm（例如 202505151451）
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // 拆分文件名和扩展名
        int dotIndex = originalFileName.lastIndexOf(".");
        String namePart = (dotIndex != -1) ? originalFileName.substring(0, dotIndex) : originalFileName;
        String extPart = (dotIndex != -1) ? originalFileName.substring(dotIndex) : "";

        // 拼接新文件名
        String newFileName = namePart + "." + timestamp + extPart;

        Path savedPath = Path.of("/tmp/", newFileName); // uploads 目录需存在或提前创建
        try {
            Files.createDirectories(savedPath.getParent());
            file.transferTo(savedPath.toFile());
        } catch (Exception e) {
            log.error("error when save file", e);
        }
        log.info("file saved to {}", savedPath);

        return savedPath.toString();
    }
}
