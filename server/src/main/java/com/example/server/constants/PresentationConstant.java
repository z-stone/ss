package com.example.server.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.Dimension;

public class PresentationConstant {
    @AllArgsConstructor
    @Getter
    public enum HTML{
        FILE_NAME("presentation.html");

        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum PPT{
        FILE_NAME("presentation.pptx");
        private final String value;
    }

    @AllArgsConstructor
    @Getter
    public enum CAPTURE{
        EXT("png"),
        DIR_NAME("images"),
        DIMENSION_WIDTH("1920"),
        DIMENSION_HEIGHT("1080"),
        SLIDE_SEPARATOR("slide-separator-");

        private final String value;
    }


}
