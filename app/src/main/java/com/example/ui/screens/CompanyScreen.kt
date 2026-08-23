package com.example.ui.screens

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
import com.example.model.CompanyStage
import com.example.model.FundingRoundType
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun CompanyScreen(
    uiState: GameUiState,
    onTakeLoan: (Double) -> Unit,
    onRepayLoan: (Double) -> Unit,
    onIssueShares: (Long) -> Unit,
    onOpenSubsidiary: (String, String, String, String, Double) -> Unit,
    onHireEmployees: (String, Int) -> Unit,
    onRaiseFundingRound: (FundingRoundType) -> Unit,
    onSetDividend: (Double) -> Unit,
    onDrawEmergencyCredit: (Double) -> Unit,
    onRequestSubvention: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedSection by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card: Company Valuation & Corporate Identity
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF)))
            ),
            modifier = Modifier.fillMaxWidth().testTag("company_hero_card")
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                            Text(
                                text = uiState.company.name.take(2).uppercase(),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Column {
                            Text(
                                text = uiState.company.name,
                                color = Color(0xFF1B1B1F),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Fundador: ${uiState.company.founder} • Sede: ${uiState.company.hqCountry}",
                                color = Color(0xFF44474E),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF005FB0).copy(alpha = 0.1f)
                    ) {
                        Text(
                            uiState.company.stage.displayName,
                            color = Color(0xFF005FB0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Divider(color = Color(0xFFE2E2EC))

                // Valuation & Stock KPIs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Valoración Corporativa", color = Color(0xFF44474E), fontSize = 10.sp)
                        Text(
                            "$ " + String.format(Locale.US, "%,.0f", uiState.company.valuation),
                            color = Color(0xFF005FB0),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Column {
                        Text("Precio Acción", color = Color(0xFF44474E), fontSize = 10.sp)
                        Text(
                            "$ " + String.format(Locale.US, "%.2f", uiState.company.stockPrice),
                            color = Color(0xFF10B981),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Column {
                        Text("Reputación", color = Color(0xFF44474E), fontSize = 10.sp)
                        Text(
                            "${uiState.company.reputation.toInt()}/100",
                            color = Color(0xFF0284C7),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        // Section Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E2EC), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Filiales Globales", "Plantilla & RRHH", "Inversores & Bolsa", "Financiación SOS").forEachIndexed { index, label ->
                val isSelected = selectedSection == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF005FB0) else Color.Transparent)
                        .clickable { selectedSection = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        color = if (isSelected) Color.White else Color(0xFF44474E),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        when (selectedSection) {
            0 -> {
                // ==========================================
                // SECTION 0: FILIALES INTERNACIONALES
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Filiales Internacionales", color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("${uiState.company.subsidiaries.size} Sedes Operativas", color = Color(0xFF005FB0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    if (uiState.company.subsidiaries.isEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Aún no tienes filiales en el extranjero.", color = Color(0xFF44474E), fontSize = 12.sp)
                                Text("Abre sedes en centros tecnológicos para aumentar ingresos y optimizar impuestos.", color = Color(0xFF74777F), fontSize = 10.sp)
                            }
                        }
                    }

                    uiState.company.subsidiaries.forEach { sub ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 1.dp,
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(sub.name, color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("${sub.city} (${sub.country}) • ${sub.sector}", color = Color(0xFF44474E), fontSize = 11.sp)
                                    Text("Beneficio Fiscal: -${sub.taxBenefitPercentage}% en Impuesto Sociedades", color = Color(0xFF005FB0), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("+$${String.format(Locale.US, "%,.0f", sub.monthlyRevenue)}/mes", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("${sub.localEmployeesCount} Empleados", color = Color(0xFF44474E), fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    // Expansion opportunities
                    Text("Oportunidades de Expansión Global:", color = Color(0xFF44474E), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    listOf(
                        Triple("Apex Tokyo Hub", "Tokio", "Japón") to (180000.0 to "Robótica & Hardware"),
                        Triple("Apex London Capital", "Londres", "Reino Unido") to (220000.0 to "Fintech & Algoritmos"),
                        Triple("Apex Zurich Labs", "Zúrich", "Suiza") to (300000.0 to "I+D Cuántico & Optimización Fiscal"),
                        Triple("Apex Seoul Foundry", "Seúl", "Corea del Sur") to (250000.0 to "Semiconductores & Pantallas")
                    ).forEach { (info, costAndSector) ->
                        val (name, city, country) = info
                        val (cost, sector) = costAndSector
                        val alreadyOpen = uiState.company.subsidiaries.any { it.city == city }

                        if (!alreadyOpen) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                shadowElevation = 1.dp,
                                border = CardDefaults.outlinedCardBorder(),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("$name ($city)", color = Color(0xFF1B1B1F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text("$sector • Coste: $${String.format(Locale.US, "%,.0f", cost)}", color = Color(0xFF44474E), fontSize = 10.sp)
                                    }
                                    Button(
                                        onClick = { onOpenSubsidiary(name, city, country, sector, cost) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Fundar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // ==========================================
                // SECTION 1: PLANTILLA & CONTRATACIÓN
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Gestión de Equipos y Nóminas", color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    uiState.company.employeeGroups.forEach { group ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 1.dp,
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(group.roleName, color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        "${group.count} contratados • $${String.format(Locale.US, "%,.0f", group.averageSalaryUsd)}/mes c/u",
                                        color = Color(0xFF44474E),
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        "Multiplicador Productividad: x${group.productivityMultiplier}",
                                        color = Color(0xFF005FB0),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Button(
                                    onClick = { onHireEmployees(group.roleName, 2) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("+2 Talento", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // ==========================================
                // SECTION 2: INVERSORES & BOLSA
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Rondas de Financiación de Capital", color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    FundingRoundType.entries.forEach { round ->
                        val isDone = uiState.company.completedFundingRounds.contains(round)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDone) Color(0xFFF3F4F9) else Color.White,
                            shadowElevation = if (isDone) 0.dp else 1.dp,
                            border = CardDefaults.outlinedCardBorder(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(round.displayName, color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(round.description, color = Color(0xFF44474E), fontSize = 10.sp)
                                    Text("Captación: +$${String.format(Locale.US, "%,.0f", round.capitalRaised)} (${round.sharesIssuedPercent.toInt()}% acciones)", color = Color(0xFF005FB0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                if (isDone) {
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF10B981).copy(alpha = 0.2f)) {
                                        Text("Cerrada", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                    }
                                } else {
                                    Button(
                                        onClick = { onRaiseFundingRound(round) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Cerrar Ronda", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Dividend Yield Slider
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Política de Reparto de Dividendos:", color = Color(0xFF1B1B1F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("${uiState.company.dividendYieldPercentage.toInt()}% de Beneficios", color = Color(0xFF005FB0), fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                            Slider(
                                value = uiState.company.dividendYieldPercentage.toFloat(),
                                onValueChange = { onSetDividend(it.toDouble()) },
                                valueRange = 0f..40f
                            )
                            Text("Un mayor reparto incrementa la satisfacción de los inversores y la cotización de las acciones.", color = Color(0xFF74777F), fontSize = 10.sp)
                        }
                    }
                }
            }

            3 -> {
                // ==========================================
                // SECTION 3: FINANCIACIÓN DE EMERGENCIA (SOS)
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Mecanismos de Liquidez y Financiación Inmediata", color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    // Credit Line Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Línea de Crédito Bancaria", color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Disponible: $${String.format(Locale.US, "%,.0f", uiState.company.emergencyCreditLimit - uiState.financials.creditLineDrawn)}", color = Color(0xFF005FB0), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Text("Disposición de efectivo inmediata con 5% de comisión de apertura para evitar quiebra técnica.", color = Color(0xFF44474E), fontSize = 10.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { onDrawEmergencyCredit(100000.0) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Disponer $100.000", fontSize = 10.sp)
                                }
                                Button(
                                    onClick = { onDrawEmergencyCredit(250000.0) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Disponer $250.000", fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    // Government Subvention Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Subvención Pública de I+D", color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Fondo gubernamental no reembolsable para empresas de alta tecnología.", color = Color(0xFF44474E), fontSize = 10.sp)
                            }
                            Button(
                                onClick = onRequestSubvention,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Solicitar $150k", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
