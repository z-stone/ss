package com.example.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;
import org.springframework.ai.chat.prompt.Prompt;

@Builder
@Setter
public class ToolCheckListResponseDto {
    @JsonProperty("확인 사항")
    private Prompt instruction;

    @JsonProperty("이전 작업 실행 여부")
    private boolean isExistPrevTask;
}
