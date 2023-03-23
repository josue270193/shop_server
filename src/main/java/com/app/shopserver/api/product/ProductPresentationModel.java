package com.app.shopserver.api.product;

public record ProductPresentationModel(
        String name,
        PresentationType type,
        String highlightPhotoId,
        Float price,
        Integer available
) {
}
