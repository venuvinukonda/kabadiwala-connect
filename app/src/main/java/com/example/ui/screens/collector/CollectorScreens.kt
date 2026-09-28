package com.example.ui.screens.collector

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.service.localization.LocalizationManager
import com.example.service.safety.SafetyCatalog
import com.example.ui.components.*
import com.example.ui.screens.common.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CollectorDashboardScreen(
    viewModel: KabadiwalaViewModel,
    onCollectClick: () -> Unit,
    onMyLotsClick: () -> Unit,
    onPickupsClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onPriceHistoryClick: () -> Unit,
    onSafetyClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val profile by viewModel.currentCollectorProfile.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val pendingSyncCount by viewModel.pendingSyncCount.collectAsState()
    var showAudioHelpDialog by remember { mutableStateOf(false) }

    val isApproved = currentUser?.status == UserStatus.APPROVED.name
    val collectorName = profile?.fullName ?: currentUser?.username ?: "Mitra"

    val welcomeVoice = LocalizationManager.getCollectorWelcomeVoice(collectorName, currentLang)

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("app_name"),
                onLanguageClick = onLanguageClick,
                currentLang = currentLang,
                onLogoutClick = onLogoutClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            OfflineNotificationBar(
                isOnline = isOnline,
                pendingSyncCount = pendingSyncCount,
                onToggleOnline = { viewModel.toggleOnlineSimulation() },
                onSyncNow = { viewModel.triggerSync() }
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Header User Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = LocalizationManager.getString("good_day"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = LocalizationManager.getWelcomeGreeting(collectorName, currentLang),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    StatusChip(status = currentUser?.status ?: "APPROVED")
                }

                Spacer(modifier = Modifier.height(12.dp))

                ScreenVoiceBanner(
                    title = LocalizationManager.getString("voice_assistance"),
                    narration = welcomeVoice,
                    onSpeak = { viewModel.speak(it) },
                    onStop = { viewModel.stopAudio() },
                    isSpeaking = isSpeaking,
                    speechRate = speechRate,
                    onRateChange = { viewModel.setSpeechRate(it) },
                    onReplay = { viewModel.replayAudio() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (!isApproved) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WarningAmber.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = WarningAmber)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = LocalizationManager.getString("pending_approval_notice"),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Giant Hero Action: COLLECT E-WASTE (as per design spec in Section 29)
                Card(
                    onClick = {
                        if (isApproved) {
                            viewModel.startLotCreation()
                            onCollectClick()
                        } else {
                            viewModel.showMessage(LocalizationManager.getString("account_pending_msg"))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(115.dp)
                        .testTag("collect_ewaste_hero_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Recycling,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = LocalizationManager.getString("collect_ewaste"),
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = LocalizationManager.getString("collect_ewaste_desc"),
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid of Main Action Cards (Section 5 & 29)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CollectorActionTile(
                        title = LocalizationManager.getString("my_lots"),
                        icon = Icons.Default.Inventory2,
                        containerColor = PureWhiteSurface,
                        contentColor = DeepGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onMyLotsClick,
                        testTag = "tile_my_lots"
                    )
                    CollectorActionTile(
                        title = LocalizationManager.getString("earnings"),
                        icon = Icons.Default.AccountBalanceWallet,
                        containerColor = PureWhiteSurface,
                        contentColor = WarmOrangeSecondary,
                        modifier = Modifier.weight(1f),
                        onClick = onEarningsClick,
                        testTag = "tile_earnings"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CollectorActionTile(
                        title = LocalizationManager.getString("pickup_requests"),
                        icon = Icons.Default.LocalShipping,
                        containerColor = PureWhiteSurface,
                        contentColor = TechBlueTertiary,
                        modifier = Modifier.weight(1f),
                        onClick = onPickupsClick,
                        testTag = "tile_pickups"
                    )
                    CollectorActionTile(
                        title = LocalizationManager.getString("transaction_history"),
                        icon = Icons.Default.ReceiptLong,
                        containerColor = PureWhiteSurface,
                        contentColor = DeepGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onHistoryClick,
                        testTag = "tile_history"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CollectorActionTile(
                        title = LocalizationManager.getString("safety_instructions"),
                        icon = Icons.Default.WarningAmber,
                        containerColor = PureWhiteSurface,
                        contentColor = Color(0xFFC04B00),
                        modifier = Modifier.weight(1f),
                        onClick = onSafetyClick,
                        testTag = "tile_safety"
                    )
                    CollectorActionTile(
                        title = LocalizationManager.getString("price_history"),
                        icon = Icons.Default.TrendingUp,
                        containerColor = PureWhiteSurface,
                        contentColor = DeepGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onPriceHistoryClick,
                        testTag = "tile_price_history"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CollectorActionTile(
                        title = LocalizationManager.getString("audio_help"),
                        icon = Icons.Default.Headphones,
                        containerColor = PureWhiteSurface,
                        contentColor = TechBlueTertiary,
                        modifier = Modifier.weight(1f),
                        onClick = { showAudioHelpDialog = true },
                        testTag = "tile_audio_help"
                    )
                    CollectorActionTile(
                        title = LocalizationManager.getString("language"),
                        icon = Icons.Default.Translate,
                        containerColor = PureWhiteSurface,
                        contentColor = DeepGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onLanguageClick,
                        testTag = "tile_language"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    CollectorActionTile(
                        title = LocalizationManager.getString("profile"),
                        icon = Icons.Default.Person,
                        containerColor = PureWhiteSurface,
                        contentColor = DeepGreenPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onProfileClick,
                        testTag = "tile_profile"
                    )
                }
            }
        }

        if (showAudioHelpDialog) {
            AudioHelpDialog(
                viewModel = viewModel,
                onDismiss = { showAudioHelpDialog = false }
            )
        }
    }
}

@Composable
fun CollectorActionTile(
    title: String,
    icon: ImageVector,
    containerColor: Color = PureWhiteSurface,
    contentColor: Color = DeepGreenPrimary,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(96.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGreenOutlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = contentColor,
                lineHeight = 18.sp
            )
        }
    }
}

// =========================================================================
// COLLECT E-WASTE GUIDED WIZARD
// =========================================================================

@Composable
fun CollectEWasteWizardScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onFinish: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val state by viewModel.lotCreationState.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val availableCategories = listOf(
        "Laptops", "Mobile Phones", "Computers", "Circuit Boards",
        "Batteries", "Cables", "Televisions", "Printers",
        "Refrigerators", "Washing Machines", "Chargers", "Other Electronics"
    )

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "${LocalizationManager.getString("collect_ewaste")} (${LocalizationManager.getString("step")} ${state.step}/6)",
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Step Progress Bar
            LinearProgressIndicator(
                progress = { state.step / 6f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (state.step) {
                1 -> {
                    // STEP 1: CREATE NEW MATERIAL LOT
                    Text(
                        text = LocalizationManager.getString("wizard_step1_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = LocalizationManager.getString("wizard_step1_desc"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ListenButton(
                        textToSpeak = LocalizationManager.getString("wizard_step1_desc"),
                        onSpeak = { viewModel.speak(it) },
                        onStop = { viewModel.stopAudio() },
                        isSpeaking = isSpeaking
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Photo Area
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .testTag("lot_photo_card"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.AddAPhoto,
                                    contentDescription = LocalizationManager.getString("wizard_capture_photo"),
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Button(
                                    onClick = {
                                        viewModel.updateLotInput(
                                            weight = state.weightInput,
                                            desc = state.description.ifBlank { "Laptops & Electronic Boards" },
                                            loc = state.location,
                                            photo = "content://media/photo_${System.currentTimeMillis()}"
                                        )
                                        viewModel.showMessage(LocalizationManager.getString("photo_captured"))
                                    },
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(LocalizationManager.getString("wizard_capture_photo"))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.weightInput,
                        onValueChange = {
                            viewModel.updateLotInput(it, state.description, state.location, state.photoUri)
                        },
                        label = { Text(LocalizationManager.getString("weight_kg")) },
                        leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("lot_weight_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.description,
                        onValueChange = {
                            viewModel.updateLotInput(state.weightInput, it, state.location, state.photoUri)
                        },
                        label = { Text(LocalizationManager.getString("wizard_desc_label")) },
                        modifier = Modifier.fillMaxWidth().testTag("lot_desc_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.location,
                        onValueChange = {
                            viewModel.updateLotInput(state.weightInput, state.description, it, state.photoUri)
                        },
                        label = { Text(LocalizationManager.getString("wizard_loc_label")) },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = {
                                viewModel.updateLotInput(state.weightInput, state.description, LocalizationManager.getString("wizard_use_gps"), state.photoUri)
                                viewModel.showMessage(LocalizationManager.getString("gps_updated"))
                            }) {
                                Icon(Icons.Default.MyLocation, contentDescription = "Use GPS")
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("lot_location_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.runAiClassification() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("step1_continue_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(LocalizationManager.getString("wizard_analyze_ai"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                2 -> {
                    // STEP 2: AI MATERIAL CLASSIFICATION
                    Text(
                        text = LocalizationManager.getString("wizard_step2_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = LocalizationManager.getString("wizard_step2_desc"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = LocalizationManager.getString("ai_classification"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "${state.aiClassification?.confidence ?: 94}% ${LocalizationManager.getString("match_percent")}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${LocalizationManager.getString("category")}: ${state.selectedCategory}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${LocalizationManager.getString("subcategory")}: ${state.selectedSubcategory}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = LocalizationManager.getString("wizard_confirm_ai_prompt"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { viewModel.confirmAiClassification(true) },
                            modifier = Modifier.weight(1f).height(50.dp).testTag("ai_confirm_yes"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("✓ ${LocalizationManager.getString("yes_correct")}", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.confirmAiClassification(false, "Circuit Boards", "Printed Circuit Boards")
                            },
                            modifier = Modifier.weight(1f).height(50.dp).testTag("ai_change_category"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("✎ ${LocalizationManager.getString("change")}", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                3 -> {
                    // STEP 3: MATERIAL CONDITION
                    Text(
                        text = LocalizationManager.getString("wizard_step3_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = LocalizationManager.getString("wizard_step3_desc"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    MaterialCondition.entries.forEach { condition ->
                        Card(
                            onClick = { viewModel.selectCondition(condition) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("condition_${condition.name}"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (state.selectedCondition == condition)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = when (condition) {
                                            MaterialCondition.WORKING -> Icons.Default.CheckCircle
                                            MaterialCondition.PARTIALLY_WORKING -> Icons.Default.Construction
                                            MaterialCondition.DAMAGED -> Icons.Default.BrokenImage
                                            MaterialCondition.NON_WORKING -> Icons.Default.Cancel
                                            MaterialCondition.SCRAP -> Icons.Default.DeleteOutline
                                            MaterialCondition.UNKNOWN -> Icons.Default.HelpOutline
                                        },
                                        contentDescription = null,
                                        tint = if (state.selectedCondition == condition) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = condition.getLocalizedName(currentLang),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Text(
                                            text = condition.getLocalizedAudio(currentLang),
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                IconButton(onClick = { viewModel.speak("${condition.getLocalizedName(currentLang)}. ${condition.getLocalizedAudio(currentLang)}") }) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = LocalizationManager.getString("listen"))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.runPriceEstimation() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("step3_continue_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(LocalizationManager.getString("calculate_estimated_price"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                4 -> {
                    // STEP 4: ML PRICE ESTIMATION
                    Text(
                        text = LocalizationManager.getString("wizard_step4_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    state.priceEstimate?.let { est ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("price_estimate_card"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmOrangeContainer)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Surface(
                                    color = WarmOrangeSecondary,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = LocalizationManager.getString("wizard_est_scrap_value"),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "${LocalizationManager.getString("estimated_price")}: ₹${est.estimatedPricePerKg.toInt()} / kg",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = WarmOrangeOnContainer
                                )
                                Text(
                                    text = "${LocalizationManager.getString("estimated_total")}: ₹${est.estimatedTotalValue.toInt()}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${LocalizationManager.getString("expected_range")}: ₹${est.minPricePerKg.toInt()} - ₹${est.maxPricePerKg.toInt()} / kg (${est.confidence}% ${LocalizationManager.getString("match_percent")})",
                                    fontSize = 12.sp,
                                    color = WarmOrangeOnContainer.copy(alpha = 0.8f)
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "* Note: Final payout is confirmed by authorized recycler after digital weight inspection at handover.",
                                    fontSize = 11.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = WarmOrangeOnContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.findRecyclerMatches() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("step4_find_recyclers_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(LocalizationManager.getString("wizard_select_recycler"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                5 -> {
                    // STEP 5: RECYCLER MATCHING SYSTEM
                    Text(
                        text = LocalizationManager.getString("wizard_step5_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = LocalizationManager.getString("wizard_step5_desc"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (state.matchingRecyclers.isEmpty()) {
                        Text(LocalizationManager.getString("no_matching_recyclers"))
                    } else {
                        state.matchingRecyclers.forEach { match ->
                            val isSelected = state.selectedRecyclerMatch?.recycler?.userId == match.recycler.userId
                            Card(
                                onClick = { viewModel.selectRecyclerMatch(match) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .testTag("recycler_card_${match.recycler.userId}"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = match.recycler.orgName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Surface(
                                            color = SuccessGreen.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(LocalizationManager.getString("govt_authorized"), color = SuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "${LocalizationManager.getString("offered_price")}: ₹${match.offeredPricePerKg.toInt()} / kg  •  ${LocalizationManager.getString("estimated_total")}: ₹${match.totalEstimatedPayout.toInt()}",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${LocalizationManager.getString("distance")}: ${match.distanceKm} km  •  Min: ${match.recycler.minQuantityKg.toInt()} kg",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (match.pickupAvailable) LocalizationManager.getString("doorstep_pickup_available") else LocalizationManager.getString("dropoff_required"),
                                        fontSize = 12.sp,
                                        color = if (match.pickupAvailable) SuccessGreen else MaterialTheme.colorScheme.error
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = { viewModel.selectRecyclerMatch(match) },
                                        modifier = Modifier.fillMaxWidth().height(42.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                        )
                                    ) {
                                        Text(if (isSelected) "✓ ${LocalizationManager.getString("recycler_selected")}" else LocalizationManager.getString("select_this_recycler"), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.finalizeLotAndCreatePickup { /* step 6 */ } },
                        enabled = state.selectedRecyclerMatch != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .minimumInteractiveComponentSize()
                            .testTag("step5_submit_pickup_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(LocalizationManager.getString("wizard_confirm_creation"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                6 -> {
                    // STEP 6: SUCCESS
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("lot_success_card"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = LocalizationManager.getString("wizard_step6_title"),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Lot ID: ${state.generatedLotId}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Your pickup request has been sent to ${state.selectedRecyclerMatch?.recycler?.orgName}. You will be notified once they confirm the schedule.",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = onFinish,
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Text(LocalizationManager.getString("wizard_finish_button"))
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// MY LOTS SCREEN
// =========================================================================

@Composable
fun MyLotsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val lots by viewModel.collectorLots.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("my_lots"),
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        if (lots.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(LocalizationManager.getString("no_lots_found"), fontWeight = FontWeight.Bold)
                    Text(LocalizationManager.getString("collect_ewaste_to_start"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lots) { lot ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("lot_item_${lot.lotId}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(lot.lotId, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(lot.category, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                }
                                StatusChip(status = lot.status)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${LocalizationManager.getString("weight_kg")}: ${lot.weightKg} kg  •  ${LocalizationManager.getString("estimated_price")}: ₹${lot.estimatedTotalValue.toInt()}", fontSize = 13.sp)
                            Text("${LocalizationManager.getString("condition")}: ${lot.condition}  •  ${LocalizationManager.getString("location")}: ${lot.locationName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            if (lot.matchedRecyclerName.isNotBlank()) {
                                Text("${LocalizationManager.getString("role_recycler")}: ${lot.matchedRecyclerName}", fontSize = 12.sp, color = TechBlueTertiary, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// PICKUP REQUESTS SCREEN
// =========================================================================

@Composable
fun CollectorPickupsScreen(
    viewModel: KabadiwalaViewModel,
    onStartHandover: (Long) -> Unit,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val pickups by viewModel.collectorPickups.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("pickup_requests"),
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        if (pickups.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(LocalizationManager.getString("no_pickups_found"), fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pickups) { pickup ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("pickup_item_${pickup.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = pickup.materialCategory,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                StatusChip(status = pickup.status)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("${LocalizationManager.getString("role_recycler")}: ${pickup.recyclerName}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                            Text("${LocalizationManager.getString("weight_kg")}: ${pickup.weightKg} kg  •  ${LocalizationManager.getString("agreed_rate")}: ₹${pickup.agreedPricePerKg.toInt()}/kg", fontSize = 13.sp)
                            Text("${LocalizationManager.getString("total_value")}: ₹${pickup.totalValue.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("${LocalizationManager.getString("location")}: ${pickup.pickupAddress}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            if (pickup.status == PickupStatus.ACCEPTED.name || pickup.status == PickupStatus.PICKUP_SCHEDULED.name) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { onStartHandover(pickup.id) },
                                    modifier = Modifier.fillMaxWidth().height(42.dp).testTag("start_handover_button_${pickup.id}"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(LocalizationManager.getString("start_handover"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// EARNINGS LEDGER SCREEN
// =========================================================================

@Composable
fun CollectorEarningsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val txns by viewModel.collectorTransactions.collectAsState()
    val lots by viewModel.collectorLots.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    var viewingReceiptTxn by remember { mutableStateOf<TransactionRecordEntity?>(null) }

    val totalEarnings = txns.filter { it.paymentStatus == PaymentStatus.PAID.name }.sumOf { it.totalAmount }
    val pendingDues = txns.filter { it.paymentStatus == PaymentStatus.RECORDED.name || it.paymentStatus == PaymentStatus.PENDING.name }.sumOf { it.totalAmount }
    val totalCollectedKg = lots.sumOf { it.weightKg }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("earnings"),
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Summary Cards
            Card(
                modifier = Modifier.fillMaxWidth().testTag("earnings_ledger_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DeepGreenContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(LocalizationManager.getString("total_earnings"), fontSize = 14.sp, color = DeepGreenOnContainer)
                    Text("₹${totalEarnings.toInt()}", fontSize = 32.sp, fontWeight = FontWeight.Black, color = DeepGreenOnContainer)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(LocalizationManager.getString("pending_dues"), fontSize = 12.sp, color = DeepGreenOnContainer.copy(alpha = 0.8f))
                            Text("₹${pendingDues.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WarmOrangeSecondary)
                        }
                        Column {
                            Text(LocalizationManager.getString("completed_txns"), fontSize = 12.sp, color = DeepGreenOnContainer.copy(alpha = 0.8f))
                            Text("${txns.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepGreenOnContainer)
                        }
                        Column {
                            Text(LocalizationManager.getString("collected_scrap"), fontSize = 12.sp, color = DeepGreenOnContainer.copy(alpha = 0.8f))
                            Text("${totalCollectedKg.toInt()} kg", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DeepGreenOnContainer)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(LocalizationManager.getString("transaction_history"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            if (txns.isEmpty()) {
                Text(LocalizationManager.getString("no_transactions_found"), color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                txns.forEach { txn ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(txn.category, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("₹${txn.totalAmount.toInt()}", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Txn ID: ${txn.transactionId}  •  Handover Ref: ${txn.handoverRefId}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Recycler: ${txn.recyclerName}  •  Weight: ${txn.weightKg} kg @ ₹${txn.pricePerKg.toInt()}/kg", fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                StatusChip(status = txn.paymentStatus)
                                TextButton(
                                    onClick = { viewingReceiptTxn = txn },
                                    modifier = Modifier.testTag("earnings_view_receipt_${txn.transactionId}")
                                ) {
                                    Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Receipt / रसीद", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            viewingReceiptTxn?.let { txn ->
                DigitalReceiptDialog(
                    transaction = txn,
                    onDismiss = { viewingReceiptTxn = null },
                    onShare = {
                        viewModel.showMessage("Receipt #${txn.transactionId} copied to share.")
                        viewingReceiptTxn = null
                    }
                )
            }
        }
    }
}

// =========================================================================
// SAFETY INSTRUCTIONS SCREEN (Section 22)
// =========================================================================

@Composable
fun SafetyInstructionsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("safety_instructions"),
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Safety Banner Graphic
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.safety_banner_illustration_1790574428941),
                    contentDescription = "Safety Gear Illustration",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "⚠️ ${LocalizationManager.getString("safety_instructions")}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = LocalizationManager.getString("safety_instructions_desc"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            ListenButton(
                textToSpeak = LocalizationManager.getString("safety_brief_audio"),
                onSpeak = { viewModel.speak(it) },
                onStop = { viewModel.stopAudio() },
                isSpeaking = isSpeaking,
                label = LocalizationManager.getString("listen_full_safety")
            )

            Spacer(modifier = Modifier.height(16.dp))

            SafetyCatalog.items.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .testTag("safety_card_${item.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isCrucial) DangerRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = if (item.isCrucial) Icons.Default.Warning else Icons.Default.Security,
                                    contentDescription = null,
                                    tint = if (item.isCrucial) DangerRed else MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = item.getTitle(currentLang),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (item.isCrucial) DangerRed else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            FilledTonalIconButton(
                                onClick = { viewModel.speak(item.getAudioScript(currentLang)) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = LocalizationManager.getString("listen"), modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Category & Hazard Badges
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = if (item.isCrucial) DangerRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.getCategory(currentLang),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isCrucial) DangerRed else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            if (item.isCrucial) {
                                Surface(
                                    color = DangerRed.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.Warning, contentDescription = null, tint = DangerRed, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = LocalizationManager.getString("hazard"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.getDescription(currentLang),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
