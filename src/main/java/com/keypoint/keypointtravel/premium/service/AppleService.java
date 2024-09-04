package com.keypoint.keypointtravel.premium.service;

import com.keypoint.keypointtravel.global.enumType.premium.PurchaseStatus;
import com.keypoint.keypointtravel.premium.dto.UserReceipt;
import com.keypoint.keypointtravel.premium.dto.apple.AppStoreResponse;
import com.keypoint.keypointtravel.premium.dto.apple.AppleReceipt;
import com.keypoint.keypointtravel.premium.dto.apple.InApp;
import com.keypoint.keypointtravel.premium.dto.apple.UserReceiptInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AppleService {

    private final ApplePurchaseApiService applePurchaseApiService;

    private final ApplePurchaseApiTestService applePurchaseApiTestService;


    public String updatePurchaseHistory(UserReceipt userReceipt) {

        AppStoreResponse appStoreResponse;
        try {
            appStoreResponse = verifyReceipt(userReceipt);
        } catch (Exception e) {
            System.out.println("Verify receipt fail..");
            e.printStackTrace();
            return "Fail";
        }
        UserReceiptInfo userReceiptInfo = null;
        if (appStoreResponse.getReceipt().getInApp().size() == 1) { // 소모품 (Consumable)
            userReceiptInfo = getUserReceiptInfo(appStoreResponse.getReceipt());
        } else {
            // TODO - 비소모품(Non-Consumable), 자동 갱신 구독(Auto-Renewable Subscription), 비자동 갱신 구독(Non-Renewable Subscription)에 대한 로직 필요.
        }
        return "Success";
    }

    private UserReceiptInfo getUserReceiptInfo(AppleReceipt receipt) {
        InApp inApp = receipt.getInApp().get(0);
        Date purchaseDate = new Date(Long.parseLong(inApp.getPurchaseDateMs()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(purchaseDate);
        calendar.add(Calendar.MONTH, 1);
        Date expirationDate = calendar.getTime();
        return new UserReceiptInfo(
            receipt.getApp_item_id(),
            inApp.getProductId(),
            inApp.getTransactionId(),
            inApp.getOriginalTransactionId(),
            purchaseDate,
            expirationDate,
            PurchaseStatus.COMPLETED);
    }

    private AppStoreResponse verifyReceipt(UserReceipt userReceipt) {
        Map<String, String> appStoreRequest = new HashMap<>();
        appStoreRequest.put("receipt-data", userReceipt.getReceiptData());
        AppStoreResponse response = applePurchaseApiService.verifyReceipt(appStoreRequest);
        int statusCode = response.getStatus();
        if (statusCode == 21007) {
            response = applePurchaseApiTestService.verifyReceiptTest(appStoreRequest);
        } else if (statusCode != 0) {
            verifyStatusCode(statusCode);
        }
        return response;
    }

    private void verifyStatusCode(int statusCode) {
        switch (statusCode) {
            case 21000:
                System.out.println("[Status code: " + statusCode + "] The request to the App Store was not made using the HTTP POST request method.");
                break;
            case 21001:
                System.out.println("[Status code: " + statusCode + "] This status code is no longer sent by the App Store.");
                break;
            case 21002:
                System.out.println("[Status code: " + statusCode + "] The data in the receipt-data property was malformed or the service experienced a temporary issue. Try again.");
                break;
            case 21003:
                System.out.println("[Status code: " + statusCode + "] The receipt could not be authenticated.");
                break;
            case 21004:
                System.out.println("[Status code: " + statusCode + "] The shared secret you provided does not match the shared secret on file for your account.");
                break;
            case 21005:
                System.out.println("[Status code: " + statusCode + "] The receipt server was temporarily unable to provide the receipt. Try again.");
                break;
            case 21006:
                System.out.println("[Status code: " + statusCode + "] This receipt is valid but the subscription has expired. When this status code is returned to your server, the receipt data is also decoded and returned as part of the response. Only returned for iOS 6-style transaction receipts for auto-renewable subscriptions.");
                break;
            case 21008:
                System.out.println("[Status code: " + statusCode + "] This receipt is from the production environment, but it was sent to the test environment for verification.");
                break;
            case 21009:
                System.out.println("[Status code: " + statusCode + "] Internal data access error. Try again later.");
                break;
            case 21010:
                System.out.println("[Status code: " + statusCode + "] The user account cannot be found or has been deleted.");
                break;
            default:
                System.out.println("[Status code: " + statusCode + "] The receipt for the App Store is incorrect.");
                break;
        }

        throw new IllegalStateException("[/verifyReceipt] The receipt for the App Store is incorrect.");
    }


}
