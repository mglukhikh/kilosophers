package ru.spbstu.kilosophers.impl

import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.withContext
import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher

private class ConcurrentFork : AbstractFork() {
    private val index = forkNumber++

    private val forkContext = newSingleThreadContext("Fork $index context")

    override var owner: AbstractKilosopher? = null

    override suspend fun tryTake(who: AbstractKilosopher): Boolean {
        if (owner != null) return false
        return withContext(forkContext) {
            if (owner == null) {
                owner = who
                true
            } else {
                false
            }
        }
    }

    override suspend fun tryDrop(who: AbstractKilosopher): Boolean {
        if (owner != who) return false
        return withContext(forkContext) {
            if (owner != who) {
                false
            } else {
                owner = null
                true
            }
        }
    }

    companion object {
        var forkNumber = 0
    }
}

fun createConcurrentFork(): AbstractFork = ConcurrentFork()