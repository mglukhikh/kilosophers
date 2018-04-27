package ru.spbstu.kilosophers

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*

abstract class AbstractKilosopher(private val left: Fork, private val right: Fork) {

    var holdsLeft: Boolean = false
        private set

    var holdsRight: Boolean = false
        private set

    var eatDuration: Int = 0
        private set

    private suspend fun doAction(action: Action): Boolean {
        when (action.kind) {
            ActionKind.TAKE_LEFT -> {
                if (holdsLeft) return false
                if (left.tryTake(this)) {
                    holdsLeft = true
                }
                return holdsLeft
            }
            ActionKind.TAKE_RIGHT -> {
                if (holdsRight) return false
                if (right.tryTake(this)) {
                    holdsRight = true
                }
                return holdsRight
            }
            ActionKind.DROP_LEFT -> {
                if (!holdsLeft) return false
                left.tryDrop(this)
                holdsLeft = false
                return true
            }
            ActionKind.DROP_RIGHT -> {
                if (!holdsRight) return false
                right.tryDrop(this)
                holdsRight = false
                return true
            }
            ActionKind.THINK -> {
                return true
            }
            ActionKind.EAT -> {
                if (!holdsLeft || !holdsRight) return false
                eatDuration += action.duration
                return true
            }
        }
    }

    fun act(totalDuration: Int): Job {
        var timeSpent = 0
        return launch {
            while (timeSpent < totalDuration) {
                val action = nextAction()
                val duration = action.duration
                if (duration < 100) {
                    throw AssertionError("You should wait at least 100 ms")
                }
                val result = doAction(action)
                val randomDuration = duration + random.nextInt(9 * duration)
                delay(randomDuration)
                timeSpent += randomDuration
                handleResult(action, result)
            }
        }
    }

    protected abstract fun nextAction(): Action

    protected abstract fun handleResult(action: Action, result: Boolean)

    companion object {
        val random = Random()
    }
}