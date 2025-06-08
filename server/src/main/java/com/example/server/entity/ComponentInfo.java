package com.example.server.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Map;

@Entity
@Table(name = "TB_COMPONENT_INFO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentInfo {

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "html", columnDefinition = "text")
    private String html;
    
    @Column(name = "css", columnDefinition = "text")
    private String css;

    public Map<String,String> getDesc(){
        return Map.of("id",id,"description",description);
    }

} 