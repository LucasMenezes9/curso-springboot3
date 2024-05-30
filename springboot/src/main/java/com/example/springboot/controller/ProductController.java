package com.example.springboot.controller;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
/*
    * Essa classe é responsavel pelas inserções, alterações, deleções no banco de dados.
 */
@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/products")     // Esse é o método de criação
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel); // Converte o productModelDto em productModel.
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("/products")     //Esse é o método de leitura de todos os Ids.
    public ResponseEntity<List<ProductModel>> getAllPoducts() {
        List<ProductModel> productsList = productRepository.findAll();
        if (!productsList.isEmpty()){
            for (ProductModel product : productsList){
                UUID id = product.getIdProduct();  // Obter o id do produto.
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    @GetMapping("/products/{id}")      //Esse é o método de leitura de apenas um Id.
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()) { // esse opcional está vazio?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        productO.get().add(linkTo(methodOn(ProductController.class).getAllPoducts()).withRel("Products List"));
        return ResponseEntity.status(HttpStatus.OK).body(productO.get());
    }

    @PutMapping("/products/{id}")          //Esse é o método de atualização
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> productO = productRepository.findById(id);
        if (productO.isEmpty()) {  // esse opcional está vazio?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        var productModel = productO.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        Optional<ProductModel> productO = productRepository.findById(id);
        if(productO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productRepository.delete(productO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfull.");
    }

}
