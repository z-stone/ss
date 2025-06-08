package com.example.server.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class McpResponseDto {
    private String description;
    private String content;
}
