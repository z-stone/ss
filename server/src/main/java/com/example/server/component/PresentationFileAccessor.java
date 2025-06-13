package com.example.server.component;

import com.example.server.constants.PresentationConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public abstract class PresentationFileAccessor {
    @Value("${presentation.work.directory}")
    private String WORK_DIR_PATH;

    public File getWorkDir(){
        return new File(WORK_DIR_PATH);
    }

    public File getCaptureDir(){
        Path htmlFilePath = Paths.get(WORK_DIR_PATH, PresentationConstant.CAPTURE.DIR_NAME.getValue());
        return htmlFilePath.toFile();
    }

    public File getHtmlFile(){
        Path htmlFilePath = Paths.get(WORK_DIR_PATH, PresentationConstant.HTML.FILE_NAME.getValue());
        return htmlFilePath.toFile();
    }

    public File getPptFile(){
        Path pptFilePath = Paths.get(WORK_DIR_PATH, PresentationConstant.PPT.FILE_NAME.getValue());
        return pptFilePath.toFile();
    }

}
