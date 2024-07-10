package com.keypoint.keypointtravel.banner.dto.useCase.tourListUseCase;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

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