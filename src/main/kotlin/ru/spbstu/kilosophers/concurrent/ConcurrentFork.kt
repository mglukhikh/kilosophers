package ru.spbstu.kilosophers.concurrent

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher
import java.util.concurrent.Executors

private class ConcurrentFork : AbstractFork() {
    private val index = forkNumber++

    private val forkContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName("Fork $index")

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