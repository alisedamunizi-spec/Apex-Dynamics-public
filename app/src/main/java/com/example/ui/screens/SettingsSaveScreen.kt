package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameMode
import com.example.model.GraphicProfile
import com.example.model.HistoricalEra
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState

@Composable
fun SettingsSaveScreen(
    uiState: GameUiState,
    onSaveSlot: (Int, String) -> Unit,
    onLoadSlot: (Int) -> Unit,
    onJumpEra: (HistoricalEra) -> Unit,
    onSetGraphicProfile: (GraphicProfile) -> Unit,
    onSetGameMode: (GameMode) -> Unit,
    onUpdateCompany: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var editCompanyName by remember { mutableStateOf(uiState.company.name) }
    var editFounder by remember { mutableStateOf(uiState.company.founder) }
    var editHQ by remember { mutableStateOf(uiState.company.hqCountry) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ajustes, Partidas & Simulación", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black)

        // Save & Load Slots
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyberGoldPrimary, CyberCyanAccent))),
            modifier = Modifier.fillMaxWidth().testTag("save_slots_card")
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ranuras de Guardado Local (Room Database)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                listOf(
                    1 to "Ranura 1 - Principal",
                    2 to "Ranura 2 - Secundaria",
                    3 to "Ranura 3 - Experimental",
                    4 to "Auto-Guardado Rápido"
                ).forEach { (slotId, slotName) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DeepDarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(slotName, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilledTonalButton(
                                onClick = { onSaveSlot(slotId, slotName) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Guardar", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            FilledTonalButton(
                                onClick = { onLoadSlot(slotId) },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text("Cargar", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Jump to Historical or Future Era
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Salto Temporal a Era Histórica o Futura", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Selecciona una era para saltar la simulación:", color = TextSecondary, fontSize = 10.sp)

                val eraScroll = rememberScrollState()
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(eraScroll),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HistoricalEra.entries.forEach { era ->
                        val isCurrent = uiState.gameTime.currentEra == era
                        FilterChip(
                            selected = isCurrent,
                            onClick = { onJumpEra(era) },
                            label = { Text(era.displayName, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberGoldPrimary.copy(alpha = 0.25f),
                                selectedLabelColor = CyberGoldPrimary
                            )
                        )
                    }
                }
            }
        }

        // Graphics Profile
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Perfil Gráfico del Diseñador 3D / 2.5D", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    GraphicProfile.entries.forEach { prof ->
                        val isSelected = uiState.graphicProfile == prof
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSetGraphicProfile(prof) },
                            label = { Text(prof.name, fontSize = 10.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Edit Company Info
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Editar Identidad Corporativa", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = editCompanyName,
                    onValueChange = { editCompanyName = it },
                    label = { Text("Nombre de la Empresa") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = CyberGoldPrimary,
                        unfocusedBorderColor = CardBorderColor
                    )
                )

                OutlinedTextField(
                    value = editFounder,
                    onValueChange = { editFounder = it },
                    label = { Text("Nombre del Fundador / CEO") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = CyberCyanAccent,
                        unfocusedBorderColor = CardBorderColor
                    )
                )

                Button(
                    onClick = { onUpdateCompany(editCompanyName, editFounder, editHQ) },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberGoldPrimary),
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Guardar Cambios Corporativos", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
