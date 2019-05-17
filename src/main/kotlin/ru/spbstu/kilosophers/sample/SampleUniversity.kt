package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.University

object SampleUniversity : University {
    override fun produce(left: AbstractFork, right: AbstractFork, vararg args: Any): AbstractKilosopher {
        val kilosopher = SampleKilosopher(left, right, args[0] as Int)
        left.right = kilosopher
        right.left = kilosopher
        return kilosopher
    }
}