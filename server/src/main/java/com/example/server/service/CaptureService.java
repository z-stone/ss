package com.example.server.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
//TODO 상수, 예외 처리 정리
public class CaptureService {

    @Autowired
    private GenericObjectPool<WebDriver> webDriverPool;

    @Value("${app.capture.dimension.width}")
    private String rootDirPath;

    @Value("${app.capture.dimension.width}")
    private int captureDimensionWidth;

    @Value("${app.capture.dimension.height}")
    private int captureDimensionHeight;

    @Value("${app.capture.slide.separator.prefix}")
    private String SLIDE_SEPARATOR_PREFIX;

    @Value("${app.file.image.extension}")
    private String IMG_FILE_EXT;

    @Value("${app.directory.image.name}")
    private String IMG_DIR_NAME;

    @Value("${app.file.html.name}")
    private String HTML_FILE_NAME;

    private WebDriver getWebDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    private void loadHtml(File htmlFile,WebDriver driver) throws MalformedURLException {
        driver.manage().window().setSize(new Dimension(captureDimensionWidth, captureDimensionHeight));
        String fileUrl = htmlFile.toURI().toURL().toString();

        // 페이지 로드
        driver.get(fileUrl);

        // JavaScript 로딩 대기
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }


    public boolean captureHtml() {
        File htmlFile = new File(rootDirPath+File.separator+HTML_FILE_NAME);
        WebDriver driver = null;
        try {
            driver = getWebDriver();
            this.loadHtml(htmlFile, driver);

            // 디버깅: 현재 HTML 소스 로깅
            log.info("현재 로드된 HTML: {}", driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));

            // 슬라이드 개별 검색 - ID를 명시적으로 검색
            List<WebElement> slides = new ArrayList<>();
            for (int i = 1; i <= 10; i++) { // 예상되는 최대 슬라이드 수
                try {
                    String slideId = SLIDE_SEPARATOR_PREFIX + i;
                    log.info("슬라이드 ID 검색: {}", slideId);
                    // JavaScript로 요소 존재 확인
                    boolean exists = (boolean) ((JavascriptExecutor) driver).executeScript(
                            "return document.getElementById(arguments[0]) != null", slideId);
                    
                    if (exists) {
                        WebElement slide = driver.findElement(By.id(slideId));
                        slides.add(slide);
                        log.info("슬라이드 발견: {}", slideId);
                    } else {
                        log.info("슬라이드가 존재하지 않음: {}", slideId);
                        if (i > 1) break; // 첫 번째 슬라이드 이후 발견되지 않으면 중단
                    }
                } catch (Exception e) {
                    log.warn("슬라이드 검색 중 오류: {} - {}", SLIDE_SEPARATOR_PREFIX + i, e.getMessage());
                    if (i > 1) break;
                }
            }
            
            //슬라이드 요소가 없는 경우 body
            if (slides.isEmpty()) {
                log.warn("슬라이드 요소를 찾지 못했습니다. body 태그를 사용합니다.");
                slides.add(driver.findElement(By.tagName("body")));
            }

            // 이미지 디렉토리 확인 및 생성
            File imgDirectory = new File(htmlFile.getParentFile(), IMG_DIR_NAME);
            if (imgDirectory.exists()) {
                FileUtils.cleanDirectory(imgDirectory);
            } else {
                FileUtils.forceMkdir(imgDirectory);
            }

            for (WebElement element : slides) {
                // 요소가 보이도록 스크롤
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

                // 렌더링 완료 대기
                Thread.sleep(500);

                // 요소의 위치와 크기 정보
                Rectangle rect = element.getRect();

                //스크린샷에서 요소 부분만 잘라내기
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                BufferedImage fullImg = ImageIO.read(screenshot);

                BufferedImage elementImg = fullImg.getSubimage(
                        rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                // 이미지 저장 (각 슬라이드별 파일명 지정)
                String outputFileName = element.getAttribute("id") + "."+IMG_FILE_EXT;
                File outputFile = new File(imgDirectory, outputFileName);
                ImageIO.write(elementImg, IMG_FILE_EXT, outputFile);

            }
            return true;
        }
        //TODO: Exception 처리
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
}
