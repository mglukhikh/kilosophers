package ru.spbstu.kilosophers

interface Fork {
    val owner: AbstractKilosopher?

    val left: AbstractKilosopher

    val right: AbstractKilosopher

    suspend fun tryTake(who: AbstractKilosopher): Boolean

    suspend fun tryDrop(who: AbstractKilosopher): Boolean

    fun isFree(): Boolean = owner == null
}