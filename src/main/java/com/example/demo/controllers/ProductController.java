package com.example.demo.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Operation(summary = "Get all products", description = "List all products in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)), examples = {
                            @ExampleObject(
                                    name = "List of products",
                                    value = "[{ \"id\": 4984198493824, \"name\": \"Macbook Pro 14\", \"price\": 2333.0}, { \"id\": 312131453498, \"name\": \"iPhone X\", \"price\": 1099.9}]")
                    })
            })
    })
    @GetMapping
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Operation(summary = "Create a product", description = "Add a product to the database")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class), examples = {
            @ExampleObject(name = "Product", value = "{ \"name\": \"Macbook Pro 14\", \"price\": 2333.0}")
    }))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class), examples = {
                            @ExampleObject(name = "The added product", value = "{ \"id\": 4984198493824, \"name\": \"Example\", \"price\": 999.2 }")
                    })
            })
    })
    @PostMapping
    public Product save(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @Operation(summary = "Update a product", description = "Update an existing product in the database")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(name = "Product", value = "{ \"name\": \"Example\", \"price\": 999.2 }")
    }))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class), examples = {
                            @ExampleObject(name = "The updated product", value = "{ \"id\": 4984198493824, \"name\": \"Example\", \"price\": 999.2 }")
                    })
            })
    })
    @PatchMapping(path = "/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productRepository.findById(id).orElse(null);
        if (updatedProduct != null) {
            updatedProduct.setName(product.getName());
            updatedProduct.setPrice(product.getPrice());
            return productRepository.save(updatedProduct);
        }

        return null;
    }

    @Operation(summary = "Delete a product", description = "Delete a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class), examples = {
                            @ExampleObject(name = "Bool to indicate if the product was deleted or not", value = "true|false")
                    })
            })
    })
    @DeleteMapping(path = "/{id}")
    public Boolean delete(@PathVariable Long id) {
        Product deletedProduct = productRepository.findById(id).orElse(null);
        if (deletedProduct != null) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
