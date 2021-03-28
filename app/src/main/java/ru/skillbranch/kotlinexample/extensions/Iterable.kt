package ru.skillbranch.kotlinexample.extensions

fun String.phone() = this.filter { it.isDigit() || it in setOf('+')}