package com.github.servb.kotlin.kilosophers

class WaiterNearTable(private val table: Table) {
    private var alreadyAllowed = 0

    val takingAllowed: Boolean get() {
        val result = table.freeForks - alreadyAllowed > 1
        if (result) {
            ++alreadyAllowed
        }
        return result
    }

    fun resetAllowedCount() {
        alreadyAllowed = 0
    }
}
