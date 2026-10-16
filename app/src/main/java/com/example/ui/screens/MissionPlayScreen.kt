package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MissionOption
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.theme.*
import com.example.viewmodel.MissionPlayState

@Composable
fun MissionPlayScreen(
    state: MissionPlayState,
    onSelectOption: (Int, MissionOption) -> Unit,
    onNextStep: () -> Unit,
    onFinishAndExit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val mission = state.mission ?: return

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
                    .testTag("mission_play_back_button")
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
                    text = "MISSION ${mission.number}: ${mission.title.uppercase()}",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Client: ${mission.clientName} (${mission.industry})",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        if (state.isCompleted) {
            // Completed Results Screen
            MissionCompletedView(
                state = state,
                onContinue = onFinishAndExit
            )
        } else {
            // Active Step Playing View
            val currentStep = mission.steps.getOrNull(state.currentStepIndex)
            if (currentStep != null) {
                MissionStepView(
                    stepIndex = state.currentStepIndex,
                    totalSteps = mission.steps.size,
                    brief = mission.brief,
                    prompt = currentStep.prompt,
                    contextInfo = currentStep.contextInfo,
                    options = currentStep.options,
                    selectedOption = state.selectedOptions[state.currentStepIndex],
                    onSelect = { option -> onSelectOption(state.currentStepIndex, option) },
                    onNext = onNextStep
                )
            }
        }
    }
}

@Composable
fun MissionStepView(
    stepIndex: Int,
    totalSteps: Int,
    brief: String,
    prompt: String,
    contextInfo: String,
    options: List<MissionOption>,
    selectedOption: MissionOption?,
    onSelect: (MissionOption) -> Unit,
    onNext: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step progress & Brief
        item {
            CyberCard(borderColor = CyberCardBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STAGE ${stepIndex + 1} OF $totalSteps",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CyberCardSurface
                    ) {
                        Text(
                            text = "DECISION TIME",
                            color = NeonGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = brief,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // Scenario Prompt
        item {
            Column {
                Text(
                    text = prompt,
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contextInfo,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Options List
        itemsIndexed(options) { idx, option ->
            val isSelected = selectedOption?.id == option.id
            val borderColor = if (isSelected) NeonCyan else CyberCardBorder
            val bgColor = if (isSelected) NeonCyan.copy(alpha = 0.08f) else CyberCardBg

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = bgColor,
                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelect(option) }
                    .testTag("mission_option_$idx")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) NeonCyan else CyberCardSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${('A'.code + idx).toChar()}",
                                color = TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.title,
                            color = if (isSelected) NeonCyan else TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = option.previewText,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Action Button
        item {
            Spacer(modifier = Modifier.height(8.dp))
            CyberButton(
                text = if (stepIndex == totalSteps - 1) "SUBMIT CAMPAIGN" else "CONFIRM & CONTINUE",
                onClick = onNext,
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mission_submit_step_button")
            )
        }
    }
}

@Composable
fun MissionCompletedView(
    state: MissionPlayState,
    onContinue: () -> Unit
) {
    val mission = state.mission ?: return
    val score = state.totalScore
    val grade = when {
        score >= 90 -> "S RANK"
        score >= 80 -> "A RANK"
        score >= 70 -> "B RANK"
        else -> "C RANK"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(NeonCyan, NeonPurple))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Success",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "MISSION COMPLETED!",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = NeonCyanGlow,
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "$grade • SCORE: $score / 100",
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        // Rewards Earned
        item {
            CyberCard(borderColor = NeonGold.copy(alpha = 0.4f)) {
                Text(
                    text = "REWARDS ACQUIRED",
                    color = NeonGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (state.isFirstTimeReward) "+${mission.xpReward} XP" else "+${(mission.xpReward * 0.2).toInt()} XP",
                            color = NeonCyan,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Experience", color = TextSecondary, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (state.isFirstTimeReward) "+$${mission.cashReward}" else "+$${(mission.cashReward * 0.2).toInt()}",
                            color = NeonGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Virtual Cash", color = TextSecondary, fontSize = 11.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "+${state.reputationGain}%",
                            color = NeonGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Reputation", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }

        // Client Metric Impact
        item {
            CyberCard(borderColor = CyberCardBorder) {
                Text(
                    text = "CLIENT IMPACT (${mission.clientName})",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = "Followers: +${state.followerGain}",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Engagement: +${state.engagementGain}%",
                        color = NeonPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Feedback List
        item {
            CyberCard(borderColor = CyberCardBorder) {
                Text(
                    text = "CAMPAIGN EVALUATION & FEEDBACK",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                state.feedbackList.forEachIndexed { idx, fb ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("•", color = NeonCyan, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(fb, color = TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CyberCardSurface
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "KEY TAKEAWAY",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = mission.learningTakeaway,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Exit button
        item {
            CyberButton(
                text = "CONTINUE TO DASHBOARD",
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mission_continue_button")
            )
        }
    }
}
