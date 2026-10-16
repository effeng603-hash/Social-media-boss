package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class GameRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("social_media_boss_prefs", Context.MODE_PRIVATE)

    // --- Career Levels ---
    val careerLevels = listOf(
        CareerLevel(
            levelNumber = 1,
            title = "Beginner",
            minXp = 0,
            maxXp = 250,
            description = "Learn social media basics: hooks, captions, and content formats.",
            perks = listOf("Unlock Urban Threads client", "Access Mission 1 & 2", "Daily Practice Challenges")
        ),
        CareerLevel(
            levelNumber = 2,
            title = "Junior SMM",
            minXp = 250,
            maxXp = 650,
            description = "Manage posting schedules, customer inquiries, and community engagement.",
            perks = listOf("Unlock Neon Bite Bistro", "Access Customer Care missions", "+10% Daily Reward Bonus")
        ),
        CareerLevel(
            levelNumber = 3,
            title = "Content Creator",
            minXp = 650,
            maxXp = 1250,
            description = "Develop high-engagement promotional content and creative campaigns.",
            perks = listOf("Unlock GlowLab Botanicals", "Access 7-Day Content Calendar planning", "Custom Caption Evaluator")
        ),
        CareerLevel(
            levelNumber = 4,
            title = "Social Media Strategist",
            minXp = 1250,
            maxXp = 2100,
            description = "Plan paid ad campaigns, target demographics, and allocate marketing budgets.",
            perks = listOf("Unlock Apex Skyline Realty", "Access Ad Campaign Simulator", "High-Budget Clients")
        ),
        CareerLevel(
            levelNumber = 5,
            title = "Marketing Expert",
            minXp = 2100,
            maxXp = 3200,
            description = "Analyze deep performance metrics, optimize ROAS, and manage multiple brands.",
            perks = listOf("Unlock Pulsewave Records", "Access Advanced Analytics Review", "+25% Client Retainer")
        ),
        CareerLevel(
            levelNumber = 6,
            title = "Agency Founder",
            minXp = 3200,
            maxXp = 4600,
            description = "Scale digital agency operations, hire virtual talent, and pitch enterprise clients.",
            perks = listOf("Unlock HyperVolt Tech", "Multi-Client Dashboard", "Master Agency Badge")
        ),
        CareerLevel(
            levelNumber = 7,
            title = "Social Media Boss",
            minXp = 4600,
            maxXp = 10000,
            description = "The ultimate marketing authority. Dominate viral campaigns and drive massive ROI.",
            perks = listOf("Full Business Empire unlocked", "Boss Hall of Fame", "VIP Strategy Coaching")
        )
    )

    private val _playerState = MutableStateFlow(loadPlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _businesses = MutableStateFlow(loadBusinesses())
    val businesses: StateFlow<List<Business>> = _businesses.asStateFlow()

    init {
        checkDailyStreak()
    }

    // --- Missions ---
    val missions = listOf(
        Mission(
            id = "m1_first_post",
            number = 1,
            title = "Create Your First Post",
            subtitle = "Craft a high-converting hook & caption for Urban Threads",
            levelRequired = 1,
            xpReward = 120,
            cashReward = 200,
            type = MissionType.CAPTION_CREATION,
            clientName = "Urban Threads",
            industry = "Fashion & Apparel",
            brief = "Urban Threads is launching their oversized drop. They only have 200 followers and low reach. They need a caption that stops the scroll, conveys the premium quality, and drives comments.",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "Select the best opening hook for the post:",
                    contextInfo = "The first line determines whether users tap 'more' or scroll away.",
                    options = listOf(
                        MissionOption(
                            id = "m1_s1_a",
                            title = "Generic announcement",
                            previewText = "\"New clothes are available now in our shop. Check it out.\"",
                            score = 30,
                            feedback = "Too boring. Lacks intrigue, emotion, or value. Users scroll past generic sales talk.",
                            impactFollowers = 5,
                            impactEngagement = 0.2f,
                            impactReputation = -2
                        ),
                        MissionOption(
                            id = "m1_s1_b",
                            title = "Sensory & Problem-Solving Hook",
                            previewText = "\"We spent 6 months obsessed with one question: why do most hoodies lose their shape after 3 washes? Meet the Heavyweight Box-Fit.\"",
                            score = 95,
                            feedback = "Outstanding! Directly highlights craft, solves a universal pain point, and sparks curiosity.",
                            impactFollowers = 60,
                            impactEngagement = 3.2f,
                            impactReputation = 8
                        ),
                        MissionOption(
                            id = "m1_s1_c",
                            title = "Aggressive discount hook",
                            previewText = "\"CHEAP DISCOUNT HOODIES BUY 1 GET 1 FREE TODAY ONLY!!!\"",
                            score = 50,
                            feedback = "Feels spammy and damages brand perception of high streetwear quality, even if it attracts quick clicks.",
                            impactFollowers = 20,
                            impactEngagement = 1.1f,
                            impactReputation = -5
                        )
                    )
                ),
                MissionStep(
                    stepNumber = 2,
                    prompt = "Choose the strongest Call-To-Action (CTA) for the end of the post:",
                    contextInfo = "The algorithm prioritizes comments and saves over passive double taps.",
                    options = listOf(
                        MissionOption(
                            id = "m1_s2_a",
                            title = "Conversation starter CTA",
                            previewText = "\"Drop your favorite colorway below: Vintage Washed Grey or Midnight Obsidian? We are gifting 2 pieces to top comments.\"",
                            score = 100,
                            feedback = "Brilliant! Specific question + incentive drives massive comment velocity, boosting post reach.",
                            impactFollowers = 45,
                            impactEngagement = 2.8f,
                            impactReputation = 6
                        ),
                        MissionOption(
                            id = "m1_s2_b",
                            title = "Passive statement",
                            previewText = "\"Thanks for reading and following our page.\"",
                            score = 35,
                            feedback = "A passive ending with no clear action. The algorithm does not reward dead ends.",
                            impactFollowers = 5,
                            impactEngagement = 0.1f,
                            impactReputation = 0
                        ),
                        MissionOption(
                            id = "m1_s2_c",
                            title = "Hard direct link ask",
                            previewText = "\"Click link in bio to buy right now.\"",
                            score = 65,
                            feedback = "Decent for bottom-of-funnel, but without initial engagement, few people will visit the bio.",
                            impactFollowers = 15,
                            impactEngagement = 0.8f,
                            impactReputation = 2
                        )
                    )
                )
            ),
            learningTakeaway = "Great posts hook attention with curiosity or pain points, deliver clear value, and end with an easy question to drive conversation."
        ),
        Mission(
            id = "m2_content_format",
            number = 2,
            title = "Choose the Right Content Format",
            subtitle = "Match creative format to brand objective",
            levelRequired = 1,
            xpReward = 140,
            cashReward = 220,
            type = MissionType.CONTENT_FORMAT,
            clientName = "Urban Threads",
            industry = "Fashion & Apparel",
            brief = "Urban Threads wants to prove that their $85 price tag is justified compared to fast-fashion alternatives. What format achieves this best?",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "Select the optimal content format for product education & value demonstration:",
                    contextInfo = "The goal is building trust and demonstrating tangible fabric & stitching quality.",
                    options = listOf(
                        MissionOption(
                            id = "m2_s1_a",
                            title = "Static Flat-Lay Photo",
                            previewText = "A single overhead photo of the hoodie on a white studio background.",
                            score = 40,
                            feedback = "Looks like a basic catalog item. Fails to convey weight, texture, or fit on a human body.",
                            impactFollowers = 10,
                            impactEngagement = 0.5f,
                            impactReputation = 1
                        ),
                        MissionOption(
                            id = "m2_s1_b",
                            title = "Macro ASMR Video / Fabric Stress-Test",
                            previewText = "Short vertical video showing close-up 450GSM cotton weave, double-needle stitching, and a wash-test comparison.",
                            score = 98,
                            feedback = "Exceptional choice! Macro video proofs build unshakeable credibility and retain viewer watch time.",
                            impactFollowers = 95,
                            impactEngagement = 4.1f,
                            impactReputation = 10
                        ),
                        MissionOption(
                            id = "m2_s1_c",
                            title = "Text-only Quote Card",
                            previewText = "\"Quality is not an act, it is a habit. - Urban Threads\"",
                            score = 30,
                            feedback = "Empty motivational quote. Doesn't show the product or solve customer skepticism.",
                            impactFollowers = 2,
                            impactEngagement = 0.3f,
                            impactReputation = -1
                        )
                    )
                )
            ),
            learningTakeaway = "For high-ticket or premium goods, show, don't just tell. Proof-of-quality video formats beat static photos for conversion."
        ),
        Mission(
            id = "m3_handle_angry_customer",
            number = 3,
            title = "Handle an Angry Customer",
            subtitle = "Crisis PR & customer service excellence",
            levelRequired = 2,
            xpReward = 180,
            cashReward = 280,
            type = MissionType.CUSTOMER_CARE,
            clientName = "Neon Bite Bistro",
            industry = "Food & Hospitality",
            brief = "A food influencer with 15k followers comments publicly: 'Waited 90 mins for our takeout order, food arrived cold, and nobody answered the phone. Horrible service! Never ordering again.'",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "How should the brand publicly respond in the comments?",
                    contextInfo = "Other prospective diners are watching this exchange closely.",
                    options = listOf(
                        MissionOption(
                            id = "m3_s1_a",
                            title = "Defensive blame shifting",
                            previewText = "\"It's not our fault, third-party courier was late and our kitchen was super busy tonight. You should be more understanding.\"",
                            score = 15,
                            feedback = "Disastrous. Arguing publicly destroys reputation and turns an isolated mistake into a PR disaster.",
                            impactFollowers = -40,
                            impactEngagement = 1.0f,
                            impactReputation = -25
                        ),
                        MissionOption(
                            id = "m3_s1_b",
                            title = "Delete comment and block user",
                            previewText = "[Delete comment silently and hope nobody noticed]",
                            score = 20,
                            feedback = "The influencer posted screenshots to their story! Brand accused of censorship and cowardice.",
                            impactFollowers = -80,
                            impactEngagement = 0.5f,
                            impactReputation = -30
                        ),
                        MissionOption(
                            id = "m3_s1_c",
                            title = "The HEAR Protocol (Hear, Empathize, Apologize, Resolve)",
                            previewText = "\"Hi Marcus, this is totally unacceptable and far below our standards. Cold food and 90 mins is a ruined dinner, and we own that mistake. Sending you a DM right now with a full refund and personal voucher for chef's tasting on us.\"",
                            score = 100,
                            feedback = "Masterclass in crisis management! Publicly took full accountability, empathized, and moved resolution to DM.",
                            impactFollowers = 120,
                            impactEngagement = 3.5f,
                            impactReputation = 15
                        )
                    )
                )
            ),
            learningTakeaway = "Turn complaints into brand loyalty: Acknowledge reality, apologize sincerely, fix it with generosity, and keep public discourse respectful."
        ),
        Mission(
            id = "m4_content_calendar",
            number = 4,
            title = "Plan a 7-Day Content Calendar",
            subtitle = "Design a balanced, sustainable weekly posting cadence",
            levelRequired = 3,
            xpReward = 220,
            cashReward = 350,
            type = MissionType.CONTENT_CALENDAR,
            clientName = "GlowLab Botanicals",
            industry = "Skincare & Beauty",
            brief = "GlowLab Botanicals is posting only 'Buy our moisturizer' posts and losing 50 followers a week. Plan a diversified 7-day calendar using the 70/20/10 Rule.",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "Select the most effective weekly content pillar balance:",
                    contextInfo = "70% Educational & Entertaining, 20% Community & UGC, 10% Direct Sales Promo.",
                    options = listOf(
                        MissionOption(
                            id = "m4_s1_a",
                            title = "100% Promo Aggression",
                            previewText = "7 days of discounts, product link promotions, and flash sales.",
                            score = 25,
                            feedback = "Follower fatigue hits hard. Unsubscribes surge and Instagram's algorithm suppresses repetitive sales links.",
                            impactFollowers = -30,
                            impactEngagement = -1.2f,
                            impactReputation = -10
                        ),
                        MissionOption(
                            id = "m4_s1_b",
                            title = "Balanced Value-First Framework",
                            previewText = "Mon: Skincare myth busting, Wed: Routine tutorial carousel, Fri: Real customer before/after UGC, Sun: Limited bundle promo + Q&A.",
                            score = 96,
                            feedback = "Superb strategy! Value creates trust, UGC builds social proof, and Sunday's promotion converts warm leads.",
                            impactFollowers = 180,
                            impactEngagement = 3.9f,
                            impactReputation = 14
                        ),
                        MissionOption(
                            id = "m4_s1_c",
                            title = "Pure viral memes with zero product tie-in",
                            previewText = "Posting funny trending cat memes every day without any skincare context.",
                            score = 45,
                            feedback = "Draws random clicks, but zero relevant buyers. None of these followers care about botanical skincare.",
                            impactFollowers = 70,
                            impactEngagement = 2.0f,
                            impactReputation = -2
                        )
                    )
                )
            ),
            learningTakeaway = "A content calendar needs variety. Give 80% free value (education, entertainment) so followers happily support the 20% sales asks."
        ),
        Mission(
            id = "m5_launch_campaign",
            number = 5,
            title = "Launch a Paid Ad Campaign",
            subtitle = "Allocate ad budget, objective, and creative angle",
            levelRequired = 4,
            xpReward = 260,
            cashReward = 450,
            type = MissionType.AD_CAMPAIGN,
            clientName = "Apex Skyline Realty",
            industry = "Real Estate",
            brief = "Apex Realty has a $1,000 monthly ad spend to generate qualified homebuyer leads for a modern downtown condo development.",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "Select Campaign Objective & Audience Targeting:",
                    contextInfo = "High-ticket purchases require hyper-local targeting and intent-driven objectives.",
                    options = listOf(
                        MissionOption(
                            id = "m5_s1_a",
                            title = "Broad Nationwide Reach Campaign",
                            previewText = "Target ages 18-65 nationwide, optimize for 'Post Likes'.",
                            score = 20,
                            feedback = "Budget wasted. You got 10,000 cheap likes from teenagers across the country who cannot buy real estate.",
                            impactFollowers = 200,
                            impactEngagement = 1.0f,
                            impactReputation = -5
                        ),
                        MissionOption(
                            id = "m5_s1_b",
                            title = "Local Lead Generation with Virtual Tour Funnel",
                            previewText = "Target 30-mile radius, ages 28-55, interests: First-Time Homebuyers, Architecture. Lead form with automated brochure & WhatsApp booking.",
                            score = 100,
                            feedback = "Incredible ROI! Generated 42 high-intent buyer inquiries at $23 per lead, resulting in 3 signed private showings.",
                            impactFollowers = 150,
                            impactEngagement = 2.4f,
                            impactReputation = 20
                        ),
                        MissionOption(
                            id = "m5_s1_c",
                            title = "Traffic to Home Page with no tracking pixel",
                            previewText = "Send traffic to general website homepage without a dedicated landing page.",
                            score = 45,
                            feedback = "Visitors get lost on general navigation and bounce without converting.",
                            impactFollowers = 40,
                            impactEngagement = 0.6f,
                            impactReputation = 2
                        )
                    )
                )
            ),
            learningTakeaway = "In paid advertising, vanity metrics (likes/reach) don't pay bills. Match targeted demographics to clear lead-capture funnels."
        ),
        Mission(
            id = "m6_review_performance",
            number = 6,
            title = "Review Performance & Analytics",
            subtitle = "Decode marketing metrics and formulate data-driven pivots",
            levelRequired = 5,
            xpReward = 300,
            cashReward = 600,
            type = MissionType.ANALYTICS_REVIEW,
            clientName = "Pulsewave Records",
            industry = "Entertainment & Music",
            brief = "Pulsewave launched a single release campaign. Stats: 100k impressions, 4.8% CTR (very high), but 85% bounce rate on the Spotify landing link. What is happening?",
            steps = listOf(
                MissionStep(
                    stepNumber = 1,
                    prompt = "Analyze the analytics discrepancy and choose the correct remedy:",
                    contextInfo = "High Click-Through-Rate (CTR) means creative is great; high bounce means expectation mismatch or landing page friction.",
                    options = listOf(
                        MissionOption(
                            id = "m6_s1_a",
                            title = "Blame the creative and change ad imagery",
                            previewText = "\"The ad video is clearly failing, scrap it and shoot a new video.\"",
                            score = 30,
                            feedback = "Incorrect analysis. A 4.8% CTR is phenomenal—the creative did its job perfectly by getting people to click.",
                            impactFollowers = 10,
                            impactEngagement = 0.2f,
                            impactReputation = -2
                        ),
                        MissionOption(
                            id = "m6_s1_b",
                            title = "Fix Destination Friction & Smart Deep-Linking",
                            previewText = "\"The landing page took 6 seconds to load and asked users to log in through mobile web. Implement instant Spotify SmartLinks that launch native app directly.\"",
                            score = 100,
                            feedback = "Genius diagnostic! Removing web login friction drops bounce rate from 85% to 18%, doubling stream conversions instantly.",
                            impactFollowers = 450,
                            impactEngagement = 4.8f,
                            impactReputation = 22
                        ),
                        MissionOption(
                            id = "m6_s1_c",
                            title = "Double the ad budget to brute-force streams",
                            previewText = "\"Just spend $5,000 more so even with high bounce rate we get enough listeners.\"",
                            score = 20,
                            feedback = "Pouring water into a leaking bucket. Fix the funnel leak before scaling spend.",
                            impactFollowers = 50,
                            impactEngagement = 0.5f,
                            impactReputation = -10
                        )
                    )
                )
            ),
            learningTakeaway = "Every metric tells a story. High CTR + High Bounce Rate indicates an issue at the destination (speed, friction, or misleading hook)."
        )
    )

    // --- Achievements ---
    val achievements = listOf(
        Achievement("ach_first_mission", "First Step to Glory", "Complete your first mission successfully.", "trophy", 100, 150),
        Achievement("ach_caption_master", "Caption Architect", "Score 90%+ on a copywriting mission.", "edit", 120, 200),
        Achievement("ach_crisis_manager", "Crisis PR Virtuoso", "Solve a customer complaint with 100% satisfaction.", "shield", 150, 250),
        Achievement("ach_calendar_guru", "Strategic Planner", "Complete 7-day content schedule planning.", "calendar", 160, 300),
        Achievement("ach_ad_wizard", "Ad Spend Mastermind", "Deliver positive ROI in paid ad simulator.", "trending_up", 200, 400),
        Achievement("ach_data_detective", "Data Sleuth", "Correctly diagnose an analytics funnel leak.", "analytics", 220, 450),
        Achievement("ach_streak_3", "Consistency Beast", "Maintain a 3-day daily challenge streak.", "local_fire_department", 180, 350),
        Achievement("ach_client_unlocked", "Agency Expansion", "Unlock your third virtual client.", "store", 250, 500),
        Achievement("ach_rep_80", "Reputation Royalty", "Reach 80+ Brand Reputation.", "stars", 300, 600),
        Achievement("ach_social_boss", "The Social Media Boss", "Reach Level 7 Career rank.", "military_tech", 1000, 2000)
    )

    // --- Daily Challenges Pool ---
    val dailyChallenges = listOf(
        DailyChallenge(
            id = "dc_hook_test",
            title = "Hook Optimizer",
            category = "Copywriting",
            scenario = "A local specialty coffee roastery is launching cold brew cans. Which hook generates the highest curiosity & save rate on Instagram?",
            options = listOf(
                "\"Our cold brew is on sale this weekend only.\"",
                "\"The exact chemistry behind why brewing coffee cold eliminates 68% of stomach acidity:\"",
                "\"Drink our new coffee cans now! Link in bio.\"",
                "\"Happy Friday coffee lovers! Have a great weekend.\""
            ),
            correctIndex = 1,
            explanation = "Educational curiosity hooks that promise actionable insider knowledge trigger high save-rates, which strongly alerts the discovery algorithm."
        ),
        DailyChallenge(
            id = "dc_hashtag_strategy",
            title = "Hashtag Master",
            category = "Algorithm & Discovery",
            scenario = "An account with 1,200 followers wants to rank in search. Which hashtag strategy is most effective?",
            options = listOf(
                "Spamming #love, #instagood, and #viral (1 Billion+ posts)",
                "Using 0 hashtags because they are completely useless",
                "Targeting 3-5 niche-specific hashtags (10k-250k volume) matching exact post keywords",
                "Copying 30 random tags into the comments section"
            ),
            correctIndex = 2,
            explanation = "Hyper-relevant, moderate-volume hashtags give smaller accounts the highest probability of ranking in top search feeds."
        ),
        DailyChallenge(
            id = "dc_engagement_audit",
            title = "Algorithm Investigator",
            category = "Strategy",
            scenario = "Why did an account's reach collapse after they bought 5,000 followers on a third-party website?",
            options = listOf(
                "Instagram hates small creators.",
                "Fake bot followers don't view or engage with posts, causing engagement rate to crater so algorithm stops recommending it.",
                "The followers need 24 hours to sync.",
                "They didn't use enough emojis in their bio."
            ),
            correctIndex = 1,
            explanation = "When a new post is tested on a sample of followers and none engage (because they are bots), the platform concludes the content is low-grade and kills organic distribution."
        )
    )

    // --- AI Coach Tips ---
    val coachTips = listOf(
        AICoachTip(
            id = "tip_hook_framework",
            category = "Copywriting",
            question = "How do I write hooks that stop mindless scrolling?",
            coreLesson = "The Hook Formula: Pattern Interrupt + Emotional Benefit or Intriguing Gap.",
            actionableSteps = listOf(
                "Break expectations in the first 1.5 seconds or first 8 words.",
                "Use specific numbers (e.g. '3 reasons' or '$147 mistake') instead of vague generalities.",
                "Address a known frustration directly: 'If you struggle with X, try this:'"
            )
        ),
        AICoachTip(
            id = "tip_algorithm_secrets",
            category = "Algorithm",
            question = "What does the algorithm actually reward in 2026?",
            coreLesson = "Retention Time, DM Shares, and Saves drive 80% of viral reach.",
            actionableSteps = listOf(
                "DM Shares are the #1 viral signal: Create content that makes users want to tag a friend or send to their group chat.",
                "Saves signify durable value: Create cheat sheets, tutorials, or resource lists people bookmark.",
                "Watch Time: Avoid long rambling intros; get straight to the payoff."
            )
        ),
        AICoachTip(
            id = "tip_conversion",
            category = "Sales & ROI",
            question = "How do I turn casual followers into paying clients?",
            coreLesson = "Followers don't buy because they like you; they buy when they trust you can solve their exact problem.",
            actionableSteps = listOf(
                "Showcase unedited client transformations and case studies regularly.",
                "Give a low-friction entry offer (free audit, mini template, checklist).",
                "Use direct keyword automation (e.g., 'Comment AUDIT to get the free checklist')."
            )
        )
    )

    // --- State Management & Persistence ---

    private fun loadPlayerState(): PlayerState {
        val name = prefs.getString("player_name", "Boss Player") ?: "Boss Player"
        val avatar = prefs.getInt("avatar_index", 0)
        val xp = prefs.getInt("player_xp", 0)
        val cash = prefs.getInt("player_cash", 500)
        val rep = prefs.getInt("player_rep", 50)
        val completed = prefs.getStringSet("completed_missions", emptySet()) ?: emptySet()
        val achievements = prefs.getStringSet("unlocked_achievements", emptySet()) ?: emptySet()
        val lastDate = prefs.getString("last_daily_date", "") ?: ""
        val streak = prefs.getInt("streak_days", 1)
        val sound = prefs.getBoolean("sound_enabled", true)
        val haptic = prefs.getBoolean("haptic_enabled", true)

        return PlayerState(
            name = name,
            avatarIndex = avatar,
            xp = xp,
            cash = cash,
            reputation = rep,
            completedMissionIds = completed,
            unlockedAchievementIds = achievements,
            lastDailyDate = lastDate,
            streakDays = streak,
            soundEnabled = sound,
            hapticEnabled = haptic
        )
    }

    private fun savePlayerState(state: PlayerState) {
        prefs.edit().apply {
            putString("player_name", state.name)
            putInt("avatar_index", state.avatarIndex)
            putInt("player_xp", state.xp)
            putInt("player_cash", state.cash)
            putInt("player_rep", state.reputation)
            putStringSet("completed_missions", state.completedMissionIds)
            putStringSet("unlocked_achievements", state.unlockedAchievementIds)
            putString("last_daily_date", state.lastDailyDate)
            putInt("streak_days", state.streakDays)
            putBoolean("sound_enabled", state.soundEnabled)
            putBoolean("haptic_enabled", state.hapticEnabled)
            apply()
        }
        _playerState.value = state
    }

    private fun loadBusinesses(): List<Business> {
        val currentXp = prefs.getInt("player_xp", 0)
        val currentLevel = calculateLevel(currentXp).levelNumber

        return listOf(
            Business(
                id = "biz_urban_threads",
                name = "Urban Threads",
                industry = "Fashion & Streetwear",
                description = "Struggling indie apparel brand with premium heavy box-fit hoodies and streetwear.",
                targetAudience = "Gen Z & Millennials (18-28), streetwear enthusiasts, aesthetic minimalists",
                followers = prefs.getInt("biz_urban_followers", 200),
                engagementRate = prefs.getFloat("biz_urban_eng", 1.4f),
                reputation = prefs.getInt("biz_urban_rep", 55),
                budget = prefs.getInt("biz_urban_budget", 400),
                customerSatisfaction = prefs.getInt("biz_urban_sat", 60),
                campaignCount = prefs.getInt("biz_urban_camps", 0),
                isUnlocked = true,
                unlocksAtLevel = 1
            ),
            Business(
                id = "biz_neon_bite",
                name = "Neon Bite Bistro",
                industry = "Food & Hospitality",
                description = "Late night fusion burger joint with cyberpunk decor and secret spicy sauces.",
                targetAudience = "Night owls, foodies, university students, casual diners",
                followers = prefs.getInt("biz_neon_followers", 850),
                engagementRate = prefs.getFloat("biz_neon_eng", 2.8f),
                reputation = prefs.getInt("biz_neon_rep", 62),
                budget = prefs.getInt("biz_neon_budget", 850),
                customerSatisfaction = prefs.getInt("biz_neon_sat", 68),
                campaignCount = prefs.getInt("biz_neon_camps", 0),
                isUnlocked = currentLevel >= 2,
                unlocksAtLevel = 2
            ),
            Business(
                id = "biz_glowlab",
                name = "GlowLab Botanicals",
                industry = "Skincare & Beauty",
                description = "Clean, cruelty-free botanical skincare crafted with certified organic cold-pressed oils.",
                targetAudience = "Women & men (22-40), conscious consumers, skincare minimalists",
                followers = prefs.getInt("biz_glow_followers", 2400),
                engagementRate = prefs.getFloat("biz_glow_eng", 3.2f),
                reputation = prefs.getInt("biz_glow_rep", 70),
                budget = prefs.getInt("biz_glow_budget", 1500),
                customerSatisfaction = prefs.getInt("biz_glow_sat", 74),
                campaignCount = prefs.getInt("biz_glow_camps", 0),
                isUnlocked = currentLevel >= 3,
                unlocksAtLevel = 3
            ),
            Business(
                id = "biz_apex_realty",
                name = "Apex Skyline Realty",
                industry = "Real Estate",
                description = "Luxury urban condominiums and architectural penthouses in prime metropolitan centers.",
                targetAudience = "High-income professionals, real estate investors, luxury seekers (30-55)",
                followers = prefs.getInt("biz_apex_followers", 5200),
                engagementRate = prefs.getFloat("biz_apex_eng", 1.2f),
                reputation = prefs.getInt("biz_apex_rep", 78),
                budget = prefs.getInt("biz_apex_budget", 3200),
                customerSatisfaction = prefs.getInt("biz_apex_sat", 80),
                campaignCount = prefs.getInt("biz_apex_camps", 0),
                isUnlocked = currentLevel >= 4,
                unlocksAtLevel = 4
            ),
            Business(
                id = "biz_pulsewave",
                name = "Pulsewave Records",
                industry = "Music & Entertainment",
                description = "Independent electronic and synthwave music label promoting rising indie artists.",
                targetAudience = "Music producers, festivalgoers, gamers, electronic music fans (16-34)",
                followers = prefs.getInt("biz_pulse_followers", 14200),
                engagementRate = prefs.getFloat("biz_pulse_eng", 4.5f),
                reputation = prefs.getInt("biz_pulse_rep", 82),
                budget = prefs.getInt("biz_pulse_budget", 5500),
                customerSatisfaction = prefs.getInt("biz_pulse_sat", 85),
                campaignCount = prefs.getInt("biz_pulse_camps", 0),
                isUnlocked = currentLevel >= 5,
                unlocksAtLevel = 5
            ),
            Business(
                id = "biz_hypervolt",
                name = "HyperVolt Tech",
                industry = "Electronics & Gadgets",
                description = "High-performance ergonomic gaming peripherals and ultra-fast wireless chargers.",
                targetAudience = "PC gamers, tech enthusiasts, remote workers, esports players",
                followers = prefs.getInt("biz_hyper_followers", 38000),
                engagementRate = prefs.getFloat("biz_hyper_eng", 3.1f),
                reputation = prefs.getInt("biz_hyper_rep", 88),
                budget = prefs.getInt("biz_hyper_budget", 10000),
                customerSatisfaction = prefs.getInt("biz_hyper_sat", 89),
                campaignCount = prefs.getInt("biz_hyper_camps", 0),
                isUnlocked = currentLevel >= 6,
                unlocksAtLevel = 6
            )
        )
    }

    private fun saveBusinesses(list: List<Business>) {
        prefs.edit().apply {
            list.forEach { b ->
                val prefix = when (b.id) {
                    "biz_urban_threads" -> "biz_urban"
                    "biz_neon_bite" -> "biz_neon"
                    "biz_glowlab" -> "biz_glow"
                    "biz_apex_realty" -> "biz_apex"
                    "biz_pulsewave" -> "biz_pulse"
                    else -> "biz_hyper"
                }
                putInt("${prefix}_followers", b.followers)
                putFloat("${prefix}_eng", b.engagementRate)
                putInt("${prefix}_rep", b.reputation)
                putInt("${prefix}_budget", b.budget)
                putInt("${prefix}_sat", b.customerSatisfaction)
                putInt("${prefix}_camps", b.campaignCount)
            }
            apply()
        }
        _businesses.value = list
    }

    fun calculateLevel(xp: Int): CareerLevel {
        for (i in careerLevels.indices.reversed()) {
            if (xp >= careerLevels[i].minXp) {
                return careerLevels[i]
            }
        }
        return careerLevels.first()
    }

    fun getTodayString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun checkDailyStreak() {
        val today = getTodayString()
        val lastDate = _playerState.value.lastDailyDate
        if (lastDate.isEmpty()) return

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val dToday = sdf.parse(today)
            val dLast = sdf.parse(lastDate)
            if (dToday != null && dLast != null) {
                val diffDays = (dToday.time - dLast.time) / (1000 * 60 * 60 * 24)
                if (diffDays > 1) {
                    // Missed more than a day, reset streak to 1
                    savePlayerState(_playerState.value.copy(streakDays = 1))
                }
            }
        } catch (_: Exception) {}
    }

    fun completeMission(
        mission: Mission,
        totalScorePercentage: Int,
        followerGain: Int,
        engagementGain: Float,
        reputationGain: Int
    ): Boolean {
        val current = _playerState.value
        val isFirstTime = !current.completedMissionIds.contains(mission.id)

        // Award full XP and Cash only once to prevent duplicate farming
        val xpGain = if (isFirstTime) mission.xpReward else (mission.xpReward * 0.2).toInt()
        val cashGain = if (isFirstTime) mission.cashReward else (mission.cashReward * 0.2).toInt()

        val newCompleted = current.completedMissionIds + mission.id
        val newXp = current.xp + xpGain
        val newCash = current.cash + cashGain
        val newRep = (current.reputation + reputationGain).coerceIn(0, 100)

        // Check achievements
        val newAchievements = current.unlockedAchievementIds.toMutableSet()
        if (newCompleted.size >= 1) newAchievements.add("ach_first_mission")
        if (totalScorePercentage >= 90) newAchievements.add("ach_caption_master")
        if (mission.type == MissionType.CUSTOMER_CARE && totalScorePercentage == 100) newAchievements.add("ach_crisis_manager")
        if (mission.type == MissionType.CONTENT_CALENDAR) newAchievements.add("ach_calendar_guru")
        if (mission.type == MissionType.AD_CAMPAIGN) newAchievements.add("ach_ad_wizard")
        if (mission.type == MissionType.ANALYTICS_REVIEW) newAchievements.add("ach_data_detective")
        if (newRep >= 80) newAchievements.add("ach_rep_80")
        if (calculateLevel(newXp).levelNumber >= 7) newAchievements.add("ach_social_boss")

        savePlayerState(
            current.copy(
                xp = newXp,
                cash = newCash,
                reputation = newRep,
                completedMissionIds = newCompleted,
                unlockedAchievementIds = newAchievements
            )
        )

        // Also update the associated business
        val updatedBizList = _businesses.value.map { b ->
            if (b.name == mission.clientName || (b.id == "biz_urban_threads" && mission.clientName == "Urban Threads")) {
                b.copy(
                    followers = (b.followers + followerGain).coerceAtLeast(0),
                    engagementRate = ((b.engagementRate + engagementGain) * 10f).toInt() / 10f,
                    reputation = (b.reputation + reputationGain).coerceIn(0, 100),
                    campaignCount = b.campaignCount + 1,
                    customerSatisfaction = (b.customerSatisfaction + (totalScorePercentage / 10 - 5)).coerceIn(10, 100)
                )
            } else {
                b
            }
        }
        saveBusinesses(updatedBizList)
        checkUnlocks(newXp)
        return isFirstTime
    }

    fun completeDailyChallenge(challenge: DailyChallenge, isCorrect: Boolean): Boolean {
        val today = getTodayString()
        val current = _playerState.value
        if (current.lastDailyDate == today) {
            return false // Already claimed today
        }

        if (isCorrect) {
            val newStreak = current.streakDays + 1
            val newAchievements = current.unlockedAchievementIds.toMutableSet()
            if (newStreak >= 3) newAchievements.add("ach_streak_3")

            savePlayerState(
                current.copy(
                    xp = current.xp + challenge.xpReward,
                    cash = current.cash + challenge.cashReward,
                    lastDailyDate = today,
                    streakDays = newStreak,
                    unlockedAchievementIds = newAchievements
                )
            )
            checkUnlocks(current.xp + challenge.xpReward)
            return true
        } else {
            // Still marks daily as attempted
            savePlayerState(current.copy(lastDailyDate = today))
            return false
        }
    }

    private fun checkUnlocks(newXp: Int) {
        val lvl = calculateLevel(newXp).levelNumber
        val updated = _businesses.value.map { b ->
            b.copy(isUnlocked = lvl >= b.unlocksAtLevel)
        }
        val unlockedCount = updated.count { it.isUnlocked }
        if (unlockedCount >= 3) {
            val achs = _playerState.value.unlockedAchievementIds.toMutableSet()
            achs.add("ach_client_unlocked")
            savePlayerState(_playerState.value.copy(unlockedAchievementIds = achs))
        }
        saveBusinesses(updated)
    }

    fun runBusinessAction(
        businessId: String,
        actionType: String, // "ORGANIC_POST", "PAID_BOOST", "COMMUNITY_REPLY"
        cost: Int
    ): Pair<Boolean, String> {
        val current = _playerState.value
        if (current.cash < cost) {
            return Pair(false, "Insufficient funds! Complete missions or daily challenges to earn cash.")
        }

        val bizList = _businesses.value.toMutableList()
        val index = bizList.indexOfFirst { it.id == businessId }
        if (index == -1) return Pair(false, "Business not found.")

        val target = bizList[index]
        val (followerGain, engGain, repGain, satGain, msg) = when (actionType) {
            "ORGANIC_POST" -> {
                val f = (15..45).random()
                val e = 0.2f
                val r = 1
                val s = 2
                Quint(f, e, r, s, "Published high-value organic reel! Gained +$f followers and boosted engagement.")
            }
            "PAID_BOOST" -> {
                val f = (120..280).random()
                val e = 0.6f
                val r = 3
                val s = 1
                Quint(f, e, r, s, "Targeted ad blitz launched! Reached new audience: +$f followers.")
            }
            "COMMUNITY_REPLY" -> {
                val f = (5..15).random()
                val e = 0.5f
                val r = 4
                val s = 6
                Quint(f, e, r, s, "Answered 100% of customer DMs & comments! Customer satisfaction skyrocketed.")
            }
            else -> Quint(0, 0f, 0, 0, "No change.")
        }

        bizList[index] = target.copy(
            followers = target.followers + followerGain,
            engagementRate = ((target.engagementRate + engGain) * 10f).toInt() / 10f,
            reputation = (target.reputation + repGain).coerceIn(0, 100),
            customerSatisfaction = (target.customerSatisfaction + satGain).coerceIn(0, 100),
            campaignCount = target.campaignCount + 1
        )
        saveBusinesses(bizList)

        // Deduct cost and award slight XP
        savePlayerState(
            current.copy(
                cash = current.cash - cost,
                xp = current.xp + 25
            )
        )
        return Pair(true, msg)
    }

    private data class Quint(val f: Int, val e: Float, val r: Int, val s: Int, val m: String)

    fun updatePlayerProfile(name: String, avatarIndex: Int) {
        savePlayerState(_playerState.value.copy(name = name.trim(), avatarIndex = avatarIndex))
    }

    fun toggleSound(enabled: Boolean) {
        savePlayerState(_playerState.value.copy(soundEnabled = enabled))
    }

    fun toggleHaptics(enabled: Boolean) {
        savePlayerState(_playerState.value.copy(hapticEnabled = enabled))
    }

    fun resetGame() {
        prefs.edit().clear().apply()
        _playerState.value = PlayerState()
        _businesses.value = loadBusinesses()
    }

    // --- Offline Intelligent Caption Analyzer ---
    fun analyzeCaptionLocally(caption: String): CaptionAnalysisResult {
        val trimmed = caption.trim()
        if (trimmed.isEmpty()) {
            return CaptionAnalysisResult(
                totalScore = 0,
                hookScore = 0,
                valueScore = 0,
                clarityScore = 0,
                ctaScore = 0,
                strengths = emptyList(),
                improvements = listOf("Please enter or paste your social media caption to get instant feedback."),
                suggestedHook = "Want to double your engagement without posting 5x a day?",
                suggestedCta = "Save this post for your next campaign planning session!"
            )
        }

        val lines = trimmed.split("\n").filter { it.isNotBlank() }
        val firstLine = lines.firstOrNull() ?: ""
        val words = trimmed.split("\\s+".toRegex()).filter { it.isNotBlank() }
        val wordCount = words.size

        var hookScore = 12
        val strengths = mutableListOf<String>()
        val improvements = mutableListOf<String>()

        // Analyze Hook
        val hookTriggers = listOf("how to", "why", "secret", "stop", "mistake", "truth", "this is", "never", "step", "hack", "what if", "meet the", "proven", "guide")
        val hasTrigger = hookTriggers.any { firstLine.lowercase().contains(it) }
        val hasNumber = firstLine.any { it.isDigit() }
        val hasQuestion = firstLine.contains("?")

        if (hasTrigger) {
            hookScore += 6
            strengths.add("Strong psychological trigger words in the opening hook.")
        }
        if (hasNumber) {
            hookScore += 4
            strengths.add("Specific numbers used in the hook (drives curiosity).")
        }
        if (hasQuestion) {
            hookScore += 3
            strengths.add("Engaging question format in opening hook.")
        }
        if (firstLine.length in 25..90) {
            hookScore += 3
        } else if (firstLine.length < 15) {
            improvements.add("Hook is too short. Give readers a compelling reason to tap 'more'.")
        }
        hookScore = hookScore.coerceIn(0, 25)

        // Analyze Value
        var valueScore = 10
        if (wordCount in 40..250) {
            valueScore += 8
            strengths.add("Optimal caption length ($wordCount words) providing real depth and storytelling.")
        } else if (wordCount < 20) {
            improvements.add("Caption is very brief. Expand with context, benefits, or a short relatable story.")
        } else if (wordCount > 350) {
            valueScore += 4
            improvements.add("Caption is quite long; ensure you break up large paragraphs with bullet points or spacing.")
        }
        if (trimmed.contains("•") || trimmed.contains("-") || lines.size >= 4) {
            valueScore += 4
            strengths.add("Excellent spacing and readability structure.")
        }
        valueScore = valueScore.coerceIn(0, 25)

        // Analyze Clarity
        var clarityScore = 15
        if (lines.size > 2) clarityScore += 5
        val emojisCount = trimmed.count { Character.isSurrogate(it) } / 2
        if (emojisCount in 2..8) {
            clarityScore += 5
            strengths.add("Good taste in emojis to visually guide the reader's eyes.")
        } else if (emojisCount > 12) {
            improvements.add("Excessive emojis can look spammy. Keep it to 2-5 intentional visual markers.")
        }
        clarityScore = clarityScore.coerceIn(0, 25)

        // Analyze CTA
        var ctaScore = 8
        val ctaKeywords = listOf("comment", "save", "share", "link", "bio", "dm", "drop", "tag", "tap", "click", "tell us")
        val hasCta = ctaKeywords.any { trimmed.lowercase().contains(it) }
        if (hasCta) {
            ctaScore += 12
            strengths.add("Clear Call-To-Action (CTA) directing user activity.")
        } else {
            improvements.add("Missing clear Call-To-Action (CTA). Explicitly ask readers to comment, save, or check your link.")
        }
        val hashtags = words.filter { it.startsWith("#") }
        if (hashtags.size in 3..6) {
            ctaScore += 5
            strengths.add("Focused hashtag usage (${hashtags.size} tags) within ideal algorithmic range.")
        } else if (hashtags.size > 15) {
            improvements.add("Hashtag stuffing detected (${hashtags.size} tags). Keep to 3-6 targeted keywords.")
        }
        ctaScore = ctaScore.coerceIn(0, 25)

        val total = hookScore + valueScore + clarityScore + ctaScore

        return CaptionAnalysisResult(
            totalScore = total,
            hookScore = hookScore,
            valueScore = valueScore,
            clarityScore = clarityScore,
            ctaScore = ctaScore,
            strengths = strengths,
            improvements = improvements,
            suggestedHook = "The #1 mistake brands make with this (and how to fix it in 2 minutes):",
            suggestedCta = "Which approach fits your business? Drop a 🔥 in the comments or save for later!"
        )
    }
}
