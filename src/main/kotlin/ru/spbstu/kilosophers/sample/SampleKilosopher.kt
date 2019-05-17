package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.sample.SampleKilosopher.State.*

class SampleKilosopher(left: Fork, right: Fork, val index: Int) : AbstractKilosopher(left, right) {

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
            WAITS_BOTH -> TAKE_LEFT(10)
            WAITS_RIGHT -> TAKE_RIGHT(10)
            EATS -> EAT(50)
            HOLDS_BOTH -> DROP_LEFT(10)
            HOLDS_RIGHT -> DROP_RIGHT(10)
            THINKS -> THINK(100)
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        state = when (action.kind) {
            TAKE_LEFT -> if (result) WAITS_RIGHT else WAITS_BOTH
            TAKE_RIGHT -> if (result) EATS else WAITS_RIGHT
            EAT -> HOLDS_BOTH
            DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            THINK -> WAITS_BOTH
        }
    }

    override fun toString(): String {
        return "Kilosopher #$index"
    }
}