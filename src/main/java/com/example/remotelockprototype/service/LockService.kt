package com.example.remotelockprototype.service

import mu.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit


@Service
class LockService(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    private val logger = KotlinLogging.logger {}

    private val extendLockScript = DefaultRedisScript(
        """
            if redis.call("GET", KEYS[1]) == ARGV[1] then
                return redis.call("PEXPIRE", KEYS[1], ARGV[2])
            else
                return 0
            end
        """.trimIndent(),
        Int::class.java
    )

    /**
     * Acquires lock with the specified key and expiration time.
     * @param key The key for the lock
     * @param value The value to set for the lock (can be a unique identifier)
     * @param expiration Time in seconds for the lock key to expire to avoid starvation
     * */
    fun acquireLock(key: String, value: String, expiration: Long): Boolean {
        val result = redisTemplate.opsForValue()
            .setIfAbsent(key, value, expiration, TimeUnit.SECONDS)
            .also {
                logger.info { "Acquired lock for key $key: $it" }
            }
        return result == true
    }

    /**
     * Releases the lock by deleting the key.
     * @param key The key for the lock.
     * */
    fun releaseLock(key: String) {
        redisTemplate.delete(key).also {
            logger.info { "Lock released with key $key: $it" }
        }
    }

    /**
     * Extend the lock TTL atomically if the lock is owned.
     * @param lockKey The Redis lock key.
     * @param lockValue The unique value of the lock.
     * @param newTtl The new TTL in milliseconds.
     * @return True if the TTL was successfully extended, false otherwise.
     */
    fun extendLock(lockKey: String, lockValue: String, newTtl: Long): Boolean {
        val result = redisTemplate.execute(
            extendLockScript,
            listOf(lockKey),
            lockValue,
            newTtl,
        )

        return result == 1
    }

}