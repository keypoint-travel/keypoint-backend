package com.keypoint.keypointtravel.global.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ExcelUtils {
    
    public static Workbook readExcel(String path) throws IOException {
        InputStream inputStream = new ClassPathResource(path).getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        return workbook;
    }
}
