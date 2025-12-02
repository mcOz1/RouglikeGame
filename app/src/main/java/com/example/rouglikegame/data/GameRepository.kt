package com.example.rouglikegame.data

import android.content.Context
import com.example.rouglikegame.model.EffectType
import com.example.rouglikegame.model.GameCharacter
import com.example.rouglikegame.model.Upgrade

class GameRepository(context: Context) {
    private val loader = AssetDataLoader(context)
    private var loadedEnemies: List<GameCharacter> = emptyList()
    private var loadedUpgrades: List<Upgrade> = emptyList()

    fun initializeData() {
        loadedEnemies = loader.loadEnemies()
        loadedUpgrades = loader.loadUpgrades()
    }

    fun getRandomEnemy(isBoss: Boolean, level: Int): GameCharacter {
        var level = level
        while (true) {
            val pool = loadedEnemies.filter { it.isBoss == isBoss && it.level == level }
            if (!pool.isEmpty()) {
                return pool.random().copy()
            }
            if (level == 0) {
                return GameCharacter(if(isBoss) "Boss Testowy" else "Wróg Testowy", 20, 20, 3, isBoss, level=level)
            }
            level -= 1
        }
    }

    fun getRandomUpgrades(count: Int): List<Upgrade> {
        return loadedUpgrades.shuffled().take(count)
    }

    // Logika działania przedmiotów
    fun applyUpgrade(player: GameCharacter, upgrade: Upgrade): GameCharacter {
        return when (upgrade.effectType) {
            EffectType.HEAL -> {
                val newHp = (player.currentHp + upgrade.value).coerceAtMost(player.maxHp)
                player.copy(currentHp = newHp, upgrades=player.upgrades.plus(upgrade))
            }
            EffectType.INCREASE_MAX_HP -> {
                player.copy(maxHp = player.maxHp + upgrade.value,
                    currentHp = player.currentHp + upgrade.value,
                    upgrades=player.upgrades.plus(upgrade))
            }
            EffectType.INCREASE_DMG -> {
                player.copy(damage = player.damage + upgrade.value,
                    upgrades=player.upgrades.plus(upgrade))
            }
            EffectType.BERSEKER -> {
                val hpLoss = (player.maxHp * 0.2).toInt()
                player.copy(
                    maxHp = (player.maxHp - hpLoss).coerceAtLeast(1),
                    currentHp = (player.currentHp - hpLoss).coerceAtLeast(1),
                    damage = player.damage + upgrade.value,
                    upgrades=player.upgrades.plus(upgrade)
                )
            }
        }
    }
}