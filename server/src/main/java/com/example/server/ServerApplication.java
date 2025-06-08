package com.example.server;

import com.example.server.tools.PresentationTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.example.server"})
public class ServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider PresentationToolsProvider(PresentationTools tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }
}
