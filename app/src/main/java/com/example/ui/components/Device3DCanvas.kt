package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChassisMaterial
import com.example.model.DeviceCategory
import com.example.model.DeviceSpec
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Device3DCanvas(
    deviceSpec: DeviceSpec,
    rotationX: Float,
    rotationY: Float,
    zoomScale: Float,
    explodedProgress: Float,
    isXRayMode: Boolean,
    onRotate: (Float, Float) -> Unit,
    onZoom: (Float) -> Unit,
    onExplodedChange: (Float) -> Unit,
    onToggleXRay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(
                Brush.radialGradient(
                    listOf(Color(0xFFF8F9FF), Color(0xFFE2E7F3)),
                    radius = 700f
                ),
                RoundedCornerShape(24.dp)
            )
            .border(1.dp, CardBorderColor, RoundedCornerShape(24.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onRotate(dragAmount.y * 0.4f, dragAmount.x * 0.4f)
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    onZoom(zoom)
                }
            }
            .testTag("device_3d_canvas")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f

            // Geometric grid background
            drawCyberGrid(canvasWidth, canvasHeight, isXRayMode)

            // Draw clean shadow under device
            drawOval(
                brush = Brush.radialGradient(
                    listOf(Color(0x33001D36), Color.Transparent),
                    center = Offset(centerX, centerY + 130f * zoomScale),
                    radius = 120f * zoomScale
                ),
                topLeft = Offset(centerX - 100f * zoomScale, centerY + 110f * zoomScale),
                size = Size(200f * zoomScale, 40f * zoomScale)
            )

            // Render Device in 2.5D/3D perspective with exploded offset
            val baseWidth = when (deviceSpec.category) {
                DeviceCategory.SMARTPHONE -> 130f
                DeviceCategory.TABLET -> 190f
                DeviceCategory.LAPTOP -> 220f
                DeviceCategory.SMARTWATCH -> 80f
                DeviceCategory.CONSOLE -> 180f
                DeviceCategory.ROBOT -> 140f
                DeviceCategory.SERVER_BLADE -> 240f
                DeviceCategory.SMART_GLASSES -> 150f
                else -> 150f
            } * zoomScale

            val baseHeight = when (deviceSpec.category) {
                DeviceCategory.SMARTPHONE -> 240f
                DeviceCategory.TABLET -> 270f
                DeviceCategory.LAPTOP -> 170f
                DeviceCategory.SMARTWATCH -> 90f
                DeviceCategory.CONSOLE -> 210f
                DeviceCategory.ROBOT -> 260f
                DeviceCategory.SERVER_BLADE -> 90f
                DeviceCategory.SMART_GLASSES -> 70f
                else -> 220f
            } * zoomScale

            val rotAngle = rotationY * 0.6f
            val pitch = (rotationX / 90f).coerceIn(-0.4f, 0.4f)

            rotate(degrees = rotAngle, pivot = Offset(centerX, centerY)) {
                // If exploded view > 0, render internal layers first
                if (explodedProgress > 0.05f) {
                    drawExplodedInternals(
                        centerX = centerX,
                        centerY = centerY,
                        width = baseWidth,
                        height = baseHeight,
                        exploded = explodedProgress,
                        deviceSpec = deviceSpec,
                        isXRay = isXRayMode,
                        textMeasurer = textMeasurer
                    )
                }

                // Render Back Chassis or Main Enclosure
                val chassisColor = Color(deviceSpec.bodyColorHex)
                val frameColor = Color(deviceSpec.frameColorHex)

                val chassisOffset = if (explodedProgress > 0f) Offset(0f, -60f * explodedProgress) else Offset.Zero
                val chassisCenter = Offset(centerX + chassisOffset.x, centerY + chassisOffset.y)

                if (isXRayMode) {
                    // Holographic Wireframe Blueprint
                    drawRoundRect(
                        color = CyberCyanAccent,
                        topLeft = Offset(chassisCenter.x - baseWidth / 2, chassisCenter.y - baseHeight / 2),
                        size = Size(baseWidth, baseHeight),
                        cornerRadius = CornerRadius(22f * zoomScale, 22f * zoomScale),
                        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f)))
                    )
                    // Internal Chip Blueprint Box
                    drawRoundRect(
                        color = CyberGoldPrimary,
                        topLeft = Offset(chassisCenter.x - 30f * zoomScale, chassisCenter.y - 40f * zoomScale),
                        size = Size(60f * zoomScale, 60f * zoomScale),
                        cornerRadius = CornerRadius(6f, 6f),
                        style = Stroke(width = 1.5f)
                    )
                } else {
                    // Solid / Material Render with Specular Lighting
                    val gradientBrush = Brush.linearGradient(
                        colors = listOf(
                            chassisColor.copy(alpha = 0.95f),
                            frameColor,
                            chassisColor.copy(alpha = 0.8f)
                        ),
                        start = Offset(chassisCenter.x - baseWidth, chassisCenter.y - baseHeight),
                        end = Offset(chassisCenter.x + baseWidth, chassisCenter.y + baseHeight)
                    )

                    // Outer Frame Bezel
                    drawRoundRect(
                        brush = Brush.linearGradient(listOf(frameColor, frameColor.copy(alpha = 0.6f), frameColor)),
                        topLeft = Offset(chassisCenter.x - baseWidth / 2 - 3f, chassisCenter.y - baseHeight / 2 - 3f),
                        size = Size(baseWidth + 6f, baseHeight + 6f),
                        cornerRadius = CornerRadius(24f * zoomScale, 24f * zoomScale)
                    )

                    // Body
                    drawRoundRect(
                        brush = gradientBrush,
                        topLeft = Offset(chassisCenter.x - baseWidth / 2, chassisCenter.y - baseHeight / 2),
                        size = Size(baseWidth, baseHeight),
                        cornerRadius = CornerRadius(22f * zoomScale, 22f * zoomScale)
                    )

                    // Display Screen Layer (if front or rotated)
                    val screenMargin = 8f * zoomScale
                    val screenWidth = baseWidth - (screenMargin * 2)
                    val screenHeight = baseHeight - (screenMargin * 2)

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF030712), Color(0xFF0F172A), Color(0xFF020617))
                        ),
                        topLeft = Offset(chassisCenter.x - screenWidth / 2, chassisCenter.y - screenHeight / 2),
                        size = Size(screenWidth, screenHeight),
                        cornerRadius = CornerRadius(16f * zoomScale, 16f * zoomScale)
                    )

                    // Display Wallpaper Glow / Cyber Grid
                    drawRoundRect(
                        brush = Brush.radialGradient(
                            listOf(CyberCyanAccent.copy(alpha = 0.25f), Color.Transparent),
                            center = chassisCenter,
                            radius = screenWidth * 0.8f
                        ),
                        topLeft = Offset(chassisCenter.x - screenWidth / 2, chassisCenter.y - screenHeight / 2),
                        size = Size(screenWidth, screenHeight),
                        cornerRadius = CornerRadius(16f * zoomScale, 16f * zoomScale)
                    )

                    // Camera Notch / Punch-hole
                    drawCircle(
                        color = Color.Black,
                        radius = 4.5f * zoomScale,
                        center = Offset(chassisCenter.x, chassisCenter.y - screenHeight / 2 + 10f * zoomScale)
                    )

                    // Camera Module Bump on Rear / Top Corner
                    val camModuleWidth = 44f * zoomScale
                    val camModuleHeight = 44f * zoomScale
                    val camLeft = chassisCenter.x - baseWidth / 2 + 14f * zoomScale
                    val camTop = chassisCenter.y - baseHeight / 2 + 14f * zoomScale

                    drawRoundRect(
                        brush = Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))),
                        topLeft = Offset(camLeft, camTop),
                        size = Size(camModuleWidth, camModuleHeight),
                        cornerRadius = CornerRadius(10f * zoomScale, 10f * zoomScale),
                        style = Stroke(width = 1.5f)
                    )

                    // Camera Lenses
                    val lensRadius = 6.5f * zoomScale
                    drawCircle(
                        brush = Brush.radialGradient(listOf(CyberCyanAccent, Color.Black)),
                        radius = lensRadius,
                        center = Offset(camLeft + 12f * zoomScale, camTop + 12f * zoomScale)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(listOf(CyberGoldPrimary, Color.Black)),
                        radius = lensRadius,
                        center = Offset(camLeft + 12f * zoomScale, camTop + 32f * zoomScale)
                    )
                    if (deviceSpec.cameraSensorsCount >= 3) {
                        drawCircle(
                            brush = Brush.radialGradient(listOf(CyberPurpleSpeculative, Color.Black)),
                            radius = lensRadius,
                            center = Offset(camLeft + 32f * zoomScale, camTop + 22f * zoomScale)
                        )
                    }

                    // Apex Brand Logo Glow
                    if (deviceSpec.logoGlowEnabled) {
                        drawCircle(
                            brush = Brush.radialGradient(listOf(CyberGoldPrimary.copy(alpha = 0.6f), Color.Transparent)),
                            radius = 16f * zoomScale,
                            center = Offset(chassisCenter.x, chassisCenter.y + 40f * zoomScale)
                        )
                        drawCircle(
                            color = CyberGoldPrimary,
                            radius = 4f * zoomScale,
                            center = Offset(chassisCenter.x, chassisCenter.y + 40f * zoomScale)
                        )
                    }

                    // Side Hardware Buttons
                    drawRoundRect(
                        color = frameColor,
                        topLeft = Offset(chassisCenter.x + baseWidth / 2, chassisCenter.y - 20f * zoomScale),
                        size = Size(3f * zoomScale, 28f * zoomScale),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRoundRect(
                        color = frameColor,
                        topLeft = Offset(chassisCenter.x - baseWidth / 2 - 3f * zoomScale, chassisCenter.y - 35f * zoomScale),
                        size = Size(3f * zoomScale, 45f * zoomScale),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                }
            }
        }

        // Top Header inside Canvas: Live Score Badge & View Mode Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Pill tag & Category Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GeoPrimary,
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    Text(
                        text = "LABORATORIO 3D",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(
                    text = deviceSpec.name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Global Score Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GeoSurface,
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SCORE",
                        color = TextSecondary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = deviceSpec.formattedScore(),
                        color = GeoPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        // Bottom Controls inside Canvas: Rotate Hint, Exploded View Slider & X-Ray Button
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Exploded View Slider Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(GeoSurface, RoundedCornerShape(20.dp))
                    .border(1.dp, CardBorderColor, RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Layers,
                    contentDescription = null,
                    tint = if (explodedProgress > 0f) GeoPrimary else TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Text("Despiece:", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = explodedProgress,
                    onValueChange = onExplodedChange,
                    valueRange = 0f..1f,
                    modifier = Modifier.width(90.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = GeoPrimary,
                        activeTrackColor = GeoPrimary,
                        inactiveTrackColor = GeoSurfaceVariant
                    )
                )
            }

            // Right Action Buttons (Geometric circular action buttons)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Rotation Button
                Surface(
                    onClick = { onRotate(-rotationX + 15f, -rotationY - 25f) },
                    shape = CircleShape,
                    color = GeoSurface,
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.RotateRight,
                            contentDescription = "Reiniciar Rotación",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // X-Ray Mode Toggle Button
                Surface(
                    onClick = onToggleXRay,
                    shape = CircleShape,
                    color = if (isXRayMode) GeoPrimary else GeoSurface,
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(if (isXRayMode) GeoPrimary else CardBorderColor)
                    ),
                    modifier = Modifier
                        .size(38.dp)
                        .testTag("xray_mode_toggle_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Vista Rayos X",
                            tint = if (isXRayMode) Color.White else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawCyberGrid(width: Float, height: Float, isXRay: Boolean) {
    val step = 40f
    val lineColor = if (isXRay) GeoPrimary.copy(alpha = 0.2f) else CardBorderColor.copy(alpha = 0.6f)
    var x = 0f
    while (x < width) {
        drawLine(color = lineColor, start = Offset(x, 0f), end = Offset(x, height), strokeWidth = 0.8f)
        x += step
    }
    var y = 0f
    while (y < height) {
        drawLine(color = lineColor, start = Offset(0f, y), end = Offset(width, y), strokeWidth = 0.8f)
        y += step
    }
}

private fun DrawScope.drawExplodedInternals(
    centerX: Float,
    centerY: Float,
    width: Float,
    height: Float,
    exploded: Float,
    deviceSpec: DeviceSpec,
    isXRay: Boolean,
    textMeasurer: TextMeasurer
) {
    val explodedShift = 75f * exploded

    // 1. Motherboard PCB Layer (shifts downward)
    val pcbCenter = Offset(centerX, centerY + explodedShift * 0.6f)
    val pcbWidth = width * 0.82f
    val pcbHeight = height * 0.82f

    drawRoundRect(
        color = Color(0xFF064E3B), // Deep PCB Emerald
        topLeft = Offset(pcbCenter.x - pcbWidth / 2, pcbCenter.y - pcbHeight / 2),
        size = Size(pcbWidth, pcbHeight),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Gold Circuit Traces
    drawLine(
        color = CyberGoldPrimary.copy(alpha = 0.7f),
        start = Offset(pcbCenter.x - 30f, pcbCenter.y - 20f),
        end = Offset(pcbCenter.x + 30f, pcbCenter.y - 20f),
        strokeWidth = 1.5f
    )
    drawLine(
        color = CyberGoldPrimary.copy(alpha = 0.7f),
        start = Offset(pcbCenter.x - 20f, pcbCenter.y + 10f),
        end = Offset(pcbCenter.x + 20f, pcbCenter.y + 30f),
        strokeWidth = 1.5f
    )

    // Custom SoC Chip with glowing gold shield
    val socSize = 36f
    drawRoundRect(
        brush = Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))),
        topLeft = Offset(pcbCenter.x - socSize / 2, pcbCenter.y - 35f),
        size = Size(socSize, socSize),
        cornerRadius = CornerRadius(4f, 4f),
        style = Stroke(width = 1.5f)
    )
    drawCircle(
        color = CyberGoldPrimary,
        radius = 4f,
        center = Offset(pcbCenter.x, pcbCenter.y - 35f + socSize / 2)
    )

    // 2. High-Density Battery Cell (shifts slightly left)
    val batteryWidth = pcbWidth * 0.55f
    val batteryHeight = pcbHeight * 0.48f
    val batteryCenter = Offset(pcbCenter.x + 15f, pcbCenter.y + 25f)

    drawRoundRect(
        color = Color(0xFF0F172A),
        topLeft = Offset(batteryCenter.x - batteryWidth / 2, batteryCenter.y - batteryHeight / 2),
        size = Size(batteryWidth, batteryHeight),
        cornerRadius = CornerRadius(6f, 6f)
    )
    drawRoundRect(
        color = CyberCyanAccent.copy(alpha = 0.8f),
        topLeft = Offset(batteryCenter.x - batteryWidth / 2, batteryCenter.y - batteryHeight / 2),
        size = Size(batteryWidth, batteryHeight),
        cornerRadius = CornerRadius(6f, 6f),
        style = Stroke(width = 1.5f)
    )

    // 3. Copper Heatpipe / Vapor Chamber (shifts right)
    drawLine(
        color = Color(0xFFB45309), // Copper
        start = Offset(pcbCenter.x - 10f, pcbCenter.y - 45f),
        end = Offset(pcbCenter.x - 25f, pcbCenter.y + 40f),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )

    // Component Callout Labels (if exploded > 0.5f)
    if (exploded > 0.5f) {
        val style = TextStyle(color = CyberGoldPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        drawText(
            textMeasurer = textMeasurer,
            text = "${deviceSpec.cpuName} (${deviceSpec.processNodeNm}nm)",
            topLeft = Offset(pcbCenter.x + 25f, pcbCenter.y - 40f),
            style = style
        )
        drawText(
            textMeasurer = textMeasurer,
            text = "${deviceSpec.batteryMah}mAh ${deviceSpec.batteryType.take(12)}",
            topLeft = Offset(pcbCenter.x - 80f, pcbCenter.y + 40f),
            style = TextStyle(color = CyberCyanAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        )
    }
}
