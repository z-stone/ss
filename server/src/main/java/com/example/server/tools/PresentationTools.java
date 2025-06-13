package com.example.server.tools;

import com.example.server.constants.PresentationConstant;
import com.example.server.dto.ComponentSaveDto;
import com.example.server.dto.ToolCheckListResponseDto;
import com.example.server.entity.ComponentInfo;
import com.example.server.prompt.PresentationPrompts;
import com.example.server.repository.ComponentInfoRepository;
import com.example.server.service.PresentationCaptureService;
import com.example.server.service.PresentationCreateService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class PresentationTools {
    @Autowired
    private ComponentInfoRepository componentInfoRepository;

    @Autowired
    private PresentationCaptureService presentationCaptureService;

    @Autowired
    private PresentationCreateService presentationCreateService;

    @Tool(description = "디자인 컴포넌트 정보 저장 전 필수 확인 사항",returnDirect = false)
    public ToolCheckListResponseDto getSaveComponentInfosCheckList(){
        return ToolCheckListResponseDto.builder()
                .instruction(PresentationPrompts.getSaveComponentPrompt() )
                .isExistPrevTask(false)
                .build();

    }

    @Tool(description = "디자인 컴포넌트 정보 저장",returnDirect = true)
    public String saveComponentInfos(List<ComponentSaveDto> dtoList) {
        List<ComponentInfo> entities = dtoList.stream()
                .map(ComponentSaveDto::toEntity)
                .collect(Collectors.toList());

        componentInfoRepository.saveAll(entities);
        return "컴포넌트 저장에 성공했습니다.";
    }

    @Tool(description = "디자인 컴포넌트 목록 조회", returnDirect = false)
    public List<Map<String,String>> getComponentDisList() {
        List<ComponentInfo> allComponents = componentInfoRepository.findAll();
        return allComponents.stream()
                .map(ComponentInfo::getDesc)
                .collect(Collectors.toList());
    }

    @Tool(description = "id를 통해 디자인 컴포넌트 정보(html,css) 조회", returnDirect = false)
    public List<ComponentInfo> getComponentsByIds(List<String> idList) {
        return componentInfoRepository.findAllById(idList);
    }

    @Tool(description = "HTML 파일 생성 전 지침 사항",returnDirect = false)
    public ToolCheckListResponseDto getCreateHtmlInstruction(){

        return  ToolCheckListResponseDto.builder()
                .instruction(PresentationPrompts.getCreateHtmlPrompt(PresentationConstant.CAPTURE.SLIDE_SEPARATOR.getValue()) )
                .isExistPrevTask(false)
                .build();
    }

    @Tool(description = "HTML 파일 생성",returnDirect = false)
    public String createHtmlFile(String htmlString) {
        return presentationCreateService.createHtmlFile(htmlString) ? "http://localhost:8080/presentation/downloadHtml" : "html 파일을 생성하지 못했습니다.";
    }

    @Tool(description = "PPT 파일 생성 tool 호출 전 필수 확인 사항",returnDirect = false)
    public ToolCheckListResponseDto getCreatePptInstruction(){
        return ToolCheckListResponseDto.builder()
                .instruction(PresentationPrompts.getCreatePptPrompt())
                .isExistPrevTask(!presentationCreateService.getPptFile().exists())
                .build();

    }

    @Tool(description = "PPT 파일 생성",returnDirect = false)
    public String createPptFile() {

        return presentationCaptureService.captureHtml() && presentationCreateService.createPptFile() ?
            "http://localhost:8080/presentation/downloadPpt 경로에 접속하면 ppt 파일을 다운로드할 수 있습니다.": "PPT 파일 생성에 실패했습니다.";
    }
}
