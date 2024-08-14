package com.keypoint.keypointtravel.place.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keypoint.keypointtravel.global.utils.ExcelUtils;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CountryService {
    
    public void generateCountryData() throws IOException {
        // 1. 전체 국가 엑셀 파일 Load
        Workbook workbookCountries = ExcelUtils.readExcel("null");

        // 2. Object로 생성
        List<CountryExcelUseCase> countries = createCountryExcelUseCase(workbookCountries);

        // 3. 좌표, 경도 추가

        // 4. 좌표, 경도 추가 데이터 Excel로 저장
        createExcelFile(countries);

        // 5. Country & Place 저장
    }

    private List<CountryExcelUseCase> createCountryExcelUseCase(Workbook workbook) throws IOException {
        List<CountryExcelUseCase> useCase = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            useCase.add(
                    CountryExcelUseCase.of(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue()));
        }

        return useCase;
    }
    
public byte[] createExcelFile(List<CountryExcelUseCase> useCases) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("MyObjects");

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Field1");
            headerRow.createCell(1).setCellValue("Field2");
            headerRow.createCell(2).setCellValue("Field3");

            // 데이터 행 생성
            int rowNum = 1;
            for (CountryExcelUseCase useCase : useCases) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(useCase.getCountryCode());
                row.createCell(1).setCellValue(useCase.getName_EN());
                row.createCell(2).setCellValue(useCase.getName_JP());
                row.createCell(3).setCellValue(useCase.getName_KO());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
