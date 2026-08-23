package com.example.model

enum class ResourceType(val displayName: String, val baseCostPerUnit: Double, val category: String) {
    PIEDRA("Piedra y Sílex", 2.0, "Primitivo"),
    MADERA("Madera Estructural", 5.0, "Primitivo"),
    COBRE("Cobre Refinado", 25.0, "Metales"),
    BRONCE("Bronce Metalúrgico", 40.0, "Metales"),
    HIERRO("Hierro Forjado y Acero", 55.0, "Metales"),
    CARBON("Carbón Mineral", 30.0, "Energía Fósil"),
    PETROLEO("Petróleo y Polímeros", 70.0, "Energía Fósil"),
    GAS_NATURAL("Gas Natural", 60.0, "Energía Fósil"),
    SILICIO("Silicio Grado Electrónico (99.9999%)", 280.0, "Semiconductores"),
    TIERRAS_RARAS("Tierras Raras (Neodimio, Lantano)", 650.0, "Alta Tecnología"),
    MATERIALES_AVANZADOS("Grafeno y Metamateriales", 1800.0, "Materiales Futuros"),
    RECURSOS_ESPACIALES("Helio-3 y Minerales de Asteroides", 8500.0, "Cosmología")
}

enum class EnergySourceType(val displayName: String, val powerOutputMw: Double, val cleanEnergyIndex: Double, val costPerMonth: Double) {
    FUEGO_BIOMASA("Fuego & Biomasa", 0.5, 0.2, 50.0),
    CARBON_TERMICA("Central Térmica de Carbón", 50.0, 0.1, 1500.0),
    PETROLEO_COMBUSTION("Generación por Fuel/Petróleo", 80.0, 0.2, 2200.0),
    GAS_CICLO_COMBINADO("Gas de Ciclo Combinado", 120.0, 0.4, 2800.0),
    HIDROELECTRICA("Presa Hidroeléctrica", 200.0, 0.9, 1800.0),
    SOLAR_FOTOVOLTAICA("Parque Solar Fotovoltaico", 150.0, 1.0, 1200.0),
    EOLICA_OFFSHORE("Parque Eólico Marino", 180.0, 1.0, 1400.0),
    NUCLEAR_FISION("Central Nuclear de Fisión Avanzada", 800.0, 0.85, 4500.0),
    FUSION_NUCLEAR("Reactor Tokamak de Fusión Magnética", 3500.0, 1.0, 9500.0),
    ENJAMBRE_DYSON("Colectores Solares Orbitales / Dyson", 50000.0, 1.0, 25000.0)
}

data class Factory(
    val id: String,
    val name: String,
    val factoryType: String, // "Fab de Semiconductores 2nm", "Planta de Ensamblaje Robotizada", "Fundición Metalúrgica", "Astillero Orbital"
    val countryLocation: String,
    val unitsCapacityPerMonth: Long,
    val currentProductionRatePercent: Float = 85f,
    val workersCount: Int = 120,
    val robotsCount: Int = 45,
    val monthlyMaintenanceCost: Double = 18000.0
)

data class LogisticsFleet(
    val trucksCount: Int = 25,
    val maglevTrainsCount: Int = 6,
    val cargoShipsCount: Int = 2,
    val cargoPlanesCount: Int = 4,
    val autonomousDronesCount: Int = 80,
    val orbitalRocketsCount: Int = 1,
    val logisticsEfficiencyPercent: Float = 96.5f,
    val monthlyLogisticsCost: Double = 12500.0
)
