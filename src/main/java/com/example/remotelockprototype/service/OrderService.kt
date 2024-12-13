package com.example.remotelockprototype.service

import com.example.remotelockprototype.ORDERS_QUEUE
import com.example.remotelockprototype.model.Order
import com.example.remotelockprototype.model.toJsonString
import com.example.remotelockprototype.repository.InventoryRepository
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class OrderService(
    private val inventoryRepository: InventoryRepository,
    private val lockService: LockService,
    private val redisTemplate: RedisTemplate<String, String>,
) {

    private val logger = KotlinLogging.logger {}

    fun processOrder(order: Order) {
        val lockKey = getLockKey(order)
        try {
            val lockAcquired = lockService.acquireLock(lockKey, "locked", 5000)
            if (lockAcquired) {
                updateInventory(order)
                lockService.releaseLock(lockKey)
            } else {
                logger.warn { "Could not acquire lock with $lockKey. Requeuing order ${order.orderId}" }
                redisTemplate.opsForList().rightPush(ORDERS_QUEUE, order.toJsonString())
            }
        } catch (e: Exception) {
            logger.error(e) { "Error while processing order" }
        }
    }

    fun updateInventory(order: Order) {
        val inventory = inventoryRepository.findById(order.productId).getOrNull()
        if (inventory != null && inventory.stock >= order.quantity) {
            logger.info { "Stock before update for ${order.productId}: ${inventory.stock}" }
            val updatedInventory = inventoryRepository.save(
                inventory.copy(stock = inventory.stock - order.quantity)
            )
            logger.info {
                "Order processed: ${order.orderId}, Stock after update: ${updatedInventory.stock}"
            }
        } else {
            logger.info { "Stock was not found for ${order.productId}" }
        }
    }

    private fun getLockKey(order: Order) = "lock:${order.productId}"
}

