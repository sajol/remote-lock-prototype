package com.example.remotelockprototype.service.consumer

import com.example.remotelockprototype.ORDERS_QUEUE
import com.example.remotelockprototype.model.toOrder
import com.example.remotelockprototype.service.OrderService
import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class RedisOrderConsumer(
    private val redisTemplate: RedisTemplate<String, String>,
    private val orderService: OrderService,
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedDelay = 1000) // polls every 1 second
    fun consumerOne() {
        val orderJson = redisTemplate.opsForList().leftPop(ORDERS_QUEUE)
        if (orderJson != null) {
            logger.info { "Consumer one consumed: $orderJson" }
            handleOrder(orderJson)
        }
    }

    @Scheduled(fixedDelay = 1000) // polls every 1 second
    fun consumerTwo() {
        val orderJson = redisTemplate.opsForList().leftPop(ORDERS_QUEUE)
        if (orderJson != null) {
            logger.info { "Consumer two consumed: $orderJson" }
            handleOrder(orderJson)
        }
    }

    fun handleOrder(orderJson: String) {
        try {
            orderService.processOrder(orderJson.toOrder())
        } catch (ex: Exception) {
            logger.error(ex) { "Error while processing order: $orderJson" }
        }
    }
}