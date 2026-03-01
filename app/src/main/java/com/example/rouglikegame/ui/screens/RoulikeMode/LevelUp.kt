package com.example.rouglikegame.ui.screens.RoulikeMode

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rouglikegame.model.EffectType
import com.example.rouglikegame.model.Upgrade
import com.example.rouglikegame.ui.components.GameColors
import com.example.rouglikegame.ui.components.GameImageFromAssets

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

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun LevelUpScreenPreview() {
    val sampleRewards = listOf(
        Upgrade(
            id = "heal_potion",
            name = "Mikstura Leczenia",
            description = "Przywraca zdrowie",
            effectType = EffectType.HEAL,
            value = 30,
            imagePath = null
        ),
        Upgrade(
            id = "damage_boost",
            name = "Zwiększ Obrażenia",
            description = "Twój atak staje się silniejszy",
            effectType = EffectType.INCREASE_DMG,
            value = 5,
            imagePath = null
        ),
        Upgrade(
            id = "max_hp_boost",
            name = "Zwiększ Max HP",
            description = "Zwiększa maksymalne zdrowie",
            effectType = EffectType.INCREASE_MAX_HP,
            value = 20,
            imagePath = null
        )
    )

    LevelUpScreen(
        rewards = sampleRewards,
        onSelect = {}
    )
}
