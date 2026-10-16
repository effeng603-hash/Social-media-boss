package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyChallenge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.theme.*

@Composable
fun DailyChallengeScreen(
    challenge: DailyChallenge,
    isCompletedToday: Boolean,
    challengeResult: Pair<Int?, Boolean?>?,
    streakDays: Int,
    onAnswer: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var selectedIndex by remember { mutableStateOf<Int?>(challengeResult?.first) }
    val isEvaluated = isCompletedToday || challengeResult != null

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
                    .testTag("daily_back_button")
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
                    text = "DAILY CHALLENGE",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Daily test of marketing acumen • ${streakDays}d Streak 🔥",
                    color = NeonPink,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reward Banner
            item {
                CyberCard(borderColor = NeonPink.copy(alpha = 0.4f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = challenge.title.uppercase(),
                                color = NeonPink,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Category: ${challenge.category}",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CyberCardSurface
                            ) {
                                Text(
                                    text = "+${challenge.xpReward} XP",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CyberCardSurface
                            ) {
                                Text(
                                    text = "+$${challenge.cashReward}",
                                    color = NeonGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Scenario Question
            item {
                CyberCard(borderColor = CyberCardBorder) {
                    Text(
                        text = "SCENARIO OF THE DAY",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = challenge.scenario,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )
                }
            }

            // Options
            itemsIndexed(challenge.options) { idx, optionText ->
                val isSelected = selectedIndex == idx
                val isCorrectOption = idx == challenge.correctIndex

                val cardBorder = when {
                    isEvaluated && isCorrectOption -> NeonGreen
                    isEvaluated && isSelected && !isCorrectOption -> NeonRed
                    isSelected -> NeonCyan
                    else -> CyberCardBorder
                }

                val cardBg = when {
                    isEvaluated && isCorrectOption -> NeonGreen.copy(alpha = 0.1f)
                    isEvaluated && isSelected && !isCorrectOption -> NeonRed.copy(alpha = 0.1f)
                    isSelected -> NeonCyan.copy(alpha = 0.08f)
                    else -> CyberCardBg
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = cardBg,
                    border = BorderStroke(1.dp, cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(enabled = !isEvaluated) {
                            selectedIndex = idx
                        }
                        .testTag("daily_option_$idx")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isEvaluated && isCorrectOption -> NeonGreen
                                        isEvaluated && isSelected && !isCorrectOption -> NeonRed
                                        isSelected -> NeonCyan
                                        else -> CyberCardSurface
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${('A'.code + idx).toChar()}",
                                color = if (isSelected || (isEvaluated && isCorrectOption)) Color.Black else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = optionText,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Action / Explanation
            if (!isEvaluated) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    CyberButton(
                        text = "LOCK IN ANSWER",
                        onClick = {
                            selectedIndex?.let { onAnswer(it) }
                        },
                        enabled = selectedIndex != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("daily_submit_button")
                    )
                }
            } else {
                item {
                    val wasCorrect = (challengeResult?.second == true) || isCompletedToday
                    CyberCard(borderColor = if (wasCorrect) NeonGreen else NeonGold) {
                        Text(
                            text = if (wasCorrect) "EXCELLENT ANALYSIS!" else "LEARNING OPPORTUNITY",
                            color = if (wasCorrect) NeonGreen else NeonGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = challenge.explanation,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
