package com.example.client.vo;

import com.example.client.constants.WorkFlow;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Builder
public class TaskPlanVo {
    WorkFlow type;
    String toolName;
    Map<String,Object> parameter;

    public static String getPrompt(){
        String prompt = """
                <Task_Plan_Info>
                규칙:
                1. 너의 응답을 type(Enum), description(String)를 필드로 갖는 java class 배열로 맵핑할거야
                2. type은 반드시 WorkFlow 중 하나여야 합니다.(필수)
                3. description은 해당 작업에 대한 설명이야.
                4. 작업 흐름은 무조건 순서대로
                5. 모든 응답은 유효한 JSON 형식 [{type,description} , ...   ] 이어야 합니다.
                </Task_Plan_Info>
                """;
        return prompt;
    }
}
