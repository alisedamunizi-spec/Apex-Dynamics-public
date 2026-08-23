package com.example.model

enum class ResearchBranch(val displayName: String, val iconName: String) {
    TECNOLOGIA("Tecnología & Hardware", "memory"),
    CIENCIA("Ciencia & Fundamentos", "science"),
    INTELIGENCIA_ARTIFICIAL("Inteligencia Artificial", "psychology"),
    EMPRESA("Estrategia & Empresa", "corporate_fare"),
    CIVILIZACION("Civilización & Futuro", "public")
}

enum class AIDomain(val displayName: String, val description: String, val iconName: String) {
    MACHINE_LEARNING("Machine Learning", "Modelos estadísticos, regresión, clustering y gradient boosting", "timeline"),
    REDES_NEURONALES("Redes Neuronales", "Perceptrones multicapa, retropropagación y arquitecturas densas", "hub"),
    DEEP_LEARNING("Deep Learning", "Transformers profundos, MoE, autoencoders y redes de difusión", "schema"),
    VISION_COMPUTADORA("Visión por Computadora", "Segmentación semántica, detección de objetos y reconstrucción 3D", "visibility"),
    PROCESAMIENTO_VOZ("Procesamiento de Voz", "Reconocimiento ASR, síntesis neuronal TTS y clonación vocal", "mic"),
    NLP("Lenguaje Natural (NLP)", "Grandes modelos de lenguaje (LLMs), tokenización y razonamiento", "translate"),
    IA_GENERATIVA("IA Generativa", "Generación de imágenes fotorrealistas, video, audio y código sintético", "auto_awesome"),
    IA_MULTIMODAL("IA Multimodal", "Fusión sensorial unificada de texto, imagen, audio y robótica", "blur_linear"),
    AGENTES_AUTONOMOS("Agentes Autónomos", "Cadenas de razonamiento ReAct, planificación y ejecución multi-agente", "support_agent"),
    ROBOTICA("Robótica Inteligente", "Cinemática inversa neuronal, SLAM, visión espacial y control humanoide", "smart_toy"),
    IA_CIENTIFICA("IA Científica", "Plegamiento de proteínas, simulación de fusión nuclear y química molecular", "science"),
    IA_MEDICA("IA Médica", "Diagnóstico por imagen clínica, descubrimiento de fármacos y genómica", "medical_services"),
    IA_EMPRESARIAL("IA Empresarial", "Optimización algorítmica de supply chain, finanzas y predicción de demanda", "analytics"),
    IA_AUTONOMA("IA Autónoma (AGI/ASI)", "Auto-mejora recursiva, alineamiento ético y superinteligencia general", "psychology")
}

enum class AIApplicationType(val displayName: String, val description: String, val perkSummary: String) {
    OPTIMIZACION_PRODUCCION(
        "Optimización de Producción",
        "Aplica modelos predictivos para reducir mermas y costes BOM en fábricas.",
        "-20% Coste BOM en nuevos dispositivos y +25% velocidad de ensamblaje"
    ),
    ANALISIS_MERCADO(
        "Análisis y Predicción de Mercado",
        "Algoritmos de demanda que proyectan campañas óptimas y maximizan ventas.",
        "+30% Popularidad de mercado y ventas de dispositivos"
    ),
    DESARROLLO_SOFTWARE_OS(
        "Desarrollo de Software & OS",
        "Copilotos de código autónomos que aceleran compilaciones y depuran kernels.",
        "+35% Instalaciones de ApexOS y +15% rendimiento en Benchmark"
    ),
    CONTROL_ROBOTS_FABRICA(
        "Control de Robots & Automatización",
        "Orquestación neuronal de líneas de montaje automatizadas y logística.",
        "+40% Capacidad de producción mensual y -25% consumo energético"
    ),
    DIAGNOSTICO_PATENTES_MEDICAS(
        "Descubrimiento Científico & Patentes",
        "Modelos de investigación aplicada que descubren compuestos y algoritmos patentables.",
        "Genera hasta $150.000/mes en royalties y patentes pasivas"
    ),
    FINANZAS_EMPRESARIAL(
        "Finanzas & Optimización Fiscal",
        "Análisis cuantitativo de tipos impositivos y optimización de márgenes operativos.",
        "-15% Gastos generales y optimización de impuestos corporativos"
    ),
    NINGUNA(
        "Sin Asignación Activa",
        "El modelo está disponible en el repositorio de la empresa sin asignar a un departamento específico.",
        "Sin bonus aplicados actualmente"
    )
}

data class TechNode(
    val id: String,
    val name: String,
    val branch: ResearchBranch,
    val aiDomain: AIDomain? = null,
    val era: HistoricalEra,
    val classification: TechClassification,
    val costPoints: Double,
    val researchedPoints: Double = 0.0,
    val isCompleted: Boolean = false,
    val prerequisites: List<String> = emptyList(),
    val description: String,
    val scientificFacts: String,
    val speculationNotes: String? = null,
    val unlocksFeatures: String,
    val level: Int = 1,
    val maxLevel: Int = 5
)

data class ResearchFacility(
    val id: String,
    val name: String,
    val facilityType: String,
    val scientistsCount: Int,
    val researchPointsPerSec: Double,
    val monthlyCost: Double,
    val level: Int = 1
)

data class Scientist(
    val id: String,
    val name: String,
    val specialization: String,
    val experienceLevel: Int,
    val productivityMultiplier: Double,
    val salaryMonthly: Double
)

data class AIModel(
    val id: String,
    val name: String,
    val domain: AIDomain = AIDomain.NLP,
    val architecture: String = "Transformer Multimodal MoE",
    val parameterCountBillions: Double = 70.0,
    val modalities: List<String> = listOf("Texto", "Visión", "Código"),
    val accuracyScore: Double = 96.5, // 0.0 to 100.0%
    val inferenceSpeedTokensPerSec: Int = 145,
    val modelSizeGb: Double = 35.0,
    val trainingCostUsd: Double = 84000.0,
    val energyTdpWatts: Double = 350.0,
    val quantization: String = "FP8 Optimizada",
    val practicalApplication: AIApplicationType = AIApplicationType.OPTIMIZACION_PRODUCCION,
    val isAssigned: Boolean = true,
    val trainingProgressPercent: Float = 100f,
    val isTrained: Boolean = true,
    val classification: TechClassification = TechClassification.REAL,
    val commercialValuation: Double = 2450000.0
)
