package com.example.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor//(access = AccessLevel.PRIVATE)
@ToString
public class ToolCallConditionDto {
    @JsonProperty("toolName")
    private String toolName;
    @JsonProperty("parameters")
    private Map<String,Object> parameters;

    public void refactorToolName() {
        if (this.toolName != null && this.toolName.contains("functions.")) {
            this.toolName = this.toolName.replace("functions.", "");
        }
    }
}
