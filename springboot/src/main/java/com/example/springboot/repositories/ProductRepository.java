package com.example.springboot.repositories;


import com.example.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository  //esse esteriótipo indica que essa classe será um bin gerenciada pelo Spring.
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
}
