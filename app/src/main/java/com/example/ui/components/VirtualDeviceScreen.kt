package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OperatingSystem
import com.example.model.VirtualAppType
import com.example.model.VirtualOSState
import com.example.ui.theme.*

@Composable
fun VirtualDeviceScreen(
    operatingSystem: OperatingSystem?,
    virtualState: VirtualOSState,
    onBoot: () -> Unit,
    onShutdown: () -> Unit,
    onRestart: () -> Unit,
    onLaunchApp: (VirtualAppType?) -> Unit,
    onTerminalCommand: (String) -> Unit,
    onRunBenchmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    var terminalInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Outer Physical Hardware Frame of Virtual Smartphone / Terminal
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(DeepDarkSurface, DeepDarkBackground)),
                RoundedCornerShape(24.dp)
            )
            .border(2.dp, CardBorderColor, RoundedCornerShape(24.dp))
            .padding(12.dp)
            .testTag("virtual_device_chassis")
    ) {
        // Physical Bezel Top: Speaker grill, Front Camera, Power & Restart hardware buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Power / Status indicator LED
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            if (virtualState.isBooted) CyberGreenReal else Color.Red,
                            CircleShape
                        )
                )
                Text(
                    text = if (virtualState.isBooted) "APEX VIRTUAL RUNTIME (ONLINE)" else "APEX HARDWARE (OFF)",
                    color = if (virtualState.isBooted) CyberGreenReal else Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Hardware Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = if (virtualState.isBooted) onShutdown else onBoot,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (virtualState.isBooted) Color(0xFFEF4444).copy(alpha = 0.2f) else CyberGreenReal.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Encender/Apagar",
                        tint = if (virtualState.isBooted) Color(0xFFEF4444) else CyberGreenReal,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (virtualState.isBooted) "Apagar" else "Encender",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (virtualState.isBooted) {
                    IconButton(
                        onClick = onRestart,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reiniciar", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Inner Virtual Screen Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (virtualState.isBooted) Color(0xFF030712) else Color.Black)
                .border(1.dp, CardBorderColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .testTag("virtual_screen_display")
        ) {
            if (!virtualState.isBooted) {
                // Screen Off State
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.PowerSettingsNew,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Dispositivo Virtual Apagado",
                        color = TextTertiary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Pulsa 'Encender' para arrancar ${operatingSystem?.name ?: "ApexOS"}",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )
                }
            } else {
                // Screen Booted State
                Column(modifier = Modifier.fillMaxSize()) {
                    // Virtual Status Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F172A).copy(alpha = 0.8f))
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "12:00",
                            color = TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = CyberCyanAccent, modifier = Modifier.size(12.dp))
                            Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = CyberCyanAccent, modifier = Modifier.size(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("${virtualState.batteryLevelPercent}%", color = TextPrimary, fontSize = 9.sp)
                                Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = CyberGreenReal, modifier = Modifier.size(12.dp))
                            }
                        }
                    }

                    // Main Virtual Desktop or Active App
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        when (virtualState.activeApp) {
                            null -> {
                                // Desktop Home Screen with App Grid
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // OS Header Banner
                                    Surface(
                                        color = DeepDarkSurfaceVariant.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.Memory, contentDescription = null, tint = CyberGoldPrimary)
                                            Column {
                                                Text(
                                                    text = operatingSystem?.name ?: "ApexOS Nova",
                                                    color = TextPrimary,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = "Kernel: ${operatingSystem?.kernel?.displayName ?: "Hybrid"} | Arch: ${operatingSystem?.architecture?.displayName ?: "ARM64"}",
                                                    color = TextSecondary,
                                                    fontSize = 9.sp
                                                )
                                            }
                                        }
                                    }

                                    // App Icon Launcher Grid (2 rows x 4 cols)
                                    val apps = VirtualAppType.entries
                                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                        apps.chunked(4).forEach { rowApps ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceAround
                                            ) {
                                                rowApps.forEach { app ->
                                                    VirtualAppIcon(
                                                        app = app,
                                                        onClick = { onLaunchApp(app) }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Bottom Dock
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = DeepDarkSurfaceVariant.copy(alpha = 0.8f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            VirtualDockIcon(Icons.Default.Terminal, "Terminal") { onLaunchApp(VirtualAppType.TERMINAL) }
                                            VirtualDockIcon(Icons.Default.Folder, "Archivos") { onLaunchApp(VirtualAppType.FILES) }
                                            VirtualDockIcon(Icons.Default.Speed, "Benchmark") { onLaunchApp(VirtualAppType.BENCHMARK) }
                                            VirtualDockIcon(Icons.Default.Settings, "Ajustes") { onLaunchApp(VirtualAppType.SETTINGS) }
                                        }
                                    }
                                }
                            }

                            VirtualAppType.TERMINAL -> {
                                // Interactive Virtual Shell CLI
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF050811))
                                        .padding(8.dp)
                                ) {
                                    // App Top Bar
                                    VirtualAppHeader(title = "Apex Shell CLI", onClose = { onLaunchApp(null) })

                                    // Terminal Log Lines
                                    LazyColumn(
                                        state = listState,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        items(virtualState.terminalLines) { line ->
                                            Text(
                                                text = line,
                                                color = if (line.startsWith("$")) CyberGoldPrimary else if (line.contains("Error") || line.contains("no reconocido")) Color(0xFFEF4444) else CyberCyanAccent,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp,
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }

                                    // Terminal Command Input
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("$ ", color = CyberGoldPrimary, fontFamily = FontFamily.Monospace, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        OutlinedTextField(
                                            value = terminalInput,
                                            onValueChange = { terminalInput = it },
                                            placeholder = { Text("Escribe 'help', 'sysinfo', 'neofetch'...", fontSize = 10.sp, color = TextTertiary) },
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                            keyboardActions = KeyboardActions(
                                                onSend = {
                                                    if (terminalInput.isNotBlank()) {
                                                        onTerminalCommand(terminalInput)
                                                        terminalInput = ""
                                                    }
                                                }
                                            ),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = TextPrimary,
                                                unfocusedTextColor = TextPrimary,
                                                focusedBorderColor = CyberCyanAccent,
                                                unfocusedBorderColor = CardBorderColor
                                            ),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp),
                                            textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                        )
                                        IconButton(
                                            onClick = {
                                                if (terminalInput.isNotBlank()) {
                                                    onTerminalCommand(terminalInput)
                                                    terminalInput = ""
                                                }
                                            },
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(Icons.Default.Send, contentDescription = "Enviar comando", tint = CyberCyanAccent, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }

                            VirtualAppType.BENCHMARK -> {
                                // Real-time Animated Benchmark Suite
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DeepDarkBackground)
                                        .padding(8.dp)
                                ) {
                                    VirtualAppHeader(title = "Apex Benchmark Pro", onClose = { onLaunchApp(null) })

                                    Spacer(modifier = Modifier.height(6.dp))

                                    if (virtualState.isBenchmarking) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            CircularProgressIndicator(
                                                progress = { virtualState.benchmarkProgress },
                                                color = CyberGoldPrimary,
                                                modifier = Modifier.size(54.dp)
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(
                                                text = "Estresando núcleos de CPU & NPU... ${(virtualState.benchmarkProgress * 100).toInt()}%",
                                                color = CyberCyanAccent,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Temperatura SoC: ${String.format("%.1f", virtualState.cpuTemperatureCelsius)}°C",
                                                color = TextSecondary,
                                                fontSize = 10.sp
                                            )
                                        }
                                    } else {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            BenchmarkScoreCard("CPU Mononúcleo (Single-Core)", "${virtualState.benchmarkScoreSingleCore} pts", 0.85f, CyberCyanAccent)
                                            BenchmarkScoreCard("CPU Multinúcleo (Multi-Core)", "${virtualState.benchmarkScoreMultiCore} pts", 0.92f, CyberGoldPrimary)
                                            BenchmarkScoreCard("GPU Compute & Ray Tracing", "${virtualState.benchmarkScoreGpuCompute} pts", 0.88f, CyberPurpleSpeculative)
                                            BenchmarkScoreCard("IA Neural TOPS (NPU Engine)", "${virtualState.benchmarkScoreAiTops} TOPS", 0.95f, CyberGreenReal)

                                            Button(
                                                onClick = onRunBenchmark,
                                                colors = ButtonDefaults.buttonColors(containerColor = CyberGoldPrimary),
                                                modifier = Modifier.fillMaxWidth().height(36.dp),
                                                shape = RoundedCornerShape(8.dp)
                                            ) {
                                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Ejecutar Test de Estrés Completo", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            VirtualAppType.FILES -> {
                                // Simulated File Explorer
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DeepDarkBackground)
                                        .padding(8.dp)
                                ) {
                                    VirtualAppHeader(title = "Explorador de Archivos (APFS)", onClose = { onLaunchApp(null) })
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LazyColumn(modifier = Modifier.weight(1f)) {
                                        items(virtualState.fileSystem) { file ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp)
                                                    .background(DeepDarkSurfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    Icon(
                                                        imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.Description,
                                                        contentDescription = null,
                                                        tint = if (file.isDirectory) CyberGoldPrimary else CyberCyanAccent,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                    Column {
                                                        Text(file.name, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                        Text(file.path, color = TextTertiary, fontSize = 9.sp)
                                                    }
                                                }
                                                Text("${file.sizeKb} KB", color = TextSecondary, fontSize = 9.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            VirtualAppType.SETTINGS -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DeepDarkBackground)
                                        .padding(8.dp)
                                ) {
                                    VirtualAppHeader(title = "Ajustes del Sistema", onClose = { onLaunchApp(null) })
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        SettingInfoRow("Versión de SO", "${operatingSystem?.name ?: "ApexOS"} ${operatingSystem?.version ?: "1.0"}")
                                        SettingInfoRow("Kernel", operatingSystem?.kernel?.displayName ?: "Hybrid Multithread")
                                        SettingInfoRow("Arquitectura", operatingSystem?.architecture?.displayName ?: "ARM64")
                                        SettingInfoRow("Sistema de Archivos", operatingSystem?.fileSystem ?: "APFS-Quantum")
                                        SettingInfoRow("Seguridad", operatingSystem?.securityLevel ?: "Post-Quantum 512")
                                        SettingInfoRow("Tasa de Refresco", "120Hz Adaptativo")
                                        SettingInfoRow("Salud de Batería", "100% (5400 mAh)")
                                    }
                                }
                            }

                            VirtualAppType.ECOSYSTEM -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DeepDarkBackground)
                                        .padding(8.dp)
                                ) {
                                    VirtualAppHeader(title = "Ecosistema Apex Cloud", onClose = { onLaunchApp(null) })
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Dispositivos Enlazados Sincronizados:", color = CyberCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        EcosystemDeviceItem("Apex Phone Pro Max", "Smartphone Primario", Icons.Default.PhoneAndroid)
                                        EcosystemDeviceItem("ApexBook Quantum 16", "Estación de Trabajo", Icons.Default.Laptop)
                                        EcosystemDeviceItem("ApexWatch Chrono 2", "Reloj Inteligente", Icons.Default.Watch)
                                        EcosystemDeviceItem("Apex Blade Cloud Server", "Servidor Neuronal", Icons.Default.Dns)
                                    }
                                }
                            }

                            else -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DeepDarkBackground)
                                        .padding(8.dp)
                                ) {
                                    VirtualAppHeader(title = virtualState.activeApp.title, onClose = { onLaunchApp(null) })
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text("Módulo activo de ${virtualState.activeApp.title} en ejecución normal.", color = TextSecondary, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VirtualAppIcon(app: VirtualAppType, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        val iconVector = when (app) {
            VirtualAppType.TERMINAL -> Icons.Default.Terminal
            VirtualAppType.FILES -> Icons.Default.Folder
            VirtualAppType.BENCHMARK -> Icons.Default.Speed
            VirtualAppType.SETTINGS -> Icons.Default.Settings
            VirtualAppType.APP_STORE -> Icons.Default.ShoppingBag
            VirtualAppType.SECURITY -> Icons.Default.Security
            VirtualAppType.AI_ASSISTANT -> Icons.Default.Psychology
            VirtualAppType.ECOSYSTEM -> Icons.Default.CloudSync
        }
        val iconColor = when (app) {
            VirtualAppType.TERMINAL -> CyberGoldPrimary
            VirtualAppType.BENCHMARK -> CyberCyanAccent
            VirtualAppType.AI_ASSISTANT -> CyberPurpleSpeculative
            VirtualAppType.SECURITY -> CyberGreenReal
            else -> TextPrimary
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DeepDarkSurfaceElevated,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(iconColor, iconColor.copy(alpha = 0.3f)))),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(iconVector, contentDescription = app.title, tint = iconColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = app.title.take(8),
            color = TextSecondary,
            fontSize = 9.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun VirtualDockIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(34.dp)) {
        Icon(icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun VirtualAppHeader(title: String, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = CyberGoldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = onClose, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun BenchmarkScoreCard(label: String, score: String, progress: Float, color: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepDarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            Text(score, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = color,
            trackColor = DeepDarkSurfaceElevated,
            modifier = Modifier.fillMaxWidth().height(4.dp)
        )
    }
}

@Composable
private fun SettingInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepDarkSurfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 10.sp)
        Text(value, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EcosystemDeviceItem(name: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepDarkSurfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = CyberCyanAccent, modifier = Modifier.size(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(name, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = TextTertiary, fontSize = 9.sp)
        }
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CyberGreenReal, modifier = Modifier.size(14.dp))
    }
}
