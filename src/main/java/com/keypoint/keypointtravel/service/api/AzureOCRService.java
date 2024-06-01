package com.keypoint.keypointtravel.service.api;

import com.keypoint.keypointtravel.common.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.common.enumType.ocr.OCROperationStatus;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.utils.HttpUtils;
import com.keypoint.keypointtravel.common.utils.MultiPartFileUtils;
import com.keypoint.keypointtravel.dto.api.azure.request.OCRAnalysisRequest;
import com.keypoint.keypointtravel.dto.api.azure.response.OCRResultResponse;
import com.keypoint.keypointtravel.dto.recipt.response.ReceiptDTO;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AzureOCRService {

    private static String RECEIPT_MODEL_ID = "prebuilt-receipt";
    private static String AZURE_KEY_HEADER = "Ocp-Apim-Subscription-Key";
    private static String OCR_RESULT_HEADER = "Operation-Location";
    private static List<OCROperationStatus> VALID_OCR_STATUS = Arrays.asList(
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

    /**
     * OCR 분석 요청하는 함수
     *
     * @return OCR 결과 url
     */
    private String requestOCRAnalysis(String documentURL) {
        String requestURL = getOCRAnalysisUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AZURE_KEY_HEADER, apiKey);

        OCRAnalysisRequest request = OCRAnalysisRequest.toRequest(documentURL);

        ResponseEntity<String> response = HttpUtils.post(requestURL, headers, request,
            String.class);

        return response.getHeaders().get(OCR_RESULT_HEADER).get(0);
    }

    /**
     * OCR 결과를 요청하는 함수
     *
     * @param url
     * @return
     */
    private OCRResultResponse requestOCRResult(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AZURE_KEY_HEADER, apiKey);

        ResponseEntity<OCRResultResponse> response = HttpUtils.get(
            url,
            headers,
            OCRResultResponse.class
        );

        return response.getBody();
    }

    /**
     * (Multipart) 영수증을 분석하는 함수
     *
     * @param file
     * @return
     */
    public ReceiptDTO analyzeReceipt(MultipartFile file) {
        try {
            String base64Source = MultiPartFileUtils.convertToBase64(file);
            String ocrResultUrl = requestOCRAnalysis(base64Source);
            OCRResultResponse response = getOCRResult(ocrResultUrl);

            ReceiptDTO receiptDTO = ReceiptDTO.toDTO(
                response.getAnalyzeResult().getDocuments().get(0));
            return receiptDTO;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * OCR 결과를 가져오는 함수
     * @param ocrResultUrl
     * @return
     */
    private OCRResultResponse getOCRResult(String ocrResultUrl) {
        OCROperationStatus status;
        OCRResultResponse ocrResult;

        // 1. OCR 이미지 분석이 마무리 될 때까지 요청
        do {
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
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
