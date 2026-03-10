package com.resource;

import com.dto.CustomerRequest;
import com.dto.CustomerResponse;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerMapper;
import com.proto.service.Customer;
import com.proto.service.CustomerList;
import com.proto.service.CustomerService;
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

@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "customer", description = "Customer Operations")
public class CustomerResource {
    @GrpcClient("customer-service")
    CustomerService customerService;
    @Inject
    CustomerMapper mapper;



    @POST
    @APIResponse(responseCode = "201", description = "Customer Created",
            content = @Content(schema = @Schema(implementation = Customer.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid Customer")
    public Uni<Response> create(CustomerRequest customerRequest) {
        return customerService.create(mapper.customerRequestToCustomer(customerRequest))
                .onItem().transform(inserted -> Response.created(URI.create("/api/v1/customers/" + inserted.getId())).build());
    }

    @PUT
    @APIResponse(responseCode = "204", description = "Customer updated")
    @APIResponse(responseCode = "400", description = "Invalid Customer")
    @APIResponse(responseCode = "404", description = "Customer does not exist for given id")
    public Uni<CustomerResponse> update(CustomerRequest customerRequest) {
        return customerService.update(mapper.customerRequestToCustomer(customerRequest))
                .map(mapper::customerToCustomerResponse);
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Get Customer by id",
            content = @Content(schema = @Schema(implementation = Customer.class))
    )
    @APIResponse(responseCode = "404", description = "Customer does not exist for given id")
    @CacheResult(cacheName = "cashedCustomer")
    public Uni<CustomerResponse> findById(@Parameter(name = "id", required = true) @PathParam("id") Long id){
        return customerService.findById(Int64Value.of(id)).map(mapper::customerToCustomerResponse);
    }

    @GET
    @CacheResult(cacheName = "cashedCustomerList")
    public Uni<List<CustomerResponse>> list() {
        return customerService.list(Empty.newBuilder().build())
                .onItem().transform(CustomerList::getResultListList)
                .map(mapper::customerListToCustomerResponseList);
    }

    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "Customer deleted")
    @APIResponse(responseCode = "204", description = "Customer does not exist for given id")
    public Uni<Response> delete(@Parameter(name = "id", required = true) @PathParam("id") Long id){
        return customerService.delete(Int64Value.of(id)).onItem()
                .transform(boolValue ->
                        boolValue.getValue() ?
                                Response.ok().build() :
                                Response.noContent().build());
    }

}
