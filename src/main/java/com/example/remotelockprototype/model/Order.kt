package com.example.remotelockprototype.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Serializable
data class Order(
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
)

fun Order.toJsonString(): String {
    return Json.encodeToString(serializer(), this)
}

fun String.toOrder(): Order {
    return Json.decodeFromString(serializer(), this)
}


