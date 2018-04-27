package ru.spbstu.kilosophers.sample

import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.impl.createConcurrentFork

class SampleTest {

    @Test
    fun testSampleKilosopher() {
        val count = 5
        val forks = MutableList(count) { createConcurrentFork() }
        val kilosophers = mutableListOf<AbstractKilosopher>()
        for (index in 0 until count) {
            val leftFork = forks[index]
            val rightFork = forks[(index + 1) % count]
            val kilosopher = SampleKilosopher(leftFork, rightFork)
            kilosophers.add(kilosopher)
            leftFork.right = kilosopher
            rightFork.left = kilosopher
        }

        val jobs = kilosophers.map { it.act(20000) }

        val controllerJob = launch {
            delay(1000)
            val owners = forks.map { it.owner }.distinct()
            assertNotEquals("Deadlock detected, fork owners: $owners", count, owners.size)
        }

        runBlocking {
            jobs.forEach { it.join() }
            controllerJob.cancelAndJoin()
        }

        for (kilosopher in kilosophers) {
            assertTrue("Eat durations: ${kilosophers.map { it.eatDuration }}", kilosopher.eatDuration > 0)
        }
    }
}