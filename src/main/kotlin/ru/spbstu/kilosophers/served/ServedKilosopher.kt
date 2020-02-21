package ru.spbstu.kilosophers.served

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.served.ServedKilosopher.State.*
import java.util.concurrent.Semaphore


class ServedKilosopher(
        left: Fork,
        right: Fork,
        private val index: Int,
        private val semaphore: Semaphore
) : AbstractKilosopher(left, right) {

    internal enum class State {
        WAITS_BOTH,
        WAITS_RIGHT,
        EATS,
        HOLDS_BOTH,
        HOLDS_RIGHT,
        THINKS
    }

    private var state = WAITS_BOTH

    override fun nextAction(): Action {
        return when (state) {
            WAITS_BOTH -> {
                val isAvailable = semaphore.tryAcquire(2)
                if (isAvailable) {
                    TAKE_LEFT(10)
                } else {
                    THINK(50)
                }
            }
            WAITS_RIGHT -> {
                TAKE_RIGHT(10)
            }
            EATS -> EAT(50)
            HOLDS_BOTH -> {
                DROP_LEFT(10)
            }
            HOLDS_RIGHT -> {
                DROP_RIGHT(10).also { semaphore.release(2) }
            }
            THINKS -> THINK(50)
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        state = when (action.kind) {
            TAKE_LEFT -> if (result) WAITS_RIGHT else WAITS_BOTH.also { semaphore.release() }
            TAKE_RIGHT -> if (result) EATS else WAITS_RIGHT.also { semaphore.release() }
            EAT -> HOLDS_BOTH
            DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            THINK -> if (state == WAITS_RIGHT) WAITS_RIGHT else WAITS_BOTH
        }
    }

    override fun toString(): String {
        return "Kilosopher #$index"
    }
}