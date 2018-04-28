package ru.spbstu.kilosophers.atomic

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher
import java.util.concurrent.atomic.AtomicReference

class AtomicFork : AbstractFork() {

    private val ownerReference = AtomicReference<AbstractKilosopher>()

    override val owner: AbstractKilosopher?
        get() = ownerReference.get()

    override suspend fun tryTake(who: AbstractKilosopher): Boolean = ownerReference.compareAndSet(null, who)

    override suspend fun tryDrop(who: AbstractKilosopher): Boolean = ownerReference.compareAndSet(who, null)
}