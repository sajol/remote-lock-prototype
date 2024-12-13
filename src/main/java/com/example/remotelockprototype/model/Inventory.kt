package com.example.remotelockprototype.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.sql.Timestamp
import java.time.Instant.now

@Entity
data class Inventory(
    @Id val productId: Int? = null,
    val stock: Int,
    val updatedAt: Timestamp? = Timestamp.from(now()),
)