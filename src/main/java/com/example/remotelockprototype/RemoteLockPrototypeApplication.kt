package com.example.remotelockprototype

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

const val ORDERS_QUEUE = "ORDERS"

@SpringBootApplication
@EnableScheduling
class RemoteLockPrototypeApplication


fun main(args: Array<String>) {
    runApplication<RemoteLockPrototypeApplication>(*args)
}