package com.keypoint.keypointtravel.place.service;

import com.keypoint.keypointtravel.global.utils.ExcelUtils;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase.CountryDetailContentUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.countryDetailUseCase.CountryDetailUseCase;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.repository.CountryRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class CountryService {

    private final CountriesnowService countriesnowService;
    private final CountryRepository countryRepository;

    /**
     * 전체 국가 리스트 (외교부_국가·지역별 표준코드 데이터) 에서 필요한 데이터를 채워서 저장하는 함수
     *
     * @throws IOException
     */
    public void generateCountryData() throws IOException {
        // 1. 전체 국가 엑셀 파일 Load
        Workbook workbookCountries = ExcelUtils.readExcel("static/excel/base-countries.xlsx");

        // 2. Object로 생성
        List<CountryExcelUseCase> basecCuntries = createCountryExcelUseCase(workbookCountries);

        // 3. 좌표, 경도 추가
        List<CountryExcelUseCase> countries = fillLocation(basecCuntries);

        // 4. 좌표, 경도 추가 데이터 Excel로 저장
        createExcelFile(countries, System.getProperty("user.dir") + "/countries-final.xlsx");
    }

    /**
     * 국가 Excel 데이터를 Object로 변환하는 함수
     *
     * @param workbook
     * @return
     * @throws IOException
     */
    private List<CountryExcelUseCase> createCountryExcelUseCase(Workbook workbook) {
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
                    row.getCell(3).getStringCellValue(),
                    null,
                    null
                ));
        }

        return useCase;
    }

    /**
     * 엑셀 생성 함수 (공유 목적)
     *
     * @param useCases
     * @return
     * @throws IOException
     */
    private void createExcelFile(List<CountryExcelUseCase> useCases, String filePath)
        throws IOException {
        // 1. 엑셀 데이터 생성
        try (Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("countries");

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
            for (CountryExcelUseCase useCase : useCases) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(useCase.getIso2());
                row.createCell(1).setCellValue(useCase.getNameEN());
                row.createCell(2).setCellValue(useCase.getNameKO());
                row.createCell(3).setCellValue(useCase.getNameJA());

                Double longitude = useCase.getLongitude();
                Double latitude = useCase.getLatitude();

                row.createCell(4).setCellValue(longitude != null ? longitude : 0.0);
                row.createCell(5).setCellValue(latitude != null ? latitude : 0.0);
            }

            workbook.write(out);

            // 2. 엑셀 저장
            ExcelUtils.saveExcel(filePath, out.toByteArray());
        }
    }

    /**
     * 국가 API 를 이용해서 국가의 경도, 위도 좌표를 채우는 함수
     *
     * @param excelCountries
     * @return
     */
    private List<CountryExcelUseCase> fillLocation(List<CountryExcelUseCase> excelCountries) {
        // 1. 국가 좌표 반환 API 호춫
        CountryDetailUseCase countryDetails = countriesnowService.getCountryDetails();

        // 2. List<CountryExcelUseCase> 에 경도, 위도 추가
        for (CountryExcelUseCase excelCountry : excelCountries) {
            for (CountryDetailContentUseCase detailCountry : countryDetails.getData()) {
                if (excelCountry.getIso2().equalsIgnoreCase(detailCountry.getIso2())) {
                    excelCountry.setLocation(
                        detailCountry.getLongitude(),
                        detailCountry.getLatitude()
                    );
                    break;
                }
            }
        }

        return excelCountries;
    }

    /**
     * Country 저장하는 함수
     *
     * @param useCase
     * @return
     */
    @Transactional
    public Country addCountry(CountryExcelUseCase useCase) {
        Country country = useCase.toCountryEntity();
        countryRepository.save(country);

        return country;
    }
}

