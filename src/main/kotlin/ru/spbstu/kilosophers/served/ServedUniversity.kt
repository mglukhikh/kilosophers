package ru.spbstu.kilosophers.served

import ru.spbstu.kilosophers.AbstractFork
import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.University
import java.util.concurrent.Semaphore


object ServedUniversity : University {
        private val semaphore = Semaphore(-1)


    override fun produce(left: AbstractFork, right: AbstractFork, vararg args: Any): AbstractKilosopher {
        val kilosopher = ServedKilosopher(left, right, args[0] as Int, semaphore)
        left.right = kilosopher
        right.left = kilosopher

        semaphore.release()


        return kilosopher
    }
}