package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EnergySourceType
import com.example.model.Factory
import com.example.model.ResourceType
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun ProductionLogisticsScreen(
    uiState: GameUiState,
    onChangeEnergy: (EnergySourceType) -> Unit,
    onBuildFactory: (String, String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Raw Resources Inventory Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderColor, CardBorderColor))),
            modifier = Modifier.fillMaxWidth().testTag("resources_inventory_card")
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Inventario de Materias Primas y Recursos Estratégicos", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                val entriesList = uiState.resourcesInventory.entries.toList()
                entriesList.chunked(2).forEach { rowEntries ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowEntries.forEach { entry ->
                            val resource = entry.key
                            val amount = entry.value
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = DeepDarkSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(resource.displayName.take(16), color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        Text(resource.category, color = TextTertiary, fontSize = 8.sp)
                                    }
                                    Text(String.format(Locale.US, "%,d u.", amount), color = CyberCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Energy Grid Matrix
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyberGoldPrimary, CyberCyanAccent))),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Matriz Energética Industrial", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Surface(shape = RoundedCornerShape(6.dp), color = CyberGoldPrimary.copy(alpha = 0.2f)) {
                        Text(
                            text = "${uiState.activeEnergySource.powerOutputMw} MW",
                            color = CyberGoldPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text("Fuente Activa: ${uiState.activeEnergySource.displayName}", color = CyberCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                val energyScroll = rememberScrollState()
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(energyScroll),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    EnergySourceType.entries.forEach { energy ->
                        val isSelected = uiState.activeEnergySource == energy
                        FilterChip(
                            selected = isSelected,
                            onClick = { onChangeEnergy(energy) },
                            label = { Text(energy.displayName, fontSize = 10.sp) }
                        )
                    }
                }
            }
        }

        // Semiconductor Fabs & Factories
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Plantas de Fabricación y Fundiciones de Silicio (${uiState.factories.size})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    uiState.factories.forEach { fab ->
                        FactoryCard(fab = fab)
                    }
                }

                Button(
                    onClick = { onBuildFactory("GigaFab 2 Super-Semiconductors", "Planta Automatizada 2nm GAA", 250000.0) },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGoldPrimary),
                    modifier = Modifier.fillMaxWidth().height(38.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Construir Nueva GigaFab ($250,000)", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        // Logistics & Global Transport Fleet
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Flota Logística y Transporte Interplanetario", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Camiones Autónomos", color = TextSecondary, fontSize = 10.sp)
                        Text("${uiState.logisticsFleet.trucksCount} unidades", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Trenes Maglev", color = TextSecondary, fontSize = 10.sp)
                        Text("${uiState.logisticsFleet.maglevTrainsCount} líneas", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Drones Autónomos", color = TextSecondary, fontSize = 10.sp)
                        Text("${uiState.logisticsFleet.autonomousDronesCount} enjambres", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FactoryCard(fab: Factory) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = DeepDarkSurfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(fab.name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("${fab.factoryType} • ${fab.countryLocation}", color = TextSecondary, fontSize = 10.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Capacidad: ${String.format(Locale.US, "%,d", fab.unitsCapacityPerMonth)} u/mes", color = CyberCyanAccent, fontSize = 10.sp)
                Text("Robots: ${fab.robotsCount} | Operarios: ${fab.workersCount}", color = TextTertiary, fontSize = 10.sp)
            }
        }
    }
}
