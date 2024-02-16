package com.example.mvvmnewsappincompose

enum class SortType(val value: String){
    BREAKING("Breaking News"),
    ECONOMIC("Economic News"),
    Sports("Sports News"),
}

fun getAllTypes(): List<SortType>{
    return listOf(SortType.BREAKING, SortType.ECONOMIC, SortType.Sports)
}

fun getSortType(value: String): SortType {
    val map = SortType.values().associateBy(SortType::value)
    return map[value]!!
}