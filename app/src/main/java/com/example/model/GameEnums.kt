package com.example.model

enum class HistoricalEra(
    val id: String,
    val displayName: String,
    val startYear: Long,
    val endYear: Long,
    val category: EraCategory,
    val description: String
) {
    PALEOLITHIC("paleo", "Paleolítico", -2500000, -10000, EraCategory.HISTORIA_REAL, "Primeras herramientas de piedra tallada y control del fuego."),
    MESOLITHIC("meso", "Mesolítico", -10000, -8000, EraCategory.HISTORIA_REAL, "Transición y microlitos avanzados."),
    NEOLITHIC("neo", "Neolítico", -8000, -4000, EraCategory.HISTORIA_REAL, "Nacimiento de la agricultura, ganadería y cerámica."),
    AGE_OF_METALS("metals", "Edad de los Metales", -4000, -3300, EraCategory.HISTORIA_REAL, "Metalurgia del cobre y primeros hornos."),
    BRONZE_AGE("bronze", "Edad del Bronce", -3300, -1200, EraCategory.HISTORIA_REAL, "Aleaciones de bronce, escritura y primeras civilizaciones."),
    IRON_AGE("iron", "Edad del Hierro", -1200, -500, EraCategory.HISTORIA_REAL, "Fundición del hierro y herramientas duraderas."),
    CLASSICAL_ANTIQUITY("classic_antiq", "Antigüedad Clásica", -500, 300, EraCategory.HISTORIA_REAL, "Mundo grecorromano, acueductos, matemáticas y filosofía."),
    LATE_ANTIQUITY("late_antiq", "Antigüedad Tardía", 300, 500, EraCategory.HISTORIA_REAL, "Transición imperial y preservación del conocimiento."),
    MIDDLE_AGES("middle_ages", "Edad Media", 500, 1450, EraCategory.HISTORIA_REAL, "Molinos de viento, arado pesado, pólvora e imprenta inicial."),
    RENAISSANCE("renaissance", "Renacimiento", 1450, 1600, EraCategory.HISTORIA_REAL, "Imprenta de tipos móviles, navegación oceánica y anatomía."),
    SCIENTIFIC_REVOLUTION("sci_rev", "Revolución Científica", 1600, 1750, EraCategory.HISTORIA_REAL, "Método científico, telescopio, cálculo y física newtoniana."),
    ENLIGHTENMENT("enlighten", "Ilustración", 1750, 1780, EraCategory.HISTORIA_REAL, "Enciclopedismo, química moderna y primeras máquinas térmicas."),
    INDUSTRIAL_REVOLUTION("ind_rev", "Revolución Industrial", 1780, 1870, EraCategory.HISTORIA_REAL, "Máquina de vapor, ferrocarril, telar mecánico y siderurgia."),
    ELECTRIFICATION("electrif", "Electrificación", 1870, 1910, EraCategory.HISTORIA_REAL, "Red eléctrica, bombilla incandescente, telégrafo y dinamos."),
    AUTOMOBILE_ERA("auto_era", "Era del Automóvil", 1910, 1940, EraCategory.HISTORIA_REAL, "Motor de combustión interna, aviación y cadenas de montaje."),
    TELECOMMUNICATIONS("telecom", "Telecomunicaciones", 1940, 1960, EraCategory.HISTORIA_REAL, "Radar, radio FM, televisión, computación electro-mecánica."),
    ELECTRONIC_ERA("electronic", "Era Electrónica", 1960, 1975, EraCategory.HISTORIA_REAL, "Transistores de estado sólido y primeros circuitos integrados."),
    SPACE_AGE("space_age", "Era Espacial", 1965, 1985, EraCategory.HISTORIA_REAL, "Carrera lunar, satélites orbitales y telecomunicaciones globales."),
    COMPUTING_ERA("computing", "Informática Clásica", 1975, 1995, EraCategory.HISTORIA_REAL, "Microprocesadores, computadoras personales y GUI."),
    INTERNET_ERA("internet", "Era de Internet", 1995, 2008, EraCategory.HISTORIA_REAL, "World Wide Web, fibra óptica, buscadores y comercio online."),
    DIGITAL_MOBILE("digital_mobile", "Era Digital y Móvil", 2008, 2022, EraCategory.HISTORIA_REAL, "Smartphones, redes 4G/5G, cloud computing y big data."),
    AI_ERA("ai_era", "Era de la Inteligencia Artificial", 2022, 2035, EraCategory.HISTORIA_REAL, "Redes neuronales profundas, LLMs, modelos generativos y robots."),
    NEAR_FUTURE("near_future", "Futuro Próximo", 2035, 2070, EraCategory.FUTURO_HIPOTETICO, "IA autónoma general (AGI), fusión nuclear y computación cuántica."),
    INTERPLANETARY("interplanetary", "Era Interplanetaria", 2070, 2200, EraCategory.FUTURO_HIPOTETICO, "Bases permanentes en Luna y Marte, minería de asteroides."),
    SINGULARITY("singularity", "Singularidad Tecnológica", 2200, 3000, EraCategory.FUTURO_HIPOTETICO, "Superinteligencia sintética (ASI), nanorobótica autorreplicante."),
    POST_BIOLOGICAL("post_bio", "Era Post-Biológica", 3000, 100000, EraCategory.CIENCIA_FICCION, "Consciencias digitales, megaestructuras estelares."),
    KARDASHEV_TYPE2("kardashev2", "Civilización Tipo II", 100000, 1000000, EraCategory.CIENCIA_FICCION, "Enjambres de Dyson, manipulación gravitacional estelar."),
    COSMIC_ERA("cosmic", "Era Cósmica Trascendente", 1000000, 100000000, EraCategory.CIENCIA_FICCION, "Viajes hiperespaciales y control de la entropía universal.");

    companion object {
        fun fromYear(year: Long): HistoricalEra {
            return entries.firstOrNull { year in it.startYear..it.endYear }
                ?: if (year < -2500000) PALEOLITHIC else COSMIC_ERA
        }
    }
}

