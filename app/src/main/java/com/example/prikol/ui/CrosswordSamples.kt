package com.example.prikol.ui

// Grid: 2D List of Characters (including '#')
val filledGrid = listOf(
    listOf("s", "t", "a", "t", "e", "m", "e", "n", "t", "#"),
    listOf("#", "r", "#", "o", "#", "e", "#", "o", "#", "a"),
    listOf("d", "e", "t", "o", "n", "a", "t", "o", "r", "s"),
    listOf("#", "y", "#", "k", "#", "d", "#", "d", "#", "s"),
    listOf("a", "#", "a", "#", "g", "o", "a", "l", "i", "e"),
    listOf("n", "e", "p", "h", "e", "w", "#", "e", "#", "t"),
    listOf("t", "#", "o", "#", "n", "#", "m", "#", "n", "#"),
    listOf("s", "p", "l", "a", "t", "t", "e", "r", "e", "d"),
    listOf("y", "#", "l", "#", "l", "#", "n", "#", "a", "#"),
    listOf("#", "b", "o", "d", "y", "g", "u", "a", "r", "d")
)

// Clues: Map with Pair<Int, Int> as keys and List of Strings as values, nested under "hor" and "vert"
val clues = mapOf(
    "hor" to mapOf(
        Pair(0, 0) to listOf("A declaration or remark"),
        Pair(2, 0) to listOf("A device used to detonate an explosive device etc"),
        Pair(4, 4) to listOf("A goalkeeper or goaltender"),
        Pair(5, 0) to listOf("A son of one's sibling, brother-in-law, or sister-in-law"),
        Pair(7, 0) to listOf("To splash"),
        Pair(9, 1) to listOf("To act as bodyguard for")
    ),
    "vert" to mapOf(
        Pair(0, 1) to listOf("A playing card or die with the rank of three"),
        Pair(0, 3) to listOf("To get into one's hands, possession or control, with or without force"),
        Pair(0, 5) to listOf("A field or pasture"),
        Pair(0, 7) to listOf("To think or ponder"),
        Pair(1, 9) to listOf("Something or someone of any value"),
        Pair(4, 0) to listOf("Restless, apprehensive and fidgety"),
        Pair(4, 2) to listOf("A very handsome young man"),
        Pair(4, 4) to listOf("In a gentle manner"),
        Pair(6, 6) to listOf("a bill of fare"),
        Pair(6, 8) to listOf("At or towards a position close in space or time")
    )
)

// Word Cells: Map with Pair<Int, Int> as keys and List of Pair<Int, Int> as values, nested under "hor" and "vert"
val wordCells = mapOf(
    "hor" to mapOf(
        Pair(0, 0) to listOf(
            Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(0, 4),
            Pair(0, 5), Pair(0, 6), Pair(0, 7), Pair(0, 8)
        ),
        Pair(2, 0) to listOf(
            Pair(2, 0), Pair(2, 1), Pair(2, 2), Pair(2, 3), Pair(2, 4),
            Pair(2, 5), Pair(2, 6), Pair(2, 7), Pair(2, 8), Pair(2, 9)
        ),
        Pair(4, 4) to listOf(
            Pair(4, 4), Pair(4, 5), Pair(4, 6), Pair(4, 7), Pair(4, 8), Pair(4, 9)
        ),
        Pair(5, 0) to listOf(
            Pair(5, 0), Pair(5, 1), Pair(5, 2), Pair(5, 3), Pair(5, 4), Pair(5, 5)
        ),
        Pair(7, 0) to listOf(
            Pair(7, 0), Pair(7, 1), Pair(7, 2), Pair(7, 3), Pair(7, 4),
            Pair(7, 5), Pair(7, 6), Pair(7, 7), Pair(7, 8), Pair(7, 9)
        ),
        Pair(9, 1) to listOf(
            Pair(9, 1), Pair(9, 2), Pair(9, 3), Pair(9, 4), Pair(9, 5),
            Pair(9, 6), Pair(9, 7), Pair(9, 8), Pair(9, 9)
        )
    ),
    "vert" to mapOf(
        Pair(0, 1) to listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1), Pair(3, 1)),
        Pair(0, 3) to listOf(Pair(0, 3), Pair(1, 3), Pair(2, 3), Pair(3, 3)),
        Pair(0, 5) to listOf(
            Pair(0, 5), Pair(1, 5), Pair(2, 5), Pair(3, 5), Pair(4, 5), Pair(5, 5)
        ),
        Pair(0, 7) to listOf(
            Pair(0, 7), Pair(1, 7), Pair(2, 7), Pair(3, 7), Pair(4, 7), Pair(5, 7)
        ),
        Pair(1, 9) to listOf(
            Pair(1, 9), Pair(2, 9), Pair(3, 9), Pair(4, 9), Pair(5, 9)
        ),
        Pair(4, 0) to listOf(
            Pair(4, 0), Pair(5, 0), Pair(6, 0), Pair(7, 0), Pair(8, 0)
        ),
        Pair(4, 2) to listOf(
            Pair(4, 2), Pair(5, 2), Pair(6, 2), Pair(7, 2), Pair(8, 2), Pair(9, 2)
        ),
        Pair(4, 4) to listOf(
            Pair(4, 4), Pair(5, 4), Pair(6, 4), Pair(7, 4), Pair(8, 4), Pair(9, 4)
        ),
        Pair(6, 6) to listOf(Pair(6, 6), Pair(7, 6), Pair(8, 6), Pair(9, 6)),
        Pair(6, 8) to listOf(Pair(6, 8), Pair(7, 8), Pair(8, 8), Pair(9, 8))
    )
)

// Placed Words: List of Strings
val placedWords = listOf(
    "detonators", "asset", "took", "trey", "statement", "noodle",
    "meadow", "goalie", "nephew", "gently", "bodyguard", "apollo",
    "splattered", "menu", "antsy", "near"
)