package com.example.rouglikegame.model

enum class GameState {
    MENU, PLAYING, LEVEL_UP, GAME_OVER, PAUSE
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
    val imagePath: String? = null,
    val level: Int = 1,
    val upgrades: List<Upgrade> = emptyList()
) {
    fun isAlive() = currentHp > 0
    fun heal(amount: Int? = null): GameCharacter {
        if(amount == null) {
            return this.copy(currentHp = maxHp)
        }
        val newHp = (currentHp + amount).coerceAtMost(maxHp)
        return this.copy(currentHp = newHp)
    }
    fun attack(enemy: GameCharacter): GameCharacter {
        val enemyNewHp = (enemy.currentHp - this.damage).coerceAtLeast(0)
        return enemy.copy(currentHp = enemyNewHp)
    }
}

data class Upgrade(
    val id: String,
    val name: String,
    val description: String,
    val effectType: EffectType,
    val value: Int,
    val imagePath: String? = null
)