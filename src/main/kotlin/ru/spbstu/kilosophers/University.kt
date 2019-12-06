package ru.spbstu.kilosophers

import kotlinx.coroutines.*

// Produces kilosophers
interface University {
    fun produce(left: AbstractFork, right: AbstractFork, vararg args: Any): AbstractKilosopher
}

fun main() {
    sampleD()
}

fun sampleA() {
    //runBlocking {
    GlobalScope.launch {
        delay(1000)
        println("Hello, koroutine!")
    }

    Thread.sleep(500)
    println("Hello, world!")
    Thread.sleep(1500)
}

fun sampleB() {
    GlobalScope.launch {
        // Enter coroutine...
        repeat(1000) { i ->
            println("Sleeping: $i")
            delay(500)
        }
    }
    Thread.sleep(1300)
}

fun sampleC() = runBlocking {
    val job = launch {
        delay(1000)
        println("coroutine!")
    }
    delay(500)
    print("Hello, ")
    // job.join()
}

fun sampleD() = runBlocking {
    repeat(100_000) {
        launch {
            delay(1000L)
            print(".")
        }
    }
}

