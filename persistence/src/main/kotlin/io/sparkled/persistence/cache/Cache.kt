package io.sparkled.persistence.cache

import common.logging.getLogger
import java.time.Duration
import java.time.Instant
import java.util.concurrent.atomic.AtomicReference

abstract class Cache<T>(
    val name: String,
    private val expiryInterval: Duration = defaultExpiryInterval,
    private val fallback: T,
) {
    private val cache = AtomicReference<T?>(null)
    private var lastLoadedAt = Instant.MIN

    init {
        logger.info("Creating '$name' cache with expiry of $expiryInterval.")
    }

    abstract fun reload(lastLoadedAt: Instant?): T

    fun clear() {
        logger.info("Clearing '$name' cache.")
        cache.set(null)
        lastLoadedAt = Instant.MIN
        logger.info("Cleared '$name' cache.")
    }

    fun get(): T {
        if (getStatus() != CacheStatus.POPULATED) {
            synchronized(cache) {
                // Recheck to prevent a second reload from occurring before the first reload completes.
                val cacheStatus = getStatus()
                if (cacheStatus != CacheStatus.POPULATED) {
                    logger.info("Reloading '$name' cache (status: $cacheStatus).")
                    cache.set(reload(lastLoadedAt))
                    lastLoadedAt = Instant.now()
                    logger.info("Populated '$name' cache ${getCacheSizeString()}.")
                }
            }
        }

        return cache.get() ?: fallback
    }

    /**
     * A convenience method that allows cached results to be used in a callback.
     */
    fun <U> use(fn: (T) -> U): U = fn(get())

    /**
     * Allow immediate updates (or "patching") of the cache. This allows for a best attempt at maintaining an accurate
     * cache at all times, with the expectation that data might not be 100% perfect.
     *
     * Most caches will expire periodically, which will bring them back into line.
     */
    fun modify(fn: (T) -> T) {
        synchronized(cache) {
            val oldCache = get()
            val modifiedCache = fn(get())

            if (oldCache === modifiedCache) {
                throw IllegalStateException("Attempted mutation of '$name' cache, a new object must be returned.")
            } else {
                cache.set(modifiedCache)
            }
        }
    }

    fun getStatus(): CacheStatus {
        return when {
            isEmpty() -> CacheStatus.IS_EMPTY
            (lastLoadedAt.plus(expiryInterval)).isBefore(Instant.now()) -> CacheStatus.EXPIRED
            else -> CacheStatus.POPULATED
        }
    }

    private fun getCacheSizeString(): String {
        return when (val value = cache.get()) {
            is Collection<*> -> "(${value.size} items)"
            is Map<*, *> -> "(${value.size} items)"
            else -> ""
        }
    }

    private fun isEmpty(): Boolean {
        val value = cache.get()
        return value == null ||
                (value is Collection<*> && value.isEmpty()) ||
                (value is Map<*, *> && value.isEmpty())
    }

    companion object {
        private val logger = getLogger<Cache<*>>()
        private val defaultExpiryInterval: Duration = Duration.ofDays(99999) // Never expire by default.
    }
}
