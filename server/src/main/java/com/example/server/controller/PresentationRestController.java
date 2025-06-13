package com.example.server.controller;
import com.example.server.component.PresentationFileAccessor;
import com.example.server.constants.PresentationConstant;
import com.example.server.service.PresentationCaptureService;
import com.example.server.service.PresentationCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class PresentationRestController extends PresentationFileAccessor {


    @GetMapping("/presentation/downloadHtml")
    public ResponseEntity<Resource> downloadHtml() throws Exception {

        File htmlFile = this.getHtmlFile();
        if (!htmlFile.exists()) return ResponseEntity.notFound().build();
        Resource resource = new FileSystemResource(htmlFile);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + htmlFile.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/html");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }


    @GetMapping("/presentation/downloadPpt")
    public ResponseEntity<Resource> downloadPptFile() throws Exception {

        File pptFile = this.getPptFile();
        if (!pptFile.exists()) return ResponseEntity.notFound().build();
        Resource resource = new FileSystemResource(pptFile);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pptFile.getName() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-powerpoint");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-powerpoint"))
                .body(resource);
    }

}
