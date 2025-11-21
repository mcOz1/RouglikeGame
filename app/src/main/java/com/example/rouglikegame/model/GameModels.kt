package com.example.rouglikegame.model

enum class GameState {
    MENU, PLAYING, LEVEL_UP, GAME_OVER
}

enum class EffectType {
    HEAL, INCREASE_MAX_HP, INCREASE_DMG, BERSEKER
}

data class GameCharacter(
    val name: String,
    val maxHp: Int,
    var currentHp: Int,
    val damage: Int,
    val isBoss: Boolean = false,
    val imagePath: String? = null
) {
    fun isAlive() = currentHp > 0
}

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val effectType: EffectType,
    val value: Int,
    val imagePath: String? = null
)