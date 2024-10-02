package com.keypoint.keypointtravel.global.initializer;

import com.keypoint.keypointtravel.global.constants.PlaceConstants;
import com.keypoint.keypointtravel.global.utils.ExcelUtils;
import com.keypoint.keypointtravel.global.utils.LogUtils;
import com.keypoint.keypointtravel.place.dto.useCase.CityExcelUseCase;
import com.keypoint.keypointtravel.place.dto.useCase.CountryExcelUseCase;
import com.keypoint.keypointtravel.place.entity.Country;
import com.keypoint.keypointtravel.place.entity.Place;
import com.keypoint.keypointtravel.place.entity.PlaceVersionHistory;
import com.keypoint.keypointtravel.place.repository.PlaceVersionHistoryRepository;
import com.keypoint.keypointtravel.place.service.CountryService;
import com.keypoint.keypointtravel.place.service.PlaceService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PlaceInitializer {

    private final static String VERSION = "1.0";

    private final PlaceService placeService;
    private final CountryService countryService;
    private final PlaceVersionHistoryRepository placeVersionHistoryRepository;

    public PlaceInitializer(
        PlaceService placeService,
        CountryService countryService,
        PlaceVersionHistoryRepository placeVersionHistoryRepository
    ) {
        this.placeService = placeService;
        this.countryService = countryService;
        this.placeVersionHistoryRepository = placeVersionHistoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void generateDummyPlaceData() {
        try {
            // Place 가 존재하는지 확인
            if (placeVersionHistoryRepository.existsByVersion(VERSION)) {
                return;
            }

            Workbook workbook = ExcelUtils.readExcel("static/excel/countries-v1_0.xlsx");

            // 1, 국가, 주요 국가 도시 Object 생성
            List<Place> existedPlaces = placeService.findAllPlacesForCity();
            List<CountryExcelUseCase> countries = createCountryExcelUseCase(workbook);
            Map<String, List<CityExcelUseCase>> cities = createCityCountryExcelUseCase(workbook);

            if (existedPlaces.isEmpty()) {
                // 2-1. 기존의 데이터가 존재하지 않았던 경우, Country&Place 생성
                // Country 를 추가하면서, 만약 주요 국가인 경우 도시 데이터 함께 추가
                // City 인 경우, 엑셀의 no가 cityId로 저장
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
            } else {
                // 2-2 기존에 데이터가 존재했던 경우
                for (List<CityExcelUseCase> places : cities.values()) {
                    for (CityExcelUseCase city : places) {
                        Long cityId = city.getId();
                        if (cityId > existedPlaces.size()) {
                            // 새로 생성해야 하는 데이터인 경우 (현재 X)
                        }

                        // 이미 존재하는 데이터인 경우 (유지, 수정)
                        Place checkPlace = existedPlaces.get(cityId.intValue() - 1);
                        if (!checkPlace.equals(city)) {
                            // 수정
                            placeService.updatePlace(city);
                        }

                        // 삭제해야 하는 데이터인 경우 (현재 X)
                    }
                }
            }

            // 3. 버전 데이터 생성
            PlaceVersionHistory placeVersionHistory = PlaceVersionHistory.from(VERSION);
            placeVersionHistoryRepository.save(placeVersionHistory);
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

            CityExcelUseCase city = CityExcelUseCase.of(
                row.getCell(0) == null ? null : (long) row.getCell(0).getNumericCellValue(),
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                row.getCell(4).getStringCellValue(),
                row.getCell(5) == null ? null : row.getCell(5).getNumericCellValue(),
                row.getCell(6) == null ? null : row.getCell(6).getNumericCellValue()
            );
            map.get(city.getIso2()).add(city);
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
