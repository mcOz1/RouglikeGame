package com.example.rouglikegame.ui.screens.RoulikeMode

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rouglikegame.model.GameCharacter
import com.example.rouglikegame.model.GameState
import com.example.rouglikegame.ui.components.CharacterDisplay
import com.example.rouglikegame.ui.components.GameColors
import com.example.rouglikegame.viewmodel.GameUiState

@Composable
fun GamePlayScreen(state: com.example.rouglikegame.viewmodel.GameUiState, onPause: () -> Unit, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Box(
            modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight()
                .clickable { onTap() }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1117),
                            Color(0xFF161B22),
                            Color(0xFF0D1117)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GameColors.Primary.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            radius = 1000f
                        )
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        CharacterDisplay(state.player, isEnemy = false)
                    }

                    val rotation by rememberInfiniteTransition(label = "vs_rotation")
                        .animateFloat(
                            initialValue = -5f,
                            targetValue = 5f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "rotation"
                        )

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(60.dp)
                            .rotate(rotation)
                    ) {
                        Text(
                            text = "VS",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = GameColors.Accent,
                            style = androidx.compose.ui.text.TextStyle(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = GameColors.Accent.copy(alpha = 0.6f),
                                    blurRadius = 12f
                                )
                            )
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        if (state.enemy != null) {
                            CharacterDisplay(state.enemy, isEnemy = true)
                        }
                    }
                }
            }

            val alpha by rememberInfiniteTransition(label = "instruction")
                .animateFloat(
                    initialValue = 0.3f,
                    targetValue = 0.8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "alpha"
                )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👆",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Stuknij",
                    color = GameColors.Primary.copy(alpha = alpha),
                    fontSize = 14.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            GameColors.Primary.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        // --- PRAWA STRONA: STATYSTYKI I LOGI ---
        Column(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            GameColors.Surface,
                            GameColors.SurfaceVariant
                        )
                    )
                )
                .padding(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = GameColors.Background
                ),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(GameColors.Primary, GameColors.Secondary)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Button(onClick = onPause) {
                        Text(
                            text = "||",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = "📊 STATYSTYKI",
                        fontSize = 10.sp,
                        color = GameColors.Primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💀",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "${state.enemiesDefeated}",
                                color = GameColors.Accent,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "WROGÓW",
                                color = GameColors.TextSecondary,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📜 DZIENNIK BITWY",
                    fontSize = 10.sp,
                    color = GameColors.TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 1.5.dp,
                color = GameColors.Primary.copy(alpha = 0.3f)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(state.logs.reversed()) { log ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1C1C1C)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = ">",
                                color = GameColors.Primary,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = log,
                                color = when {
                                    log.contains("Pokonałeś") -> GameColors.HealthGood
                                    log.contains("Wybrano") -> GameColors.Accent
                                    log.contains("otrzymujesz") -> GameColors.Gold
                                    else -> GameColors.TextPrimary
                                },
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun GamePlayScreenPreview() {
    val samplePlayer = GameCharacter(
        name = "Bohater",
        maxHp = 100,
        currentHp = 75,
        damage = 15,
        isBoss = false,
        imagePath = null,
        level = 3
    )

    val sampleEnemy = GameCharacter(
        name = "Zombie",
        maxHp = 50,
        currentHp = 30,
        damage = 8,
        isBoss = false,
        imagePath = null,
        level = 1
    )

    val sampleState = GameUiState(
        player = samplePlayer,
        enemy = sampleEnemy,
        gameState = GameState.PLAYING,
        enemiesDefeated = 12,
        logs = listOf(
            "Pojawia się: Zombie",
            "Klik: Szybki atak za 15!",
            "Auto: Wymiana ciosów! (-8 HP)",
            "Auto: Pokonałeś wroga!",
            "Wybrano ulepszenie: Zwiększ Obrażenia"
        )
    )

    GamePlayScreen(
        state = sampleState,
        onPause = {},
        onTap = {}
    )
}

