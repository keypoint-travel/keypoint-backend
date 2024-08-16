package com.keypoint.keypointtravel.global.utils;

import java.io.FileOutputStream;
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

    public static void saveExcel(String filePath, byte[] excelData) throws IOException {
        // Save the byte array to a file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            fileOut.write(excelData);
        }
    }
}
