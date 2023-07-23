package ru.netology.nmedia.dto

import kotlin.math.floor

public class Counter {
    fun count(counter: Int): String {
        val result: String = when (counter) {
            in 0..999 -> "$counter"
            in 1000..9999 -> "${floor(counter / 100.0) / 10}K"
            in 10000..999999 -> "${counter / 1000}K"
            else -> "${floor(counter / 100000.0) / 10}M"
        }
        return result.replace(".0", "")
    }
}