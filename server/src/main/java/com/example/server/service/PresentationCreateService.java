package com.example.server.service;

import com.example.server.component.PresentationFileAccessor;
import com.example.server.constants.PresentationConstant;
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
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PresentationCreateService extends PresentationFileAccessor {

    public boolean createHtmlFile(String htmlString){
        try {

            FileUtils.forceMkdir(this.getWorkDir());

            File htmlFile = this.getHtmlFile();

            if (htmlFile.exists()) htmlFile.delete();

            FileUtils.writeStringToFile(htmlFile, htmlString, StandardCharsets.UTF_8);
            log.info("html 파일 생성");

            return true;

        } catch (Exception e) {
            log.error("HTML 파일 생성 중 오류가 발생했습니다. {}",e.getMessage());
            return false;
        }
    }

    /**
     * 지정된 디렉토리의 이미지를 사용하여 PPT 파일을 생성
     */
    public boolean createPptFile() {
        try {

            List<File> imageFiles = getImageFiles(this.getCaptureDir());

            if (imageFiles.isEmpty()) {
                return false;
            }

            createPPTFromImages(imageFiles, this.getPptFile());// PPT 파일 생성

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
                new SuffixFileFilter(PresentationConstant.CAPTURE.EXT.getValue()),
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
     * 이미지 파일로부터 PPT 생성
     */
    private void createPPTFromImages(List<File> imageFiles, File pptFile) throws Exception{
            // 기존 파일 존재 여부 확인 및 삭제
            if (pptFile.exists()) pptFile.delete();

            try (XMLSlideShow ppt = new XMLSlideShow()) {

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
                try (OutputStream out = new FileOutputStream(pptFile)) {
                    ppt.write(out);
                    log.info("PPT 파일이 저장되었습니다: {}", pptFile.getPath());
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
    }
}
