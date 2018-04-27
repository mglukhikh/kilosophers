package ru.spbstu.kilosophers

abstract class AbstractFork : Fork {
    override lateinit var left: AbstractKilosopher

    override lateinit var right: AbstractKilosopher
}