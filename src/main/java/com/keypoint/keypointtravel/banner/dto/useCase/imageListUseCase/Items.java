package com.keypoint.keypointtravel.banner.dto.useCase.imageListUseCase;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class Items {
    private List<Item> item;

    public Items(List<Item> item) {
        this.item = item;
    }

    public Items(String item) {
        this.item = Collections.emptyList();
    }
}