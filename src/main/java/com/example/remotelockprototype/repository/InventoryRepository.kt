package com.example.remotelockprototype.repository

import com.example.remotelockprototype.model.Inventory
import org.springframework.data.repository.CrudRepository

interface InventoryRepository : CrudRepository<Inventory, Int> {
    fun findStockByProductId(productId: Int): Int
}