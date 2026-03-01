
package com.example.rouglikegame.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rouglikegame.data.GameRepository
import com.example.rouglikegame.model.GameState
import com.example.rouglikegame.ui.screens.RoulikeMode.GameOverScreen
import com.example.rouglikegame.ui.screens.RoulikeMode.GamePlayScreen
import com.example.rouglikegame.ui.screens.RoulikeMode.LevelUpScreen
import com.example.rouglikegame.ui.screens.RoulikeMode.PauseScreen
import com.example.rouglikegame.ui.screens.main.MenuScreen
import com.example.rouglikegame.viewmodel.GameViewModel

@Composable
fun MainScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.gameState) {
            GameState.MENU -> MenuScreen { viewModel.startGame() }
            GameState.PLAYING -> GamePlayScreen(state, { viewModel.pauseGame() }) { viewModel.playerTapAttack() }
            GameState.LEVEL_UP -> LevelUpScreen(state.availableRewards) { viewModel.selectUpgrade(it) }
            GameState.GAME_OVER -> GameOverScreen(state.enemiesDefeated) { viewModel.restartGame() }
            GameState.PAUSE -> PauseScreen { viewModel.pauseGame() }
        }
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun MainScreenPreview() {
    val context = LocalContext.current
    val viewModel: GameViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(GameRepository(context)) as T
            }
        }
    )

    MainScreen(viewModel = viewModel)
}
