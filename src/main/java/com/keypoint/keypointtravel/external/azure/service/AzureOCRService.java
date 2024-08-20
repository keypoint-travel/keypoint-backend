package com.keypoint.keypointtravel.external.azure.service;

import com.keypoint.keypointtravel.api.dto.useCase.ReceiptUseCase;
import com.keypoint.keypointtravel.external.azure.dto.response.OCRResultResponse;
import com.keypoint.keypointtravel.external.azure.dto.useCase.OCRAnalysisUseCase;
import com.keypoint.keypointtravel.external.azure.dto.useCase.WholeReceiptUseCase;
import com.keypoint.keypointtravel.global.enumType.error.ReceiptError;
import com.keypoint.keypointtravel.global.exception.GeneralException;
import com.keypoint.keypointtravel.global.exception.HttpClientException;
import com.keypoint.keypointtravel.global.utils.MultiPartFileUtils;
import com.keypoint.keypointtravel.receipt.dto.response.receiptOCRResult.ReceiptOCRResponse;
import com.keypoint.keypointtravel.receipt.dto.useCase.ReceiptImageUseCase;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class AzureOCRService {

    private static final String OCR_RESULT_HEADER = "Operation-Location";
    private final AzureAPIService azureAPIService;
    private final OCRRetryableService ocrRetryableService;

    @Value("${key.azure.key1}")
    private String apiKey;
    
    /**
     * 영수증 분석 결과를 반환하는 함수
     *
     * @param useCase 분석할 영수증 이미지
     * @return
     */
    public ReceiptOCRResponse analyzeReceipt(ReceiptImageUseCase useCase) {
        try {
            MultipartFile file = useCase.getReceiptImage();

            // 1. ocr 분석 요청 및 결과 URL 가져오기
            String base64Source = MultiPartFileUtils.convertToBase64(file);
            String ocrResultURL = getOCRResultURL(base64Source);

            // 2. ocr 결과 가져오기
            OCRResultResponse response = ocrRetryableService.getOCRResult(ocrResultURL);
            WholeReceiptUseCase dto = WholeReceiptUseCase.from(
                response.getAnalyzeResult().getDocuments().get(0));
            return ReceiptOCRResponse.from(dto);
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * OCR 분석 요청하는 함수
     *
     * @return OCR 결과 url
     */
    private String getOCRResultURL(String documentURL) {
        ResponseEntity<String> requestResult = azureAPIService.requestOCRAnalysis(
            apiKey,
            OCRAnalysisUseCase.from(documentURL)
        );
        if (requestResult.getStatusCode() == HttpStatus.ACCEPTED) {
            return Objects.requireNonNull(requestResult.getHeaders().get(OCR_RESULT_HEADER)).get(0);
        } else {
            throw new GeneralException(ReceiptError.RECEIPT_RECOGNITION_FAILED);
        }
    }

    /**
     * (Multipart) 영수증을 분석하는 함수
     *
     * @param useCase
     * @return
     */
    public WholeReceiptUseCase analyzeReceipt(ReceiptUseCase useCase) {
        try {
            MultipartFile file = useCase.getFile();
            String base64Source = MultiPartFileUtils.convertToBase64(file);
            String ocrResultUrl = getOCRResultURL(base64Source);
            OCRResultResponse response = ocrRetryableService.getOCRResult(ocrResultUrl);

            return WholeReceiptUseCase.from(
                response.getAnalyzeResult().getDocuments().get(0));
        } catch (HttpClientException e) {
            throw e;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }
}
