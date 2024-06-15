package com.keypoint.keypointtravel.external.azure.service;

import com.keypoint.keypointtravel.api.dto.response.ReceiptDTO;
import com.keypoint.keypointtravel.api.dto.useCase.ReceiptUseCase;
import com.keypoint.keypointtravel.external.azure.dto.request.OCRAnalysisRequest;
import com.keypoint.keypointtravel.external.azure.dto.response.OCRResultResponse;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.enumType.ocr.OCROperationStatus;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import com.keypoint.keypointtravel.global.utils.AzureOCRUtils;
import com.keypoint.keypointtravel.global.utils.HttpUtils;
import com.keypoint.keypointtravel.global.utils.MultiPartFileUtils;
import com.keypoint.keypointtravel.global.utils.StringUtils;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j

public class AzureOCRService {

    private static final String RECEIPT_MODEL_ID = "prebuilt-receipt";
    private static final String AZURE_KEY_HEADER = "Ocp-Apim-Subscription-Key";
    private static final String OCR_RESULT_HEADER = "Operation-Location";
    private static final List<OCROperationStatus> VALID_OCR_STATUS = Arrays.asList(
        OCROperationStatus.FAILED,
        OCROperationStatus.SUCCEEDED
    );


    @Value("${key.azure.endpoint}")
    private String endpoint;

    @Value("${key.azure.key1}")
    private String apiKey;

    @Value("${key.azure.api-version}")
    private String appVersion;

    /**
     * OCR 분석 요청을 하는 API url 반환하는 함수
     *
     * @return
     */
    private String getOCRAnalysisUrl() {
        return String.format(
            "%sformrecognizer/documentModels/%s:analyze?api-version=%s&stringIndexType=utf16CodeUnit",
            endpoint,
            RECEIPT_MODEL_ID,
            appVersion
        );
    }

    private HttpHeaders createHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AZURE_KEY_HEADER, apiKey);

        return headers;
    }

    /**
     * OCR 분석 요청하는 함수
     *
     * @return OCR 결과 url
     */
    private String requestOCRAnalysis(String documentURL) {
        String requestURL = getOCRAnalysisUrl();
        HttpHeaders headers = createHeader();
        OCRAnalysisRequest request = OCRAnalysisRequest.from(documentURL);

        while (true) {
            try {
                ResponseEntity<String> response = HttpUtils.post(requestURL, headers, request,
                    String.class);
                return response.getHeaders().get(OCR_RESULT_HEADER).get(0);
            } catch (HttpClientErrorException ex) {
                handleHttpClientErrorException(ex);
            }
        }
    }

    /**
     * OCR 결과를 요청하는 함수
     *
     * @param url
     * @return
     */
    private OCRResultResponse requestOCRResult(String url) {
        HttpHeaders headers = createHeader();

        while (true) {
            try {
                ResponseEntity<OCRResultResponse> response = HttpUtils.get(
                    url,
                    headers,
                    OCRResultResponse.class
                );

                return response.getBody();
            } catch (HttpClientErrorException ex) {
                handleHttpClientErrorException(ex);
            }
        }
    }

    /**
     * HttpClientErrorException 에 대한 예외 처리 진행
     *
     * @param ex
     */
    private void handleHttpClientErrorException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            String retrySec = ex.getResponseHeaders().get("Retry-After").get(0);
            Integer sec = StringUtils.convertToInteger(retrySec);

            try {
                Thread.sleep(sec * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            throw new HttpClientException(ex);
        }
    }

    /**
     * (Multipart) 영수증을 분석하는 함수
     * @param useCase
     * @return
     */
    public ReceiptDTO analyzeReceipt(ReceiptUseCase useCase) {
        try {
            MultipartFile file = useCase.getFile();
            String base64Source = MultiPartFileUtils.convertToBase64(file);
            String ocrResultUrl = requestOCRAnalysis(base64Source);
            OCRResultResponse response = getOCRResult(ocrResultUrl);

            ReceiptDTO receiptDTO = processReceiptResponse(response);

            return receiptDTO;
        } catch (HttpClientException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * OCR 결과를 가공하여 ReceiptDTO를 생성하는 함수
     *
     * @param response
     * @return
     */
    private ReceiptDTO processReceiptResponse(OCRResultResponse response) {
        ReceiptDTO receiptDTO = ReceiptDTO.from(
            response.getAnalyzeResult().getDocuments().get(0));
        String content = response.getAnalyzeResult().getContent();

        receiptDTO.setCardNumber(AzureOCRUtils.extractCardNumber(content));
        receiptDTO.setApprovalNumber(AzureOCRUtils.extractApprovalNumber(content));
        receiptDTO.setRewardPoint(AzureOCRUtils.extractRewardPoints(content));
        receiptDTO.setAvailablePoint(AzureOCRUtils.extractAvailablePoints(content));

        return receiptDTO;
    }

    /**
     * OCR 결과를 가져오는 함수
     *
     * @param ocrResultUrl
     * @return
     */
    private OCRResultResponse getOCRResult(String ocrResultUrl) {
        OCROperationStatus status;
        OCRResultResponse ocrResult;

        // 1. OCR 이미지 분석이 마무리 될 때까지 요청
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ocrResult = requestOCRResult(
                ocrResultUrl
            );
            status = ocrResult.getStatus();
        } while (!VALID_OCR_STATUS.contains(status));

        // 2. 결과가 성공인 경우에만 결과 반환
        if (status == OCROperationStatus.SUCCEEDED) {
            return ocrResult;
        } else {
            throw new GeneralException(ReceiptError.RECEIPT_RECOGNITION_FAILED);
        }
    }

}
