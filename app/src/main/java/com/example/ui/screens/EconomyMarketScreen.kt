package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.CountryEconomy
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun EconomyMarketScreen(
    uiState: GameUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF)))
            ),
            modifier = Modifier.fillMaxWidth().testTag("economy_hero")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF))),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                }
                Column {
                    Text("Economía Global y Balances Financieros", color = Color(0xFF1B1B1F), fontSize = 15.sp, fontWeight = FontWeight.Black)
                    Text("Desglose Mensual / Anual y Tipos de Cambio Internacionales", color = Color(0xFF44474E), fontSize = 11.sp)
                }
            }
        }

        // Tab Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE2E2EC), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Estados Financieros (P&L)", "Mercados y Divisas Nacionales").forEachIndexed { index, label ->
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
                        label,
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
                // TAB 0: FINANCIAL STATEMENTS (P&L)
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Summary Banner
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Ingresos Mensuales", color = Color(0xFF44474E), fontSize = 10.sp)
                                Text(
                                    "+$ " + String.format(Locale.US, "%,.0f", uiState.financials.monthlyRevenue),
                                    color = Color(0xFF10B981),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Gastos Mensuales", color = Color(0xFF44474E), fontSize = 10.sp)
                                Text(
                                    "-$ " + String.format(Locale.US, "%,.0f", uiState.financials.monthlyExpenses),
                                    color = Color(0xFFB3261E),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Beneficio Neto", color = Color(0xFF44474E), fontSize = 10.sp)
                                val isProfitable = uiState.financials.monthlyProfit >= 0
                                Text(
                                    (if (isProfitable) "+$ " else "-$ ") + String.format(Locale.US, "%,.0f", Math.abs(uiState.financials.monthlyProfit)),
                                    color = if (isProfitable) Color(0xFF005FB0) else Color(0xFFB3261E),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    // Income Sources Breakdown Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Fuentes de Ingresos Operativos", color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            FinancialRow("Ventas de Hardware (Dispositivos)", uiState.financials.monthlyHardwareSales, Color(0xFF10B981))
                            FinancialRow("Licencias de Sistemas Operativos (ApexOS)", uiState.financials.monthlyOsLicensing, Color(0xFF10B981))
                            FinancialRow("Suscripciones de IA & Cloud Enterprise", uiState.financials.monthlyAiSubscriptions, Color(0xFF10B981))
                            FinancialRow("Royalties por Patentes Científicas", uiState.financials.monthlyPatentRoyalties, Color(0xFF10B981))
                            FinancialRow("Rendimiento de Filiales Internacionales", uiState.financials.monthlySubsidiariesIncome, Color(0xFF10B981))
                        }
                    }

                    // Expense Breakdown Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        border = CardDefaults.outlinedCardBorder(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Estructura de Costes y Gastos", color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)

                            FinancialRow("Nóminas y Salarios de Plantilla", uiState.financials.monthlySalaries, Color(0xFFB3261E))
                            FinancialRow("Laboratorios I+D & Centros de Computación", uiState.financials.monthlyResearchCost, Color(0xFFB3261E))
                            FinancialRow("Mantenimiento de GigaFabs y Fábricas", uiState.financials.monthlyFabMaintenance, Color(0xFFB3261E))
                            FinancialRow("Factura Energética & Data Centers", uiState.financials.monthlyEnergyCost, Color(0xFFB3261E))
                            FinancialRow("Logística y Envíos Globales", uiState.financials.monthlyLogisticsCost, Color(0xFFB3261E))
                            FinancialRow("Impuesto sobre Sociedades (Corporate Tax)", uiState.financials.monthlyCorporateTaxes, Color(0xFFB3261E))
                        }
                    }
                }
            }

            1 -> {
                // ==========================================
                // TAB 1: PAÍSES, DIVISAS Y MACROECONOMÍA
                // ==========================================
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Estructura Monetaria y Fiscal por País", color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Black)

                    uiState.countries.forEach { country ->
                        CountryEconomyCard(country = country)
                    }
                }
            }
        }
    }
}

@Composable
private fun FinancialRow(title: String, amount: Double, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color(0xFF44474E), fontSize = 11.sp)
        Text(
            "$ " + String.format(Locale.US, "%,.0f", amount),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CountryEconomyCard(country: CountryEconomy) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(country.flagEmoji, fontSize = 20.sp)
                    Text(country.name, color = Color(0xFF1B1B1F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF005FB0).copy(alpha = 0.1f)
                ) {
                    Text(
                        "${country.currencySymbol} ${country.currencyCode} (1 USD = ${country.exchangeRateToUSD} ${country.currencyCode})",
                        color = Color(0xFF005FB0),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Salario Medio", color = Color(0xFF44474E), fontSize = 9.sp)
                    Text("$" + String.format(Locale.US, "%,.0f", country.averageMonthlySalaryUsd), color = Color(0xFF1B1B1F), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Impuesto Sociedades", color = Color(0xFF44474E), fontSize = 9.sp)
                    Text("${(country.corporateTaxRate * 100).toInt()}%", color = Color(0xFFB3261E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Adopción Tech", color = Color(0xFF44474E), fontSize = 9.sp)
                    Text("${(country.techAdoptionIndex * 100).toInt()}%", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Población", color = Color(0xFF44474E), fontSize = 9.sp)
                    Text(String.format(Locale.US, "%,.0f M", country.population / 1000000.0), color = Color(0xFF44474E), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
