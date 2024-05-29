package com.keypoint.keypointtravel.dto.recipt.response;

import java.util.List;
import lombok.Data;

@Data
public class ReceiptDTO {

    private String receiptType; // 영수증 타입
    private String merchantName;
    private String merchantPhoneNumber;
    private String merchantAddress;
    private double total;
    private String unit; // 돈 단위 (엔, 달러, 원) $
    private String transactionDate; // YYYY-MM-DD
    private String transactionTime; // HH:mm:ss
    private String subtotal; // total에서 tax를 뺀 금액
    private String tax;
    private List<ReceiptItemDTO> items; // 결제 항목에 대한 세부 데이터
}
