package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.global.constants.PlaceConstants;
import com.keypoint.keypointtravel.global.utils.ExcelUtils;
import com.keypoint.keypointtravel.place.dto.useCase.cityByCountryUseCase.CityByCountryContentUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.cityByCountryUseCase.CityByCountryUseCase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CityService {

    private final CountriesnowService countriesnowService;


    /**
     * 주요 국가의 도시 데이터를 저장하는 함수
     */
    public void generateCityData() throws IOException {
        // 1. 주요 국가 도시 리스트 생성
        Map<String, List<String>> cities = getCities();

        // 2. 엑셀 생성
        createExcelFile(cities, System.getProperty("user.dir") + "/cities-final.xlsx");
    }

    /**
     * 도시 API로 부터 주요 국가 도시 리스트만 필터링해서 반환
     *
     * @return
     */
    private Map<String, List<String>> getCities() {
        Map<String, List<String>> cities = new HashMap<>();
        // 1. 전체 국가 도시 리스트 API 호출
        CityByCountryUseCase useCase = countriesnowService.getCitiesByCountry();

        // 2. 주요국가의 도시 데이터 필터링
        for (CityByCountryContentUseCase country : useCase.getData()) {
            String iso2 = country.getIso2();
            if (PlaceConstants.MAYOR_COUNTRIES.contains(iso2)) {
                cities.put(iso2, country.getCities());
            }
        }
        return cities;
    }

    /**
     * 엑셀 생성 함수 (공유 목적)
     *
     * @param cities
     * @return
     * @throws IOException
     */
    private void createExcelFile(Map<String, List<String>> cities, String filePath)
        throws IOException {
        // 1. 엑셀 데이터 생성
        try (Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("major_country_cities");

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("iso2");
            headerRow.createCell(1).setCellValue("EN");
            headerRow.createCell(2).setCellValue("KO");
            headerRow.createCell(3).setCellValue("JA");
            headerRow.createCell(4).setCellValue("longitude");
            headerRow.createCell(5).setCellValue("latitude");

            // 데이터 행 생성
            int rowNum = 1;
            for (Map.Entry<String, List<String>> entry : cities.entrySet()) {
                String iso2 = entry.getKey();

                for (String city : entry.getValue()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(iso2);
                    row.createCell(1).setCellValue(city);
                }
            }
            workbook.write(out);

            // 2. 엑셀 저장
            ExcelUtils.saveExcel(filePath, out.toByteArray());
        }
    }

}
