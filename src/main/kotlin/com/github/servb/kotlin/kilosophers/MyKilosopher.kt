package com.github.servb.kotlin.kilosophers

import com.github.servb.kotlin.kilosophers.MyKilosopher.State.*
import ru.spbstu.kilosophers.AbstractKilosopher
import ru.spbstu.kilosophers.Action
import ru.spbstu.kilosophers.ActionKind
import ru.spbstu.kilosophers.Fork

class MyKilosopher(left: Fork, right: Fork, private val waiter: WaiterNearTable)
    : AbstractKilosopher(left, right) {

    enum class State {
        WAITS_BOTH,
        WAITS_RIGHT,
        EATS,
        HOLDS_BOTH,
        HOLDS_RIGHT,
        THINKS
    }

    var state = WAITS_BOTH
        private set

    override fun nextAction(): Action {
        return when (state) {
            WAITS_BOTH -> if (waiter.takingAllowed) {
                                ActionKind.TAKE_LEFT(10)
                            } else {
                                ActionKind.THINK(10)
                            }
            WAITS_RIGHT -> ActionKind.TAKE_RIGHT(10)
            EATS -> ActionKind.EAT(10)
            HOLDS_BOTH -> ActionKind.DROP_LEFT(10)
            HOLDS_RIGHT -> ActionKind.DROP_RIGHT(10)
            THINKS -> ActionKind.THINK(10)
        }
    }

    override fun handleResult(action: Action, result: Boolean) {
        state = when (action.kind) {
            ActionKind.TAKE_LEFT -> if (result) WAITS_RIGHT else WAITS_BOTH
            ActionKind.TAKE_RIGHT -> if (result) EATS else WAITS_RIGHT
            ActionKind.EAT -> HOLDS_BOTH
            ActionKind.DROP_LEFT -> if (result) HOLDS_RIGHT else HOLDS_BOTH
            ActionKind.DROP_RIGHT -> if (result) THINKS else HOLDS_RIGHT
            ActionKind.THINK -> WAITS_BOTH
        }
    }
}