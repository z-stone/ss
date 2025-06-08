package com.example.client.constants;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.EnumSet;

@AllArgsConstructor
public enum WorkFlow {
    SELF("생성형 Ai를 통해 작업을 수행합니다."),
    MCP("MCP 도구를 사용하여 작업을 수행합니다."),
    USER("사용자에게 질의하여 작업을 수행합니다.");

    private final String description;

    public String getDescription() {
        return this.description;
    }

    public static String getPrompt() {

        StringBuilder prompt = new StringBuilder();

        prompt.append("<Work_Flow_Type>");
        prompt.append(System.lineSeparator());
        for (WorkFlow type : EnumSet.allOf(WorkFlow.class)) {
            prompt.append(System.lineSeparator());
            prompt.append(String.format("- %s: %s", type.name(), type.getDescription()));
        }
        prompt.append(System.lineSeparator());
        prompt.append("</Work_Flow_Type");

        return prompt.toString();
    }
}
