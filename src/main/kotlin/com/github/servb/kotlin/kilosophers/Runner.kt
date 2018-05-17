package com.github.servb.kotlin.kilosophers

fun main(args: Array<String>) {
    val table = Table(5)
    var tick = 0

    while (true) {
        for (i in table.kilosophers.indices) {
            val nowKilosopher = table.kilosophers[i]
            nowKilosopher.act(100)

            if (nowKilosopher.state == MyKilosopher.State.EATS) {
                println("$tick: $i eats!")
            }
        }

        Thread.sleep(500)
        ++tick
        if (tick % 1000 == 0) {
            println("$tick ticks passed")
        }
    }
}
