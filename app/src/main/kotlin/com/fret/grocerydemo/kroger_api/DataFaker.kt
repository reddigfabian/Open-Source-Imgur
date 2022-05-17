package com.fret.grocerydemo.kroger_api


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