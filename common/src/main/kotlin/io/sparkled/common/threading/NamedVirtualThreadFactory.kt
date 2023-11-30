package io.sparkled.common.threading

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class NamedVirtualThreadFactory(private val prefix: String) : ThreadFactory {
    private val impl = Thread.ofVirtual().factory()
    private val counter = AtomicInteger()

    override fun newThread(r: Runnable): Thread =
        impl.newThread(r).apply { name = "$prefix-${counter.getAndIncrement()}" }
}
