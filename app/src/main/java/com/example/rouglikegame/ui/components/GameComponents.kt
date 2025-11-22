package com.example.rouglikegame.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rouglikegame.model.GameCharacter


object GameColors {
    val Background = Color(0xFF0A0E27)
    val Surface = Color(0xFF1A1F3A)
    val SurfaceVariant = Color(0xFF252B4A)
    val Primary = Color(0xFF00D9FF)
    val Secondary = Color(0xFFFF006E)
    val Accent = Color(0xFFFFBE0B)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB8C5D6)
    val HealthGood = Color(0xFF00F5A0)
    val HealthMedium = Color(0xFFFFBE0B)
    val HealthBad = Color(0xFFFF006E)
    val EnemyRed = Color(0xFFFF3D3D)
    val Gold = Color(0xFFFFD700)
}

@Composable
fun StyledHealthBar(current: Int, max: Int, color: Color) {
    val progress = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)

    // Animacja zmiany HP
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "hp_animation"
    )

    // Dynamiczny kolor w zależności od HP
    val dynamicColor = when {
        progress > 0.6f -> GameColors.HealthGood
        progress > 0.3f -> GameColors.HealthMedium
        else -> GameColors.HealthBad
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
    ) {
        // Tło paska
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = GameColors.Primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Pasek HP z gradientem
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            dynamicColor.copy(alpha = 0.8f),
                            dynamicColor
                        )
                    )
                )
        )

        // Efekt połysku
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun CharacterDisplay(
    character: GameCharacter,
    isEnemy: Boolean,
    modifier: Modifier = Modifier
) {
    // Animacja pulsowania dla bossa
    val scale by rememberInfiniteTransition(label = "boss_pulse").animateFloat(
        initialValue = 1f,
        targetValue = if (character.isBoss) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (character.isBoss)
                GameColors.SurfaceVariant
            else
                GameColors.Surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .padding(12.dp)
            .width(160.dp)
            .scale(if (character.isBoss) scale else 1f)
            .then(
                if (character.isBoss) {
                    Modifier.border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(GameColors.EnemyRed, GameColors.Accent, GameColors.EnemyRed)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                } else Modifier
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            // Nazwa z efektem
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                color = when {
                    character.isBoss -> GameColors.EnemyRed
                    isEnemy -> GameColors.Secondary
                    else -> GameColors.Primary
                },
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Obrazek w ozdobnej ramce
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(if (character.isBoss) 120.dp else 100.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = if (isEnemy) GameColors.EnemyRed else GameColors.Primary
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GameColors.SurfaceVariant,
                                GameColors.Background
                            )
                        )
                    )
                    .border(
                        width = 3.dp,
                        brush = if (character.isBoss) {
                            Brush.sweepGradient(
                                colors = listOf(
                                    GameColors.EnemyRed,
                                    GameColors.Accent,
                                    GameColors.EnemyRed
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    if (isEnemy) GameColors.Secondary else GameColors.Primary,
                                    if (isEnemy) GameColors.EnemyRed else GameColors.Accent
                                )
                            )
                        },
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                if (character.imagePath != null) {
                    GameImageFromAssets(
                        path = character.imagePath,
                        size = if (character.isBoss) 100.dp else 80.dp
                    )
                } else {
                    StickmanFigure(
                        isEnemy = isEnemy,
                        isBoss = character.isBoss,
                        size = if (character.isBoss) 100.dp else 80.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Pasek HP
            StyledHealthBar(
                current = character.currentHp,
                max = character.maxHp,
                color = if (isEnemy) GameColors.HealthBad else GameColors.HealthGood
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Statystyki
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // HP
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "❤",
                        fontSize = 14.sp,
                        color = GameColors.EnemyRed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${character.currentHp}/${character.maxHp}",
                        fontSize = 12.sp,
                        color = GameColors.TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // DMG
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚔",
                        fontSize = 14.sp,
                        color = GameColors.Accent
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${character.damage}",
                        fontSize = 12.sp,
                        color = GameColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GameImageFromAssets(path: String, size: Dp) {
    val context = LocalContext.current
    val assetUri = "file:///android_asset/$path"

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(assetUri)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}

@Composable
fun StickmanFigure(isEnemy: Boolean, isBoss: Boolean, size: Dp) {
    val color = when {
        isBoss -> GameColors.EnemyRed
        isEnemy -> GameColors.Secondary
        else -> GameColors.Primary
    }

    Canvas(modifier = Modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val strokeWidth = if (isBoss) 5.dp.toPx() else 4.dp.toPx()

        // Cień
        drawCircle(
            color = Color.Black.copy(alpha = 0.3f),
            center = Offset(w / 2 + 2.dp.toPx(), h / 4 + 2.dp.toPx()),
            radius = h / 8,
            style = Stroke(strokeWidth)
        )

        // Głowa z gradientem
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color, color.copy(alpha = 0.7f))
            ),
            center = Offset(w / 2, h / 4),
            radius = h / 8,
            style = Stroke(strokeWidth)
        )

        // Tułów
        drawLine(
            color = color,
            start = Offset(w / 2, h / 4 + h / 8),
            end = Offset(w / 2, h * 0.65f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Ręce
        drawLine(
            color = color,
            start = Offset(w / 2, h * 0.38f),
            end = Offset(w * 0.15f, h * 0.45f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w / 2, h * 0.38f),
            end = Offset(w * 0.85f, h * 0.45f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        // Nogi
        drawLine(
            color = color,
            start = Offset(w / 2, h * 0.65f),
            end = Offset(w * 0.25f, h * 0.95f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(w / 2, h * 0.65f),
            end = Offset(w * 0.75f, h * 0.95f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}