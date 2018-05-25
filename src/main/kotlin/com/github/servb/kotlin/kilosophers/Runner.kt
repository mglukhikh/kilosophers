package com.github.servb.kotlin.kilosophers

import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) {
    val n = 5
    val eatStat = IntArray(n) { 0 }
    val table = Table(n)

    var tick = 0
    runBlocking {
        while (true) {
            table.kilosophers.shuffled().map { it.act(100) }.forEach { it.join() }

            for ((i, nowKilosopher) in table.kilosophers.withIndex()) {
                if (nowKilosopher.state == MyKilosopher.State.EATS) {
                    ++eatStat[i]
                    println("$tick: $i eats! Stats: ${eatStat.joinToString(" ")}.")
                }
            }

            if (table.isLocked) {
                throw IllegalStateException("Have a lock: everybody waits. Tick: $tick.")
            }

            ++tick
            table.waiter.resetAllowedCount()
            if (tick % 10 == 0) {
                println("$tick: ${table.states}")
            }
        }
    }
}
