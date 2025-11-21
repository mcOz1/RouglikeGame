package com.example.rouglikegame.data

import android.content.Context
import com.example.rouglikegame.model.EffectType
import com.example.rouglikegame.model.GameCharacter
import com.example.rouglikegame.model.Upgrade
import java.io.BufferedReader
import java.io.InputStreamReader

class AssetDataLoader(private val context: Context) {

    fun loadEnemies(): List<GameCharacter> {
        val enemies = mutableListOf<GameCharacter>()
        try {
            // Czyta wszystkie pliki z folderu enemies w assets
            val files = context.assets.list("enemies") ?: return emptyList()
            files.forEach { fileName ->
                parseCharacterFile("enemies/$fileName")?.let { enemies.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return enemies
    }

    fun loadUpgrades(): List<Upgrade> {
        val upgrades = mutableListOf<Upgrade>()
        try {
            val files = context.assets.list("upgrades") ?: return emptyList()
            files.forEach { fileName ->
                parseUpgradeFile("upgrades/$fileName")?.let { upgrades.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return upgrades
    }

    private fun parseCharacterFile(path: String): GameCharacter? {
        val map = readProperties(path) ?: return null
        return GameCharacter(
            name = map["name"] ?: "Nieznany",
            maxHp = map["hp"]?.toIntOrNull() ?: 10,
            currentHp = map["hp"]?.toIntOrNull() ?: 10,
            damage = map["damage"]?.toIntOrNull() ?: 1,
            isBoss = map["isBoss"]?.toBoolean() ?: false,
            imagePath = map["image"]
        )
    }

    private fun parseUpgradeFile(path: String): Upgrade? {
        val map = readProperties(path) ?: return null
        return Upgrade(
            id = map["id"] ?: "unknown",
            name = map["name"] ?: "Upgrade",
            description = map["description"] ?: "",
            effectType = runCatching { EffectType.valueOf(map["effectType"] ?: "HEAL") }.getOrDefault(EffectType.HEAL),
            value = map["value"]?.toIntOrNull() ?: 0,
            imagePath = map["image"]
        )
    }

    private fun readProperties(filePath: String): Map<String, String>? {
        return try {
            val inputStream = context.assets.open(filePath)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val map = mutableMapOf<String, String>()
            reader.useLines { lines ->
                lines.forEach { line ->
                    if (line.contains("=")) {
                        val parts = line.split("=", limit = 2)
                        map[parts[0].trim()] = parts[1].trim()
                    }
                }
            }
            map
        } catch (e: Exception) {
            print(e)
            null
        }
    }
}