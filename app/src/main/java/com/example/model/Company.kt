package com.example.model

data class Company(
    val name: String = "Apex Dynamics",
    val founder: String = "Alex Mercer",
    val logoIndex: Int = 0,
    val hqCountry: String = "Estados Unidos",
    val hqCity: String = "Silicon Valley",
    val sector: String = "Tecnología, IA y Semiconductores",
    val strategy: String = "Innovación Radical e Integración Vertical",
    val stage: CompanyStage = CompanyStage.STARTUP,
    val valuation: Double = 350000.0,
    val stockPrice: Double = 35.0,
    val totalShares: Long = 1000000L,
    val publicShares: Long = 200000L,
    val playerShares: Long = 800000L,
    val dividendYieldPercentage: Double = 10.0, // % of profits distributed to shareholders
    val reputation: Double = 82.0, // 0-100
    val creditRating: String = "AA+",
    val emergencyCreditLimit: Double = 500000.0,
    val employeesCount: Int = 24,
    val employeeGroups: List<EmployeeGroup> = listOf(
        EmployeeGroup("Investigadores I+D", 6, 8500.0, 1.3),
        EmployeeGroup("Ingenieros de Hardware", 8, 7200.0, 1.2),
        EmployeeGroup("Desarrolladores de Software & OS", 5, 6800.0, 1.25),
        EmployeeGroup("Especialistas en IA & ML", 3, 9200.0, 1.4),
        EmployeeGroup("Operarios de Fábrica", 2, 3400.0, 1.0)
    ),
    val subsidiaries: List<Subsidiary> = listOf(),
    val brands: List<Brand> = listOf(Brand("Apex Prime", "Gama Alta Ultra-Premium", 100.0)),
    val completedFundingRounds: List<FundingRoundType> = listOf(FundingRoundType.SEED)
)

enum class FundingRoundType(
    val displayName: String,
    val targetValuation: Double,
    val capitalRaised: Double,
    val sharesIssuedPercent: Double,
    val description: String
) {
    SEED("Ronda Semilla (Ángeles)", 500000.0, 150000.0, 10.0, "Capital inicial para prototipos y primeras contrataciones."),
    SERIES_A("Serie A (Venture Capital)", 3000000.0, 800000.0, 12.0, "Escalado de ingeniería y desarrollo de ApexOS."),
    SERIES_B("Serie B (Crecimiento)", 15000000.0, 3500000.0, 10.0, "Construcción de GigaFabs y expansión internacional."),
    SERIES_C("Serie C (Expansión Global)", 60000000.0, 12000000.0, 8.0, "Supercomputación de IA y centros de I+D mundiales."),
    IPO("Salida a Bolsa (IPO Wall Street)", 250000000.0, 50000000.0, 15.0, "Cotización pública con acceso al mercado global de capitales.")
}

data class EmployeeGroup(
    val roleName: String,
    val count: Int,
    val averageSalaryUsd: Double,
    val productivityMultiplier: Double
)

data class Subsidiary(
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    val sector: String,
    val localEmployeesCount: Int,
    val monthlyRevenue: Double,
    val monthlyCost: Double,
    val taxBenefitPercentage: Double // e.g. 5.0% tax deduction
)

data class Brand(
    val name: String,
    val marketPosition: String,
    val brandEquity: Double
)

data class CountryEconomy(
    val name: String,
    val flagEmoji: String,
    val currencyCode: String,
    val currencySymbol: String,
    val exchangeRateToUSD: Double, // e.g. EUR = 0.92, JPY = 155.0
    val corporateTaxRate: Double, // e.g. 0.21 (21%)
    val averageMonthlySalaryUsd: Double, // e.g. 4800.0
    val population: Long,
    val gdpBillions: Double,
    val techAdoptionIndex: Double, // 0.0 to 1.0
    val stabilityIndex: Double, // 0.0 to 1.0
    val marketDemandFactor: Double = 1.0
)

data class FinancialStatement(
    val cash: Double = 500000.0,
    val incomePerSec: Double = 0.0,
    val expensePerSec: Double = 0.0,
    val profitPerSec: Double = 0.0,

    // Monthly View
    val monthlyHardwareSales: Double = 0.0,
    val monthlyOsLicensing: Double = 0.0,
    val monthlyAiSubscriptions: Double = 0.0,
    val monthlyPatentRoyalties: Double = 0.0,
    val monthlySubsidiariesIncome: Double = 0.0,
    val monthlyRevenue: Double = 0.0,

    val monthlySalaries: Double = 0.0,
    val monthlyResearchCost: Double = 0.0,
    val monthlyFabMaintenance: Double = 0.0,
    val monthlyEnergyCost: Double = 0.0,
    val monthlyLogisticsCost: Double = 0.0,
    val monthlyCorporateTaxes: Double = 0.0,
    val monthlyLoanInterest: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val monthlyProfit: Double = 0.0,

    // Yearly View
    val yearlyRevenue: Double = 0.0,
    val yearlyExpenses: Double = 0.0,
    val yearlyProfit: Double = 0.0,

    // Debt & Financial Assistance
    val activeLoans: Double = 0.0,
    val creditLineDrawn: Double = 0.0,
    val totalDividendsPaidYearly: Double = 0.0,
    val researchBudgetMonthly: Double = 15000.0,
    val marketingBudgetMonthly: Double = 10000.0
)
