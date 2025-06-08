package com.example.server.dto;

import com.example.server.entity.ComponentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentSaveDto {
    private String html;
    private String css;
    private String description;


    // ComponentInfo 엔티티로 변환
    public ComponentInfo toEntity() {
        return ComponentInfo.builder()
                .description(this.description)
                .html(this.html)
                .css(this.css)
                .build();
    }
}
