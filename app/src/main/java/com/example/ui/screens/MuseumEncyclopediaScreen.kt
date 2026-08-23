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
import com.example.model.EraCategory
import com.example.model.GameAchievement
import com.example.model.TimelineMilestone
import com.example.ui.components.EraCategoryBadge
import com.example.ui.theme.*
import com.example.viewmodel.GameUiState
import java.util.Locale

@Composable
fun MuseumEncyclopediaScreen(
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
        Text("Museo Tecnológico & Línea Temporal", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black)

        // Achievements Trophy Room
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DeepDarkSurface,
            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CyberGoldPrimary, CyberGoldPrimary.copy(alpha = 0.3f)))),
            modifier = Modifier.fillMaxWidth().testTag("achievements_card")
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = CyberGoldPrimary, modifier = Modifier.size(20.dp))
                    Text("Sala de Logros y Trofeos", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    uiState.achievements.forEach { ach ->
                        AchievementRow(ach = ach)
                    }
                }
            }
        }

        // Timeline Milestones
        Text("Hitos Históricos de la Empresa y la Civilización", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.companyMilestones.forEach { milestone ->
                MilestoneCard(milestone = milestone)
            }
        }
    }
}

@Composable
private fun AchievementRow(ach: GameAchievement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (ach.isUnlocked) CyberGoldPrimary.copy(alpha = 0.1f) else DeepDarkSurfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = if (ach.isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
            contentDescription = null,
            tint = if (ach.isUnlocked) CyberGoldPrimary else TextTertiary,
            modifier = Modifier.size(18.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(ach.title, color = if (ach.isUnlocked) TextPrimary else TextTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(ach.description, color = TextSecondary, fontSize = 9.sp)
        }
        if (ach.isUnlocked) {
            Text("+$ " + String.format(Locale.US, "%,.0f", ach.rewardMoney), color = CyberGreenReal, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun MilestoneCard(milestone: TimelineMilestone) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DeepDarkSurface,
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CardBorderColor, CardBorderColor))),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (milestone.year < 0) "${-milestone.year} a.C." else "${milestone.year}",
                        color = CyberGoldPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(milestone.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                EraCategoryBadge(category = milestone.category)
            }
            Text(milestone.description, color = TextSecondary, fontSize = 10.sp)
        }
    }
}
