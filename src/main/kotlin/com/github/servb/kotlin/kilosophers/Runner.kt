package com.github.servb.kotlin.kilosophers

fun main(args: Array<String>) {
    val n = 5
    val eatStat = IntArray(n) { 0 }
    val table = Table(n)

    var tick = 0
    while (true) {
        for ((i, nowKilosopher) in table.kilosophers.withIndex()) {
            nowKilosopher.act(100)

            if (nowKilosopher.state == MyKilosopher.State.EATS) {
                ++eatStat[i]
                println("$tick: $i eats! Stats: ${eatStat.joinToString(" ")}.")
            }
        }

        if (table.isLocked) {
            throw IllegalStateException("Have a lock: everybody waits. Tick: $tick.")
        }

        Thread.sleep(500)
        ++tick
        if (tick % 1000 == 0) {
            println("$tick ticks passed")
        }
    }
}
