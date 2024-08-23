package com.keypoint.keypointtravel.receipt.dto.useCase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ReceiptImageUseCase {

    private MultipartFile receiptImage;

    public static ReceiptImageUseCase from(MultipartFile receiptImage) {
        return new ReceiptImageUseCase(receiptImage);
    }
}
