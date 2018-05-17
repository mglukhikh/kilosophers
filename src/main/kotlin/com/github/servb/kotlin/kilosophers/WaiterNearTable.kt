package com.github.servb.kotlin.kilosophers

class WaiterNearTable(private val table: Table) {
    val takingAllowed get() = table.freeForks > 1
}
