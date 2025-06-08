package com.example.server.controller;
import com.example.server.dto.ComponentSaveDto;
import com.example.server.tools.PresentationTools;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PresentationController {
    //TODO : Tool이 아닌 service로 변경 예정
    private final PresentationTools presentationTools;

    @PostMapping("/saveComponent")
    public ResponseEntity<String> createComponentInfos(@RequestBody List<ComponentSaveDto> dtoList) {
        String result = presentationTools.saveComponentInfos(dtoList);
        return ResponseEntity.ok(result);
    }
}


























//
//    @PostMapping(value = "/from-svg")
//    public ResponseEntity<String> svgToPpt(@RequestBody String svgContent) {
//        try {
//            // SVG를 PNG로 변환
//            byte[] pngImage = pptService.convertSvgToPng(svgContent);
//
//            // PPT 생성 (파일 이름은 "presentation.pptx")
//            boolean isCreated = pptService.createPptWithImage(pngImage, "presentation.pptx");
//
//            if (isCreated) {
//                return ResponseEntity.ok("PPT file created successfully at src/main/resources/ppt/presentation.pptx");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create PPT file");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
//        }
//    }