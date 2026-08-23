package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.Device3DCanvas
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

enum class StudioTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    VISOR_3D("Diseño 3D", Icons.Default.ViewInAr),
    CHIPS_CPU("CPU & SoC", Icons.Default.Memory),
    PANTALLA("Pantalla", Icons.Default.Smartphone),
    CAMARAS("Cámaras", Icons.Default.CameraAlt),
    BATERIA_REFRIG("Batería & Térmica", Icons.Default.BatteryChargingFull),
    MATERIALES("Materiales & Color", Icons.Default.Palette),
    PRODUCCION_PRECIO("Lanzamiento & Precio", Icons.Default.PriceCheck)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceStudioScreen(
    uiState: GameUiState,
    onRotate: (Float, Float) -> Unit,
    onZoom: (Float) -> Unit,
    onExplodedChange: (Float) -> Unit,
    onToggleXRay: () -> Unit,
    onUpdateSpec: ((DeviceSpec) -> DeviceSpec) -> Unit,
    onSavePrototype: () -> Unit,
    onBenchmark: (String) -> Unit,
    onLaunchMarket: (String, Double, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val dev = uiState.activeDeviceInDesigner
    var selectedTab by remember { mutableStateOf(StudioTab.VISOR_3D) }
    val mainScrollState = rememberScrollState()

    var customName by remember(dev.id) { mutableStateOf(dev.name) }
    var retailPrice by remember(dev.id) { mutableStateOf(dev.retailPriceUsd.toString()) }
    var initialUnits by remember(dev.id) { mutableStateOf("10000") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(mainScrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Device Category Selector & Header
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GeoSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
            modifier = Modifier.fillMaxWidth().testTag("device_studio_header")
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Estudio de Hardware & Diseño 3D",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Puntuación de Ingeniería: ${dev.formattedScore()}",
                            color = GeoPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = GeoPrimaryContainer,
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GeoPrimary.copy(alpha = 0.3f)))
                    ) {
                        Text(
                            text = "${uiState.devices.size} Dispositivos",
                            color = GeoPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Category selector row
                val categoryScroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(categoryScroll),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DeviceCategory.entries.forEach { category ->
                        val isSelected = dev.category == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { onUpdateSpec { it.copy(category = category) } },
                            label = { Text(category.displayName, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GeoPrimary,
                                selectedLabelColor = Color.White,
                                containerColor = GeoSurfaceVariant,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) GeoPrimary else CardBorderColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        // 3D / 2.5D Interactive Device Canvas Viewport
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = GeoSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Device3DCanvas(
                    deviceSpec = dev,
                    rotationX = uiState.designerRotationX,
                    rotationY = uiState.designerRotationY,
                    zoomScale = uiState.designerZoom,
                    explodedProgress = uiState.designerExplodedView,
                    isXRayMode = uiState.designerXRayMode,
                    onRotate = onRotate,
                    onZoom = onZoom,
                    onExplodedChange = onExplodedChange,
                    onToggleXRay = onToggleXRay,
                    modifier = Modifier.fillMaxWidth().height(280.dp)
                )
            }
        }

        // Studio Navigation Tabs Row
        val tabScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tabScroll),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StudioTab.entries.forEach { tab ->
                val isSelected = selectedTab == tab
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTab = tab },
                    leadingIcon = {
                        Icon(tab.icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = if (isSelected) Color.White else GeoPrimary)
                    },
                    label = { Text(tab.label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GeoPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = GeoSurface,
                        labelColor = TextPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) GeoPrimary else CardBorderColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        // Tab Content Panels
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GeoSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
            modifier = Modifier.fillMaxWidth().testTag("studio_tab_content")
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when (selectedTab) {
                    StudioTab.VISOR_3D -> {
                        Text("Personalización General del Prototipo", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = customName,
                            onValueChange = {
                                customName = it
                                onUpdateSpec { spec -> spec.copy(name = it) }
                            },
                            label = { Text("Nombre Comercial del Dispositivo") },
                            modifier = Modifier.fillMaxWidth().testTag("device_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = GeoPrimary,
                                unfocusedBorderColor = CardBorderColor
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dev.modelVariant,
                                onValueChange = { v -> onUpdateSpec { it.copy(modelVariant = v) } },
                                label = { Text("Variante (Pro, Ultra, Air)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = dev.generation.toString(),
                                onValueChange = { g -> g.toIntOrNull()?.let { num -> onUpdateSpec { it.copy(generation = num) } } },
                                label = { Text("Generación") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    StudioTab.CHIPS_CPU -> {
                        Text("Arquitectura del Procesador & Silicio (SoC)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dev.cpuName,
                                onValueChange = { v -> onUpdateSpec { it.copy(cpuName = v) } },
                                label = { Text("Nombre CPU / SoC") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = dev.cpuCores.toString(),
                                onValueChange = { v -> v.toIntOrNull()?.let { cores -> onUpdateSpec { it.copy(cpuCores = cores) } } },
                                label = { Text("Núcleos") },
                                modifier = Modifier.weight(0.6f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Text("Nodo Litográfico (Nanómetros): ${dev.processNodeNm} nm", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.processNodeNm.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(processNodeNm = it.toDouble()) } },
                            valueRange = 0.5f..14f,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )

                        Text("Memoria RAM Unificada: ${dev.ramGb} GB (${dev.ramType})", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.ramGb.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(ramGb = it.toInt()) } },
                            valueRange = 4f..128f,
                            steps = 15,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )

                        Text("Almacenamiento Interno: ${dev.storageGb} GB (${dev.storageType})", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.storageGb.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(storageGb = it.toInt()) } },
                            valueRange = 64f..2048f,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )
                    }

                    StudioTab.PANTALLA -> {
                        Text("Tecnología de Panel & Visualización", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                        DisplayTech.entries.forEach { tech ->
                            val isSelected = dev.displayTech == tech
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) GeoPrimaryContainer else GeoSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) GeoPrimary.copy(alpha = 0.4f) else CardBorderColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onUpdateSpec { it.copy(displayTech = tech) } },
                                    colors = RadioButtonDefaults.colors(selectedColor = GeoPrimary)
                                )
                                Column {
                                    Text(tech.displayName, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Claridad: ${tech.clarityScore}/10 • Eficiencia: ${tech.powerEfficiency}x", color = TextSecondary, fontSize = 10.sp)
                                }
                            }
                        }

                        Text("Tasa de Refresco: ${dev.displayRefreshHz} Hz", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.displayRefreshHz.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(displayRefreshHz = it.toInt()) } },
                            valueRange = 60f..240f,
                            steps = 5,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )
                    }

                    StudioTab.CAMARAS -> {
                        Text("Sistema Óptico & Sensores Computacionales", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Sensor Principal: ${dev.cameraMainMp} Megapíxeles", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.cameraMainMp.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(cameraMainMp = it.toInt()) } },
                            valueRange = 12f..300f,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )

                        Text("Zoom Óptico Periscópico: ${String.format(Locale.US, "%.1f", dev.cameraZoomOpticalX)}x", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.cameraZoomOpticalX.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(cameraZoomOpticalX = it.toDouble()) } },
                            valueRange = 1f..20f,
                            colors = SliderDefaults.colors(thumbColor = GeoPrimary, activeTrackColor = GeoPrimary, inactiveTrackColor = GeoSurfaceVariant)
                        )
                    }

                    StudioTab.BATERIA_REFRIG -> {
                        Text("Batería & Gestión Térmica", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Capacidad de Batería: ${dev.batteryMah} mAh (${dev.chargingWatts}W Carga Rápida)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Slider(
                            value = dev.batteryMah.toFloat(),
                            onValueChange = { onUpdateSpec { s -> s.copy(batteryMah = it.toInt()) } },
                            valueRange = 2000f..10000f,
                            colors = SliderDefaults.colors(thumbColor = GeoGreenReal, activeTrackColor = GeoGreenReal, inactiveTrackColor = GeoSurfaceVariant)
                        )

                        Text("Sistema de Refrigeración:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        CoolingType.entries.forEach { cooling ->
                            val isSelected = dev.cooling == cooling
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) GeoPrimaryContainer else GeoSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) GeoPrimary.copy(alpha = 0.4f) else CardBorderColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onUpdateSpec { it.copy(cooling = cooling) } },
                                    colors = RadioButtonDefaults.colors(selectedColor = GeoPrimary)
                                )
                                Column {
                                    Text(cooling.displayName, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Eficiencia: ${cooling.thermalEfficiency}/10 • Coste extra: $${cooling.cost}", color = TextSecondary, fontSize = 9.sp)
                                }
                            }
                        }
                    }

                    StudioTab.MATERIALES -> {
                        Text("Chasis & Metales Aeroespaciales", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        ChassisMaterial.entries.forEach { mat ->
                            val isSelected = dev.chassisMaterial == mat
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) GeoPrimaryContainer else GeoSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) GeoPrimary.copy(alpha = 0.4f) else CardBorderColor, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onUpdateSpec { it.copy(chassisMaterial = mat, bodyColorHex = mat.hexColor) } },
                                    colors = RadioButtonDefaults.colors(selectedColor = GeoPrimary)
                                )
                                Column {
                                    Text(mat.displayName, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Multiplicador Premium: ${mat.premiumFactor}x", color = TextSecondary, fontSize = 9.sp)
                                }
                            }
                        }
                    }

                    StudioTab.PRODUCCION_PRECIO -> {
                        Text("Economía de Escala & Lanzamiento Comercial", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                        Surface(
                            color = GeoSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Coste de Fabricación (BOM):", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text("$ " + String.format(Locale.US, "%,.2f", dev.bomCostUsd), color = GeoPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Margen Bruto por Unidad:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text("${dev.marginPercentage.toInt()}% ($" + String.format(Locale.US, "%,.2f", dev.profitPerUnit) + ")", color = GeoGreenReal, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                                }
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = retailPrice,
                                onValueChange = {
                                    retailPrice = it
                                    it.toDoubleOrNull()?.let { p -> onUpdateSpec { s -> s.copy(retailPriceUsd = p) } }
                                },
                                label = { Text("Precio Venta ($)") },
                                modifier = Modifier.weight(1f).testTag("retail_price_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = initialUnits,
                                onValueChange = { initialUnits = it },
                                label = { Text("Unidades Lote") },
                                modifier = Modifier.weight(1f).testTag("initial_units_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    focusedBorderColor = GeoPrimary,
                                    unfocusedBorderColor = CardBorderColor
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onBenchmark(dev.id) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                                modifier = Modifier.weight(1f).height(44.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Test Benchmark", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    val price = retailPrice.toDoubleOrNull() ?: 999.0
                                    val units = initialUnits.toLongOrNull() ?: 10000L
                                    onLaunchMarket(dev.id, price, units)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GeoPrimary),
                                modifier = Modifier.weight(1.5f).height(44.dp).testTag("launch_market_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lanzar al Mercado", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Global Save Prototype button
                OutlinedButton(
                    onClick = onSavePrototype,
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = GeoSurfaceVariant, contentColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth().height(42.dp).testTag("save_prototype_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Guardar Prototipo en Catálogo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // List of Existing Devices in Portfolio
        if (uiState.devices.isNotEmpty()) {
            Text("Catálogo de Dispositivos Desarrollados (${uiState.devices.size})", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.devices.forEach { d ->
                    DevicePortfolioItem(device = d)
                }
            }
        }
    }
}

@Composable
private fun DevicePortfolioItem(device: DeviceSpec) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = GeoSurface,
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(device.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("${device.category.displayName} • ${device.modelVariant}", color = TextSecondary, fontSize = 10.sp)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (device.stage) {
                        DeviceStage.EN_MERCADO -> GeoGreenReal.copy(alpha = 0.15f)
                        DeviceStage.FABRICACION -> GeoPrimaryContainer
                        else -> GeoSurfaceVariant
                    }
                ) {
                    Text(
                        text = device.stage.name,
                        color = when (device.stage) {
                            DeviceStage.EN_MERCADO -> GeoGreenReal
                            DeviceStage.FABRICACION -> GeoPrimary
                            else -> TextSecondary
                        },
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            HorizontalDivider(color = CardBorderColor, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Precio Venta", color = TextSecondary, fontSize = 9.sp)
                    Text("$ " + String.format(Locale.US, "%,.0f", device.retailPriceUsd), color = GeoPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                Column {
                    Text("Unidades Vendidas", color = TextSecondary, fontSize = 9.sp)
                    Text(String.format(Locale.US, "%,d", device.unitsSold), color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                Column {
                    Text("Ingresos Totales", color = TextSecondary, fontSize = 9.sp)
                    Text("$ " + String.format(Locale.US, "%,.0f", device.totalRevenueUsd), color = GeoGreenReal, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                }
                Column {
                    Text("Calidad", color = TextSecondary, fontSize = 9.sp)
                    Text(device.formattedScore(), color = GeoPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
