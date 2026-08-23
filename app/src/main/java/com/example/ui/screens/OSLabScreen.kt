package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.VirtualDeviceScreen
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OSLabScreen(
    uiState: GameUiState,
    onCreateOS: (String, String, DeviceCategory, OSKernelType, OSArchitectureType) -> Unit,
    onSelectOS: (OperatingSystem) -> Unit,
    onBoot: () -> Unit,
    onShutdown: () -> Unit,
    onRestart: () -> Unit,
    onLaunchApp: (VirtualAppType?) -> Unit,
    onTerminalCommand: (String) -> Unit,
    onRunBenchmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var osName by remember { mutableStateOf("ApexOS Cyber") }
    var osVersion by remember { mutableStateOf("2.0") }
    var targetCategory by remember { mutableStateOf(DeviceCategory.SMARTPHONE) }
    var kernelType by remember { mutableStateOf(OSKernelType.HYBRID) }
    var archType by remember { mutableStateOf(OSArchitectureType.ARM_64) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Header
        Text(
            text = "Laboratorio de Sistemas Operativos & Entorno Virtual",
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black
        )

        // Interactive Virtual Device & Sandbox Runtime
        VirtualDeviceScreen(
            operatingSystem = uiState.activeOS,
            virtualState = uiState.virtualOSState,
            onBoot = onBoot,
            onShutdown = onShutdown,
            onRestart = onRestart,
            onLaunchApp = onLaunchApp,
            onTerminalCommand = onTerminalCommand,
            onRunBenchmark = onRunBenchmark
        )

        // OS Architecture & Creation Studio
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderColor, CardBorderColor))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Desarrollo de Nuevo Sistema Operativo", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = osName,
                        onValueChange = { osName = it },
                        label = { Text("Nombre del SO") },
                        modifier = Modifier.weight(1.5f).testTag("os_name_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyberGoldPrimary,
                            unfocusedBorderColor = CardBorderColor
                        )
                    )
                    OutlinedTextField(
                        value = osVersion,
                        onValueChange = { osVersion = it },
                        label = { Text("Versión") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyberCyanAccent,
                            unfocusedBorderColor = CardBorderColor
                        )
                    )
                }

                // Kernel Selector
                Text("Tipo de Kernel:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OSKernelType.entries.take(4).forEach { k ->
                        val isSelected = kernelType == k
                        FilterChip(
                            selected = isSelected,
                            onClick = { kernelType = k },
                            label = { Text(k.displayName.take(12), fontSize = 10.sp) }
                        )
                    }
                }

                // Architecture Selector
                Text("Arquitectura de CPU / ISA:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OSArchitectureType.entries.take(4).forEach { a ->
                        val isSelected = archType == a
                        FilterChip(
                            selected = isSelected,
                            onClick = { archType = a },
                            label = { Text(a.displayName.take(12), fontSize = 10.sp) }
                        )
                    }
                }

                Button(
                    onClick = { onCreateOS(osName, osVersion, targetCategory, kernelType, archType) },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyanAccent),
                    modifier = Modifier.fillMaxWidth().height(42.dp).testTag("create_os_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.DeveloperMode, contentDescription = null, tint = androidx.compose.ui.graphics.Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Compilar y Desplegar Sistema Operativo", color = androidx.compose.ui.graphics.Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        // Active Operating Systems in Portfolio
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Sistemas Operativos en Cartera (${uiState.operatingSystems.size})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    uiState.operatingSystems.forEach { os ->
                        OSListItem(
                            os = os,
                            isActive = uiState.activeOS?.id == os.id,
                            onSelect = { onSelectOS(os) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OSListItem(os: OperatingSystem, isActive: Boolean, onSelect: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isActive) CyberCyanAccent.copy(alpha = 0.15f) else DeepDarkSurfaceVariant.copy(alpha = 0.4f),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                if (isActive) listOf(CyberCyanAccent, CyberGoldPrimary) else listOf(CardBorderColor, CardBorderColor)
            )
        ),
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("${os.name} v${os.version}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("Kernel: ${os.kernel.displayName} • Arch: ${os.architecture.displayName}", color = TextSecondary, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${os.marketSharePercentage}% Cuota", color = CyberGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("${os.activeInstalls} Instalaciones", color = TextTertiary, fontSize = 9.sp)
            }
        }
    }
}
