package com.resource;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.*;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "product", description = "Product Operations")
public class ProductResource {

    @GrpcClient("product-service")
    ProductService productService;
    @Inject
    ProductMapper mapper;



    @POST
    @APIResponse(responseCode = "201", description = "Product Created",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid Product")
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> create(ProductRequest productRequest) {
        return productService.create(mapper.productRequestToProduct(productRequest))
                .onItem().transform(inserted -> Response.created(URI.create("/api/v1/products/" + inserted.getId())).build());
    }

    @PUT
    @APIResponse(responseCode = "204", description = "Product updated")
    @APIResponse(responseCode = "400", description = "Invalid Product")
    @APIResponse(responseCode = "404", description = "Product does not exist for given id")
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<ProductResponse> update(ProductRequest productRequest) {
        return productService.update(mapper.productRequestToProduct(productRequest))
                .map(mapper::productToProductResponse);
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Get Product by id",
            content = @Content(schema = @Schema(implementation = Product.class))
    )
    @APIResponse(responseCode = "404", description = "Product does not exist for given id")
    @CacheResult(cacheName = "cashedProduct")
    public Uni<ProductResponse> findById(@Parameter(name = "id", required = true) @PathParam("id") Long id){
        return productService.findById(Int64Value.of(id)).map(mapper::productToProductResponse);
    }

    @GET
    @CacheResult(cacheName = "cashedProductList")
    public Uni<List<ProductResponse>> list() {
        return productService.list(Empty.newBuilder().build())
                .onItem().transform(ProductList::getResultListList)
                .map(mapper::productListToProductResponseList);
    }

    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Product deleted")
    @APIResponse(responseCode = "204", description = "Product does not exist for given id")
    @CacheInvalidateAll(cacheName = "cashedProduct")
    @CacheInvalidateAll(cacheName = "cashedProductList")
    public Uni<Response> delete(@Parameter(name = "id", required = true) @PathParam("id") Long id){
        return productService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }
}
