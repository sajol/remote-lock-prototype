package com.example.remotelockprototype.service.publisher

import com.example.remotelockprototype.ORDERS_QUEUE
import com.example.remotelockprototype.model.Order
import jakarta.annotation.PostConstruct
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisOrderProducer(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun publishDummyOrders() {
        redisTemplate.delete(ORDERS_QUEUE)
        redisTemplate.delete(listOf("lock:1", "lock:2"))
        val orders = listOf(
            Order(123, 1, 7),
            Order(124, 1, 3),
            Order(125, 2, 7),
            Order(126, 2, 3),
        )

        orders.forEach { order ->
            val orderJson = Json.encodeToString(Order.serializer(), order)
            redisTemplate.opsForList().rightPush(ORDERS_QUEUE, orderJson)
            logger.info { "Publishing order $orderJson" }
        }
    }
}