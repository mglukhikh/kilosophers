package com.github.servb.kotlin.kilosophers

fun main(args: Array<String>) {
    val n = 5
    val eatStat = IntArray(n, { 0 })
    var table = Table(n)

    var tick = 0
    while (true) {
        for (i in table.kilosophers.indices) {
            val nowKilosopher = table.kilosophers[i]
            nowKilosopher.act(100)

            if (nowKilosopher.state == MyKilosopher.State.EATS) {
                ++eatStat[i]
                println("$tick: $i eats! Stats: ${eatStat.joinToString(" ")}.")
            }
        }

        if (table.isLocked) {
            if (tick == 1) {  // TODO: Fix this workaround
                table = Table(n)
                tick = 0
            } else {
                throw IllegalStateException("Have a lock: everybody waits. Tick: $tick.")
            }
        }

        Thread.sleep(500)
        ++tick
        if (tick % 1000 == 0) {
            println("$tick ticks passed")
        }
    }
}
