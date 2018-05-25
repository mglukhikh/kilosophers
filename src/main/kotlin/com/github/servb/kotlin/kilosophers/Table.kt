package com.github.servb.kotlin.kilosophers

import ru.spbstu.kilosophers.atomic.AtomicFork

class Table(val n: Int) {
    val waiter = WaiterNearTable(this)

    val forks = List(n) { AtomicFork() }
    val kilosophers = List(n) { MyKilosopher(forks[it % n], forks[(it + 1) % n], waiter) }

    val freeForks get() = forks.count { it.isFree() }

    val isLocked get() = kilosophers.all { it.state == MyKilosopher.State.WAITS_RIGHT }

    val states get() = kilosophers.joinToString(", ") { it.state.toString() }
}
