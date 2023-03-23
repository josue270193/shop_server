package com.app.shopserver.api.product;

import java.util.List;

public record ProductCompositionModel(
        String name,
        ProductCompositionType type,
        List<ProductIngredientModel> ingredients
) { }
