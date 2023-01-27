package com.blindauction.blindauctionshopproject.dto.seller;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRegisterRequest {
    private final String title;
    private final Long price;
    private final String productDetail;

    public ProductRegisterRequest(String title, Long price, String productDetail) {
        this.title = title;
        this.price = price;
        this.productDetail = productDetail;
    }
}
