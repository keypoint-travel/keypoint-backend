package com.keypoint.keypointtravel.service.api;

import com.keypoint.keypointtravel.common.enumType.api.OCROperationStatus;
import com.keypoint.keypointtravel.common.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.common.exception.GeneralException;
import com.keypoint.keypointtravel.common.utils.HttpUtils;
import com.keypoint.keypointtravel.dto.api.azure.request.OCRAnalysisRequest;
import com.keypoint.keypointtravel.dto.api.azure.response.OCRResultResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    private String requestOCRAnalysis() {
        String url = getOCRAnalysisUrl();
        //String documentUrl = "https://raw.githubusercontent.com/Azure-Samples/cognitive-services-REST-api-samples/master/curl/form-recognizer/rest-api/receipt.png";
        String documentUrl = "https://github.com/chea-young/Dev_Springboot/assets/63410509/ce667733-4fed-4109-bae2-08c65058bb1c";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(AZURE_KEY_HEADER, apiKey);

        OCRAnalysisRequest request = OCRAnalysisRequest.toRequest(documentUrl);

        ResponseEntity<String> response = HttpUtils.post(url, headers, request, String.class);

        return response.getHeaders().get(OCR_RESULT_HEADER).get(0);
    }

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


    public OCRResultResponse analyzeReceipt() {
        String ocrResultUrl = requestOCRAnalysis();
        OCRResultResponse response = getOCRResult(ocrResultUrl);

        // OCRResultResponse -> ReceiptDTO로 변환
        return response;
    }

    private OCRResultResponse getOCRResult(String ocrResultUrl) {
        OCROperationStatus status;
        OCRResultResponse ocrResult;

        do {
            ocrResult = requestOCRResult(
                ocrResultUrl
            );
            status = ocrResult.getStatus();
        } while (!VALID_OCR_STATUS.contains(status));

        if (status == OCROperationStatus.SUCCEEDED) {
            return ocrResult;
        } else {
            throw new GeneralException(ReceiptError.RECEIPT_RECOGNITION_FAILED);
        }
    }

}
