package com.example.model

import java.util.Locale

enum class DeviceStage(val displayName: String) {
    PROTOTIPO("Prototipo de Ingeniería"),
    PRUEBAS_BENCHMARK("Pruebas & Benchmark"),
    FABRICACION("En Fabricación"),
    EN_MERCADO("Lanzado al Mercado"),
    DESCATALOGADO("Descatalogado"),
    MUSEO("En Exhibición Histórica")
}

enum class ChassisMaterial(
    val displayName: String,
    val premiumFactor: Double,
    val weightFactor: Double,
    val durabilityScore: Double,
    val hexColor: Long
) {
    PLASTICO_POLICARBONATO("Policarbonato Reforzado", 1.0, 0.9, 7.0, 0xFF334155),
    ALUMINIO_AEROESPACIAL("Aluminio Aeroespacial Serie 7000", 1.8, 1.1, 8.8, 0xFF94A3B8),
    TITANIO_GRADO_5("Titanio Grado 5 Forjado", 3.2, 1.2, 9.8, 0xFF64748B),
    CERAMICA_CIRCONIO("Cerámica de Circonio Pulida", 2.8, 1.4, 9.5, 0xFFF1F5F9),
    GRAFENO_METAMATERIAL("Metamaterial de Grafeno Cuántico", 5.0, 0.7, 10.0, 0xFF0F172A),
    ALEACION_ESTELAR("Aleación Estelar Tridimensional", 8.0, 0.5, 10.0, 0xFF38BDF8)
}

enum class DisplayTech(
    val displayName: String,
    val powerEfficiency: Double,
    val clarityScore: Double,
    val costBase: Double
) {
    LCD_IPS("IPS LCD Retina", 1.0, 7.5, 35.0),
    OLED_120HZ("Dynamic AMOLED 2X (1-120Hz LTPO)", 1.4, 9.2, 75.0),
    MICRO_LED("MicroLED Pro Ultra-Bright (2000 nits)", 1.8, 9.8, 140.0),
    HOLOGRAPHIC_3D("Pantalla Holográfica Volumétrica 3D", 2.5, 9.9, 260.0),
    NEURAL_DIRECT("Proyección Neural Directa de Retina", 3.0, 10.0, 480.0)
}

enum class CoolingType(
    val displayName: String,
    val thermalEfficiency: Double,
    val cost: Double
) {
    PASIVA_GRAFITO("Disipación Pasiva con Lámina de Grafito", 6.0, 4.0),
    HEATPIPE_COBRE("Cámara de Vapor de Cobre 3D (Vapor Chamber)", 8.5, 14.0),
    LIQUIDO_MICROCANALES("Refrigeración Líquida con Microbombas", 9.5, 38.0),
    CRIOGENICA_ESTADO_SOLIDO("Refrigeración Criogénica de Estado Sólido", 10.0, 95.0)
}

data class SemiconductorChip(
    val id: String,
    val name: String,
    val chipType: String,
    val architecture: String,
    val lithographyNodeNm: Double,
    val coresCount: Int,
    val clockFrequencyGhz: Double,
    val tdpWatts: Double,
    val aiNpuTops: Double,
    val gpuTflops: Double,
    val waferCostUsd: Double
)

