package ru.spbstu.kilosophers

// Produces kilosophers
interface University {
    fun produce(left: AbstractFork, right: AbstractFork, vararg args: Any): AbstractKilosopher
}