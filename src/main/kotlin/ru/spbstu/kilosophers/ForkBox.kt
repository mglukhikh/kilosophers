package ru.spbstu.kilosophers

// Produces forks
interface ForkBox {
    fun produce(vararg args: Any): AbstractFork
}