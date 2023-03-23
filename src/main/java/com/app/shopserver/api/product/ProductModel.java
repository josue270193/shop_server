package com.app.shopserver.api.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("products")
public record ProductModel(
        @Id String id,
        String name,
        String headline,
        String description,
        Float ranking,
        List<ProductPresentationModel> presentations,
        List<ProductCompositionModel> compositions,
        List<ProductPhotoModel> photos
) {

}
