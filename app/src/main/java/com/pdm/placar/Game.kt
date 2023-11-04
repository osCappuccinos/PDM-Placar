package com.pdm.placar

import java.io.Serializable

data class Game(
    var title: String,
    val teamA: Team,
    val teamB: Team,
    val winner: String? = null,
) : Serializable

data class Team(
    var name: String,
    var score: Int,
) : Serializable