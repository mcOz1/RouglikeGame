package com.example.rouglikegame.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rouglikegame.model.GameCharacter

// Dodaj na samym początku pliku ui/components/GameComponents.kt
object GameColors {
    val Background = Color(0xFF121212) // Ciemne tło
    val Surface = Color(0xFF1E1E1E)    // Tło kart
    val Primary = Color(0xFFBB86FC)    // Akcent (fiolet)
    val TextPrimary = Color(0xFFEEEEEE)
    val TextSecondary = Color(0xFFB0B0B0)
    val HealthGood = Color(0xFF43A047) // Soczysta zieleń
    val HealthBad = Color(0xFFE53935)  // Czerwień
    val Gold = Color(0xFFFFD700)       // Złoto dla nagród
}

@Composable
fun StyledHealthBar(current: Int, max: Int, color: Color) {
    val progress = (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(12.dp)
            .background(Color.Black.copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
            .padding(1.dp) // Padding tworzy efekt ramki
    ) {
        // Pasek właściwy
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(color, androidx.compose.foundation.shape.CircleShape)
        )
    }
}

@Composable
fun CharacterDisplay(
    character: GameCharacter,
    isEnemy: Boolean,
    modifier: Modifier = Modifier
) {
    // Karta postaci z cieniem i tłem
    Card(
        colors = CardDefaults.cardColors(containerColor = GameColors.Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(8.dp)
            .width(140.dp) // Stała szerokość karty
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            // Nazwa
            Text(
                character.name,
                style = MaterialTheme.typography.titleMedium,
                color = if(character.isBoss) GameColors.HealthBad else GameColors.TextPrimary,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Obrazek w ramce
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(if (character.isBoss) 110.dp else 90.dp)
            ) {
                if (character.imagePath != null) {
                    GameImageFromAssets(path = character.imagePath, size = if (character.isBoss) 100.dp else 80.dp)
                } else {
                    StickmanFigure(
                        isEnemy = isEnemy,
                        isBoss = character.isBoss,
                        size = if (character.isBoss) 100.dp else 80.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Statystyki
            StyledHealthBar(
                current = character.currentHp,
                max = character.maxHp,
                color = if (isEnemy) GameColors.HealthBad else GameColors.HealthGood
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${character.currentHp}/${character.maxHp}", fontSize = 10.sp, color = GameColors.TextSecondary)
                Text("DMG: ${character.damage}", fontSize = 10.sp, color = GameColors.TextPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun GameImageFromAssets(path: String, size: Dp) {
    val context = LocalContext.current
    // Specjalny prefix Coil do ładowania z assets
    val assetUri = "file:///android_asset/$path"

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(assetUri)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier.size(size)
    )
}

@Composable
fun StickmanFigure(isEnemy: Boolean, isBoss: Boolean, size: Dp) {
    val color = if (isEnemy) Color.Red else Color.Blue
    Canvas(modifier = Modifier.size(size)) {
        val w = size.toPx()
        val h = size.toPx()
        val s = 4.dp.toPx()

        drawCircle(color, center = Offset(w/2, h/4), radius = h/8, style = Stroke(s)) // Głowa
        drawLine(color, Offset(w/2, h/4 + h/8), Offset(w/2, h*0.75f), s, StrokeCap.Round) // Tułów
        drawLine(color, Offset(w/2, h*0.4f), Offset(w*0.1f, h*0.3f), s, StrokeCap.Round) // Ręce
        drawLine(color, Offset(w/2, h*0.4f), Offset(w*0.9f, h*0.3f), s, StrokeCap.Round)
        drawLine(color, Offset(w/2, h*0.75f), Offset(w*0.2f, h), s, StrokeCap.Round) // Nogi
        drawLine(color, Offset(w/2, h*0.75f), Offset(w*0.8f, h), s, StrokeCap.Round)
    }
}