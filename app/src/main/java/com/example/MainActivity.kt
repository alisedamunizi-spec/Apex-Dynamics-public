package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TopGameBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.GameViewModel

enum class NavigationScreen(val title: String, val icon: ImageVector) {
    HARDWARE("Hardware 3D", Icons.Default.Devices),
    SISTEMAS("Sistemas / Lab", Icons.Default.Terminal),
    I_MAS_D("I+D Ciencia", Icons.Default.Science),
    IA_STUDIO("IA Studio", Icons.Default.Psychology),
    EMPRESA("Empresa", Icons.Default.Business),
    FABRICAS("Fábricas", Icons.Default.PrecisionManufacturing),
    RANKINGS("Rankings", Icons.Default.Leaderboard),
    MERCADOS("Mercados", Icons.Default.Public),
    MUSEO("Museo", Icons.Default.Museum),
    AJUSTES("Ajustes", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TechnologyEmpireApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnologyEmpireApp(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf(NavigationScreen.HARDWARE) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(GeoBackground),
        topBar = {
            TopGameBar(
                uiState = uiState,
                onSpeedSelected = { viewModel.setGameSpeed(it) },
                onNotificationDismiss = { viewModel.clearNotification() },
                onSettingsClick = { currentScreen = NavigationScreen.AJUSTES }
            )
        },
        bottomBar = {
            // Geometric Balance bottom navigation bar
            Surface(
                color = GeoSurface,
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(CardBorderColor)),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                val navScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(navScrollState)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NavigationScreen.entries.forEach { screen ->
                        val isSelected = currentScreen == screen
                        NavigationItemPill(
                            screen = screen,
                            isSelected = isSelected,
                            onClick = { currentScreen = screen }
                        )
                    }
                }
            }
        },
        containerColor = GeoBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(GeoBackground)
        ) {
            when (currentScreen) {
                NavigationScreen.HARDWARE -> {
                    DeviceStudioScreen(
                        uiState = uiState,
                        onRotate = { dx, dy -> viewModel.updateDesignerRotation(dx, dy) },
                        onZoom = { dz -> viewModel.updateDesignerZoom(dz) },
                        onExplodedChange = { prog -> viewModel.setExplodedViewProgress(prog) },
                        onToggleXRay = { viewModel.toggleXRayMode() },
                        onUpdateSpec = { updater -> viewModel.updateActiveDeviceInDesigner(updater) },
                        onSavePrototype = { viewModel.saveDevicePrototype() },
                        onBenchmark = { devId -> viewModel.benchmarkDevice(devId) },
                        onLaunchMarket = { devId, price, units -> viewModel.launchDeviceToMarket(devId, price, units) }
                    )
                }
                NavigationScreen.SISTEMAS -> {
                    OSLabScreen(
                        uiState = uiState,
                        onCreateOS = { name, ver, cat, kernel, arch -> viewModel.createOperatingSystem(name, ver, cat, kernel, arch) },
                        onSelectOS = { os -> viewModel.selectActiveOS(os) },
                        onBoot = { viewModel.bootVirtualOS() },
                        onShutdown = { viewModel.shutdownVirtualOS() },
                        onRestart = { viewModel.restartVirtualOS() },
                        onLaunchApp = { app -> viewModel.launchVirtualApp(app) },
                        onTerminalCommand = { cmd -> viewModel.executeTerminalCommand(cmd) },
                        onRunBenchmark = { viewModel.runVirtualBenchmark() }
                    )
                }
                NavigationScreen.I_MAS_D -> {
                    ResearchScreen(
                        uiState = uiState,
                        onStartResearch = { nodeId -> viewModel.setActiveResearch(nodeId) },
                        onHireScientist = { name, spec -> viewModel.hireScientist(name, spec) },
                        onBuildLab = { name, type, cost -> viewModel.buildResearchFacility(name, type, cost) }
                    )
                }
                NavigationScreen.IA_STUDIO -> {
                    AIStudioScreen(
                        uiState = uiState,
                        onTrainAdvancedModel = { name, domain, arch, params, mods, acc, speed, size, cost, watts, quant, app ->
                            viewModel.createAndTrainAdvancedAIModel(
                                name, domain, arch, params, mods, acc, speed, size, cost, watts, quant, app
                            )
                        },
                        onAssignApplication = { modelId, app -> viewModel.assignAIModelApplication(modelId, app) }
                    )
                }
                NavigationScreen.EMPRESA -> {
                    CompanyScreen(
                        uiState = uiState,
                        onTakeLoan = { amount -> viewModel.takeLoan(amount) },
                        onRepayLoan = { amount -> viewModel.repayLoan(amount) },
                        onIssueShares = { shares -> viewModel.issueShares(shares) },
                        onOpenSubsidiary = { name, city, country, sector, cost ->
                            viewModel.openSubsidiary(name, city, country, sector, cost)
                        },
                        onHireEmployees = { role, count -> viewModel.hireEmployeesForRole(role, count) },
                        onRaiseFundingRound = { round -> viewModel.raiseFundingRound(round) },
                        onSetDividend = { yield -> viewModel.setDividendYield(yield) },
                        onDrawEmergencyCredit = { amount -> viewModel.drawEmergencyCredit(amount) },
                        onRequestSubvention = { viewModel.requestGovernmentSubvention() }
                    )
                }
                NavigationScreen.FABRICAS -> {
                    ProductionLogisticsScreen(
                        uiState = uiState,
                        onChangeEnergy = { energy -> viewModel.changeEnergySource(energy) },
                        onBuildFactory = { name, type, cost -> viewModel.buildFactory(name, type, cost) }
                    )
                }
                NavigationScreen.RANKINGS -> {
                    CompetitorsRankingsScreen(uiState = uiState)
                }
                NavigationScreen.MERCADOS -> {
                    EconomyMarketScreen(uiState = uiState)
                }
                NavigationScreen.MUSEO -> {
                    MuseumEncyclopediaScreen(uiState = uiState)
                }
                NavigationScreen.AJUSTES -> {
                    SettingsSaveScreen(
                        uiState = uiState,
                        onSaveSlot = { slot, name -> viewModel.saveGameToSlot(slot, name) },
                        onLoadSlot = { slot -> viewModel.loadGameFromSlot(slot) },
                        onJumpEra = { era -> viewModel.jumpToEra(era) },
                        onSetGraphicProfile = { prof -> viewModel.setGraphicProfile(prof) },
                        onSetGameMode = { mode -> viewModel.setGameMode(mode) },
                        onUpdateCompany = { name, founder, hq -> viewModel.updateCompanyName(name, founder, hq) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationItemPill(
    screen: NavigationScreen,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) GeoPrimary else GeoSurfaceVariant,
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) GeoPrimary else CardBorderColor)
        ),
        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title,
                tint = if (isSelected) Color.White else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = screen.title,
                color = if (isSelected) Color.White else TextPrimary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
