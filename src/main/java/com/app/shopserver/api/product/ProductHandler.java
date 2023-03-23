package com.app.shopserver.api.product;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static com.app.shopserver.api.product.PresentationType.SIZE;
import static com.app.shopserver.api.product.ProductCompositionType.HEART_NOTE;
import static com.app.shopserver.api.product.ProductCompositionType.TOP_NOTE;

@Component
public class ProductHandler {

    private static final String QUERY_PARAM_NAME = "name";
    private static final String QUERY_PARAM_ID = "id";
    private ProductRepository productRepository;

    public ProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ServerResponse> getByNameOrAll(ServerRequest request) {
        var nameOptional = request.queryParam(QUERY_PARAM_NAME);
        if (nameOptional.isPresent()) {
            return getByName(request);
        } else {
            return getAll(request);
        }
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return productRepository.findAll()
                .collectList()
                .flatMap(products -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(products)))
                ;
    }

    public Mono<ServerResponse> getByName(ServerRequest request) {
        var name = request.queryParam(QUERY_PARAM_NAME).orElse(null);
        return productRepository.findByName(name)
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                ;
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        var id = request.pathVariable(QUERY_PARAM_ID);
        return productRepository.findById(id)
                .flatMap(products -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(products)))
                ;
    }

    public Mono<ServerResponse> insertOneRandom(ServerRequest request) {
        var random = new ProductModel(null, "name", "headline ... headline",
                "Description......................................",
                4.5F,
                Arrays.asList(
                        new ProductPresentationModel("Model 1", SIZE, "1001", 14.5F, 10),
                        new ProductPresentationModel("Model 2", SIZE, "1001", 50.5F, 10),
                        new ProductPresentationModel("Model 3", SIZE, "1003", 150.5F, 0)
                ),
                Arrays.asList(
                        new ProductCompositionModel("Top Note", TOP_NOTE,
                                Arrays.asList(
                                        new ProductIngredientModel("Ingredient1", "FRUIT")
                                )
                        ),
                        new ProductCompositionModel("Heart Note", HEART_NOTE,
                                Arrays.asList(
                                        new ProductIngredientModel("Ingredient2", "FRUIT")
                                )
                        )
                ),
                Arrays.asList(
                        new ProductPhotoModel("1001", "https://www.aromasartesanales.de/wp-content/uploads/2020/10/Aromas-man.jpg"),
                        new ProductPhotoModel("1003", "https://www.aromasartesanales.de/wp-content/uploads/2019/05/Eau-de-Cologne.jpg")
                ));
        return productRepository.insert(random)
                .flatMap(productInserted -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(productInserted))
                );
    }

}