enum class EraCategory(val label: String, val badgeColor: Long) {
    HISTORIA_REAL("HISTORIA REAL", 0xFF0284C7),
    HISTORIA_ALTERNATIVA("HISTORIA ALTERNATIVA", 0xFFD97706),
    FUTURO_HIPOTETICO("FUTURO HIPOTÉTICO", 0xFF8B5CF6),
    CIENCIA_FICCION("CIENCIA FICCIÓN", 0xFFEC4899)
}

enum class TechClassification(val label: String, val color: Long, val description: String) {
    REAL("REAL", 0xFF10B981, "Tecnología históricamente verificada y probada."),
    EN_DESARROLLO("EN DESARROLLO", 0xFF06B6D4, "Tecnología en fase activa de investigación o prototipo actual."),
    PREDICCION("PREDICCIÓN", 0xFFF59E0B, "Predicción científica extrapolada según física conocida."),
    HIPOTESIS("HIPÓTESIS", 0xFF8B5CF6, "Hipótesis teórica con bases matemáticas no probadas."),
    CIENCIA_FICCION("CIENCIA FICCIÓN", 0xFFEC4899, "Especulación futurista y tecnología hiper-avanzada.")
}

enum class GameSpeed(val multiplier: Long, val label: String) {
    PAUSE(0L, "Pausa"),
    X1(1L, "1x"),
    X2(2L, "2x"),
    X5(5L, "5x"),
    X10(10L, "10x"),
    X25(25L, "25x"),
    X50(50L, "50x"),
    X100(100L, "100x"),
    X250(250L, "250x"),
    X500(500L, "500x"),
    X1000(1000L, "1.000x"),
    X10000(10000L, "10.000x"),
    X100000(100000L, "100.000x"),
    X1M(1000000L, "1M x")
}

enum class GameMode(val label: String, val description: String) {
    REAL_HISTORY("Historia Real", "Avanza fielmente siguiendo la cronología tecnológica humana."),
    ALT_HISTORY("Historia Alternativa", "Tus inventos adelantados alteran el curso de la historia."),
    HYPOTHETICAL_FUTURE("Futuro Hipotético", "Simula escenarios futuros hasta millones de años."),
    SANDBOX("Modo Sandbox", "Recursos infinitos y acceso libre a todas las eras."),
    CHALLENGES("Desafíos Históricos", "Supera hitos concretos en tiempo récord.")
}

enum class GraphicProfile(val label: String, val particles: Int, val antialias: Boolean, val lightingQuality: String) {
    BAJO("Bajo", 10, false, "Simple"),
    MEDIO("Medio", 30, true, "Estándar"),
    ALTO("Alto", 60, true, "Completa"),
    ULTRA("Ultra", 120, true, "Ray-Tracing Simulado")
}

enum class DeviceCategory(val displayName: String, val iconName: String) {
    SMARTPHONE("Smartphone", "phone_android"),
    TABLET("Tablet", "tablet_android"),
    PC_DESKTOP("PC de Escritorio", "desktop_windows"),
    LAPTOP("Portátil", "laptop"),
    SMARTWATCH("Smartwatch", "watch"),
    SMART_TV("Smart TV", "tv"),
    CONSOLE("Consola de Juegos", "sports_esports"),
    CAMERA("Cámara Digital", "photo_camera"),
    DRONE("Drone Autónomo", "flight"),
    ROBOT("Robot Humanoide / Industrial", "smart_toy"),
    VEHICLE("Vehículo Autónomo / EV", "directions_car"),
    MEDICAL_TECH("Dispositivo Médico", "medical_services"),
    IOT_DEVICE("Dispositivo IoT", "sensors"),
    SMART_GLASSES("Gafas Inteligentes AR/VR", "visibility"),
    SERVER_BLADE("Servidor Cloud / Blade", "dns"),
    QUANTUM_SUPERCOMPUTER("Superordenador Cuántico", "memory"),
    FUTURISTIC_DEVICE("Dispositivo Futurista Transcendente", "all_inclusive")
}

enum class CompanyStage(val displayName: String, val requiredValuation: Double) {
    STARTUP("Startup Tecnológica", 0.0),
    NACIONAL("Empresa Nacional", 500000.0),
    MULTINACIONAL("Multinacional", 5000000.0),
    TECH_GIANT("Gigante Tecnológico", 50000000.0),
    CONGLOMERADO("Conglomerado Global", 500000000.0),
    WORLD_CORP("Corporación Mundial", 5000000000.0),
    FUTURISTIC_CORP("Corporación Interplanetaria", 50000000000.0)
}
