package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Business
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.StatPill
import com.example.ui.theme.*

@Composable
fun BusinessSimulatorScreen(
    businesses: List<Business>,
    playerCash: Int,
    currentLevelNumber: Int,
    onSelectBusiness: (Business) -> Unit,
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
                    .testTag("businesses_back_button")
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
                    text = "BUSINESS SIMULATOR",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Manage & scale virtual brand accounts",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(businesses) { biz ->
                BusinessItemCard(
                    business = biz,
                    onClick = { if (biz.isUnlocked) onSelectBusiness(biz) }
                )
            }
        }
    }
}

@Composable
fun BusinessItemCard(
    business: Business,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (business.isUnlocked) NeonCyan.copy(alpha = 0.35f) else CyberCardBorder

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CyberCardBg,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = business.isUnlocked, onClick = onClick)
            .testTag("business_card_${business.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = business.name,
                        color = if (business.isUnlocked) NeonCyan else TextMuted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = business.industry,
                        color = if (business.isUnlocked) TextSecondary else TextMuted,
                        fontSize = 12.sp
                    )
                }

                if (business.isUnlocked) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CyberCardSurface
                    ) {
                        Text(
                            text = "MANAGE",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CyberCardSurface
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LEVEL ${business.unlocksAtLevel}",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = business.description,
                color = if (business.isUnlocked) TextSecondary else TextMuted,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (business.isUnlocked) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Followers", color = TextSecondary, fontSize = 11.sp)
                        Text("${business.followers}", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Engagement", color = TextSecondary, fontSize = 11.sp)
                        Text("${business.engagementRate}%", color = NeonCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Satisfaction", color = TextSecondary, fontSize = 11.sp)
                        Text("${business.customerSatisfaction}%", color = NeonPurple, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Campaigns", color = TextSecondary, fontSize = 11.sp)
                        Text("${business.campaignCount}", color = NeonGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Text(
                    text = "Requires Level ${business.unlocksAtLevel} to manage this enterprise brand.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BusinessDetailScreen(
    business: Business,
    playerCash: Int,
    onRunAction: (String, Int) -> Unit,
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
                    .testTag("biz_detail_back_button")
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
                    text = business.name.uppercase(),
                    color = NeonCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = business.industry,
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
            // Live Brand Metrics
            item {
                CyberCard(borderColor = NeonCyanGlow) {
                    Text(
                        text = "BRAND TELEMETRY",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Audience", color = TextSecondary, fontSize = 11.sp)
                            Text("${business.followers}", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Engagement Rate", color = TextSecondary, fontSize = 11.sp)
                            Text("${business.engagementRate}%", color = NeonCyan, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("Brand Satisfaction", color = TextSecondary, fontSize = 11.sp)
                            Text("${business.customerSatisfaction}%", color = NeonPurple, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Target Audience: ${business.targetAudience}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Available Budget
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatPill(
                        label = "Your Cash",
                        value = "$$playerCash",
                        icon = Icons.Default.MonetizationOn,
                        color = NeonGold
                    )
                    StatPill(
                        label = "Brand Reputation",
                        value = "${business.reputation}%",
                        icon = Icons.Default.ThumbUp,
                        color = NeonGreen
                    )
                }
            }

            // Strategic Actions
            item {
                Text(
                    text = "DEPLOY MARKETING ACTIONS",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            item {
                ActionCard(
                    title = "Organic Content Surge",
                    description = "Produce & publish viral reels and carousel education. High engagement and steady follower conversion.",
                    cost = 50,
                    playerCash = playerCash,
                    icon = Icons.Default.Create,
                    color = NeonCyan,
                    onExecute = { onRunAction("ORGANIC_POST", 50) }
                )
            }

            item {
                ActionCard(
                    title = "Targeted Paid Ad Blitz",
                    description = "Run hyper-targeted feed ads with custom demographic segmentation. Massive reach spike.",
                    cost = 150,
                    playerCash = playerCash,
                    icon = Icons.Default.Campaign,
                    color = NeonPurple,
                    onExecute = { onRunAction("PAID_BOOST", 150) }
                )
            }

            item {
                ActionCard(
                    title = "Community Care Drive",
                    description = "Dedicated community management: reply to all comments, resolve DMs, and reward top fans.",
                    cost = 30,
                    playerCash = playerCash,
                    icon = Icons.Default.Forum,
                    color = NeonGreen,
                    onExecute = { onRunAction("COMMUNITY_REPLY", 30) }
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    cost: Int,
    playerCash: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onExecute: () -> Unit
) {
    val canAfford = playerCash >= cost

    CyberCard(borderColor = color.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cost: $$cost",
                        color = if (canAfford) NeonGold else NeonRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            CyberButton(
                text = "EXECUTE",
                onClick = onExecute,
                enabled = canAfford,
                isPrimary = canAfford,
                modifier = Modifier.height(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
        )
    }
}
