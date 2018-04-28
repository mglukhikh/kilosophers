package ru.spbstu.kilosophers

import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.spbstu.kilosophers.concurrent.ConcurrentForkBox
import ru.spbstu.kilosophers.sample.SampleUniversity

class SampleTest {

    private fun doTest(university: University, forkBox: ForkBox, kilosopherCount: Int, duration: Int) {
        val forks = MutableList(kilosopherCount) { forkBox.produce() }
        val kilosophers = mutableListOf<AbstractKilosopher>()
        for (index in 0 until kilosopherCount) {
            val leftFork = forks[index]
            val rightFork = forks[(index + 1) % kilosopherCount]
            val kilosopher = university.produce(leftFork, rightFork)
            kilosophers.add(kilosopher)
        }

        val jobs = kilosophers.map { it.act(duration) }
        var owners: List<AbstractKilosopher> = emptyList()

        val controllerJob = launch {
            do {
                delay(maxOf(100, minOf(duration / 50, 1000)))
                owners = forks.mapNotNull { it.owner }.distinct()
            } while (owners.size < kilosopherCount)
        }

        runBlocking {
            jobs.forEach { it.join() }
            controllerJob.cancelAndJoin()
        }

        assertNotEquals("Deadlock detected, fork owners: $owners", kilosopherCount, owners.size)

        for (kilosopher in kilosophers) {
            assertTrue("Eat durations: ${kilosophers.map { it.eatDuration }}", kilosopher.eatDuration > 0)
        }

    }

    @Test
    fun testSampleKilosopherWithConcurrentFork() {
        doTest(SampleUniversity, ConcurrentForkBox, kilosopherCount = 5, duration = 20000)
    }
}