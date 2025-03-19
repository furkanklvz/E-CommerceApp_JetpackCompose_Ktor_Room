package com.klavs.e_commerceapp.extensions

fun Double.format(digit: Int): String {
    return String.format("%.${digit}f", this)
}