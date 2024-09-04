package com.keypoint.keypointtravel.premium.controller;

import com.keypoint.keypointtravel.premium.dto.UserReceipt;
import com.keypoint.keypointtravel.premium.service.AppleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AppleService appleService;

    @PostMapping("/purchase")
    public ResponseEntity<String> purchase(@RequestBody UserReceipt userReceipt) {

        String code = appleService.updatePurchaseHistory(userReceipt);

        return ResponseEntity.ok(code);
    }
}
