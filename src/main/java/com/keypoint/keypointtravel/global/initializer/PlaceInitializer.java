package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.global.constants.PlaceConstants;
import com.keypoint.keypointtravel.global.utils.ExcelUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.place.dto.useCase.CityExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.service.CountryService;
import com.keypoint.keypointtravel.place.service.PlaceService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class PlaceInitializer {

    private final PlaceService placeService;
    private final CountryService countryService;

    public PlaceInitializer(PlaceService placeService, CountryService countryService) {
        this.placeService = placeService;
        this.countryService = countryService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyPlaceData() {
        try {
            // Place 가 존재하는지 확인
            if (placeService.countAllPlaces() > 0L) {
                return;
            }

            Workbook workbook = ExcelUtils.readExcel("static/excel/countries-v1_0.xlsx");

            // 1, 국가, 주요 국가 도시 Object 생성
            List<CountryExcelUseCase> countries = createCountryExcelUseCase(workbook);
            Map<String, List<CityExcelUseCase>> cities = createCityCountryExcelUseCase(workbook);

            // 2. Country&Place 생성
            // Country 를 추가하면서, 만약 주요 국가인 경우 도시 데이터 함께 추가
            for (CountryExcelUseCase country : countries) {
                // 2-1. 국가 추가
                Country newCountry = countryService.addCountry(country);
                placeService.addPlaceForCountry(newCountry, country);

                // 2-2. 도시 추가
                String iso2 = country.getIso2();
                if (PlaceConstants.MAYOR_COUNTRIES.contains(iso2)) {
                    for (CityExcelUseCase city : cities.get(iso2)) {
                        placeService.addPlaceForCity(newCountry, city);
                    }
                }
            }
        } catch (Exception ex) {
            LogUtils.writeErrorLog("generateDummyPlaceData", "Fail to generate places");
        }

    }

    private Map<String, List<CityExcelUseCase>> createCityCountryExcelUseCase(Workbook workbook) {
        Map<String, List<CityExcelUseCase>> map = new HashMap<>();
        for (String iso2 : PlaceConstants.MAYOR_COUNTRIES) {
            map.put(iso2, new ArrayList<>());
        }

        Sheet sheet = workbook.getSheetAt(1);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            try {
                CityExcelUseCase city = CityExcelUseCase.of(
                    row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue(),
                    row.getCell(4) == null ? null : row.getCell(4).getNumericCellValue(),
                    row.getCell(5) == null ? null : row.getCell(5).getNumericCellValue()
                );

                map.get(city.getIso2()).add(city);

            } catch (Exception ex) {
                LogUtils.writeErrorLog("generateDummyPlaceData", "Fail to generate places");
            }


        }

        return map;
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
                    row.getCell(4).getNumericCellValue(),
                    row.getCell(5).getNumericCellValue()
                )
            );
        }

        return useCase;
    }

}
