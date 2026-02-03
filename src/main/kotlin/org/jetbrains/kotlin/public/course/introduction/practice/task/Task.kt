package org.jetbrains.kotlin.public.course.introduction.practice.task

const val TO = 100
const val FROM = 10

// Implement a simple console game where a random number between 10 and 100 is generated,
// and the user has to guess this number.
// The program should inform the user if their guess is low or high.

// Then log all user's answers into a string and print the log og the game.

fun readNumber(): Int?
{
    TODO("Read the input and return it; return null if the input is not a number")
}

fun main() {
    val secretNumber = (FROM..TO).random()

    TODO("Implement the game logic")
}
