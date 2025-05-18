package cn.izualzhy.demo.controller;

import cn.izualzhy.demo.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@RestController
@RequestMapping("/file")
public class TestFileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/upload")
    public String uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("lang") String lang,
            @RequestParam("query_param") String queryParam) {
        String contentType = request.getContentType();
        log.info("originalFilename: {} fileContentType: {} requestContentType: {} lang: {} queryParam: {}",
                file.getOriginalFilename(), file.getContentType(), contentType, lang, queryParam);
        return fileService.uploadFile(file);
    }
}
