package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    DASHBOARD,
    MISSIONS,
    MISSION_PLAY,
    BUSINESSES,
    BUSINESS_DETAIL,
    CAREER,
    AI_COACH,
    DAILY_CHALLENGE,
    ACHIEVEMENTS,
    PROFILE_SETTINGS
}

data class MissionPlayState(
    val mission: Mission? = null,
    val currentStepIndex: Int = 0,
    val selectedOptions: Map<Int, MissionOption> = emptyMap(),
    val isCompleted: Boolean = false,
    val totalScore: Int = 0,
    val followerGain: Int = 0,
    val engagementGain: Float = 0f,
    val reputationGain: Int = 0,
    val feedbackList: List<String> = emptyList(),
    val isFirstTimeReward: Boolean = true
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    val repository = GameRepository(application.applicationContext)

    val playerState: StateFlow<PlayerState> = repository.playerState
    val businesses: StateFlow<List<Business>> = repository.businesses

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _missionPlayState = MutableStateFlow(MissionPlayState())
    val missionPlayState: StateFlow<MissionPlayState> = _missionPlayState.asStateFlow()

    private val _selectedBusiness = MutableStateFlow<Business?>(null)
    val selectedBusiness: StateFlow<Business?> = _selectedBusiness.asStateFlow()

    private val _dailyChallengeState = MutableStateFlow<Pair<Int?, Boolean?>?>(null) // selectedIndex, isCorrect
    val dailyChallengeState: StateFlow<Pair<Int?, Boolean?>?> = _dailyChallengeState.asStateFlow()

    private val _captionInput = MutableStateFlow("")
    val captionInput: StateFlow<String> = _captionInput.asStateFlow()

    private val _captionAnalysis = MutableStateFlow<CaptionAnalysisResult?>(null)
    val captionAnalysis: StateFlow<CaptionAnalysisResult?> = _captionAnalysis.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val currentCareerLevel: CareerLevel
        get() = repository.calculateLevel(playerState.value.xp)

    val isDailyCompletedToday: Boolean
        get() = playerState.value.lastDailyDate == repository.getTodayString()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    fun startMission(mission: Mission) {
        _missionPlayState.value = MissionPlayState(
            mission = mission,
            currentStepIndex = 0,
            selectedOptions = emptyMap(),
            isCompleted = false
        )
        _currentScreen.value = Screen.MISSION_PLAY
    }

    fun selectMissionOption(stepIndex: Int, option: MissionOption) {
        val current = _missionPlayState.value
        val updatedMap = current.selectedOptions.toMutableMap()
        updatedMap[stepIndex] = option
        _missionPlayState.value = current.copy(selectedOptions = updatedMap)
    }

    fun nextMissionStep() {
        val current = _missionPlayState.value
        val mission = current.mission ?: return
        if (current.currentStepIndex < mission.steps.size - 1) {
            _missionPlayState.value = current.copy(currentStepIndex = current.currentStepIndex + 1)
        } else {
            // Finish mission
            finishMission()
        }
    }

    private fun finishMission() {
        val current = _missionPlayState.value
        val mission = current.mission ?: return

        var sumScore = 0
        var totalFollowers = 0
        var totalEng = 0f
        var totalRep = 0
        val feedbacks = mutableListOf<String>()

        mission.steps.forEachIndexed { idx, _ ->
            val opt = current.selectedOptions[idx]
            if (opt != null) {
                sumScore += opt.score
                totalFollowers += opt.impactFollowers
                totalEng += opt.impactEngagement
                totalRep += opt.impactReputation
                feedbacks.add(opt.feedback)
            }
        }

        val avgScore = if (mission.steps.isNotEmpty()) sumScore / mission.steps.size else 0
        val isFirstTime = repository.completeMission(mission, avgScore, totalFollowers, totalEng, totalRep)

        _missionPlayState.value = current.copy(
            isCompleted = true,
            totalScore = avgScore,
            followerGain = totalFollowers,
            engagementGain = totalEng,
            reputationGain = totalRep,
            feedbackList = feedbacks,
            isFirstTimeReward = isFirstTime
        )

        _toastMessage.value = if (avgScore >= 80) "Awesome Job! +${mission.xpReward} XP, +$${mission.cashReward}" else "Mission Completed! Review feedback to improve."
    }

    fun selectBusiness(business: Business) {
        _selectedBusiness.value = business
        _currentScreen.value = Screen.BUSINESS_DETAIL
    }

    fun runBusinessAction(actionType: String, cost: Int) {
        val biz = _selectedBusiness.value ?: return
        val (success, message) = repository.runBusinessAction(biz.id, actionType, cost)
        _toastMessage.value = message
        if (success) {
            // refresh selected business from repo
            _selectedBusiness.value = repository.businesses.value.firstOrNull { it.id == biz.id }
        }
    }

    fun answerDailyChallenge(challenge: DailyChallenge, selectedIndex: Int) {
        val isCorrect = selectedIndex == challenge.correctIndex
        _dailyChallengeState.value = Pair(selectedIndex, isCorrect)
        val awarded = repository.completeDailyChallenge(challenge, isCorrect)
        if (awarded) {
            _toastMessage.value = "Correct! +${challenge.xpReward} XP, +$${challenge.cashReward} Streak: ${playerState.value.streakDays} Days 🔥"
        } else if (!isCorrect) {
            _toastMessage.value = "Not quite! Read the explanation to learn why."
        }
    }

    fun updateCaptionInput(text: String) {
        _captionInput.value = text
    }

    fun analyzeCaption() {
        val result = repository.analyzeCaptionLocally(_captionInput.value)
        _captionAnalysis.value = result
    }

    fun clearCaptionAnalysis() {
        _captionAnalysis.value = null
        _captionInput.value = ""
    }

    fun updatePlayerProfile(name: String, avatarIndex: Int) {
        repository.updatePlayerProfile(name, avatarIndex)
        _toastMessage.value = "Profile updated successfully!"
    }

    fun toggleSound() {
        repository.toggleSound(!playerState.value.soundEnabled)
    }

    fun toggleHaptics() {
        repository.toggleHaptics(!playerState.value.hapticEnabled)
    }

    fun resetGame() {
        repository.resetGame()
        _dailyChallengeState.value = null
        _captionAnalysis.value = null
        _captionInput.value = ""
        _toastMessage.value = "Game progress has been reset."
        _currentScreen.value = Screen.DASHBOARD
    }
}
