package com.example.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/presentation")
@Slf4j
@RequiredArgsConstructor
public class PresentationController {

    // 업로드된 파일 저장 경로
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping
    public String presentationPage() {
        // 타임리프 템플릿으로 presentation.html 페이지 반환
        return "presentation";
    }

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<String> handleMessageSubmit(
            @RequestParam("message") String message,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        log.info("메시지 수신: {}", message);
        
        // 파일이 있는 경우 저장만 하고 URL은 반환하지 않음
        if (file != null && !file.isEmpty()) {
            try {
                // 저장 디렉토리 확인 및 생성
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                // 고유한 파일명 생성
                String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                
                // 파일 저장
                Files.copy(file.getInputStream(), filePath);
                
                log.info("파일 업로드 완료: {}", uniqueFileName);
            } catch (IOException e) {
                log.error("파일 업로드 실패", e);
                return ResponseEntity.badRequest()
                        .body("파일 업로드 중 오류가 발생했습니다.");
            }
        }
        
        // 서버 응답 메시지 생성 (간단한 예시로 에코 응답)
        String serverResponse = "\"" + message + "\"에 대한 서버 응답입니다.";
        
        // 단순 텍스트 응답만 반환
        return ResponseEntity.ok(serverResponse);
    }
} 