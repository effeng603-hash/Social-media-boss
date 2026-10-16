package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Business
import com.example.model.CareerLevel
import com.example.model.PlayerState
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    playerState: PlayerState,
    careerLevel: CareerLevel,
    businesses: List<Business>,
    isDailyCompleted: Boolean,
    onStartMission: () -> Unit,
    onOpenBusinesses: () -> Unit,
    onOpenCareer: () -> Unit,
    onOpenAICoach: () -> Unit,
    onOpenDailyChallenge: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val startingBiz = businesses.firstOrNull() ?: Business(
        id = "biz_urban_threads",
        name = "Urban Threads",
        industry = "Fashion & Apparel",
        description = "Streetwear apparel brand",
        targetAudience = "Gen Z",
        followers = 200,
        engagementRate = 1.4f,
        reputation = 55,
        budget = 400,
        customerSatisfaction = 60
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Player Profile Top Bar
        item {
            CyberCard(
                borderColor = NeonCyanGlow,
                backgroundColor = CyberCardBg
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(onClick = onOpenProfile)
                            .testTag("dashboard_profile_button")
                    ) {
                        val avatarIcon = avatarIcons.getOrElse(playerState.avatarIndex) { Icons.Default.Person }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(NeonCyan, NeonPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = avatarIcon,
                                contentDescription = "Profile Avatar",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = playerState.name,
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = careerLevel.title,
                                color = NeonCyan,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Settings / Profile Icon
                    IconButton(
                        onClick = onOpenProfile,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Row: Cash, Rep, Streak
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatPill(
                        label = "Cash",
                        value = "$${playerState.cash}",
                        icon = Icons.Default.MonetizationOn,
                        color = NeonGold
                    )
                    StatPill(
                        label = "Reputation",
                        value = "${playerState.reputation}%",
                        icon = Icons.Default.ThumbUp,
                        color = NeonGreen
                    )
                    StatPill(
                        label = "Streak",
                        value = "${playerState.streakDays}d",
                        icon = Icons.Default.LocalFireDepartment,
                        color = NeonPink
                    )
                }
            }
        }

        // XP Progress
        item {
            XpProgressBar(
                currentXp = playerState.xp,
                minXp = careerLevel.minXp,
                maxXp = careerLevel.maxXp,
                levelTitle = careerLevel.title,
                levelNumber = careerLevel.levelNumber
            )
        }

        // Hero CTA: START MISSION
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start_mission_banner")
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onStartMission),
                color = CyberCardBg,
                border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(NeonCyan, NeonPurple)))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(NeonCyan.copy(alpha = 0.12f), NeonPurple.copy(alpha = 0.12f))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(NeonCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CAREER MISSIONS",
                                    color = NeonCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "START MISSION",
                                color = TextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${playerState.completedMissionIds.size} of 6 Completed • Earn XP & Cash",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(NeonCyan, NeonPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // Daily Challenge Card
        item {
            CyberCard(
                borderColor = if (isDailyCompleted) NeonGreen.copy(alpha = 0.4f) else NeonPink.copy(alpha = 0.5f),
                modifier = Modifier.clickable(onClick = onOpenDailyChallenge).testTag("daily_challenge_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonPink.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Daily",
                                tint = NeonPink,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "DAILY CHALLENGE",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isDailyCompleted) "Completed for today! Streak +1" else "Earn +80 XP & +$150 Cash",
                                color = if (isDailyCompleted) NeonGreen else TextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (isDailyCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = NeonGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        CyberButton(
                            text = "PLAY",
                            onClick = onOpenDailyChallenge,
                            isPrimary = false,
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }
            }
        }

        // Active Client Spotlight: Urban Threads
        item {
            SectionHeader(
                title = "Primary Client Spotlight",
                subtitle = "Manage brand growth & social analytics",
                actionText = "VIEW ALL (${businesses.size})",
                onActionClick = onOpenBusinesses
            )
            CyberCard(
                borderColor = CyberCardBorder,
                modifier = Modifier.clickable(onClick = onOpenBusinesses)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = startingBiz.name,
                            color = NeonCyan,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${startingBiz.industry} • 30-Day Contract",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = CyberCardSurface
                    ) {
                        Text(
                            text = "ACTIVE",
                            color = NeonGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Followers", color = TextSecondary, fontSize = 11.sp)
                        Text("${startingBiz.followers}", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Engagement", color = TextSecondary, fontSize = 11.sp)
                        Text("${startingBiz.engagementRate}%", color = NeonCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Satisfaction", color = TextSecondary, fontSize = 11.sp)
                        Text("${startingBiz.customerSatisfaction}%", color = NeonPurple, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Budget", color = TextSecondary, fontSize = 11.sp)
                        Text("$${startingBiz.budget}", color = NeonGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Hub Grid (Career, AI Coach, Businesses, Achievements)
        item {
            SectionHeader(title = "Operations Hub")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HubCard(
                    title = "Career Ladder",
                    subtitle = "Level ${careerLevel.levelNumber} / 7",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    color = NeonCyan,
                    onClick = onOpenCareer,
                    modifier = Modifier.weight(1f)
                )
                HubCard(
                    title = "AI Coach",
                    subtitle = "Effeng Marketing AI",
                    icon = Icons.Default.SmartToy,
                    color = NeonPurple,
                    onClick = onOpenAICoach,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HubCard(
                    title = "Businesses",
                    subtitle = "${businesses.count { it.isUnlocked }} Unlocked",
                    icon = Icons.Default.Storefront,
                    color = NeonGold,
                    onClick = onOpenBusinesses,
                    modifier = Modifier.weight(1f)
                )
                HubCard(
                    title = "Badges",
                    subtitle = "${playerState.unlockedAchievementIds.size} / 10 Earned",
                    icon = Icons.Default.EmojiEvents,
                    color = NeonPink,
                    onClick = onOpenAchievements,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun HubCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = CyberCardBg,
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
