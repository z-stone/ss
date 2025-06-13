package com.example.server.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Map<String,String> getDesc(){
        return Map.of("id",id,"description",description);
    }

} 