package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CareerLevel
import com.example.ui.components.CyberCard
import com.example.ui.components.XpProgressBar
import com.example.ui.theme.*

@Composable
fun CareerScreen(
    careerLevels: List<CareerLevel>,
    currentLevel: CareerLevel,
    currentXp: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("career_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "CAREER PROGRESSION",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "From Beginner to Social Media Boss",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                XpProgressBar(
                    currentXp = currentXp,
                    minXp = currentLevel.minXp,
                    maxXp = currentLevel.maxXp,
                    levelTitle = currentLevel.title,
                    levelNumber = currentLevel.levelNumber
                )
            }

            items(careerLevels) { lvl ->
                val isCurrent = lvl.levelNumber == currentLevel.levelNumber
                val isPast = lvl.levelNumber < currentLevel.levelNumber
                val isLocked = lvl.levelNumber > currentLevel.levelNumber

                val borderColor = when {
                    isCurrent -> NeonCyan
                    isPast -> NeonGreen.copy(alpha = 0.5f)
                    else -> CyberCardBorder
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CyberCardBg,
                    border = BorderStroke(if (isCurrent) 1.5.dp else 1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isCurrent -> Brush.linearGradient(listOf(NeonCyan, NeonPurple))
                                                isPast -> Brush.linearGradient(listOf(NeonGreen, NeonGreen))
                                                else -> Brush.linearGradient(listOf(CyberCardSurface, CyberCardSurface))
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isPast) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Completed",
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "L${lvl.levelNumber}",
                                            color = if (isCurrent) Color.Black else TextMuted,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = lvl.title,
                                        color = if (isLocked) TextMuted else TextPrimary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Req: ${lvl.minXp} XP",
                                        color = if (isCurrent) NeonCyan else TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (isCurrent) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = NeonCyanGlow,
                                    border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "CURRENT",
                                        color = NeonCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else if (isLocked) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = lvl.description,
                            color = if (isLocked) TextMuted else TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "UNLOCKED PERKS:",
                            color = if (isLocked) TextMuted else NeonGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        lvl.perks.forEach { perk ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (isLocked) TextMuted else NeonCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = perk,
                                    color = if (isLocked) TextMuted else TextPrimary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
