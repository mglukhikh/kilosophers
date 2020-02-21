package ru.spbstu.kilosophers

import kotlinx.coroutines.*
import ru.spbstu.kilosophers.atomic.AtomicForkBox
import ru.spbstu.kilosophers.concurrent.ConcurrentForkBox
import ru.spbstu.kilosophers.sample.SampleUniversity
import ru.spbstu.kilosophers.served.ServedUniversity
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

const val DURATION = 20_000


class SampleTest {

    private fun doTest(university: University, forkBox: ForkBox, kilosopherCount: Int, duration: Int) {
        val forks = MutableList(kilosopherCount) { forkBox.produce() }
        val kilosophers = mutableListOf<AbstractKilosopher>()
        for (index in 0 until kilosopherCount) {
            val leftFork = forks[index]
            val rightFork = forks[(index + 1) % kilosopherCount]
            val kilosopher = university.produce(leftFork, rightFork, index)
            kilosophers.add(kilosopher)
        }

        val jobs = kilosophers.map { it.act(duration) }
        var owners: List<AbstractKilosopher> = emptyList()

        val controllerJob = GlobalScope.launch {
            do {
                delay(maxOf(100, minOf(duration / 50, 1000)).toLong())
                owners = forks.mapNotNull { it.owner }.distinct()
            } while (owners.size < kilosopherCount)
        }

        runBlocking {
            jobs.forEach { it.join() }
            controllerJob.cancelAndJoin()
        }

        assertNotEquals(kilosopherCount, owners.size, "Deadlock detected, fork owners: $owners")

        for (kilosopher in kilosophers) {
            assertTrue(kilosopher.eatDuration > 0, "Eat durations: ${kilosophers.map { it.eatDuration }}")
        }
    }

    @Ignore
    @Test
    fun testSampleKilosopherWithConcurrentFork() {
        doTest(SampleUniversity, ConcurrentForkBox, kilosopherCount = 5, duration = DURATION)
    }

    @Ignore
    @Test
    fun testSampleKilosopherWithAtomicFork() {
        doTest(SampleUniversity, AtomicForkBox, kilosopherCount = 5, duration = DURATION)
    }

    @Test
    fun testServedKilosopherWithConcurrentFork() {
        doTest(ServedUniversity, ConcurrentForkBox, kilosopherCount = 5, duration = DURATION)
    }

    @Test
    fun testServedKilosopherWithAtomicFork() {
        doTest(ServedUniversity, AtomicForkBox, kilosopherCount = 5, duration = DURATION)
    }
}