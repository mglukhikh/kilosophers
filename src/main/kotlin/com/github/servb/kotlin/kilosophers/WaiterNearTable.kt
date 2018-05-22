package com.github.servb.kotlin.kilosophers

class WaiterNearTable(private val table: Table) {
    private var alreadyAllowed = 0

    val takingAllowed: Boolean get() {
        val result = table.freeForks - alreadyAllowed > 1
        ++alreadyAllowed
        return result
    }

    fun returnAllowedFork() {
        alreadyAllowed = Math.max(0, alreadyAllowed - 1)
    }
}
