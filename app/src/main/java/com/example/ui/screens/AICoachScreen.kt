package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.model.AICoachTip
import com.example.model.CaptionAnalysisResult
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun AICoachScreen(
    captionText: String,
    analysisResult: CaptionAnalysisResult?,
    coachTips: List<AICoachTip>,
    onCaptionChange: (String) -> Unit,
    onAnalyze: () -> Unit,
    onClear: () -> Unit,
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
                    .testTag("coach_back_button")
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
                    text = "AI MARKETING COACH",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Powered by Effeng AI Engine (Offline Ready)",
                    color = NeonPurple,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Caption Evaluator Card
            item {
                CyberCard(borderColor = NeonPurpleGlow) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NeonPurple.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = NeonPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CAPTION AUDITOR",
                                color = NeonPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        if (captionText.isNotEmpty()) {
                            Text(
                                text = "CLEAR",
                                color = TextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable(onClick = onClear)
                                    .padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Enter or paste your draft caption below. The AI evaluates hook power, value retention, readability, and call to action:",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = captionText,
                        onValueChange = onCaptionChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 110.dp)
                            .testTag("caption_input_field"),
                        placeholder = {
                            Text(
                                "e.g. Why do 90% of brands fail at organic video? Here are 3 rules we use to generate 100k views...\n\nSave this for your next post! #marketing #growth",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = NeonCyan
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PresetPill("Sample Hook") {
                            onCaptionChange("Stop wasting money on random ads. Here is the 3-step organic formula that got us 15,000 email subscribers in 60 days.\n\n1. Target acute pain points\n2. Provide micro-checklists\n3. Drive to link in bio\n\nDrop a 🔥 below if you want the full template! #marketing #entrepreneur")
                        }
                        PresetPill("Food Bistro") {
                            onCaptionChange("Craving late-night gourmet? 🍔 Our triple smash burger with smoked chipotle glaze is sizzling until 2AM.\n\nTag a friend who owes you dinner! First 10 comments get 20% off tonight.")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CyberButton(
                        text = "AUDIT CAPTION",
                        onClick = onAnalyze,
                        enabled = captionText.isNotBlank(),
                        icon = Icons.Default.AutoAwesome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("audit_caption_button")
                    )
                }
            }

            // Analysis Result if available
            if (analysisResult != null) {
                item {
                    CaptionResultCard(result = analysisResult)
                }
            }

            // Coach Playbooks
            item {
                SectionHeader(title = "Marketing Playbooks")
            }

            items(coachTips.size) { idx ->
                val tip = coachTips[idx]
                CyberCard(borderColor = CyberCardBorder) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CyberCardSurface
                        ) {
                            Text(
                                text = tip.category.uppercase(),
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tip.question,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tip.coreLesson,
                        color = NeonGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    tip.actionableSteps.forEach { step ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("✓", color = NeonGreen, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(step, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PresetPill(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CyberCardSurface,
        border = BorderStroke(1.dp, CyberCardBorder),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun CaptionResultCard(result: CaptionAnalysisResult) {
    CyberCard(borderColor = NeonCyan) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AUDIT SCORE: ${result.totalScore} / 100",
                color = if (result.totalScore >= 75) NeonGreen else NeonGold,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
            )
            val badge = if (result.totalScore >= 80) "VIRAL READY" else "NEEDS POLISH"
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CyberCardSurface
            ) {
                Text(
                    text = badge,
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Breakdown scores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ScoreBarItem("Hook", result.hookScore, 25)
            ScoreBarItem("Value", result.valueScore, 25)
            ScoreBarItem("Clarity", result.clarityScore, 25)
            ScoreBarItem("CTA", result.ctaScore, 25)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Strengths
        if (result.strengths.isNotEmpty()) {
            Text("Strengths Identified:", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            result.strengths.forEach { s ->
                Text("• $s", color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(vertical = 2.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Improvements
        if (result.improvements.isNotEmpty()) {
            Text("Actionable Recommendations:", color = NeonGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            result.improvements.forEach { imp ->
                Text("• $imp", color = TextSecondary, fontSize = 12.sp, modifier = Modifier.padding(vertical = 2.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Suggestions
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = CyberCardSurface
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("PRO HOOK ALTERNATIVE:", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(result.suggestedHook, color = TextPrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("PRO CTA ALTERNATIVE:", color = NeonPurple, fontSize = 11.sp, fontWeight = FontWeight.Black)
                Text(result.suggestedCta, color = TextPrimary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ScoreBarItem(label: String, score: Int, max: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextSecondary, fontSize = 11.sp)
        Text("$score/$max", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
