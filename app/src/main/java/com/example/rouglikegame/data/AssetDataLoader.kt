package com.example.rouglikegame.data

import android.content.Context
import com.example.rouglikegame.model.EffectType
import com.example.rouglikegame.model.GameCharacter
import com.example.rouglikegame.model.Upgrade
import kotlinx.serialization.json.*

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
        return try {
            val inputStream = context.assets.open(path)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            
            GameCharacter(
                name = jsonObject["name"]?.jsonPrimitive?.content ?: "Nieznany",
                maxHp = jsonObject["hp"]?.jsonPrimitive?.intOrNull ?: 10,
                currentHp = jsonObject["hp"]?.jsonPrimitive?.intOrNull ?: 10,
                damage = jsonObject["damage"]?.jsonPrimitive?.intOrNull ?: 1,
                isBoss = jsonObject["isBoss"]?.jsonPrimitive?.booleanOrNull ?: false,
                imagePath = jsonObject["image"]?.jsonPrimitive?.contentOrNull,
                level = jsonObject["level"]?.jsonPrimitive?.intOrNull ?: 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseUpgradeFile(path: String): Upgrade? {
        return try {
            val inputStream = context.assets.open(path)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val json = Json { ignoreUnknownKeys = true }
            val jsonObject = json.parseToJsonElement(jsonString).jsonObject
            
            Upgrade(
                id = jsonObject["id"]?.jsonPrimitive?.content ?: "unknown",
                name = jsonObject["name"]?.jsonPrimitive?.content ?: "Upgrade",
                description = jsonObject["description"]?.jsonPrimitive?.content ?: "",
                effectType = runCatching { 
                    EffectType.valueOf(jsonObject["effectType"]?.jsonPrimitive?.content ?: "HEAL") 
                }.getOrDefault(EffectType.HEAL),
                value = jsonObject["value"]?.jsonPrimitive?.intOrNull ?: 0,
                imagePath = jsonObject["image"]?.jsonPrimitive?.contentOrNull
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}