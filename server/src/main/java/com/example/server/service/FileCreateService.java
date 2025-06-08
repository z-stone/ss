package com.example.server.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j

//TODO 상수, 예외 처리 정리
public class FileCreateService {

    @Value("${app.directory.root.path}")
    private String rootDirPath;

    @Value("${app.directory.image.name}")
    private String IMG_DIR_NAME;

    @Value("${app.file.image.extension}")
    private String IMG_FILE_EXT;

    @Value("${app.file.ppt.name}")
    private String PPT_FILE_NAME;

    @Value("${app.file.html.name}")
    private String HTML_FILE_NAME;

    public boolean createHtmlFile(String htmlString){
        try {
            //DIR 생성
            File directory = new File(rootDirPath);
            FileUtils.forceMkdir(directory);

            //HTML 파일 생성
            File htmlFile = new File(directory, HTML_FILE_NAME);
            FileUtils.writeStringToFile(htmlFile, htmlString, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            log.error("HTML 파일 생성 중 오류가 발생했습니다. {}",e.getMessage());
            return false;
        }
    }
    /**
     * 지정된 디렉토리의 이미지를 사용하여 PPT 파일을 생성
     */
    public boolean createPptFile() {
        try {
            File imgDir = new File(rootDirPath +File.separator+IMG_DIR_NAME);

            // 디렉토리에서 이미지 파일 가져오기
            List<File> imageFiles = getImageFiles(imgDir);

            if (imageFiles.isEmpty()) {
                return false;
            }

            // PPT 생성
            createPPTFromImages(imageFiles, rootDirPath +File.separator+PPT_FILE_NAME);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 디렉토리에서 PNG 이미지 파일을 가져와 정렬.
     */
    private List<File> getImageFiles(File imgDir) throws IOException {

        // PNG 파일만 필터링 (하위 디렉토리 검색 안함)
        Collection<File> imgFiles = FileUtils.listFiles(
                imgDir,
                new SuffixFileFilter(IMG_FILE_EXT),
                FalseFileFilter.INSTANCE
        );

        // 이미지 순서대로 정렬
        return imgFiles.stream()
                .sorted(Comparator.comparingInt(file -> {
                    String fileName = file.getName();
                    try {
                        //숫자 부분 추출
                        String numPart = fileName.replaceAll("[^0-9]", "");
                        return numPart.isEmpty() ? 0 : Integer.parseInt(numPart);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }))
                .collect(Collectors.toList());
    }

    /**
     * 이미지 파일로부터 PPT를 생성.
     */
    private void createPPTFromImages(List<File> imageFiles, String outputPath) throws IOException {
        // 새 프레젠테이션 생성
        XMLSlideShow ppt = new XMLSlideShow();

        // 첫 번째 이미지의 크기 읽기
        BufferedImage firstImage = ImageIO.read(imageFiles.get(0));
        int imgWidth = firstImage.getWidth();
        int imgHeight = firstImage.getHeight();

        // 슬라이드 크기를 이미지 크기에 맞게 설정
        ppt.setPageSize(new Dimension(imgWidth, imgHeight));

        for (File imageFile : imageFiles) {
            log.info("슬라이드 {} 추가 중 ", imageFile.getName());
            XSLFSlide slide = ppt.createSlide();
            XSLFPictureData pictureData = ppt.addPicture(imageFile, PictureData.PictureType.PNG);
            XSLFPictureShape picture = slide.createPicture(pictureData);
            picture.setAnchor(new Rectangle2D.Double(0, 0, imgWidth, imgHeight));
        }

        // PPT 파일 저장
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            ppt.write(out);
        }

        System.out.println("프레젠테이션이 저장되었습니다: " + outputPath);
    }
}
