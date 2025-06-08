package com.example.client;

import com.example.client.constants.WorkFlow;
import com.example.client.vo.TaskPlanVo;
import com.fasterxml.jackson.databind.JsonNode;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/presentation")
public class ClientController {
    private final ChatClient.Builder chatClientbuilder;

    private final List<McpSyncClient> mcpSyncClients;
    private final SyncMcpToolCallbackProvider provider;

    private ChatClient chatClient;
    private ChatClient preChatClient;
    @PostConstruct
    public void initChatClient() {
        ToolCallback[] callbacks = provider.getToolCallbacks();
        List<String> toolDescription = new ArrayList<>();
        for (ToolCallback callback : callbacks) {
            toolDescription.add(callback.getToolDefinition().description());
        }

        //String systemMessage = "<ToolDescription>"+ toolDescription +"</ToolDescription>" + WorkFlow.getPrompt();
        this.chatClient = chatClientbuilder
            .defaultToolCallbacks(callbacks)
            .build();

        this.preChatClient = chatClientbuilder.defaultSystem("당신은 사용자 질의에 따라 작업 계획을 수립하는 Ai Assistant입니다.").build();
    }

    @PostMapping("/test")
    public String d(@RequestParam("message") String message)throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String taskPlanStr = preChatClient.prompt(this.getPlanTaskPrompt()).user(message).call().content();
        List<TaskPlanVo> taskPlanVos = objectMapper.readValue(taskPlanStr,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskPlanVo.class));

        return "Success";
    }

    @PostMapping("/send")
    public String chat(@RequestParam("message") String message,
                       @RequestParam(value = "files", required = false) List<MultipartFile> files) {


        if(!files.isEmpty()) {
            try {
                List<Media> medias = new ArrayList<>();
                for(MultipartFile file: files){
                    // MultipartFile을 ByteArrayResource로 변환
                    ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };

                    // Media 객체 생성
                    medias.add(new Media(MediaType.parseMediaType(file.getContentType()), fileResource));
                }
                
                // UserMessage 생성 - Resource와 Media 모두 설정
                UserMessage userMessage = UserMessage.builder()
                        .text("해당 이미지에서 보이는 디자인 정보를 css와 html로 추출하고 싶어" +
                                "크기의 경우 동적 요소임으로 size_value로 값을 대체해주고 text의 경우도 동적 요소로 text_value로 대처해주세요." +
                                        "응답 형식은 [{css,html,description},...]형태로 제공하세요. 당신의 응답 그대로 Object Mapper에 맵핑할거에요. 응답 형식외의 설명은 하지말아주세요."


                                )
                        .media(medias)
                        .build();

                //TODO UserMessage의 text와 user의 관계
                Prompt prompt = new Prompt(userMessage);
                String res = chatClientbuilder.build().prompt(prompt).toolCallbacks(List.of()).call().content();

                return chatClientbuilder.build().prompt("하위 정보를 이용하세요. 그리고 당신의 응답 그대로 복사해서 html 파일에 붙혀넣을거니 html 코드만 제공하세요. [정보]" +
                        res).user(message).toolCallbacks(List.of()).call().content();
                
            } catch (Exception e) {
                log.error("파일 처리 중 오류 발생", e);
            }
        }
        
        // 파일이 없거나 파일 처리 중 오류가 발생한 경우
        return chatClient.prompt(WorkFlow.getPrompt()).user(message).toolCallbacks(List.of()).call().content();
    }

        private Prompt getPlanTaskPrompt(){
            String command ="""
 
  1.작업 흐름 유형을 토대로 작업 계획을 수립하세요.
  {WorkFlowType}
  
  2.각 작업의 경우 다음 규칙에 맞게 정의하세요.
  {TaskPlan}
  
  3.작업 흐름 결과 이 외의 답변은 하지 않습니다.
  """;

            PromptTemplate template = new PromptTemplate(command);
            template.add("WorkFlowType", WorkFlow.getPrompt());
            template.add("TaskPlan", TaskPlanVo.getPrompt());
        return template.create();
    }
}


//
//@PostMapping("/chat")
//public String handleFileUpload(@RequestBody String description) {
//    String aiResponse = chatClient
//            .prompt(this.getToolPrompt())
//            .user("사용자 입려 : " + description)
//            .call()
//            .content();
//    //TODO 불필요 ..?
//    aiResponse = trimData(aiResponse);
//    System.out.println(aiResponse);
//    try {
//        List<ToolCallConditionDto> tools =
//                objectMapper.readValue(aiResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, ToolCallConditionDto.class));
//
//        tools.forEach(ToolCallConditionDto::refactorToolName);
//        tools.forEach(tool->log.info(tool.toString()));
//        if(tools.isEmpty()){
//            return "요청에 대한 결과를 만들지 못했습니다.";
//        }
//
//        else if (tools.size() == 1) {
//            ToolCallConditionDto conditionDto = tools.get(0);
//
//            CallToolResult mcpResult =
//                    redisMcpClient.callTool(new McpSchema.CallToolRequest(conditionDto.getToolName(), conditionDto.getParameters()));
//
//            return extractTextFromCallToolResult(mcpResult);
//        } else {
//            StringBuilder sb = new StringBuilder();
//            for (ToolCallConditionDto conditionDto : tools) {
//                CallToolResult mcpResult =
//                        redisMcpClient.callTool(new McpSchema.CallToolRequest(conditionDto.getToolName(), conditionDto.getParameters()));
//                sb.append("다음은 사용자 입력에 필요한 데이터들이야 이를 바탕으로 사용자 요청 결과에 대해 알려줘");
//                sb.append("<---" + conditionDto.getToolName() + " tool결과 --->");
//                sb.append(extractTextFromCallToolResult(mcpResult));
//            }
//            log.info("mcpResult : "+sb.toString());
//            return chatClient.prompt(sb.toString())
//                    .user(description)
//                    .call()
//                    .content();
//        }
//
//
//    } catch (Exception e) {
//        throw new RuntimeException(e);
//    }
//
//
//}
//
//private String trimData(String data){
//    data = data.trim();
//    if (data.startsWith("`")) {
//        data = data.substring(1);
//    }
//    if (data.endsWith("`")) {
//        data = data.substring(0, data.length() - 1);
//    }
//
//    System.out.println(data);
//    return data;
//}
//
//private String extractTextFromCallToolResult(CallToolResult result) {
//    for (McpSchema.Content content : result.content()) {
//        if (content instanceof McpSchema.TextContent textContent) {
//            return textContent.text();
//        }
//    }
//    return null; // or throw an exception if text is expected
//}
//
//private String getToolPrompt() {
//    System.out.println("getToolPrompt Start!");
//    StringBuilder formattedString = new StringBuilder();
//    formattedString.append("나는 이 응답과 이어서 추가적으로 요청을 보낼 수 있어 만일 사용자 입력을 해결하기 위해 필요하다고 생각되는 정보가 tool에서 제공한다면 json형태의 String 값으로 파라미터를 알려줘 ");
//    formattedString.append("오로지 [{toolName:... , parameters }] 이 형태의 데이터만 줘 너가 준 내용 그대로 java Object Mapper로 Class에 바인딩 할거니까 데이터 내용 이 외에는 안무것도 주지마 특히 ''json 이런식으로 타입 붙이는거 금지 ");
//    System.out.println(formattedString.toString());
//    return formattedString.toString();
//}