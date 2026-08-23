package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.model.AIApplicationType
import com.example.model.AIDomain
import com.example.model.AIModel
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIStudioScreen(
    uiState: GameUiState,
    onTrainAdvancedModel: (
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
    ) -> Unit,
    onAssignApplication: (String, AIApplicationType) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(0) }

    // Training Form State
    var modelName by remember { mutableStateOf("Apex OmniCore 6") }
    var selectedDomain by remember { mutableStateOf(AIDomain.NLP) }
    var selectedArch by remember { mutableStateOf("Transformer Multimodal MoE") }
    var paramCountBillions by remember { mutableDoubleStateOf(70.0) }
    var accuracyTarget by remember { mutableDoubleStateOf(97.5) }
    var inferenceSpeed by remember { mutableIntStateOf(160) }
    var quantizationType by remember { mutableStateOf("FP8 Optimizada") }
    var selectedApplication by remember { mutableStateOf(AIApplicationType.OPTIMIZACION_PRODUCCION) }
    val modalities = remember { mutableStateListOf("Texto", "Visión", "Código", "Razonamiento") }

    // Derived dynamic costs & stats
    val trainingCost = paramCountBillions * 1200.0 * (accuracyTarget / 90.0)
    val modelSizeGb = (paramCountBillions * 0.5) * (if (quantizationType.contains("INT4")) 0.5 else if (quantizationType.contains("FP8")) 0.8 else 1.0)
    val energyTdpWatts = (paramCountBillions * 4.5).coerceIn(120.0, 3500.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Geometric Balance Hero Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF)))
            ),
            modifier = Modifier.fillMaxWidth().testTag("ai_studio_hero")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = "AI Supercomputing",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Apex AI Research & Supercomputing",
                        color = Color(0xFF1B1B1F),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        "Entrenamiento de Modelos Fundacionales y Despliegue Empresarial",
                        color = Color(0xFF44474E),
                        fontSize = 12.sp
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF005FB0).copy(alpha = 0.1f)
                ) {
                    Text(
                        "${uiState.aiModels.size} Modelos Activos",
                        color = Color(0xFF005FB0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Tab Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E2EC), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(
                "Diseñar & Entrenar",
                "Flota de IA & Aplicaciones",
                "Árbol de Dominios IA"
            ).forEachIndexed { index, title ->
                val isSelected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF005FB0) else Color.Transparent)
                        .clickable { selectedTab = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color(0xFF44474E),
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // ==========================================
                // TAB 0: CREAR Y ENTRENAR MODELO DE IA
                // ==========================================
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            "Configuración del Modelo de IA",
                            color = Color(0xFF1B1B1F),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Model Name Field
                        OutlinedTextField(
                            value = modelName,
                            onValueChange = { modelName = it },
                            label = { Text("Nombre del Modelo Personalizado") },
                            modifier = Modifier.fillMaxWidth().testTag("ai_model_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF1B1B1F),
                                unfocusedTextColor = Color(0xFF1B1B1F),
                                focusedBorderColor = Color(0xFF005FB0),
                                unfocusedBorderColor = Color(0xFFC4C6D0)
                            )
                        )

                        // AI Domain Selection (14 Domains)
                        Text(
                            "Dominio Científico de IA (14 Ramas Disponibles):",
                            color = Color(0xFF44474E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(AIDomain.entries.toList()) { domain ->
                                val isSelected = selectedDomain == domain
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFF005FB0) else Color(0xFFF3F4F9),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF005FB0) else Color(0xFFC4C6D0)
                                    ),
                                    modifier = Modifier.clickable { selectedDomain = domain }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            domain.displayName,
                                            color = if (isSelected) Color.White else Color(0xFF1B1B1F),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Architecture Selection
                        Text(
                            "Arquitectura Neuronal:",
                            color = Color(0xFF44474E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        listOf(
                            "Transformer Multimodal MoE (Mixture of Experts)",
                            "Mamba State Space (Inferencia Lineal O(N))",
                            "Liquid Neural Network (Dinámica Continua)",
                            "Vision-Language-Action (VLA para Robótica)",
                            "Spiking Neuromórfica Cuántica"
                        ).forEach { arch ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedArch == arch) Color(0xFF005FB0).copy(alpha = 0.08f)
                                        else Color(0xFFF3F4F9)
                                    )
                                    .clickable { selectedArch = arch }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedArch == arch,
                                    onClick = { selectedArch = arch },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF005FB0))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    arch,
                                    color = Color(0xFF1B1B1F),
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedArch == arch) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        // Parameter Slider (1B to 500B)
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Capacidad del Modelo (Parámetros):",
                                    color = Color(0xFF44474E),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "${paramCountBillions.toInt()} Billones (B)",
                                    color = Color(0xFF005FB0),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Slider(
                                value = paramCountBillions.toFloat(),
                                onValueChange = { paramCountBillions = it.toDouble() },
                                valueRange = 1f..500f,
                                steps = 49,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF005FB0),
                                    activeTrackColor = Color(0xFF005FB0)
                                ),
                                modifier = Modifier.testTag("ai_param_slider")
                            )
                        }

                        // Accuracy Target Slider
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Precisión / Benchmark Esperado:",
                                    color = Color(0xFF44474E),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    String.format(Locale.US, "%.1f%%", accuracyTarget),
                                    color = Color(0xFF0284C7),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Slider(
                                value = accuracyTarget.toFloat(),
                                onValueChange = { accuracyTarget = it.toDouble() },
                                valueRange = 85f..99.8f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF0284C7),
                                    activeTrackColor = Color(0xFF0284C7)
                                )
                            )
                        }

                        // Inference Speed & Quantization
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Velocidad de Inferencia:",
                                    color = Color(0xFF44474E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "$inferenceSpeed tokens/s",
                                    color = Color(0xFF1B1B1F),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Slider(
                                    value = inferenceSpeed.toFloat(),
                                    onValueChange = { inferenceSpeed = it.toInt() },
                                    valueRange = 50f..400f
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Cuantización de Memoria:",
                                    color = Color(0xFF44474E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                listOf("FP8 Optimizada", "INT4 Edge", "FP16 Full").forEach { q ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { quantizationType = q }
                                    ) {
                                        RadioButton(
                                            selected = quantizationType == q,
                                            onClick = { quantizationType = q },
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(q, color = Color(0xFF1B1B1F), fontSize = 11.sp)
                                    }
                                }
                            }
                        }

                        // Practical Application Picker
                        Text(
                            "Aplicación Práctica en la Empresa:",
                            color = Color(0xFF44474E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        AIApplicationType.entries.filter { it != AIApplicationType.NINGUNA }.forEach { appType ->
                            val isSelected = selectedApplication == appType
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF005FB0).copy(alpha = 0.08f) else Color(0xFFF3F4F9),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF005FB0) else Color(0xFFE2E2EC)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedApplication = appType }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedApplication = appType },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF005FB0))
                                    )
                                    Column {
                                        Text(
                                            appType.displayName,
                                            color = Color(0xFF1B1B1F),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            appType.perkSummary,
                                            color = Color(0xFF005FB0),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }

                        // Live Supercomputing Cost Summary Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF3F4F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Coste de Cómputo (Cluster GPUs):", color = Color(0xFF44474E), fontSize = 11.sp)
                                    Text(
                                        "$ ${String.format(Locale.US, "%,.0f", trainingCost)}",
                                        color = Color(0xFFB3261E),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Tamaño en Memoria VRAM:", color = Color(0xFF44474E), fontSize = 11.sp)
                                    Text(
                                        String.format(Locale.US, "%.1f GB", modelSizeGb),
                                        color = Color(0xFF1B1B1F),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Consumo Energético TDP:", color = Color(0xFF44474E), fontSize = 11.sp)
                                    Text(
                                        "${energyTdpWatts.toInt()} Watts",
                                        color = Color(0xFF1B1B1F),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Action Button to Train Model
                        Button(
                            onClick = {
                                onTrainAdvancedModel(
                                    modelName,
                                    selectedDomain,
                                    selectedArch,
                                    paramCountBillions,
                                    modalities.toList(),
                                    accuracyTarget,
                                    inferenceSpeed,
                                    modelSizeGb,
                                    trainingCost,
                                    energyTdpWatts,
                                    quantizationType,
                                    selectedApplication
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("train_ai_submit_button")
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Entrenar y Desplegar Modelo ($${String.format(Locale.US, "%,.0f", trainingCost)})",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            1 -> {
                // ==========================================
                // TAB 1: FLOTA DE MODELOS & APLICACIONES PRÁCTICAS
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Modelos de IA Entrenados y Desplegados",
                        color = Color(0xFF1B1B1F),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )

                    if (uiState.aiModels.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF44474E), modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No hay modelos de IA entrenados aún.", color = Color(0xFF44474E), fontSize = 13.sp)
                                Text("Crea tu primer modelo en la pestaña de Diseñar & Entrenar.", color = Color(0xFF74777F), fontSize = 11.sp)
                            }
                        }
                    }

                    uiState.aiModels.forEach { model ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            shadowElevation = 1.dp,
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(Color(0xFF005FB0).copy(alpha = 0.1f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.SmartToy,
                                                contentDescription = null,
                                                tint = Color(0xFF005FB0),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(model.name, color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                            Text("${model.domain.displayName} • ${model.architecture}", color = Color(0xFF44474E), fontSize = 10.sp)
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF0088FF).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            "${model.parameterCountBillions.toInt()}B Params",
                                            color = Color(0xFF005FB0),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                // Key Metrics Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Precisión", color = Color(0xFF44474E), fontSize = 9.sp)
                                        Text("${String.format(Locale.US, "%.1f", model.accuracyScore)}%", color = Color(0xFF0284C7), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text("Inferencia", color = Color(0xFF44474E), fontSize = 9.sp)
                                        Text("${model.inferenceSpeedTokensPerSec} t/s", color = Color(0xFF1B1B1F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text("VRAM", color = Color(0xFF44474E), fontSize = 9.sp)
                                        Text("${String.format(Locale.US, "%.1f", model.modelSizeGb)} GB", color = Color(0xFF1B1B1F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Column {
                                        Text("Valoración", color = Color(0xFF44474E), fontSize = 9.sp)
                                        Text("$ " + String.format(Locale.US, "%,.0f", model.commercialValuation), color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Practical Application Assignment Picker
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        "Asignación Práctica Activa:",
                                        color = Color(0xFF44474E),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF005FB0).copy(alpha = 0.08f),
                                        border = BorderStroke(1.dp, Color(0xFF005FB0).copy(alpha = 0.2f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                model.practicalApplication.displayName,
                                                color = Color(0xFF005FB0),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                model.practicalApplication.perkSummary,
                                                color = Color(0xFF44474E),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }

                                    // Quick Switcher Row
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        items(AIApplicationType.entries.filter { it != AIApplicationType.NINGUNA }.toList()) { app ->
                                            val isAssigned = model.practicalApplication == app
                                            FilterChip(
                                                selected = isAssigned,
                                                onClick = { onAssignApplication(model.id, app) },
                                                label = { Text(app.displayName, fontSize = 9.sp) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // ==========================================
                // TAB 2: ÁRBOL DE DOMINIOS Y RAMAS DE IA
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Árbol de Dominios de Inteligencia Artificial (14 Especialidades)",
                        color = Color(0xFF1B1B1F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )

                    AIDomain.entries.forEach { domain ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 1.dp,
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0xFF005FB0).copy(alpha = 0.1f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Memory,
                                        contentDescription = null,
                                        tint = Color(0xFF005FB0),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        domain.displayName,
                                        color = Color(0xFF1B1B1F),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        domain.description,
                                        color = Color(0xFF44474E),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
