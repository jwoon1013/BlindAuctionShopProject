package com.blindauction.blindauctionshopproject.dto.user;

import com.blindauction.blindauctionshopproject.entity.PurchasePermission;
import com.blindauction.blindauctionshopproject.entity.PermissionStatusEnum;
import lombok.Getter;

@Getter
public class PurchaseStatusGetResponse {
    private Long productId;
    private String title;
    private PermissionStatusEnum permissionStatusEnum;

    public PurchaseStatusGetResponse(PurchasePermission purchasePermission) {
        this.productId = purchasePermission.getProduct().getId();
        this.title = purchasePermission.getProduct().getTitle();
        this.permissionStatusEnum = purchasePermission.getTransactionStatus();
    }
}
