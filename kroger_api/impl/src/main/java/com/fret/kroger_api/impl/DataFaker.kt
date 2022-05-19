package com.fret.kroger_api.impl

object DataFaker {
    fun generateFakeData(pageSize : Int, page : Int) : List<String> {
        val result = mutableListOf<String>()
        var indexCopy = (page * pageSize) + 1
        repeat(pageSize) {
            result.add("Kroger Item $indexCopy")
            indexCopy++
        }
        return result
    }
}