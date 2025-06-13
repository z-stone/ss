package com.example.server.prompt;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

public class PresentationPrompts {

    public static Prompt getSaveComponentPrompt(){
        String instruction = """
        사용자로 부터 이미지를 전달 받은 경우 해당 이미지에서 디자인 정보(html,css)만 추출한다.(text 추출x)
        디자인 정보만 제공하고 설명을 제공하지 않은 경우 반드시 사용자에게 설명을 추가해달라 요청한다.
        """;
        PromptTemplate promptTemplate = new PromptTemplate(instruction);
        return promptTemplate.create();
    }

    public static Prompt getCreateHtmlPrompt(String slideSeparator){
        String instruction = """
    당신은 40년 차 웹 디자인 전문가입니다.
    당신의 역할은 필수 요구 사항을 바탕으로 프레젠테이션 형식의 HTML을 만드는 것입니다.
    요구사항은 기본 요구 사항과 필수 요구사항이 있습니다.
    기본 요구 사항은 최대한 지키되 사용자 요청과 겹칠 경우 사용자 요구사항을 우선으로 합니다.
    필수 요구 사항은 사용자 요청보다 우선으로 적용합니다.
    
    [요구사항]
    프레젠테이션 형태: 슬라이드로 나눠져 있어 스크롤을 내리며 확인 가능한 형태
    프레젠테이션 슬라이드 수: 3~4장
    프레젠테이션 비율: 16:12 형태
    디자인 형태: 결과물마다 디자인 통일성을 위해 MCP 서버에서 제공하고 있는 디자인 컴포넌트 정보를 최대한 활용하세요.
    결과물 내용: 최대한 사용자 입력 내용을 요약해서 포함시켜주세요.
    
    [필수 요구사항]
    slide별로 꼭 최상위 태그에 id로 {slide_separator} (1부터 순차적으로 증가)를 적용해야 해야합니다.
    만약 사용자가 slide를 원하지 않는 경우에도 {slide_separator}를 적용해야합니다.
    시각자료인 만큼 디자인이 틀어지지 않게 HTML 코드를 작성하세요.
    """;
        PromptTemplate promptTemplate = new PromptTemplate(instruction);
        return promptTemplate.create(Map.of("slide_separator",slideSeparator));
    }

    public static Prompt getCreatePptPrompt(){
        String instruction = """
        이전 작업이 처리된 경우 ppt 파일을 생성할 수 있습니다.
        이전 작업이 처리되지 않은 경우 MCP 서버에서 제공하는 컴포넌트 디자인 정보를 활용해 HTML 파일을 생성해야합니다.
        """;
        PromptTemplate promptTemplate = new PromptTemplate(instruction);
        return promptTemplate.create();
    }

}
