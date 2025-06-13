package com.example.server.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO : WebDriverPool (사용 안하고 있는 상태)
@Configuration
public class WebDriverPoolConfig {
    @Bean
    public GenericObjectPool<WebDriver> webDriverPool() {
        BasePooledObjectFactory<WebDriver> factory = new BasePooledObjectFactory<WebDriver>() {
            @Override
            public WebDriver create() {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                return new ChromeDriver(options);
            }

            @Override
            public PooledObject<WebDriver> wrap(WebDriver driver) {
                return new DefaultPooledObject<>(driver);
            }

            @Override
            public void destroyObject(PooledObject<WebDriver> p) {
                p.getObject().quit();
            }
        };

        GenericObjectPool<WebDriver> pool = new GenericObjectPool<>(factory);
        pool.setMaxTotal(15);     // 최대 15개 WebDriver 인스턴스
        pool.setMaxIdle(10);      // 최대 10개 유휴 인스턴스
        pool.setMinIdle(1);       // 최소 1개 유휴 인스턴스 유지
        return pool;
    }
}