package com.example.rouglikegame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rouglikegame.data.GameRepository
import com.example.rouglikegame.model.GameCharacter
import com.example.rouglikegame.model.GameState
import com.example.rouglikegame.model.Upgrade
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

data class GameUiState(
    val gameState: GameState = GameState.MENU,
    val player: GameCharacter = GameCharacter("Bohater", 20, 20, 2),
    val enemy: GameCharacter? = null,
    val enemiesDefeated: Int = 0,
    val availableRewards: List<Upgrade> = emptyList(),
    val logs: List<String> = listOf("Witaj w grze!")
)

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()
    private var autoAttackJob: Job? = null

    init {
        // Inicjalizacja danych w tle
        viewModelScope.launch(Dispatchers.IO) {
            repository.initializeData()
        }
    }

    fun startGame() {
        _uiState.update {
            GameUiState(
                gameState = GameState.PLAYING,
                player = GameCharacter("Bohater", 20, 20, 2)
            )
        }
        spawnNextEnemy()
        startAutoAttackLoop()
    }

    private fun startAutoAttackLoop() {
        autoAttackJob?.cancel()
        autoAttackJob = viewModelScope.launch {
            while (_uiState.value.gameState == GameState.PLAYING) {
                delay(5000L) // 5 sekund
                performRound(auto = true)
            }
        }
    }

    fun playerTapAttack() {
        if (_uiState.value.gameState == GameState.PLAYING) {
            performRound(auto = false)
        }
    }

    private fun performRound(auto: Boolean) {
        val state = _uiState.value
        val enemy = state.enemy ?: return
        var player = state.player
        var logMsg: String

        // Atak gracza
        val newEnemyHp = (enemy.currentHp - player.damage).coerceAtLeast(0)
        val updatedEnemy = player.attack(enemy)

        if (auto) {
            // Wymiana ciosów
            if (newEnemyHp > 0) {
                player = enemy.attack(player)
                logMsg = "Auto: Wymiana ciosów! (-${enemy.damage} HP)"
            } else {
                logMsg = "Auto: Pokonałeś wroga!"
            }
        } else {
            logMsg = "Klik: Szybki atak za ${player.damage}!"
        }

        _uiState.update {
            it.copy(player = player, enemy = updatedEnemy, logs = (it.logs + logMsg).takeLast(4))
        }

        checkStatus(player, updatedEnemy)
    }

    private fun checkStatus(player: GameCharacter, enemy: GameCharacter) {
        if (!player.isAlive()) {
            _uiState.update { it.copy(gameState = GameState.GAME_OVER) }
            autoAttackJob?.cancel()
        } else if (!enemy.isAlive()) {
            val defeated = _uiState.value.enemiesDefeated + 1
            _uiState.update { it.copy(enemiesDefeated = defeated) }

            if (enemy.isBoss) {
                prepareLevelUp()
            } else {
                spawnNextEnemy()
            }
        }
    }

    private fun spawnNextEnemy() {
//        val state = _uiState.value
        val defeated = _uiState.value.enemiesDefeated
        // Co 10 przeciwników boss
        val isBossLevel = (defeated + 1) % 10 == 0 && defeated != 0

        val template = repository.getRandomEnemy(isBossLevel)
        // Skalowanie HP zwykłych wrogów
        val scaledHp = if(!isBossLevel) template.maxHp + defeated else template.maxHp

        _uiState.update {
            it.copy(
                enemy = template.copy(maxHp = scaledHp, currentHp = scaledHp),
                logs = it.logs + "Pojawia się: ${template.name}",
//                player = state.player.heal()
            )
        }
    }

    private fun prepareLevelUp() {
        autoAttackJob?.cancel()
        val rewards = repository.getRandomUpgrades(3)
        _uiState.update { it.copy(gameState = GameState.LEVEL_UP, availableRewards = rewards) }
    }

    fun selectUpgrade(upgrade: Upgrade) {
        val newPlayer = repository.applyUpgrade(_uiState.value.player, upgrade)
        _uiState.update {
            it.copy(player = newPlayer, gameState = GameState.PLAYING, logs = it.logs + "Ulepszenie: ${upgrade.name}")
        }
        spawnNextEnemy()
        startAutoAttackLoop()
    }

    fun restartGame() {
        startGame()
    }
}