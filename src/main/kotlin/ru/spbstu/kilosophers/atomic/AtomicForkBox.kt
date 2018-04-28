package ru.spbstu.kilosophers.atomic

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.ForkBox

object AtomicForkBox : ForkBox {
    override fun produce(vararg args: Any): AbstractFork = AtomicFork()
}