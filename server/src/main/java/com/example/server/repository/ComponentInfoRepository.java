package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.server.entity.ComponentInfo;

@Repository
public interface ComponentInfoRepository extends JpaRepository<ComponentInfo, String> { }