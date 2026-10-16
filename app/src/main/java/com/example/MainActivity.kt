package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.GameBottomBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.Screen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SocialMediaBossTheme {
                MainGameApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainGameApp(viewModel: GameViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val businesses by viewModel.businesses.collectAsStateWithLifecycle()
    val missionPlayState by viewModel.missionPlayState.collectAsStateWithLifecycle()
    val selectedBusiness by viewModel.selectedBusiness.collectAsStateWithLifecycle()
    val dailyChallengeState by viewModel.dailyChallengeState.collectAsStateWithLifecycle()
    val captionInput by viewModel.captionInput.collectAsStateWithLifecycle()
    val captionAnalysis by viewModel.captionAnalysis.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val currentCareerLevel = viewModel.currentCareerLevel
    val isDailyCompleted = viewModel.isDailyCompletedToday
    val dailyChallenge = viewModel.repository.dailyChallenges.first()

    val showBottomBar = currentScreen in listOf(
        Screen.DASHBOARD,
        Screen.MISSIONS,
        Screen.BUSINESSES,
        Screen.CAREER,
        Screen.AI_COACH
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberDarkBg)
    ) {
        // Content Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showBottomBar) 64.dp else 0.dp)
        ) {
            when (currentScreen) {
                Screen.SPLASH -> {
                    SplashScreen(
                        onStartGame = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.DASHBOARD -> {
                    DashboardScreen(
                        playerState = playerState,
                        careerLevel = currentCareerLevel,
                        businesses = businesses,
                        isDailyCompleted = isDailyCompleted,
                        onStartMission = { viewModel.navigateTo(Screen.MISSIONS) },
                        onOpenBusinesses = { viewModel.navigateTo(Screen.BUSINESSES) },
                        onOpenCareer = { viewModel.navigateTo(Screen.CAREER) },
                        onOpenAICoach = { viewModel.navigateTo(Screen.AI_COACH) },
                        onOpenDailyChallenge = { viewModel.navigateTo(Screen.DAILY_CHALLENGE) },
                        onOpenAchievements = { viewModel.navigateTo(Screen.ACHIEVEMENTS) },
                        onOpenProfile = { viewModel.navigateTo(Screen.PROFILE_SETTINGS) }
                    )
                }

                Screen.MISSIONS -> {
                    MissionsScreen(
                        missions = viewModel.repository.missions,
                        playerState = playerState,
                        currentLevelNumber = currentCareerLevel.levelNumber,
                        onSelectMission = { mission -> viewModel.startMission(mission) },
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.MISSION_PLAY -> {
                    MissionPlayScreen(
                        state = missionPlayState,
                        onSelectOption = { stepIdx, opt -> viewModel.selectMissionOption(stepIdx, opt) },
                        onNextStep = { viewModel.nextMissionStep() },
                        onFinishAndExit = { viewModel.navigateTo(Screen.DASHBOARD) },
                        onBack = { viewModel.navigateTo(Screen.MISSIONS) }
                    )
                }

                Screen.BUSINESSES -> {
                    BusinessSimulatorScreen(
                        businesses = businesses,
                        playerCash = playerState.cash,
                        currentLevelNumber = currentCareerLevel.levelNumber,
                        onSelectBusiness = { biz -> viewModel.selectBusiness(biz) },
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.BUSINESS_DETAIL -> {
                    if (selectedBusiness != null) {
                        BusinessDetailScreen(
                            business = selectedBusiness!!,
                            playerCash = playerState.cash,
                            onRunAction = { actionType, cost -> viewModel.runBusinessAction(actionType, cost) },
                            onBack = { viewModel.navigateTo(Screen.BUSINESSES) }
                        )
                    } else {
                        viewModel.navigateTo(Screen.BUSINESSES)
                    }
                }

                Screen.CAREER -> {
                    CareerScreen(
                        careerLevels = viewModel.repository.careerLevels,
                        currentLevel = currentCareerLevel,
                        currentXp = playerState.xp,
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.AI_COACH -> {
                    AICoachScreen(
                        captionText = captionInput,
                        analysisResult = captionAnalysis,
                        coachTips = viewModel.repository.coachTips,
                        onCaptionChange = { viewModel.updateCaptionInput(it) },
                        onAnalyze = { viewModel.analyzeCaption() },
                        onClear = { viewModel.clearCaptionAnalysis() },
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.DAILY_CHALLENGE -> {
                    DailyChallengeScreen(
                        challenge = dailyChallenge,
                        isCompletedToday = isDailyCompleted,
                        challengeResult = dailyChallengeState,
                        streakDays = playerState.streakDays,
                        onAnswer = { idx -> viewModel.answerDailyChallenge(dailyChallenge, idx) },
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.ACHIEVEMENTS -> {
                    AchievementsScreen(
                        achievements = viewModel.repository.achievements,
                        unlockedIds = playerState.unlockedAchievementIds,
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }

                Screen.PROFILE_SETTINGS -> {
                    ProfileSettingsScreen(
                        playerState = playerState,
                        careerLevel = currentCareerLevel,
                        onUpdateProfile = { name, avatar -> viewModel.updatePlayerProfile(name, avatar) },
                        onToggleSound = { viewModel.toggleSound() },
                        onToggleHaptics = { viewModel.toggleHaptics() },
                        onResetGame = { viewModel.resetGame() },
                        onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                    )
                }
            }
        }

        // Bottom Navigation Bar
        if (showBottomBar) {
            GameBottomBar(
                currentScreen = currentScreen,
                onSelectScreen = { screen -> viewModel.navigateTo(screen) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        // Real-time Game Toast / Notification Banner
        toastMessage?.let { msg ->
            GameNotificationBanner(
                message = msg,
                onDismiss = { viewModel.dismissToast() },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun GameNotificationBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(message) {
        delay(3500)
        onDismiss()
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = CyberCardBg,
        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onDismiss,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
                Text("DISMISS", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
