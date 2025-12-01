
package com.example.rouglikegame.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rouglikegame.model.GameState
import com.example.rouglikegame.model.Upgrade
import com.example.rouglikegame.ui.components.CharacterDisplay
import com.example.rouglikegame.ui.components.GameColors
import com.example.rouglikegame.ui.components.GameImageFromAssets
import com.example.rouglikegame.viewmodel.GameViewModel

@Composable
fun MainScreen(viewModel: GameViewModel) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.gameState) {
            GameState.MENU -> MenuScreen { viewModel.startGame() }
            GameState.PLAYING -> GamePlayScreen(state) { viewModel.playerTapAttack() }
            GameState.LEVEL_UP -> LevelUpScreen(state.availableRewards) { viewModel.selectUpgrade(it) }
            GameState.GAME_OVER -> GameOverScreen(state.enemiesDefeated) { viewModel.restartGame() }
        }
    }
}

@Composable
fun MenuScreen(onStart: () -> Unit) {
    // Animacja pulsowania tła
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
        // Animowane tło z kołami
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
            // Ikona miecza/tarczy
            Text(
                text = "⚔️",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Tytuł z efektem - responsywny rozmiar
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

            // Przycisk start z gradientem i animacją
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

            // Subtelny tekst instrukcji
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
fun GamePlayScreen(state: com.example.rouglikegame.viewmodel.GameUiState, onTap: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        // --- LEWA STRONA: ARENA ---
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
            // Siatka w tle
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

                    // Efektowne VS z animacją
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

            // Instrukcja z animacją
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

        // Separator z gradientem
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
            // Panel statystyk
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

            // Nagłówek dziennika
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

            // Logi w stylu terminala
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

@Composable
fun LevelUpScreen(rewards: List<Upgrade>, onSelect: (Upgrade) -> Unit) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GameColors.Accent.copy(alpha = 0.2f),
                        GameColors.Background
                    ),
                    radius = 1500f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animowany tytuł
            val scale by rememberInfiniteTransition(label = "title")
                .animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

            Text(
                text = "🏆",
                fontSize = 56.sp,
                modifier = Modifier
                    .scale(scale)
                    .padding(bottom = 8.dp)
            )

            Text(
                text = "ZWYCIĘSTWO!",
                color = GameColors.Accent,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = GameColors.Accent.copy(alpha = 0.5f),
                        blurRadius = 20f
                    )
                )
            )

            Text(
                text = "Wybierz nagrodę za zwycięstwo",
                color = Color.Black,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Karty nagród - responsywny layout
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                rewards.forEachIndexed { index, upgrade ->
                    val isSelected = selectedIndex == index
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "card_scale"
                    )

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 220.dp, max = 280.dp)
                            .scale(scale)
                            .clickable {
                                selectedIndex = index
                                onSelect(upgrade)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = GameColors.Surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 16.dp else 8.dp
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            width = if (isSelected) 3.dp else 2.dp,
                            brush = if (isSelected) {
                                Brush.linearGradient(
                                    colors = listOf(GameColors.Primary, GameColors.Accent)
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        GameColors.Primary.copy(alpha = 0.3f),
                                        GameColors.Secondary.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            GameColors.Primary.copy(alpha = 0.05f)
                                        )
                                    )
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Ikona nagrody
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(64.dp)
                                    .shadow(8.dp, androidx.compose.foundation.shape.CircleShape)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                GameColors.Accent.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            ) {
                                if (upgrade.imagePath != null) {
                                    GameImageFromAssets(path = upgrade.imagePath, size = 48.dp)
                                } else {
                                    Text(
                                        text = when {
                                            upgrade.name.contains("HP") -> "❤️"
                                            upgrade.name.contains("Damage") || upgrade.name.contains("Obrażenia") -> "⚔️"
                                            else -> "✨"
                                        },
                                        fontSize = 40.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = upgrade.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = GameColors.TextPrimary,
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.5.sp,
                                maxLines = 2,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = upgrade.description,
                                fontSize = 11.sp,
                                color = GameColors.TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 14.sp,
                                maxLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Wskaźnik wartości
                            if (upgrade.value > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "+${upgrade.value}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GameColors.Accent,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wskazówka
            Text(
                text = "Kliknij kartę aby wybrać ulepszenie",
                color = GameColors.TextSecondary.copy(alpha = 0.6f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp
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

            // Przycisk restart
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
