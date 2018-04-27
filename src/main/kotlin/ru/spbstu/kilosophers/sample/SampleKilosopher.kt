package ru.spbstu.kilosophers.sample

import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind.*
import ru.spbstu.kilosophers.Fork
import ru.spbstu.kilosophers.sample.SampleKilosopher.State.*

class SampleKilosopher(left: Fork, right: Fork) : AbstractKilosopher(left, right) {

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
            WAITS_BOTH -> TAKE_LEFT(100)
            WAITS_RIGHT -> TAKE_RIGHT(100)
            EATS -> EAT(500)
            HOLDS_BOTH -> DROP_LEFT(100)
            HOLDS_RIGHT -> DROP_RIGHT(100)
            THINKS -> THINK(1000)
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
}