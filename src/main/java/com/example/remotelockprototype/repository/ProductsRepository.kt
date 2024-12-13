package com.example.remotelockprototype.repository

import com.example.remotelockprototype.model.Product
import org.springframework.data.repository.CrudRepository

interface ProductsRepository : CrudRepository<Product, Int>