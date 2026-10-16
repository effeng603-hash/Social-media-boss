package com.example.model

data class PlayerState(
    val name: String = "Boss Player",
    val avatarIndex: Int = 0,
    val xp: Int = 0,
    val cash: Int = 500,
    val reputation: Int = 50,
    val completedMissionIds: Set<String> = emptySet(),
    val unlockedAchievementIds: Set<String> = emptySet(),
    val lastDailyDate: String = "",
    val streakDays: Int = 1,
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true
)

data class CareerLevel(
    val levelNumber: Int,
    val title: String,
    val minXp: Int,
    val maxXp: Int,
    val description: String,
    val perks: List<String>
)

data class Business(
    val id: String,
    val name: String,
    val industry: String,
    val description: String,
    val targetAudience: String,
    val followers: Int,
    val engagementRate: Float, // e.g. 1.8%
    val reputation: Int, // 0..100
    val budget: Int, // marketing budget
    val customerSatisfaction: Int, // 0..100
    val campaignCount: Int = 0,
    val isUnlocked: Boolean = true,
    val unlocksAtLevel: Int = 1
)

enum class MissionType {
    CAPTION_CREATION,
    CONTENT_FORMAT,
    CUSTOMER_CARE,
    CONTENT_CALENDAR,
    AD_CAMPAIGN,
    ANALYTICS_REVIEW
}

data class MissionOption(
    val id: String,
    val title: String,
    val previewText: String,
    val score: Int, // 0 to 100
    val feedback: String,
    val impactFollowers: Int,
    val impactEngagement: Float,
    val impactReputation: Int
)

data class MissionStep(
    val stepNumber: Int,
    val prompt: String,
    val contextInfo: String,
    val options: List<MissionOption>
)

data class Mission(
    val id: String,
    val number: Int,
    val title: String,
    val subtitle: String,
    val levelRequired: Int,
    val xpReward: Int,
    val cashReward: Int,
    val type: MissionType,
    val clientName: String,
    val industry: String,
    val brief: String,
    val steps: List<MissionStep>,
    val learningTakeaway: String
)

data class DailyChallenge(
    val id: String,
    val title: String,
    val category: String,
    val scenario: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val xpReward: Int = 80,
    val cashReward: Int = 150
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val xpReward: Int,
    val cashReward: Int,
    val isUnlocked: Boolean = false
)

data class CaptionAnalysisResult(
    val totalScore: Int,
    val hookScore: Int,
    val valueScore: Int,
    val clarityScore: Int,
    val ctaScore: Int,
    val strengths: List<String>,
    val improvements: List<String>,
    val suggestedHook: String,
    val suggestedCta: String
)

data class AICoachTip(
    val id: String,
    val category: String,
    val question: String,
    val coreLesson: String,
    val actionableSteps: List<String>
)
