package com.example.server.service;

import com.example.server.component.PresentationFileAccessor;
import com.example.server.constants.PresentationConstant;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PresentationCaptureService extends PresentationFileAccessor {

    public boolean captureHtml() {

        WebDriver driver = getWebDriver();
        try {

            loadHtml(this.getHtmlFile(), driver);

            // 슬라이드 Element List
            List<WebElement> slides = this.getSlideElementList(driver);

            // 이미지 디렉토리 확인 및 생성
            File captureDir = this.getCaptureDir();

            if (captureDir.exists()) {
                FileUtils.cleanDirectory(captureDir);
            } else {
                FileUtils.forceMkdir(captureDir);
            }

            for (WebElement element : slides) {
                // 요소가 보이도록 스크롤
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

                // 렌더링 완료 대기
                Thread.sleep(500);

                // 요소의 위치와 크기 정보
                Rectangle rect = element.getRect();

                // 바로 원하는 위치에 저장
                String outputFileName = element.getAttribute("id") + "." + PresentationConstant.CAPTURE.EXT.getValue();
                File imgFile = new File(captureDir, outputFileName);

                // 스크린샷을 바로 지정된 위치에 저장
                byte[] screenshotBytes = element.getScreenshotAs(OutputType.BYTES);
                Files.write(imgFile.toPath(), screenshotBytes);

            }
            return true;
        }
        catch (Exception e) {
            log.error("캡처 과정에서 문제가 발생했습니다. {}", e.getMessage(), e);
            return false;
        }
        finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.warn("WebDriver 정리 중 오류 발생: {}", e.getMessage());
                }
            }
        }
    }

    private List<WebElement> getSlideElementList(WebDriver driver){
        // 슬라이드 개별 검색 - ID를 명시적으로 검색
        List<WebElement> slides = new ArrayList<>();

        int i = 1;
        while (true) {
            String slideId = PresentationConstant.CAPTURE.SLIDE_SEPARATOR.getValue() + i;
            try {
                // Java script로 slide 요소 존재 확인
                log.info("슬라이드 ID 검색: {}", slideId);
                Object result = ((JavascriptExecutor) driver).executeScript(
                        "return document.getElementById(arguments[0]) != null", slideId);
                boolean exists = result != null && (boolean) result;


                if (exists) {
                    WebElement slide = driver.findElement(By.id(slideId));
                    slides.add(slide);
                    log.info("슬라이드 발견: {}", slideId);
                } else {
                    log.info("슬라이드가 존재하지 않음: {}", slideId);
                    if (i > 1) break; // 첫 번째 슬라이드 이후 발견되지 않으면 중단
                }
            } catch (Exception e) {
                log.warn("슬라이드 검색 중 오류: {} - {}", slideId, e.getMessage());
                if (i > 1) break;
            }
            i++; // 슬라이드 인덱스 증가
        }
        return slides;
    }

    private WebDriver getWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    private void loadHtml(File htmlFile,WebDriver driver) throws MalformedURLException {
        int width =  Integer.parseInt(PresentationConstant.CAPTURE.DIMENSION_WIDTH.getValue());
        int height = Integer.parseInt(PresentationConstant.CAPTURE.DIMENSION_HEIGHT.getValue());

        driver.manage().window().setSize(new Dimension(width, height));
        String fileUrl = htmlFile.toURI().toURL().toString();

        // 페이지 로드
        driver.get(fileUrl);

        // JavaScript 로딩 대기
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

}
