package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.model.CompetitorCompany
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun CompetitorsRankingsScreen(
    uiState: GameUiState,
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
        Text(
            text = "Ranking Global de Corporaciones Tecnológicas",
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
        )

        // Player Company Ranking Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurfaceVariant,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyberGoldPrimary, CyberCyanAccent))),
            modifier = Modifier.fillMaxWidth().testTag("player_company_ranking_card")
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier.size(28.dp).background(CyberGoldPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("TÚ", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                        Text(uiState.company.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
                    Text(
                        text = "$" + String.format(Locale.US, "%,.0f M", uiState.company.valuation / 1000000.0),
                        color = CyberGoldPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Text("Etapa: ${uiState.company.stage.displayName} • ${uiState.devices.size} productos lanzados", color = TextSecondary, fontSize = 10.sp)
            }
        }

        // Competitors List (Real Historical Giants vs AI Procedural)
        Text("Competidores del Mercado Mundial", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.competitors.forEachIndexed { index, comp ->
                CompetitorCard(index = index, comp = comp)
            }
        }
    }
}

@Composable
private fun CompetitorCard(index: Int, comp: CompetitorCompany) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DeepDarkSurface,
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderColor, CardBorderColor))),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("#${index + 1}", color = CyberCyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Black)
                    Column {
                        Text(comp.name, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${comp.country} • Fundada en ${comp.foundingYear}", color = TextTertiary, fontSize = 9.sp)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (comp.isRealHistorical) CyberGreenReal.copy(alpha = 0.2f) else CyberPurpleSpeculative.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (comp.isRealHistorical) "HISTÓRICA REAL" else "COMPETIDOR IA",
                        color = if (comp.isRealHistorical) CyberGreenReal else CyberPurpleSpeculative,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text("Producto Estrella: ${comp.flagshipProduct}", color = TextSecondary, fontSize = 10.sp)

            Divider(color = CardBorderColor.copy(alpha = 0.5f), thickness = 0.6.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Valoración", color = TextSecondary, fontSize = 9.sp)
                    Text("$ " + String.format(Locale.US, "%,.0f M", comp.valuationMillions), color = CyberGoldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Cuota Mercado", color = TextSecondary, fontSize = 9.sp)
                    Text("${comp.marketSharePercent}%", color = CyberCyanAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Innovación", color = TextSecondary, fontSize = 9.sp)
                    Text("${comp.innovationScore}/100", color = CyberGreenReal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Patentes", color = TextSecondary, fontSize = 9.sp)
                    Text(String.format(Locale.US, "%,d", comp.patentsCount), color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
