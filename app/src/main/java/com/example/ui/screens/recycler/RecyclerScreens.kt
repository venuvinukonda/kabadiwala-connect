package com.example.ui.screens.recycler

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.service.localization.LocalizationManager
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel

@Composable
fun RecyclerDashboardScreen(
    viewModel: KabadiwalaViewModel,
    onLotRequestsClick: () -> Unit,
    onRatesConfigClick: () -> Unit,
    onCertificateClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val profile by viewModel.currentRecyclerProfile.collectAsState()
    val pickups by viewModel.recyclerPickups.collectAsState()
    val txns by viewModel.recyclerTransactions.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()

    val pendingRequestsCount = pickups.count { it.status == PickupStatus.REQUESTED.name }
    val totalProcessedKg = txns.sumOf { it.weightKg }
    val totalPayouts = txns.sumOf { it.totalAmount }

    val welcomeVoice = "Welcome ${profile?.orgName ?: "Authorized Recycler"}. You have $pendingRequestsCount pending lot requests. Check your certificate validity and configure offered rates."

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Recycler Dashboard",
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
                .padding(16.dp)
        ) {
            // Header Org Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Authorized Plant",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = profile?.orgName ?: "Recycling Plant",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                StatusChip(status = profile?.certStatus ?: "APPROVED")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Certificate status warning banner
            Card(
                onClick = onCertificateClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (profile?.certStatus == "APPROVED") SuccessGreen.copy(alpha = 0.12f) else WarningAmber.copy(alpha = 0.18f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = if (profile?.certStatus == "APPROVED") SuccessGreen else WarningAmber
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Govt Authorization: ${profile?.certNumber ?: "CPCB/EW-REG/MH/2024"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Authority: ${profile?.certAuthority ?: "Central Pollution Control Board"}  •  Valid until ${profile?.certExpiryDate ?: "2027"}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = "View Certificate")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            ScreenVoiceBanner(
                title = "Recycler Overview Audio",
                narration = welcomeVoice,
                onSpeak = { viewModel.speak(it) },
                onStop = { viewModel.stopAudio() },
                isSpeaking = isSpeaking,
                speechRate = speechRate,
                onRateChange = { viewModel.setSpeechRate(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Metric Summary Cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Scrap", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${totalProcessedKg.toInt()} kg", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Payouts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${totalPayouts.toInt()}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WarmOrangeSecondary)
                    }
                }
                Card(
                    onClick = onLotRequestsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (pendingRequestsCount > 0) WarmOrangeContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Requests", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("$pendingRequestsCount New", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (pendingRequestsCount > 0) WarmOrangeOnContainer else MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Recycler Options (Section 16)
            RecyclerActionRow(
                title = "Incoming Lot Requests ($pendingRequestsCount pending)",
                subtitle = "Accept or decline pickup requests from collectors",
                icon = Icons.Default.MarkEmailUnread,
                onClick = onLotRequestsClick,
                testTag = "recycler_lot_requests_row"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecyclerActionRow(
                title = "Accepted Materials & Rates",
                subtitle = "Update offered ₹/kg rates and categories",
                icon = Icons.Default.CurrencyRupee,
                onClick = onRatesConfigClick,
                testTag = "recycler_rates_config_row"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecyclerActionRow(
                title = "Authorization Certificate",
                subtitle = "View government license validity and CPCB documents",
                icon = Icons.Default.Description,
                onClick = onCertificateClick,
                testTag = "recycler_cert_row"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecyclerActionRow(
                title = "Transactions & Digital Handovers",
                subtitle = "Verify incoming manifests and ledger records",
                icon = Icons.Default.ReceiptLong,
                onClick = onTransactionsClick,
                testTag = "recycler_txns_row"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RecyclerActionRow(
                title = "Plant Profile & Settings",
                subtitle = "Operating area, capacity & pickup radius",
                icon = Icons.Default.Person,
                onClick = onProfileClick,
                testTag = "recycler_profile_row"
            )
        }
    }
}

@Composable
fun RecyclerActionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGreenOutlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

// =========================================================================
// RECEIVE LOT REQUESTS (Section 18)
// =========================================================================

@Composable
fun RecyclerLotRequestsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val pickups by viewModel.recyclerPickups.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Incoming Lot Requests",
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        if (pickups.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(54.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No pending requests from collectors.", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pickups) { req ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("req_card_${req.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("NEW LOT REQUEST", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                                StatusChip(status = req.status)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text("Material: ${req.materialCategory}", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("Quantity: ${req.weightKg} kg  •  Estimated Rate: ₹${req.agreedPricePerKg.toInt()}/kg", fontSize = 14.sp)
                            Text("Collector: ${req.collectorName} (${req.collectorPhone})", fontSize = 13.sp)
                            Text("Location: ${req.pickupAddress}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Estimated Total Value: ₹${req.totalValue.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = WarmOrangeSecondary)

                            if (req.status == PickupStatus.REQUESTED.name) {
                                Spacer(modifier = Modifier.height(14.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Button(
                                        onClick = { viewModel.acceptPickupRequest(req) },
                                        modifier = Modifier.weight(1f).height(44.dp).testTag("accept_req_${req.id}"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("ACCEPT", fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.rejectPickupRequest(req) },
                                        modifier = Modifier.weight(1f).height(44.dp).testTag("reject_req_${req.id}"),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("REJECT", fontWeight = FontWeight.Bold)
                                    }
                                }
                            } else if (req.status == PickupStatus.ACCEPTED.name) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { viewModel.schedulePickup(req) },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Schedule Van Pickup")
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
// RECYCLER SETTINGS & RATES CONFIG (Section 17)
// =========================================================================

@Composable
fun RecyclerRatesConfigScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val profile by viewModel.currentRecyclerProfile.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var laptopRate by remember { mutableStateOf("290") }
    var mobileRate by remember { mutableStateOf("360") }
    var pcbRate by remember { mutableStateOf("440") }
    var batteryRate by remember { mutableStateOf("150") }
    var cableRate by remember { mutableStateOf("200") }
    var minQty by remember { mutableStateOf(profile?.minQuantityKg?.toString() ?: "10") }
    var radius by remember { mutableStateOf(profile?.pickupRadiusKm?.toString() ?: "40") }
    var pickupAvail by remember { mutableStateOf(profile?.pickupAvailable ?: true) }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Rates & Pickup Settings",
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
            Text("Offered Buying Rates (₹ / kg)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Update your current purchasing prices for collectors.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(14.dp))

            RateInputField(category = "Laptops", value = laptopRate, onValueChange = { laptopRate = it })
            RateInputField(category = "Mobile Phones", value = mobileRate, onValueChange = { mobileRate = it })
            RateInputField(category = "Circuit Boards (PCB)", value = pcbRate, onValueChange = { pcbRate = it })
            RateInputField(category = "Batteries", value = batteryRate, onValueChange = { batteryRate = it })
            RateInputField(category = "Cables & Wiring", value = cableRate, onValueChange = { cableRate = it })

            Spacer(modifier = Modifier.height(20.dp))

            Text("Pickup Rules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Offer Doorstep Pickup Availability", fontWeight = FontWeight.Medium)
                Switch(checked = pickupAvail, onCheckedChange = { pickupAvail = it })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = minQty,
                    onValueChange = { minQty = it },
                    label = { Text("Min Lot Qty (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = radius,
                    onValueChange = { radius = it },
                    label = { Text("Pickup Radius (km)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val ratesJson = "{\"Laptops\": ${laptopRate.toDoubleOrNull() ?: 290.0}, \"Mobile Phones\": ${mobileRate.toDoubleOrNull() ?: 360.0}, \"Circuit Boards\": ${pcbRate.toDoubleOrNull() ?: 440.0}, \"Batteries\": ${batteryRate.toDoubleOrNull() ?: 150.0}, \"Cables\": ${cableRate.toDoubleOrNull() ?: 200.0}}"
                    viewModel.updateRecyclerSettings(
                        ratesJson = ratesJson,
                        categories = "Laptops, Mobile Phones, Circuit Boards, Batteries, Cables",
                        minQty = minQty.toDoubleOrNull() ?: 10.0,
                        pickupAvail = pickupAvail,
                        radius = radius.toDoubleOrNull() ?: 40.0
                    )
                },
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("save_rates_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Rates & Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RateInputField(
    category: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(category, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            prefix = { Text("₹ ") },
            suffix = { Text("/kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(150.dp),
            singleLine = true
        )
    }
}

// =========================================================================
// RECYCLER CERTIFICATE VIEWER
// =========================================================================

@Composable
fun RecyclerCertificateScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val profile by viewModel.currentRecyclerProfile.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Authorization Certificate",
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
                        StatusChip(status = profile?.certStatus ?: "APPROVED")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("GOVERNMENT AUTHORIZATION CERTIFICATE", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                    Text(profile?.orgName ?: "GreenTech Recycling Pvt Ltd", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailItem("Certificate No.", profile?.certNumber ?: "CPCB/EW-REG/MH/2024/0912")
                    DetailItem("Issuing Authority", profile?.certAuthority ?: "Maharashtra Pollution Control Board")
                    DetailItem("Issue Date", profile?.certIssueDate ?: "2024-01-15")
                    DetailItem("Expiry Date", profile?.certExpiryDate ?: "2027-01-14")
                    DetailItem("Authorized Person", profile?.authorizedPerson ?: "Anil Verma")
                    DetailItem("Processing Capacity", "${profile?.processingCapacityKgPerDay?.toInt() ?: 5000} kg / day")
                    DetailItem("Permitted Categories", profile?.acceptedCategoriesCsv ?: "Laptops, Circuit Boards, Batteries")

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Changes to authorization certificates require Administrator verification.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
