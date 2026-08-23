package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.GameSaveEntity
import com.example.data.InitialData
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

data class GameUiState(
    val gameTime: GameTime = GameTime(),
    val gameMode: GameMode = GameMode.REAL_HISTORY,
    val graphicProfile: GraphicProfile = GraphicProfile.ALTO,
    val company: Company = Company(),
    val financials: FinancialStatement = FinancialStatement(),
    val devices: List<DeviceSpec> = emptyList(),
    val activeDeviceInDesigner: DeviceSpec = DeviceSpec(
        id = "dev_proto_1",
        name = "Apex Horizon Ultra",
        generation = 1,
        modelVariant = "Titanium Black",
        category = DeviceCategory.SMARTPHONE
    ),
    // 3D Canvas visual options
    val designerRotationX: Float = 15f,
    val designerRotationY: Float = -25f,
    val designerZoom: Float = 1.0f,
    val designerExplodedView: Float = 0.0f,
    val designerXRayMode: Boolean = false,
    val designerInspectedComponent: String? = null,

    // OS Lab & Virtual Runtime
    val operatingSystems: List<OperatingSystem> = emptyList(),
    val activeOS: OperatingSystem? = null,
    val virtualOSState: VirtualOSState = VirtualOSState(),

    // Research & Science
    val researchNodes: List<TechNode> = emptyList(),
    val activeResearchId: String? = null,
    val researchPointsTotal: Double = 35000.0,
    val researchFacilities: List<ResearchFacility> = emptyList(),
    val scientists: List<Scientist> = emptyList(),

    // AI Studio Fleet
    val aiModels: List<AIModel> = emptyList(),

    // Manufacturing & Logistics
    val resourcesInventory: Map<ResourceType, Long> = mapOf(
        ResourceType.SILICIO to 25000L,
        ResourceType.TIERRAS_RARAS to 8500L,
        ResourceType.HIERRO to 45000L,
        ResourceType.COBRE to 32000L,
        ResourceType.MATERIALES_AVANZADOS to 3500L
    ),
    val activeEnergySource: EnergySourceType = EnergySourceType.SOLAR_FOTOVOLTAICA,
    val factories: List<Factory> = emptyList(),
    val logisticsFleet: LogisticsFleet = LogisticsFleet(),

    // Competitors & World
    val competitors: List<CompetitorCompany> = emptyList(),
    val countries: List<CountryEconomy> = emptyList(),

    // Milestones & Museum
    val companyMilestones: List<TimelineMilestone> = emptyList(),
    val achievements: List<GameAchievement> = emptyList(),

    // Notification toast / banner
    val currentNotification: String? = null
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val saveDao = db.gameSaveDao()

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var simulationJob: Job? = null

    init {
        initializeInitialState()
        startSimulationLoop()
        observeSaves()
    }

    private fun initializeInitialState() {
        val initialTechs = InitialData.getDefaultTechnologies()
        val initialCompetitors = InitialData.getRealAndAiCompetitors()
        val initialCountries = InitialData.getDefaultCountries()
        val initialAchievements = InitialData.getDefaultAchievements()

        val defaultOS = OperatingSystem(
            id = "os_apex_1",
            name = "ApexOS Nova",
            version = "1.0",
            targetCategory = DeviceCategory.SMARTPHONE,
            kernel = OSKernelType.HYBRID,
            architecture = OSArchitectureType.ARM_64,
            marketSharePercentage = 32.5,
            activeInstalls = 2400000L
        )

        val defaultPhone = DeviceSpec(
            id = "dev_initial_1",
            name = "Apex Phone Pro",
            generation = 1,
            modelVariant = "Flagship Titanium",
            category = DeviceCategory.SMARTPHONE,
            stage = DeviceStage.EN_MERCADO,
            retailPriceUsd = 999.0,
            bomCostUsd = 385.0,
            unitsProduced = 500000L,
            unitsSold = 380000L,
            totalRevenueUsd = 379620000.0,
            totalProfitUsd = 233320000.0
        )

        val defaultLaptop = DeviceSpec(
            id = "dev_initial_2",
            name = "ApexBook Quantum",
            generation = 1,
            modelVariant = "Pro 16-inch",
            category = DeviceCategory.LAPTOP,
            stage = DeviceStage.EN_MERCADO,
            cpuName = "Apex M3 Ultra 16-Core",
            cpuClockGhz = 4.2,
            ramGb = 32,
            storageGb = 1024,
            displaySizeInches = 16.2,
            retailPriceUsd = 2499.0,
            bomCostUsd = 980.0,
            unitsProduced = 150000L,
            unitsSold = 112000L,
            totalRevenueUsd = 279888000.0,
            totalProfitUsd = 170128000.0
        )

        val defaultAIModel1 = AIModel(
            id = "ai_omni_1",
            name = "Apex OmniCore-4B",
            domain = AIDomain.NLP,
            architecture = "Transformer Multimodal MoE",
            parameterCountBillions = 45.0,
            modalities = listOf("Texto", "Visión", "Voz", "Código"),
            accuracyScore = 96.8,
            inferenceSpeedTokensPerSec = 160,
            modelSizeGb = 22.5,
            trainingCostUsd = 54000.0,
            energyTdpWatts = 280.0,
            quantization = "FP8 Optimizada",
            practicalApplication = AIApplicationType.DESARROLLO_SOFTWARE_OS,
            isAssigned = true,
            commercialValuation = 1800000.0
        )

        val defaultAIModel2 = AIModel(
            id = "ai_opt_1",
            name = "Apex FactoryBrain V2",
            domain = AIDomain.ROBOTICA,
            architecture = "Vision-Language-Action (VLA)",
            parameterCountBillions = 12.0,
            modalities = listOf("Visión", "Robótica", "Telemetría"),
            accuracyScore = 98.4,
            inferenceSpeedTokensPerSec = 220,
            modelSizeGb = 8.0,
            trainingCostUsd = 28000.0,
            energyTdpWatts = 150.0,
            quantization = "INT4 Edge Efficient",
            practicalApplication = AIApplicationType.OPTIMIZACION_PRODUCCION,
            isAssigned = true,
            commercialValuation = 950000.0
        )

        val defaultLab = ResearchFacility(
            id = "lab_hq_1",
            name = "Centro de Computación Avanzada & Silicio",
            facilityType = "Laboratorio I+D Cuántico",
            scientistsCount = 18,
            researchPointsPerSec = 55.0,
            monthlyCost = 42000.0,
            level = 2
        )

        val defaultFactory = Factory(
            id = "fab_austin_1",
            name = "GigaFab Silicon One",
            factoryType = "Fab de Semiconductores 3nm GAA",
            countryLocation = "Estados Unidos",
            unitsCapacityPerMonth = 250000L,
            workersCount = 350,
            robotsCount = 180,
            monthlyMaintenanceCost = 85000.0
        )

        val initialMilestones = listOf(
            TimelineMilestone("ms_1", 2024L, "Fundación de Apex Dynamics", HistoricalEra.AI_ERA, EraCategory.HISTORIA_REAL, "Comienza el viaje para construir el imperio tecnológico.", true),
            TimelineMilestone("ms_2", 2025L, "Lanzamiento de ApexOS Nova", HistoricalEra.AI_ERA, EraCategory.HISTORIA_REAL, "Primer sistema operativo con integración nativa de IA neuronal.", true),
            TimelineMilestone("ms_3", 2026L, "Estreno de Apex Phone Pro", HistoricalEra.AI_ERA, EraCategory.HISTORIA_REAL, "Éxito comercial global con más de 300.000 unidades vendidas.", true)
        )

        _uiState.value = _uiState.value.copy(
            researchNodes = initialTechs,
            competitors = initialCompetitors,
            countries = initialCountries,
            achievements = initialAchievements,
            operatingSystems = listOf(defaultOS),
            activeOS = defaultOS,
            devices = listOf(defaultPhone, defaultLaptop),
            aiModels = listOf(defaultAIModel1, defaultAIModel2),
            researchFacilities = listOf(defaultLab),
            factories = listOf(defaultFactory),
            companyMilestones = initialMilestones
        )
    }

    private fun startSimulationLoop() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (isActive) {
                val currentSpeed = _uiState.value.gameTime.speed
                if (currentSpeed == GameSpeed.PAUSE) {
                    delay(500L)
                    continue
                }

                // Calculate economic tick
                val speedMultiplier = currentSpeed.multiplier
                val deltaSec = 1

                _uiState.update { state ->
                    val newTime = state.gameTime.tick(deltaSec)

                    // Calculate research points generation
                    val labPointsRate = state.researchFacilities.sumOf { it.researchPointsPerSec } + 8.0
                    val addedResearch = (labPointsRate * (if (speedMultiplier > 100L) 10.0 else speedMultiplier.toDouble()))

                    // Update active research
                    var updatedTechs = state.researchNodes
                    var updatedCompletedPoints = state.researchPointsTotal + addedResearch
                    var notification: String? = state.currentNotification

                    if (state.activeResearchId != null) {
                        updatedTechs = state.researchNodes.map { node ->
                            if (node.id == state.activeResearchId && !node.isCompleted) {
                                val newPoints = node.researchedPoints + addedResearch
                                if (newPoints >= node.costPoints) {
                                    notification = "¡Investigación Completada: ${node.name}!"
                                    node.copy(researchedPoints = node.costPoints, isCompleted = true)
                                } else {
                                    node.copy(researchedPoints = newPoints)
                                }
                            } else {
                                node
                            }
                        }
                    }

                    // Multiplier bonuses from AI Models assigned to practical applications:
                    val marketAiBonus = state.aiModels
                        .filter { it.isAssigned && it.practicalApplication == AIApplicationType.ANALISIS_MERCADO }
                        .sumOf { (it.accuracyScore / 100.0) * 0.35 }
                        .coerceAtMost(1.5)

                    val prodAiBonus = state.aiModels
                        .filter { it.isAssigned && it.practicalApplication == AIApplicationType.OPTIMIZACION_PRODUCCION }
                        .sumOf { (it.accuracyScore / 100.0) * 0.25 }
                        .coerceAtMost(0.5)

                    val roboticsBonus = state.aiModels
                        .filter { it.isAssigned && it.practicalApplication == AIApplicationType.CONTROL_ROBOTS_FABRICA }
                        .sumOf { (it.accuracyScore / 100.0) * 0.40 }
                        .coerceAtMost(0.8)

                    val patentPassiveMonthlyIncome = state.aiModels
                        .filter { it.isAssigned && it.practicalApplication == AIApplicationType.DIAGNOSTICO_PATENTES_MEDICAS }
                        .sumOf { (it.parameterCountBillions * 1500.0) }

                    val financeAiBonus = state.aiModels
                        .filter { it.isAssigned && it.practicalApplication == AIApplicationType.FINANZAS_EMPRESARIAL }
                        .sumOf { (it.accuracyScore / 100.0) * 0.20 }
                        .coerceAtMost(0.4)

                    // Continuous revenue calculation:
                    // 1. Hardware continuous sales:
                    val productsSalesRevenuePerSec = state.devices.filter { it.stage == DeviceStage.EN_MERCADO }
                        .sumOf { dev ->
                            val popularity = (dev.marketPopularity * (1.0 + marketAiBonus)).coerceIn(10.0, 150.0)
                            (dev.retailPriceUsd * 0.08 * (popularity / 100.0))
                        }

                    // 2. OS licensing:
                    val osLicensingRevenuePerSec = state.operatingSystems.sumOf { it.activeInstalls * 0.00003 }

                    // 3. AI Cloud Enterprise subscriptions:
                    val aiCloudRevenuePerSec = state.aiModels.sumOf { it.commercialValuation * 0.000015 }

                    // 4. Subsidiary income:
                    val subsidiaryIncomePerSec = state.company.subsidiaries.sumOf { it.monthlyRevenue / (30 * 86400.0) }

                    // 5. Scientific patents royalties:
                    val patentRoyaltiesPerSec = patentPassiveMonthlyIncome / (30 * 86400.0)

                    val totalIncomePerSec = productsSalesRevenuePerSec + osLicensingRevenuePerSec + aiCloudRevenuePerSec + subsidiaryIncomePerSec + patentRoyaltiesPerSec

                    // Expenses calculation:
                    val rawPayroll = state.company.employeeGroups.sumOf { it.count * it.averageSalaryUsd } + (state.company.employeesCount * 4200.0)
                    val salariesExpensePerSec = (rawPayroll * (1.0 - financeAiBonus)) / (30 * 86400.0)

                    val factoriesMaintenancePerSec = state.factories.sumOf { it.monthlyMaintenanceCost / (30 * 86400.0) } * (1.0 - (roboticsBonus * 0.3))
                    val labsCostPerSec = state.researchFacilities.sumOf { it.monthlyCost / (30 * 86400.0) }
                    val energyCostPerSec = (state.activeEnergySource.costPerMonth / (30 * 86400.0)) * (1.0 - (roboticsBonus * 0.25))
                    val logisticsCostPerSec = state.logisticsFleet.monthlyLogisticsCost / (30 * 86400.0)
                    val loanInterestPerSec = (state.financials.activeLoans * 0.08) / (365 * 86400.0)

                    // Corporate taxes with subsidiary benefits and finance AI optimizations:
                    val taxRateBase = 0.21 - (state.company.subsidiaries.sumOf { it.taxBenefitPercentage } / 100.0) - (financeAiBonus * 0.08)
                    val effectiveTaxRate = taxRateBase.coerceIn(0.08, 0.35)
                    val corporateTaxesPerSec = (totalIncomePerSec * effectiveTaxRate).coerceAtLeast(0.0)

                    val totalExpensePerSec = salariesExpensePerSec + factoriesMaintenancePerSec + labsCostPerSec + energyCostPerSec + logisticsCostPerSec + loanInterestPerSec + corporateTaxesPerSec
                    val netProfitPerSec = totalIncomePerSec - totalExpensePerSec

                    // RULE: Cash balance never goes below zero
                    val deltaCash = (netProfitPerSec * (if (speedMultiplier > 1000L) 50.0 else speedMultiplier.toDouble()))
                    val newCash = (state.financials.cash + deltaCash).coerceAtLeast(0.0)

                    // Company valuation calculation
                    val calculatedValuation = (state.company.valuation + (netProfitPerSec * 86400 * 30 * 12 * 0.18) + (state.devices.size * 3500000.0) + (state.aiModels.sumOf { it.commercialValuation } * 0.5)).coerceAtLeast(250000.0)
                    val newStockPrice = (calculatedValuation / state.company.totalShares.coerceAtLeast(1L))

                    // Update stage based on valuation
                    val newStage = when {
                        calculatedValuation >= CompanyStage.FUTURISTIC_CORP.requiredValuation -> CompanyStage.FUTURISTIC_CORP
                        calculatedValuation >= CompanyStage.WORLD_CORP.requiredValuation -> CompanyStage.WORLD_CORP
                        calculatedValuation >= CompanyStage.CONGLOMERADO.requiredValuation -> CompanyStage.CONGLOMERADO
                        calculatedValuation >= CompanyStage.TECH_GIANT.requiredValuation -> CompanyStage.TECH_GIANT
                        calculatedValuation >= CompanyStage.MULTINACIONAL.requiredValuation -> CompanyStage.MULTINACIONAL
                        calculatedValuation >= CompanyStage.NACIONAL.requiredValuation -> CompanyStage.NACIONAL
                        else -> CompanyStage.STARTUP
                    }

                    val updatedFinancials = state.financials.copy(
                        cash = newCash,
                        incomePerSec = totalIncomePerSec,
                        expensePerSec = totalExpensePerSec,
                        profitPerSec = netProfitPerSec,

                        monthlyHardwareSales = productsSalesRevenuePerSec * 30 * 86400 * 0.05,
                        monthlyOsLicensing = osLicensingRevenuePerSec * 30 * 86400 * 0.05,
                        monthlyAiSubscriptions = aiCloudRevenuePerSec * 30 * 86400 * 0.05,
                        monthlyPatentRoyalties = patentRoyaltiesPerSec * 30 * 86400 * 0.05,
                        monthlySubsidiariesIncome = subsidiaryIncomePerSec * 30 * 86400 * 0.05,
                        monthlyRevenue = totalIncomePerSec * 30 * 86400 * 0.05,

                        monthlySalaries = salariesExpensePerSec * 30 * 86400 * 0.05,
                        monthlyResearchCost = labsCostPerSec * 30 * 86400 * 0.05,
                        monthlyFabMaintenance = factoriesMaintenancePerSec * 30 * 86400 * 0.05,
                        monthlyEnergyCost = energyCostPerSec * 30 * 86400 * 0.05,
                        monthlyLogisticsCost = logisticsCostPerSec * 30 * 86400 * 0.05,
                        monthlyCorporateTaxes = corporateTaxesPerSec * 30 * 86400 * 0.05,
                        monthlyLoanInterest = loanInterestPerSec * 30 * 86400 * 0.05,
                        monthlyExpenses = totalExpensePerSec * 30 * 86400 * 0.05,
                        monthlyProfit = netProfitPerSec * 30 * 86400 * 0.05,

                        yearlyRevenue = totalIncomePerSec * 365 * 86400 * 0.05,
                        yearlyExpenses = totalExpensePerSec * 365 * 86400 * 0.05,
                        yearlyProfit = netProfitPerSec * 365 * 86400 * 0.05
                    )

                    state.copy(
                        gameTime = newTime,
                        financials = updatedFinancials,
                        company = state.company.copy(
                            valuation = calculatedValuation,
                            stockPrice = newStockPrice,
                            stage = newStage
                        ),
                        researchNodes = updatedTechs,
                        researchPointsTotal = updatedCompletedPoints,
                        currentNotification = notification
                    )
                }

                delay(1000L)
            }
        }
    }

    private fun observeSaves() {
        // Keeps save state available for UI
    }

    // Time & Speed Controls
    fun setGameSpeed(speed: GameSpeed) {
        _uiState.update { it.copy(gameTime = it.gameTime.copy(speed = speed)) }
    }

    fun jumpToEra(era: HistoricalEra) {
        _uiState.update { state ->
            val targetYear = era.startYear
            state.copy(
                gameTime = state.gameTime.copy(
                    year = targetYear,
                    currentEra = era
                ),
                currentNotification = "Salto temporal a la era: ${era.displayName}"
            )
        }
    }

    fun clearNotification() {
        _uiState.update { it.copy(currentNotification = null) }
    }

    // 3D Canvas Visual Customizer Controls
    fun updateDesignerRotation(deltaX: Float, deltaY: Float) {
        _uiState.update {
            it.copy(
                designerRotationX = (it.designerRotationX + deltaX).coerceIn(-60f, 60f),
                designerRotationY = (it.designerRotationY + deltaY) % 360f
            )
        }
    }

    fun updateDesignerZoom(deltaZoom: Float) {
        _uiState.update {
            it.copy(designerZoom = (it.designerZoom * deltaZoom).coerceIn(0.6f, 2.5f))
        }
    }

    fun setExplodedViewProgress(progress: Float) {
        _uiState.update { it.copy(designerExplodedView = progress.coerceIn(0f, 1f)) }
    }

    fun toggleXRayMode() {
        _uiState.update { it.copy(designerXRayMode = !it.designerXRayMode) }
    }

    fun inspectComponent(componentName: String?) {
        _uiState.update { it.copy(designerInspectedComponent = componentName) }
    }

    fun updateActiveDeviceInDesigner(updater: (DeviceSpec) -> DeviceSpec) {
        _uiState.update {
            it.copy(activeDeviceInDesigner = updater(it.activeDeviceInDesigner))
        }
    }

    // Device Creation Lifecycle: Prototype -> Test -> Production -> Launch
    fun saveDevicePrototype() {
        val current = _uiState.value.activeDeviceInDesigner
        val newDevice = current.copy(
            id = "dev_" + UUID.randomUUID().toString().take(8),
            stage = DeviceStage.PRUEBAS_BENCHMARK,
            creationYear = _uiState.value.gameTime.year
        )
        _uiState.update { state ->
            state.copy(
                devices = listOf(newDevice) + state.devices,
                activeDeviceInDesigner = newDevice,
                currentNotification = "¡Prototipo '${newDevice.name}' creado y listo para pruebas de benchmark!"
            )
        }
    }

    fun benchmarkDevice(deviceId: String) {
        _uiState.update { state ->
            val updatedDevices = state.devices.map { dev ->
                if (dev.id == deviceId) {
                    dev.copy(stage = DeviceStage.FABRICACION)
                } else dev
            }
            state.copy(
                devices = updatedDevices,
                currentNotification = "Benchmark finalizado. Puntuación obtenida: ${_uiState.value.activeDeviceInDesigner.formattedScore()}"
            )
        }
    }

    fun launchDeviceToMarket(deviceId: String, retailPrice: Double, initialUnits: Long) {
        val bomCost = _uiState.value.devices.find { it.id == deviceId }?.bomCostUsd ?: 350.0
        val totalProductionCost = bomCost * initialUnits

        if (_uiState.value.financials.cash < totalProductionCost) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para fabricar $initialUnits unidades. Requieres $${String.format(Locale.US, "%,.0f", totalProductionCost)}") }
            return
        }

        _uiState.update { state ->
            val updatedDevices = state.devices.map { dev ->
                if (dev.id == deviceId) {
                    dev.copy(
                        stage = DeviceStage.EN_MERCADO,
                        retailPriceUsd = retailPrice,
                        unitsProduced = dev.unitsProduced + initialUnits,
                        marketPopularity = (80.0 + (dev.calculateScore() * 2.0)).coerceIn(50.0, 100.0)
                    )
                } else dev
            }
            val newCash = state.financials.cash - totalProductionCost
            state.copy(
                devices = updatedDevices,
                financials = state.financials.copy(cash = newCash),
                currentNotification = "¡Lanzamiento mundial completado! El dispositivo ya genera ingresos en el mercado."
            )
        }
    }

    // AI Studio: Advanced Custom Model Creator & Training
    fun createAndTrainAdvancedAIModel(
        name: String,
        domain: AIDomain,
        architecture: String,
        parameterCountBillions: Double,
        modalities: List<String>,
        accuracyTarget: Double,
        inferenceSpeed: Int,
        modelSizeGb: Double,
        trainingCostUsd: Double,
        energyTdpWatts: Double,
        quantization: String,
        applicationType: AIApplicationType
    ) {
        if (_uiState.value.financials.cash < trainingCostUsd) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para computación de entrenamiento ($${String.format(Locale.US, "%,.0f", trainingCostUsd)}).") }
            return
        }

        val newModel = AIModel(
            id = "ai_" + UUID.randomUUID().toString().take(6),
            name = name,
            domain = domain,
            architecture = architecture,
            parameterCountBillions = parameterCountBillions,
            modalities = modalities,
            accuracyScore = accuracyTarget.coerceIn(85.0, 99.9),
            inferenceSpeedTokensPerSec = inferenceSpeed,
            modelSizeGb = modelSizeGb,
            trainingCostUsd = trainingCostUsd,
            energyTdpWatts = energyTdpWatts,
            quantization = quantization,
            practicalApplication = applicationType,
            isAssigned = true,
            isTrained = true,
            commercialValuation = parameterCountBillions * 45000.0
        )

        _uiState.update { state ->
            state.copy(
                aiModels = state.aiModels + newModel,
                financials = state.financials.copy(cash = state.financials.cash - trainingCostUsd),
                currentNotification = "¡Modelo de IA '$name' ($architecture) entrenado y asignado a ${applicationType.displayName}!"
            )
        }
    }

    fun assignAIModelApplication(modelId: String, applicationType: AIApplicationType) {
        _uiState.update { state ->
            val updated = state.aiModels.map { model ->
                if (model.id == modelId) {
                    model.copy(practicalApplication = applicationType, isAssigned = applicationType != AIApplicationType.NINGUNA)
                } else model
            }
            state.copy(
                aiModels = updated,
                currentNotification = "Aplicación del modelo actualizada a: ${applicationType.displayName}"
            )
        }
    }

    // Corporate, International Subsidiaries & Employee Management
    fun updateCompanyName(newName: String, newFounder: String, newHQ: String) {
        _uiState.update { state ->
            state.copy(
                company = state.company.copy(
                    name = newName,
                    founder = newFounder,
                    hqCountry = newHQ
                ),
                currentNotification = "Datos corporativos actualizados."
            )
        }
    }

    fun openSubsidiary(name: String, city: String, country: String, sector: String, initialCost: Double) {
        if (_uiState.value.financials.cash < initialCost) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para abrir filial internacional ($${String.format(Locale.US, "%,.0f", initialCost)}).") }
            return
        }

        val newSubsidiary = Subsidiary(
            id = "sub_" + UUID.randomUUID().toString().take(6),
            name = name,
            city = city,
            country = country,
            sector = sector,
            localEmployeesCount = 15,
            monthlyRevenue = 85000.0,
            monthlyCost = 45000.0,
            taxBenefitPercentage = 3.5
        )

        _uiState.update { state ->
            state.copy(
                company = state.company.copy(
                    subsidiaries = state.company.subsidiaries + newSubsidiary,
                    employeesCount = state.company.employeesCount + 15
                ),
                financials = state.financials.copy(cash = state.financials.cash - initialCost),
                currentNotification = "¡Filial '$name' inaugurada con éxito en $city ($country)!"
            )
        }
    }

    fun hireEmployeesForRole(roleName: String, countToAdd: Int) {
        val costPerRole = when (roleName) {
            "Investigadores I+D" -> 8500.0
            "Ingenieros de Hardware" -> 7200.0
            "Desarrolladores de Software & OS" -> 6800.0
            "Especialistas en IA & ML" -> 9200.0
            else -> 3400.0
        }
        val hiringCost = countToAdd * costPerRole * 1.5

        if (_uiState.value.financials.cash < hiringCost) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para proceso de selección y contratación ($${String.format(Locale.US, "%,.0f", hiringCost)}).") }
            return
        }

        _uiState.update { state ->
            val updatedGroups = state.company.employeeGroups.map { group ->
                if (group.roleName == roleName) {
                    group.copy(count = group.count + countToAdd)
                } else group
            }
            state.copy(
                company = state.company.copy(
                    employeeGroups = updatedGroups,
                    employeesCount = state.company.employeesCount + countToAdd
                ),
                financials = state.financials.copy(cash = state.financials.cash - hiringCost),
                currentNotification = "Contratados $countToAdd nuevos talentos en '$roleName'."
            )
        }
    }

    // Investor Funding Rounds & Capital Market
    fun raiseFundingRound(roundType: FundingRoundType) {
        val state = _uiState.value
        if (state.company.valuation < roundType.targetValuation * 0.7) {
            _uiState.update { it.copy(currentNotification = "La valoración actual de la empresa ($${String.format(Locale.US, "%,.0f", state.company.valuation)}) no cumple los requisitos para ${roundType.displayName}.") }
            return
        }

        val newShares = (state.company.totalShares * (roundType.sharesIssuedPercent / 100.0)).toLong()

        _uiState.update { s ->
            s.copy(
                company = s.company.copy(
                    totalShares = s.company.totalShares + newShares,
                    publicShares = s.company.publicShares + newShares,
                    completedFundingRounds = s.company.completedFundingRounds + roundType
                ),
                financials = s.financials.copy(cash = s.financials.cash + roundType.capitalRaised),
                currentNotification = "¡Ronda ${roundType.displayName} cerrada con éxito! Captados $${String.format(Locale.US, "%,.0f", roundType.capitalRaised)}"
            )
        }
    }

    fun setDividendYield(percentage: Double) {
        _uiState.update { state ->
            state.copy(
                company = state.company.copy(dividendYieldPercentage = percentage.coerceIn(0.0, 50.0)),
                currentNotification = "Política de dividendos fijada en ${percentage.toInt()}% de los beneficios netos."
            )
        }
    }

    // Emergency Financial Mechanisms
    fun drawEmergencyCredit(amount: Double) {
        val available = _uiState.value.company.emergencyCreditLimit - _uiState.value.financials.creditLineDrawn
        val actualDraw = amount.coerceAtMost(available)

        _uiState.update { state ->
            state.copy(
                financials = state.financials.copy(
                    cash = state.financials.cash + actualDraw,
                    creditLineDrawn = state.financials.creditLineDrawn + actualDraw,
                    activeLoans = state.financials.activeLoans + (actualDraw * 1.05)
                ),
                currentNotification = "Línea de crédito de emergencia activada por $${String.format(Locale.US, "%,.0f", actualDraw)}."
            )
        }
    }

    fun requestGovernmentSubvention() {
        val subventionAmount = 150000.0
        _uiState.update { state ->
            state.copy(
                financials = state.financials.copy(cash = state.financials.cash + subventionAmount),
                currentNotification = "¡Subvención gubernamental de I+D concedida! Inyección de $${String.format(Locale.US, "%,.0f", subventionAmount)}."
            )
        }
    }

    fun takeLoan(amount: Double) {
        _uiState.update { state ->
            state.copy(
                financials = state.financials.copy(
                    cash = state.financials.cash + amount,
                    activeLoans = state.financials.activeLoans + (amount * 1.08)
                ),
                currentNotification = "Préstamo bancario de $${String.format(Locale.US, "%,.0f", amount)} concedido."
            )
        }
    }

    fun repayLoan(amount: Double) {
        if (_uiState.value.financials.cash < amount) {
            _uiState.update { it.copy(currentNotification = "Efectivo insuficiente para amortizar préstamo.") }
            return
        }
        _uiState.update { state ->
            val actualRepay = amount.coerceAtMost(state.financials.activeLoans)
            state.copy(
                financials = state.financials.copy(
                    cash = state.financials.cash - actualRepay,
                    activeLoans = state.financials.activeLoans - actualRepay
                ),
                currentNotification = "Amortización de préstamo de $${String.format(Locale.US, "%,.0f", actualRepay)} realizada."
            )
        }
    }

    fun issueShares(sharesCount: Long) {
        val capitalRaised = sharesCount * _uiState.value.company.stockPrice
        _uiState.update { state ->
            state.copy(
                company = state.company.copy(
                    totalShares = state.company.totalShares + sharesCount,
                    publicShares = state.company.publicShares + sharesCount
                ),
                financials = state.financials.copy(cash = state.financials.cash + capitalRaised),
                currentNotification = "Ampliación de capital: $${String.format(Locale.US, "%,.0f", capitalRaised)} captados."
            )
        }
    }

    // Research Actions
    fun setActiveResearch(nodeId: String) {
        _uiState.update { state ->
            state.copy(
                activeResearchId = nodeId,
                currentNotification = "Investigación iniciada: ${state.researchNodes.find { it.id == nodeId }?.name}"
            )
        }
    }

    fun hireScientist(name: String, specialization: String) {
        val newScientist = Scientist(
            id = "sci_" + UUID.randomUUID().toString().take(6),
            name = name,
            specialization = specialization,
            experienceLevel = (3..8).random(),
            productivityMultiplier = 1.25,
            salaryMonthly = 7500.0
        )
        _uiState.update { state ->
            state.copy(
                scientists = state.scientists + newScientist,
                company = state.company.copy(employeesCount = state.company.employeesCount + 1),
                currentNotification = "Científico $name ($specialization) contratado."
            )
        }
    }

    fun buildResearchFacility(name: String, type: String, cost: Double) {
        if (_uiState.value.financials.cash < cost) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para construir el centro de investigación.") }
            return
        }
        val facility = ResearchFacility(
            id = "fac_" + UUID.randomUUID().toString().take(6),
            name = name,
            facilityType = type,
            scientistsCount = 12,
            researchPointsPerSec = 35.0,
            monthlyCost = 28000.0
        )
        _uiState.update { state ->
            state.copy(
                researchFacilities = state.researchFacilities + facility,
                financials = state.financials.copy(cash = state.financials.cash - cost),
                currentNotification = "¡Nuevo $type construido!"
            )
        }
    }

    // OS Lab & Virtual Sandbox Actions
    fun createOperatingSystem(
        name: String,
        version: String,
        target: DeviceCategory,
        kernel: OSKernelType,
        arch: OSArchitectureType
    ) {
        val newOS = OperatingSystem(
            id = "os_" + UUID.randomUUID().toString().take(6),
            name = name,
            version = version,
            targetCategory = target,
            kernel = kernel,
            architecture = arch
        )
        _uiState.update { state ->
            state.copy(
                operatingSystems = state.operatingSystems + newOS,
                activeOS = newOS,
                currentNotification = "Sistema Operativo '$name $version' creado exitosamente."
            )
        }
    }

    fun selectActiveOS(os: OperatingSystem) {
        _uiState.update { it.copy(activeOS = os) }
    }

    fun bootVirtualOS() {
        _uiState.update { state ->
            state.copy(
                virtualOSState = state.virtualOSState.copy(
                    isPoweredOn = true,
                    isBooted = true,
                    terminalLines = state.virtualOSState.terminalLines + listOf(
                        "-> System booted successfully.",
                        "-> OS: ${state.activeOS?.name ?: "ApexOS Nova"}",
                        "-> Ready."
                    )
                )
            )
        }
    }

    fun shutdownVirtualOS() {
        _uiState.update {
            it.copy(
                virtualOSState = it.virtualOSState.copy(
                    isPoweredOn = false,
                    isBooted = false,
                    activeApp = null
                )
            )
        }
    }

    fun restartVirtualOS() {
        viewModelScope.launch {
            shutdownVirtualOS()
            delay(600L)
            bootVirtualOS()
        }
    }

    fun launchVirtualApp(appType: VirtualAppType?) {
        _uiState.update { state ->
            state.copy(
                virtualOSState = state.virtualOSState.copy(activeApp = appType)
            )
        }
    }

    fun executeTerminalCommand(command: String) {
        val trimmed = command.trim()
        if (trimmed.isEmpty()) return

        val responseLines = when (trimmed.lowercase()) {
            "help" -> listOf(
                "Apex Terminal v2.4 Commands:",
                "  help       - Mostrar comandos disponibles",
                "  sysinfo    - Información del kernel y hardware",
                "  neofetch   - Diagnóstico visual del sistema",
                "  benchmark  - Ejecutar prueba de rendimiento",
                "  train_ai   - Iniciar optimización neuronal",
                "  fs         - Listar sistema de archivos",
                "  ping       - Probar latencia de red cuántica",
                "  logs       - Consultar registro de eventos del kernel",
                "  clear      - Limpiar pantalla"
            )
            "sysinfo" -> listOf(
                "=== APEX SYSTEM INFORMATION ===",
                "OS: ${_uiState.value.activeOS?.name ?: "ApexOS Nova"} ${_uiState.value.activeOS?.version ?: "1.0"}",
                "Kernel: ${_uiState.value.activeOS?.kernel?.displayName ?: "Hybrid 64-bit"}",
                "Arch: ${_uiState.value.activeOS?.architecture?.displayName ?: "ARM64-v9"}",
                "Security: Post-Quantum TLS 1.4 active",
                "Battery: ${_uiState.value.virtualOSState.batteryLevelPercent}% (Temp: ${_uiState.value.virtualOSState.cpuTemperatureCelsius}°C)"
            )
            "neofetch" -> listOf(
                "   .---.     OS: ApexOS CyberEdition 2026",
                "  /     \\    Host: Apex Horizon Ultra (Titanium)",
                " | () () |   Kernel: Apex-Quantum-6.8.9-rt",
                "  \\  _  /    Uptime: 48 hours, 12 mins",
                "   '---'     CPU: Apex Silicon S1 @ 3.4GHz (8 cores)",
                "             GPU: Apex Immersion 4.8 TFLOPS",
                "             NPU: Neural Engine 45 TOPS",
                "             Memory: 6420MB / 16384MB"
            )
            "benchmark", "bench" -> {
                runVirtualBenchmark()
                listOf("Iniciando Apex Benchmark Suite...", "Estresando núcleos de CPU y NPU...")
            }
            "train_ai" -> listOf(
                "Entrenando pesos locales de Apex Copilot...",
                "Loss: 0.0412 | Accuracy: 98.4% | FP8 Quantized OK"
            )
            "fs" -> _uiState.value.virtualOSState.fileSystem.map { (if (it.isDirectory) "[DIR]  " else "[FILE] ") + it.path + " (${it.sizeKb} KB)" }
            "ping" -> listOf("PING quantum-gateway.apexcorp.net: 64 bytes, time=0.42 ms, jitter=0.01 ms")
            "logs" -> listOf(
                "[LOG 12:04:12] Kernel loaded into RAM successfully.",
                "[LOG 12:04:15] Neural NPU coprocessor initialized (45 TOPS).",
                "[LOG 12:04:22] Display driver initialized: 120Hz AMOLED.",
                "[LOG 12:05:01] Ecosystem cloud sync synchronized with cloud blade server."
            )
            "clear" -> emptyList()
            else -> listOf("Comando no reconocido: '$trimmed'. Escribe 'help' para ver la lista.")
        }

        _uiState.update { state ->
            val updatedLines = if (trimmed.lowercase() == "clear") {
                listOf("ApexOS Shell listo. Escribe 'help'.")
            } else {
                state.virtualOSState.terminalLines + listOf("$ $trimmed") + responseLines
            }
            state.copy(
                virtualOSState = state.virtualOSState.copy(terminalLines = updatedLines.takeLast(100))
            )
        }
    }

    fun runVirtualBenchmark() {
        _uiState.update { state ->
            state.copy(
                virtualOSState = state.virtualOSState.copy(isBenchmarking = true, benchmarkProgress = 0f)
            )
        }
        viewModelScope.launch {
            for (i in 1..10) {
                delay(200L)
                _uiState.update { state ->
                    state.copy(
                        virtualOSState = state.virtualOSState.copy(
                            benchmarkProgress = i / 10f,
                            cpuTemperatureCelsius = 37.0 + (i * 1.5)
                        )
                    )
                }
            }
            _uiState.update { state ->
                val deviceScore = state.activeDeviceInDesigner.calculateScore()
                state.copy(
                    virtualOSState = state.virtualOSState.copy(
                        isBenchmarking = false,
                        benchmarkProgress = 1f,
                        benchmarkScoreSingleCore = (3200 + (deviceScore * 80)).toInt(),
                        benchmarkScoreMultiCore = (16000 + (deviceScore * 600)).toInt(),
                        benchmarkScoreGpuCompute = (22000 + (deviceScore * 800)).toInt(),
                        benchmarkScoreAiTops = (30.0 + (deviceScore * 2.5)),
                        cpuTemperatureCelsius = 42.1
                    ),
                    currentNotification = "Benchmark completado. Puntuación: ${state.activeDeviceInDesigner.formattedScore()}"
                )
            }
        }
    }

    // Manufacturing & Energy
    fun buildFactory(name: String, type: String, cost: Double) {
        if (_uiState.value.financials.cash < cost) {
            _uiState.update { it.copy(currentNotification = "Fondos insuficientes para construir la fábrica.") }
            return
        }
        val factory = Factory(
            id = "fab_" + UUID.randomUUID().toString().take(6),
            name = name,
            factoryType = type,
            countryLocation = _uiState.value.company.hqCountry,
            unitsCapacityPerMonth = 150000L,
            workersCount = 200,
            robotsCount = 90,
            monthlyMaintenanceCost = 45000.0
        )
        _uiState.update { state ->
            state.copy(
                factories = state.factories + factory,
                financials = state.financials.copy(cash = state.financials.cash - cost),
                currentNotification = "¡Nueva fábrica '$name' operativa!"
            )
        }
    }

    fun changeEnergySource(source: EnergySourceType) {
        _uiState.update { state ->
            state.copy(
                activeEnergySource = source,
                currentNotification = "Matriz energética actualizada a: ${source.displayName}"
            )
        }
    }

    // Settings & Graphics Profile
    fun setGraphicProfile(profile: GraphicProfile) {
        _uiState.update { it.copy(graphicProfile = profile) }
    }

    fun setGameMode(mode: GameMode) {
        _uiState.update { it.copy(gameMode = mode) }
    }

    // Save & Load Management
    fun saveGameToSlot(slotId: Int, slotName: String) {
        viewModelScope.launch {
            val state = _uiState.value
            val entity = GameSaveEntity(
                slotId = slotId,
                slotName = slotName,
                companyName = state.company.name,
                year = state.gameTime.year,
                eraDisplayName = state.gameTime.currentEra.displayName,
                money = state.financials.cash,
                valuation = state.company.valuation,
                devicesCount = state.devices.size,
                osCount = state.operatingSystems.size,
                researchedTechCount = state.researchNodes.count { it.isCompleted },
                jsonPayload = ""
            )
            saveDao.insertOrUpdateSave(entity)
            _uiState.update { it.copy(currentNotification = "Partida guardada con éxito en $slotName.") }
        }
    }

    fun loadGameFromSlot(slotId: Int) {
        viewModelScope.launch {
            val save = saveDao.getSaveBySlotId(slotId)
            if (save != null) {
                _uiState.update { state ->
                    state.copy(
                        company = state.company.copy(name = save.companyName, valuation = save.valuation),
                        financials = state.financials.copy(cash = save.money),
                        gameTime = state.gameTime.copy(year = save.year, currentEra = HistoricalEra.fromYear(save.year)),
                        currentNotification = "Partida cargada desde ${save.slotName}."
                    )
                }
            } else {
                _uiState.update { it.copy(currentNotification = "La ranura de guardado seleccionada está vacía.") }
            }
        }
    }
}
