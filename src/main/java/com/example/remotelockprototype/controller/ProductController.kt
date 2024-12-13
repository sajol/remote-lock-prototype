package com.example.remotelockprototype.controller

import com.example.remotelockprototype.model.Product
import com.example.remotelockprototype.repository.ProductsRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productsRepository: ProductsRepository,
) {

    @GetMapping
    fun getProducts(): List<Product> {
        return productsRepository.findAll().toList()
    }
}