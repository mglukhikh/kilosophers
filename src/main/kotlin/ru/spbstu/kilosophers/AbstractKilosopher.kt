package ru.spbstu.kilosophers

import kotlinx.coroutines.*
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
        return GlobalScope.launch {
            while (timeSpent < totalDuration) {
                val action = nextAction()
                val duration = action.duration
                if (duration < 10) {
                    throw AssertionError("You should wait at least 10 ms")
                }
                val result = doAction(action)
                val randomDuration = duration + random.nextInt(9 * duration)
                delay(randomDuration.toLong())
                timeSpent += randomDuration
                handleResult(action, result)
            }
        }
    }

    // Called when kilosopher should choose his next action (take or drop fork, think, or eat)
    protected abstract fun nextAction(): Action

    // Called to provide result of previous action to kilosopher
    // true means action was successful (fork taken, spaghetti eaten)
    // false means action was not successful (fork busy, or did not have two forks to eat spaghetti)
    protected abstract fun handleResult(action: Action, result: Boolean)

    companion object {
        val random = Random()
    }
}
