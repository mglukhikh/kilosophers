package ru.spbstu.kilosophers.concurrent

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.ForkBox

object ConcurrentForkBox : ForkBox {
    override fun produce(vararg args: Any): AbstractFork = createConcurrentFork()
}