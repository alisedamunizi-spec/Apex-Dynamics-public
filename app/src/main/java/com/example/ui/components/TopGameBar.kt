package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameSpeed
import com.example.viewmodel.GameUiState
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopGameBar(
    uiState: GameUiState,
    onSpeedSelected: (GameSpeed) -> Unit,
    onNotificationDismiss: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSpeedDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GeoSurface)
            .statusBarsPadding()
            .border(width = 1.dp, color = CardBorderColor)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        // Upper row: Company branding & Financial balance
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Company Monogram & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GeoPrimary,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val monogram = uiState.company.name.split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString("")
                            .ifEmpty { "NX" }
                        Text(
                            text = monogram.uppercase(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Column {
                    Text(
                        text = uiState.company.name.uppercase(),
                        color = GeoPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "HQ: ${uiState.company.hqCountry}",
                        color = TextSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Financial Balance & Net Growth
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(GeoGreenReal, CircleShape)
                    )
                    Text(
                        text = "$ " + String.format(Locale.US, "%,.2f", uiState.financials.cash),
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                val isProfitPositive = uiState.financials.profitPerSec >= 0
                val profitColor = if (isProfitPositive) GeoGreenReal else GeoRedAlert
                Text(
                    text = (if (isProfitPositive) "+" else "") + "$ " + String.format(Locale.US, "%,.2f/s", uiState.financials.profitPerSec),
                    color = profitColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Geometric Lower Bar: Era, Simulated Time, Speed Capsule & Settings
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = GeoSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Era Display
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "ERA TECNOLÓGICA",
                        color = TextSecondary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = uiState.gameTime.currentEra.displayName,
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width(1.dp)
                        .background(CardBorderColor)
                )

                // Simulated Time
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TIEMPO SIMULADO",
                        color = TextSecondary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = uiState.gameTime.formatFullDateTime(),
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Speed Pill & Settings
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = { showSpeedDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        color = GeoSurface,
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                        modifier = Modifier.testTag("speed_selector_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = uiState.gameTime.speed.label,
                                color = TextPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        if (uiState.gameTime.speed == GameSpeed.PAUSE) GeoRedAlert else GeoPrimary,
                                        CircleShape
                                    )
                            )
                        }
                    }

                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("game_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Notification Banner
        AnimatedVisibility(
            visible = uiState.currentNotification != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            uiState.currentNotification?.let { notif ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GeoPrimaryContainer,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(GeoPrimary.copy(alpha = 0.4f))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = GeoPrimary, modifier = Modifier.size(16.dp))
                            Text(text = notif, color = GeoOnPrimaryContainer, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                        IconButton(onClick = onNotificationDismiss, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }

    // Speed Selector Modal Dialog
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = {
                Text("Velocidad de Simulación Temporal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                ) {
                    Text(
                        "Selecciona la aceleración del tiempo histórico y futuro:",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    GameSpeed.entries.chunked(3).forEach { rowSpeeds ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            rowSpeeds.forEach { speed ->
                                val isSelected = uiState.gameTime.speed == speed
                                OutlinedButton(
                                    onClick = {
                                        onSpeedSelected(speed)
                                        showSpeedDialog = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) GeoPrimary else GeoSurface,
                                        contentColor = if (isSelected) Color.White else TextPrimary
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) GeoPrimary else CardBorderColor)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = speed.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("Cerrar", color = GeoPrimary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = GeoSurface,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp
        )
    }
}

