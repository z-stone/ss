package com.example.server.tools;

import com.example.server.dto.ComponentSaveDto;
import com.example.server.entity.ComponentInfo;
import com.example.server.repository.ComponentInfoRepository;
import com.example.server.service.CaptureService;
import com.example.server.service.FileCreateService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PresentationTools {
    @Autowired
    private ComponentInfoRepository componentInfoRepository;

    @Autowired
    private CaptureService captureService;

    @Autowired
    private FileCreateService fileCreateService;

    //TODO 제거 예정
    public String saveComponentInfos(List<ComponentSaveDto> dtoList) {
        List<ComponentInfo> entities = dtoList.stream()
                .map(ComponentSaveDto::toEntity)
                .collect(Collectors.toList());

        componentInfoRepository.saveAll(entities);
        return "Success";
    }

    @Tool(description = "컴포넌트 리스트 조회", returnDirect = false)
    public List<Map<String,String>> getComponentDiscription() {
        List<ComponentInfo> allComponents = componentInfoRepository.findAll();
        return allComponents.stream()
                .map(ComponentInfo::getDesc)
                .collect(Collectors.toList());
    }

    @Tool(description = "id를 통해 컴포넌트 html,css 정보 조회", returnDirect = false)
    public List<ComponentInfo> getComponentsByIds(List<String> idList) {
        return componentInfoRepository.findAllById(idList);
    }

    @Tool(description = "HTML 파일 생성")
    public String createHtmlFile(String htmlString) {
        return fileCreateService.createHtmlFile(htmlString) ? "html 파일을 생성했습니다." : "html 파일을 생성하지 못했습니다.";
    }

    @Tool(description = "PPT 파일 생성")
    public String createPptFile() {
        if(captureService.captureHtml()){
            fileCreateService.createPptFile();
        }
        return "PPT 파일 생성에 실패했습니다.";
    }
}
