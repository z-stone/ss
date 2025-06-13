package com.example.server.dto;

import com.example.server.entity.ComponentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.document.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentSaveDto {
    private String html;
    private String css;
    private String description;

    public ComponentInfo toEntity() {
        return ComponentInfo.builder()
                .description(this.description)
                .html(this.html)
                .css(this.css)
                .build();
    }

    public Document toDocument() {
        return new Document(this.html + " " + this.css, Map.of("description", this.description));
    }
}