data class DeviceSpec(
    val id: String,
    val name: String,
    val generation: Int = 1,
    val modelVariant: String = "Ultra",
    val category: DeviceCategory = DeviceCategory.SMARTPHONE,
    val stage: DeviceStage = DeviceStage.PROTOTIPO,
    val creationYear: Long = 2026L,

    // Hardware - CPU & Computing
    val cpuName: String = "Apex Silicon S1",
    val cpuCores: Int = 8,
    val cpuClockGhz: Double = 3.4,
    val processNodeNm: Double = 3.0,

    // Hardware - GPU Graphics
    val gpuName: String = "Apex Immersion GPU",
    val gpuTflops: Double = 4.8,
    val hasHardwareRayTracing: Boolean = true,

    // Hardware - NPU & AI Engine
    val npuName: String = "Apex Neural Engine N2",
    val npuTops: Double = 45.0,

    // Hardware - Memory & Storage
    val ramGb: Int = 16,
    val ramType: String = "LPDDR5X-8533",

    val storageGb: Int = 512,
    val storageType: String = "UFS 4.0 NVMe",

    // Hardware - Display
    val displayTech: DisplayTech = DisplayTech.OLED_120HZ,
    val displaySizeInches: Double = 6.7,
    val displayResolution: String = "3200 x 1440 QHD+",
    val displayRefreshHz: Int = 120,
    val displayPeakNits: Int = 2600,

    // Hardware - Optics & Camera System
    val cameraMainMp: Int = 200,
    val cameraTelephotoMp: Int = 50,
    val cameraUltraWideMp: Int = 50,
    val cameraZoomOpticalX: Double = 5.0,
    val hasLidarSensor: Boolean = true,

    // Hardware - Power & Cooling
    val batteryMah: Int = 5400,
    val batteryType: String = "Grafeno-Silicio de Alta Densidad",
    val chargingWatts: Int = 100,
    val batteryLifeHours: Double = 28.5,
    val cooling: CoolingType = CoolingType.HEATPIPE_COBRE,

    // Chassis & Materials
    val chassisMaterial: ChassisMaterial = ChassisMaterial.TITANIO_GRADO_5,
    val finishTexture: String = "Cristal Mate Satinado",
    val waterResistance: String = "IP68 (6m / 30min)",
    val repairabilityScore: Double = 8.5,
    val weightGrams: Int = 198,
    val thicknessMm: Double = 7.6,

    // Connectivity & Security
    val connectivity5G: Boolean = true,
    val connectivityWifi7: Boolean = true,
    val connectivitySatellite: Boolean = true,
    val securityChip: String = "Apex Enclave T3",

    // Visual Customizer
    val bodyColorHex: Long = 0xFF1E293B,
    val frameColorHex: Long = 0xFF475569,
    val cameraModuleShape: Int = 0, // 0 = Rounded Island, 1 = Matrix Ring, 2 = Minimalist Floating
    val cameraSensorsCount: Int = 3,
    val logoGlowEnabled: Boolean = true,

    // Economics & Production
    val bomCostUsd: Double = 385.0,
    val retailPriceUsd: Double = 999.0,
    val unitsProduced: Long = 0L,
    val unitsSold: Long = 0L,
    val totalRevenueUsd: Double = 0.0,
    val totalProfitUsd: Double = 0.0,
    val marketPopularity: Double = 88.0,
    val userSatisfaction: Double = 94.0
) {
    // 20-Metric Dynamic Evaluation Score (0.00 to 10.00)
    fun calculateScore(): Double {
        val designScore = (if (chassisMaterial == ChassisMaterial.TITANIO_GRADO_5 || chassisMaterial == ChassisMaterial.GRAFENO_METAMATERIAL) 9.8 else 8.5)
        val buildScore = if (waterResistance.contains("IP68")) 9.6 else 7.8
        val screenScore = displayTech.clarityScore
        val cpuScore = (cpuClockGhz * 1.5 + (10.0 - processNodeNm.coerceAtMost(10.0))).coerceIn(5.0, 10.0)
        val gpuScore = (gpuTflops * 1.2).coerceIn(4.0, 10.0)
        val aiScore = (npuTops / 6.0).coerceIn(4.0, 10.0)
        val ramScore = (ramGb / 2.0).coerceIn(5.0, 10.0)
        val storageScore = (storageGb / 64.0).coerceIn(5.0, 10.0)
        val camScore = (cameraMainMp / 25.0 + cameraZoomOpticalX).coerceIn(4.0, 10.0)
        val batteryScore = (batteryLifeHours / 3.0).coerceIn(5.0, 10.0)
        val connectivityScore = if (connectivitySatellite && connectivityWifi7) 9.9 else 8.2
        val softwareScore = 9.4
        val securityScore = 9.5
        val perfScore = ((cpuScore + gpuScore + aiScore) / 3.0)
        val efficiencyScore = cooling.thermalEfficiency
        val valueScore = ((retailPriceUsd / bomCostUsd.coerceAtLeast(1.0)).let { ratio ->
            if (ratio in 1.8..2.8) 9.5 else if (ratio < 1.8) 10.0 else 7.0
        })
        val innovationScore = if (processNodeNm <= 2.0 || displayTech == DisplayTech.MICRO_LED) 9.9 else 8.6
        val ecosystemScore = 9.3
        val repairScore = repairabilityScore
        val uxScore = 9.6

        val allScores = listOf(
            designScore, buildScore, screenScore, cpuScore, gpuScore,
            aiScore, ramScore, storageScore, camScore, batteryScore,
            connectivityScore, softwareScore, securityScore, perfScore, efficiencyScore,
            valueScore, innovationScore, ecosystemScore, repairScore, uxScore
        )

        return (allScores.sum() / allScores.size).coerceIn(0.0, 10.0)
    }

    fun formattedScore(): String {
        return String.format(Locale.US, "%.2f/10", calculateScore())
    }

    val profitPerUnit: Double get() = (retailPriceUsd - bomCostUsd).coerceAtLeast(0.0)
    val marginPercentage: Double get() = if (retailPriceUsd > 0) (profitPerUnit / retailPriceUsd) * 100.0 else 0.0
}
