package com.digiquest.core

fun runnable(function: () -> Unit) : Runnable {
    return Runnable { function() }
}