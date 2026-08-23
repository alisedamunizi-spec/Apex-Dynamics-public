package com.example.model

enum class OSKernelType(val displayName: String, val memoryFootprintMb: Int, val stabilityBonus: Double) {
    MONOLITHIC("Kernel Monolítico Optimizado", 512, 8.5),
    MICROKERNEL("Microkernel Modular Ultra-Seguro", 128, 9.8),
    HYBRID("Kernel Híbrido Multihilo", 384, 9.2),
    QUANTUM_EXOKERNEL("Exokernel Cuántico Neuronal", 64, 10.0)
}

enum class OSArchitectureType(val displayName: String) {
    ARM_64("ARM64 / AArch64 Universal"),
    X86_64("x86-64 Multicore Extension"),
    RISC_V("RISC-V Vectorizado Libre"),
    NEUROMORPHIC("Arquitectura Neuromórfica Bio-Sintética")
}

data class OperatingSystem(
    val id: String,
    val name: String = "ApexOS Core",
    val version: String = "1.0 Nova",
    val targetCategory: DeviceCategory = DeviceCategory.SMARTPHONE,
    val kernel: OSKernelType = OSKernelType.HYBRID,
    val architecture: OSArchitectureType = OSArchitectureType.ARM_64,
    val fileSystem: String = "APFS-Quantum (Resilient ZFS)",
    val securityLevel: String = "Cifrado Post-Cuántico de Curva Elíptica",
    val multitaskingType: String = "Programador Preemptivo Asíncrono de Prioridad Dinámica",
    val uiStyle: String = "Glassmorphism Cyber-Dark Dinámico",
    val hasAppStore: Boolean = true,
    val hasCloudSync: Boolean = true,
    val hasAIAssistant: Boolean = true,
    val stabilityRating: Double = 9.8,
    val marketSharePercentage: Double = 34.5,
    val activeInstalls: Long = 1500000L
)

enum class VirtualAppType(val title: String, val iconName: String) {
    TERMINAL("Apex Shell CLI", "terminal"),
    FILES("Explorador de Archivos", "folder"),
    BENCHMARK("Apex Benchmark Pro", "speed"),
    SETTINGS("Configuración del Sistema", "settings"),
    APP_STORE("Apex App Store", "shopping_bag"),
    SECURITY("Escáner de Seguridad & Firewall", "security"),
    AI_ASSISTANT("Apex Neural Copilot", "psychology"),
    ECOSYSTEM("Centro de Ecosistema & Nube", "cloud_sync")
}

data class VirtualFile(
    val path: String,
    val name: String,
    val isDirectory: Boolean,
    val sizeKb: Long,
    val content: String = ""
)

data class VirtualOSState(
    val isPoweredOn: Boolean = true,
    val isBooted: Boolean = true,
    val batteryLevelPercent: Int = 98,
    val isCharging: Boolean = false,
    val activeApp: VirtualAppType? = null,
    val terminalLines: List<String> = listOf(
        "ApexOS v1.0 Nova (Kernel 6.8.4-apex-quantum)",
        "System ready. Architecture: ARM64-NEON",
        "Type 'help' for commands list."
    ),
    val fileSystem: List<VirtualFile> = listOf(
        VirtualFile("/system", "system", true, 0),
        VirtualFile("/system/kernel.sys", "kernel.sys", false, 4096, "APEX_KERNEL_CORE_V1.0_OK"),
        VirtualFile("/system/drivers.cfg", "drivers.cfg", false, 128, "GPU=APEX_TITAN;NPU=NEURAL_V2;NET=WIFI7"),
        VirtualFile("/user", "user", true, 0),
        VirtualFile("/user/documents", "documents", true, 0),
        VirtualFile("/user/documents/empresa_plan.txt", "empresa_plan.txt", false, 24, "Misión: Dominar la industria tecnológica mundial y trascender al cosmos."),
        VirtualFile("/user/ai_weights.bin", "ai_weights.bin", false, 32768, "TENSOR_WEIGHTS_45B_PARAMS_CHECKPOINT")
    ),
    val isBenchmarking: Boolean = false,
    val benchmarkProgress: Float = 0f,
    val benchmarkScoreSingleCore: Int = 3450,
    val benchmarkScoreMultiCore: Int = 18900,
    val benchmarkScoreGpuCompute: Int = 24800,
    val benchmarkScoreAiTops: Double = 45.8,
    val cpuTemperatureCelsius: Double = 37.2,
    val isEcosystemSynced: Boolean = true
)
