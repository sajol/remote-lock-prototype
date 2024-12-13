package com.example.remotelockprototype.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Product(
    @Id @GeneratedValue var id: Int? = null,
    val name: String,
    val description: String,
    val price: Double
)