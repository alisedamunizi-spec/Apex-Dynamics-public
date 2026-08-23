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
import com.example.model.ResearchBranch
import com.example.model.TechNode
import com.example.ui.components.TechClassificationBadge
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun ResearchScreen(
    uiState: GameUiState,
    onStartResearch: (String) -> Unit,
    onHireScientist: (String, String) -> Unit,
    onBuildLab: (String, String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedBranch by remember { mutableStateOf(ResearchBranch.INTELIGENCIA_ARTIFICIAL) }
    val filteredTechs = uiState.researchNodes.filter { it.branch == selectedBranch }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Research Points Hero
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(Color(0xFF005FB0), Color(0xFF0088FF)))
            ),
            modifier = Modifier.fillMaxWidth().testTag("research_points_card")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFF005FB0).copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null, tint = Color(0xFF005FB0), modifier = Modifier.size(26.dp))
                    }
                    Column {
                        Text("Puntos de I+D Acumulados", color = Color(0xFF44474E), fontSize = 11.sp)
                        Text(
                            text = String.format(Locale.US, "%,.0f pts", uiState.researchPointsTotal),
                            color = Color(0xFF005FB0),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    val totalRate = uiState.researchFacilities.sumOf { it.researchPointsPerSec } + 8.0
                    Text("Generación I+D", color = Color(0xFF44474E), fontSize = 10.sp)
                    Text("+$totalRate pts/s", color = Color(0xFF10B981), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Branch Selection Scrollable Row
        Text("Ramas de Investigación & Árbol Tecnológico:", color = Color(0xFF44474E), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        val branchScroll = rememberScrollState()
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(branchScroll),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ResearchBranch.entries.forEach { branch ->
                val isSelected = selectedBranch == branch
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedBranch = branch },
                    label = { Text(branch.displayName, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF005FB0),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Technologies Tree List
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            filteredTechs.forEach { tech ->
                TechCard(
                    tech = tech,
                    isActive = uiState.activeResearchId == tech.id,
                    onStart = { onStartResearch(tech.id) }
                )
            }
        }
    }
}

@Composable
private fun TechCard(
    tech: TechNode,
    isActive: Boolean,
    onStart: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (tech.isCompleted) Color(0xFFF3F4F9) else Color.White,
        shadowElevation = if (tech.isCompleted) 0.dp else 1.dp,
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(tech.name, color = Color(0xFF1B1B1F), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        if (tech.aiDomain != null) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF005FB0).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    tech.aiDomain.displayName,
                                    color = Color(0xFF005FB0),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text("Era: ${tech.era.displayName} • Nivel ${tech.level}/${tech.maxLevel}", color = Color(0xFF44474E), fontSize = 10.sp)
                }

                if (tech.isCompleted) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF10B981).copy(alpha = 0.2f)) {
                        Text("Completada", color = Color(0xFF10B981), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                } else if (isActive) {
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF005FB0).copy(alpha = 0.2f)) {
                        Text("En Progreso", color = Color(0xFF005FB0), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                } else {
                    Button(
                        onClick = onStart,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005FB0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Investigar (${tech.costPoints.toInt()} pts)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text(tech.description, color = Color(0xFF44474E), fontSize = 11.sp)

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF3F4F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Desbloquea: ${tech.unlocksFeatures}",
                    color = Color(0xFF005FB0),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
