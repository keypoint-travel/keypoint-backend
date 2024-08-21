package com.keypoint.keypointtravel.external.azure.service;

import com.keypoint.keypointtravel.external.azure.dto.response.OCRResultResponse;
import com.keypoint.keypointtravel.global.constants.AzureAPIConstants;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.enumType.ocr.OCROperationStatus;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OCRRetryableService {

    private static final List<OCROperationStatus> VALID_OCR_STATUS = Arrays.asList(
        OCROperationStatus.FAILED,
        OCROperationStatus.SUCCEEDED
    );
    private final AzureAPIService azureAPIService;
    @Value("${key.azure.key1}")
    private String apiKey;

    /**
     * OCR 결과를 가져오는 함수
     *
     * @param ocrResultURL
     * @return
     */
    @Retryable(
        value = {IllegalStateException.class},
        maxAttempts = 30,
        backoff = @Backoff(delay = 5000)  // 5초 간격으로 재시도
    )
    public OCRResultResponse getOCRResult(String ocrResultURL) {
        URI ocrResultURI = URI.create(ocrResultURL);
        String requestPath = ocrResultURI.getPath();

        // 1. OCR 결과 요청
        ResponseEntity<OCRResultResponse> ocrResult = azureAPIService.requestOCRResult(
            requestPath,
            AzureAPIConstants.API_VERSION,
            apiKey
        );
        OCRResultResponse response = ocrResult.getBody();

        log.info(response.getStatus().toString());
        // 2. 응답 상태 확인
        if (!VALID_OCR_STATUS.contains(response.getStatus())) {
            // 2-1. 아직 분석을 진행 중이거나 시작하지 않은 경우 재시도 요청
            throw new IllegalStateException("OCR process not completed.");
        } else {
            return response;
        }
    }

    /**
     * 영수증 분석 재시도에 실패한 경우 예외 처리
     *
     * @param e
     * @param ocrResultURL
     * @return
     */
    @Recover
    public OCRResultResponse recover(RestClientException e, String ocrResultURL) {
        throw new GeneralException(ReceiptError.RECEIPT_RECOGNITION_FAILED,
            "영수증 분석 결과 조회를 실패하였습니다.");
    }
}
