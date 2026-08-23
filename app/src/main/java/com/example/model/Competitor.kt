package com.example.model

enum class CompanyOriginType {
    EMPRESA_REAL_HISTORICA,
    COMPETIDOR_IA_PROCEDURAL,
    JUGADOR
}

data class CompetitorCompany(
    val id: String,
    val name: String,
    val founder: String,
    val foundingYear: Long,
    val originType: CompanyOriginType,
    val isRealHistorical: Boolean,
    val country: String,
    val valuationMillions: Double,
    val annualRevenueMillions: Double,
    val annualProfitMillions: Double,
    val marketSharePercent: Double,
    val innovationScore: Double, // 0-100
    val patentsCount: Int,
    val employeesCount: Int,
    val flagshipProduct: String,
    val aiCapabilityRating: Double,
    val manufacturingRating: Double,
    val reputation: Double,
    val historicalBio: String
)

data class TimelineMilestone(
    val id: String,
    val year: Long,
    val title: String,
    val era: HistoricalEra,
    val category: EraCategory,
    val description: String,
    val isPlayerAchievement: Boolean = false
)

data class GameAchievement(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val unlockDate: String? = null,
    val rewardMoney: Double = 10000.0,
    val iconName: String = "emoji_events"
)
