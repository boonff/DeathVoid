package org.boon.deathvoid.util

object Random {
    fun roll(percent: Float = 0.5f): Boolean {
        require(percent in 0.0..1.0) { "percent 必须在 0 到 100 之间" }
        return Math.random() < percent
    }
}