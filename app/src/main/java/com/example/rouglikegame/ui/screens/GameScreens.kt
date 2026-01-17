
package com.example.rouglikegame.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rouglikegame.model.GameState
import com.example.rouglikegame.ui.components.GameColors
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

@Composable
fun MenuScreen(onStart: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GameColors.Background,
                        GameColors.Surface,
                        GameColors.Background
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GameColors.Primary.copy(alpha = alpha * 0.3f),
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.3f),
                        radius = 1000f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚔️",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "Shadow Tap",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = GameColors.Primary,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = GameColors.Primary.copy(alpha = 0.5f),
                        blurRadius = 20f
                    )
                ),
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Text(
                text = "Rogue Waves",
                fontSize = 18.sp,
                color = GameColors.TextSecondary,
                letterSpacing = 6.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.05f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "button_scale"
            )

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .scale(scale)
                    .shadow(16.dp, RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                GameColors.Primary,
                                GameColors.Secondary
                            )
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(28.dp)
                    ),
                shape = RoundedCornerShape(28.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "▶ NOWA GRA",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pokonaj fale wrogów i zostań mistrzem",
                color = GameColors.TextSecondary.copy(alpha = 0.6f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}


@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0000),
                        GameColors.Background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ikona czaszki
            Text(
                text = "💀",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "KONIEC GRY",
                color = GameColors.EnemyRed,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = GameColors.EnemyRed.copy(alpha = 0.5f),
                        blurRadius = 20f
                    )
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .width(240.dp)
                    .height(50.dp)
                    .shadow(12.dp, RoundedCornerShape(25.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                GameColors.EnemyRed,
                                GameColors.Secondary
                            )
                        ),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(25.dp)
                    ),
                shape = RoundedCornerShape(25.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(
                    text = "ZAGRAJ PONOWNIE",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Panel wyniku
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = GameColors.Surface
                ),
                elevation = CardDefaults.cardElevation(12.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = GameColors.EnemyRed.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "POKONANYCH WROGÓW",
                        color = GameColors.TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$score",
                        color = GameColors.Accent,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
