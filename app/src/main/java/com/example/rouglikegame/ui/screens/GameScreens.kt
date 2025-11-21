package com.example.rouglikegame.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ROGUELIKE",
                fontSize = 50.sp,
                fontWeight = FontWeight.Black,
                color = GameColors.Primary,
                letterSpacing = 4.sp
            )
            Text("WARRIOR", fontSize = 20.sp, color = GameColors.TextSecondary, letterSpacing = 8.sp)

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = GameColors.Primary),
                modifier = Modifier.width(200.dp).height(50.dp)
            ) {
                Text("NOWA GRA", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        }
    }
}

@Composable
fun GamePlayScreen(state: com.example.rouglikegame.viewmodel.GameUiState, onTap: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxSize()
        .background(GameColors.Background) // Ciemne tło całej gry
    ) {

        // --- LEWA STRONA: ARENA ---
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxHeight()
                .clickable { onTap() }, // Klikalny obszar
            contentAlignment = Alignment.Center
        ) {
            // Subtelne tło areny (można tu dać obrazek tła)
            Box(modifier = Modifier.fillMaxSize().background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A1A1A), Color(0xFF252525))
                )
            ))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CharacterDisplay(state.player, isEnemy = false)

                // Stylizowane VS
                Text(
                    "VS",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White.copy(alpha = 0.2f),
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 4f)
                    )
                )

                if (state.enemy != null) {
                    CharacterDisplay(state.enemy, isEnemy = true)
                }
            }

            // Instrukcja na dole
            Text(
                "TAP TO ATTACK",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                color = GameColors.TextSecondary.copy(alpha = 0.5f),
                fontSize = 14.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Pionowy separator
        Box(modifier = Modifier.fillMaxHeight().width(2.dp).background(Color(0xFF333333)))

        // --- PRAWA STRONA: LOGI I INFO ---
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .background(GameColors.Surface)
                .padding(12.dp)
        ) {
            // Nagłówek statystyk
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("STATYSTYKI", fontSize = 10.sp, color = GameColors.Primary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pokonanych: ${state.enemiesDefeated}",
                        color = GameColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("DZIENNIK BITWY", fontSize = 10.sp, color = GameColors.TextSecondary)
            Divider(color = Color(0xFF333333), modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.logs.reversed()) { log ->
                    // Log w stylu terminala
                    Text(
                        text = "> $log",
                        color = if(log.contains("Pokonałeś") || log.contains("Wybrano")) GameColors.Gold else GameColors.TextPrimary,
                        fontSize = 12.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LevelUpScreen(rewards: List<Upgrade>, onSelect: (Upgrade) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f) // Węższy layout
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "VICTORY!",
                color = GameColors.Gold,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                "Wybierz nagrodę",
                color = GameColors.TextSecondary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rewards.forEach { upgrade ->
                    // Karty nagród zamiast listy
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelect(upgrade) }
                            .height(200.dp),
                        colors = CardDefaults.cardColors(containerColor = GameColors.Surface),
                        border = BorderStroke(2.dp, GameColors.Primary.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (upgrade.imagePath != null) {
                                GameImageFromAssets(path = upgrade.imagePath, size = 64.dp)
                            } else {
                                // Placeholder ikony
                                Box(
                                    Modifier.size(50.dp).background(
                                        GameColors.Primary,
                                        androidx.compose.foundation.shape.CircleShape
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                upgrade.name,
                                fontWeight = FontWeight.Bold,
                                color = GameColors.TextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                upgrade.description,
                                fontSize = 12.sp,
                                color = GameColors.TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onRestart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("KONIEC", color = Color.Red, fontSize = 40.sp)
        Text("Wynik: $score", color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onRestart) { Text("Zagraj ponownie") }
    }
}