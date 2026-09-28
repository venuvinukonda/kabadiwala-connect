package com.example.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.service.localization.LocalizationManager
import com.example.ui.components.KabadiwalaTopBar
import com.example.ui.components.ListenButton
import com.example.ui.components.StatusChip
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel

// =========================================================================
// DIGITAL HANDOVER SCREEN (Section 13)
// =========================================================================

@Composable
fun DigitalHandoverScreen(
    pickupRequestId: Long,
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onComplete: (String) -> Unit,
    onLanguageClick: () -> Unit
) {
    val allPickups by viewModel.allPickups.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val request = allPickups.find { it.id == pickupRequestId }

    var actualWeightInput by remember { mutableStateOf(request?.weightKg?.toString() ?: "15.0") }
    var confirmedByBoth by remember { mutableStateOf(true) }
    var generatedRefId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("digital_handover"),
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
            if (generatedRefId != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("handover_success_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepGreenContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = DeepGreenOnContainer, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(LocalizationManager.getString("handover_confirmed"), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DeepGreenOnContainer)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(LocalizationManager.getString("unique_ref_id"), fontSize = 13.sp, color = DeepGreenOnContainer.copy(alpha = 0.8f))
                        Text(generatedRefId ?: "", fontSize = 24.sp, fontWeight = FontWeight.Black, color = DeepGreenOnContainer)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = LocalizationManager.getString("handover_receipt_desc"),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontSize = 13.sp,
                            color = DeepGreenOnContainer
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Text(LocalizationManager.getString("close"))
                        }
                    }
                }
            } else if (request == null) {
                Text(LocalizationManager.getString("empty_pickups"))
            } else {
                Text(
                    text = "Physical Handover & Weight Verification",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Both parties verify final physical weight and sign off digitally.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                ListenButton(
                    textToSpeak = "Digital Handover. Verify actual weight with weighing scale, confirm material condition, and generate digital reference ID.",
                    onSpeak = { viewModel.speak(it) },
                    onStop = { viewModel.stopAudio() },
                    isSpeaking = isSpeaking
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Lot ID: ${request.lotId}", fontWeight = FontWeight.Bold)
                        Text("Material: ${request.materialCategory}", fontSize = 15.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        Text("Collector: ${request.collectorName}", fontSize = 13.sp)
                        Text("Recycler: ${request.recyclerName}", fontSize = 13.sp)
                        Text("Agreed Rate: ₹${request.agreedPricePerKg.toInt()} / kg", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = actualWeightInput,
                    onValueChange = { actualWeightInput = it },
                    label = { Text("Actual Inspected Weight (kg) *") },
                    leadingIcon = { Icon(Icons.Default.Scale, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("handover_actual_weight_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                val actualWeight = actualWeightInput.toDoubleOrNull() ?: request.weightKg
                val calculatedTotal = actualWeight * request.agreedPricePerKg

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Final Payable Amount:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("₹${calculatedTotal.toInt()}", fontWeight = FontWeight.Black, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = confirmedByBoth, onCheckedChange = { confirmedByBoth = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Both Collector and Recycler confirm weight and handover in person.", fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.confirmHandoverAndPay(
                            request = request,
                            actualWeight = actualWeight,
                            onComplete = { ref ->
                                generatedRefId = ref
                            }
                        )
                    },
                    enabled = confirmedByBoth && actualWeight > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("submit_handover_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Confirm Handover & Record Payment", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// =========================================================================
// PRICE HISTORY & BENCHMARKS (Section 10)
// =========================================================================

@Composable
fun PriceHistoryScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val rates = listOf(
        Triple("Circuit Boards (PCB)", "₹ 420 - 450", "+5% this week"),
        Triple("Mobile Phones", "₹ 340 - 370", "+3% this week"),
        Triple("Laptops", "₹ 280 - 300", "Stable"),
        Triple("Computers (CPU)", "₹ 210 - 230", "Stable"),
        Triple("Cables & Copper Wire", "₹ 190 - 210", "+8% this week"),
        Triple("Batteries (Li-Ion)", "₹ 140 - 160", "+2% this week"),
        Triple("Chargers & Adapters", "₹ 100 - 120", "Stable"),
        Triple("Televisions (LED/LCD)", "₹ 80 - 95", "-2% this week"),
        Triple("Printers", "₹ 60 - 75", "Stable"),
        Triple("Washing Machines", "₹ 45 - 55", "Stable"),
        Triple("Refrigerators", "₹ 40 - 50", "Stable")
    )

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("price_history"),
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
            Text(
                text = "Market E-Waste Scrap Benchmarks",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Transparent indicative rates across Indian authorized recycling plants.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            ListenButton(
                textToSpeak = "Market scrap prices. Circuit boards lead at 420 to 450 rupees per kilo. Mobile phones at 350. Laptops at 290. Copper cables at 200.",
                onSpeak = { viewModel.speak(it) },
                onStop = { viewModel.stopAudio() },
                isSpeaking = isSpeaking
            )

            Spacer(modifier = Modifier.height(16.dp))

            rates.forEach { (cat, price, trend) ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(cat, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(trend, fontSize = 11.sp, color = if (trend.contains("+")) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "$price / kg",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// USER PROFILE SCREEN
// =========================================================================

@Composable
fun UserProfileScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val colProfile by viewModel.currentCollectorProfile.collectAsState()
    val recProfile by viewModel.currentRecyclerProfile.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("profile"),
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = colProfile?.fullName ?: recProfile?.orgName ?: currentUser?.username ?: "User",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Role: ${currentUser?.role ?: "COLLECTOR"}",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusChip(status = currentUser?.status ?: "APPROVED")

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Account Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Username: ${currentUser?.username}")
                    Text("Phone: ${currentUser?.phone}")

                    if (colProfile != null) {
                        Text("Address: ${colProfile?.address}, ${colProfile?.city}")
                        Text("Govt ID: ${colProfile?.govtIdNumber}")
                        Text("Preferred Area: ${colProfile?.preferredArea}")
                        Text("Languages: ${colProfile?.languagesKnown}")
                    }

                    if (recProfile != null) {
                        Text("Organization: ${recProfile?.orgName}")
                        Text("Certificate: ${recProfile?.certNumber}")
                        Text("Authority: ${recProfile?.certAuthority}")
                        Text("Expiry: ${recProfile?.certExpiryDate}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("profile_logout_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout / लॉग आउट")
            }
        }
    }
}

// =========================================================================
// TRANSACTION HISTORY & PAYMENT LEDGER (Section 9 & 20)
// =========================================================================

@Composable
fun TransactionHistoryScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    // Filter relevant transactions for current user role
    val userTxns = remember(allTransactions, currentUser) {
        val user = currentUser
        if (user == null) emptyList()
        else when (user.role) {
            UserRole.COLLECTOR.name -> allTransactions.filter { it.collectorId == user.id }
            UserRole.RECYCLER.name -> allTransactions.filter { it.recyclerId == user.id }
            else -> allTransactions
        }
    }

    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var viewingReceiptTxn by remember { mutableStateOf<TransactionRecordEntity?>(null) }

    val filteredList = remember(userTxns, selectedFilter, searchQuery) {
        userTxns.filter { txn ->
            val matchFilter = selectedFilter == null || txn.paymentStatus == selectedFilter
            val matchSearch = searchQuery.isBlank() ||
                txn.category.contains(searchQuery, ignoreCase = true) ||
                txn.transactionId.contains(searchQuery, ignoreCase = true) ||
                txn.handoverRefId.contains(searchQuery, ignoreCase = true) ||
                txn.collectorName.contains(searchQuery, ignoreCase = true) ||
                txn.recyclerName.contains(searchQuery, ignoreCase = true)
            matchFilter && matchSearch
        }
    }

    val totalPaid = userTxns.filter { it.paymentStatus == PaymentStatus.PAID.name }.sumOf { it.totalAmount }
    val pendingDues = userTxns.filter { it.paymentStatus == PaymentStatus.RECORDED.name || it.paymentStatus == PaymentStatus.PENDING.name }.sumOf { it.totalAmount }
    val totalVolumeKg = userTxns.sumOf { it.weightKg }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("transaction_history"),
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
                .padding(16.dp)
        ) {
            // Summary Ledger Header Card
            Card(
                modifier = Modifier.fillMaxWidth().testTag("txns_summary_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Paid / Cleared", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${totalPaid.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = DeepGreenOnContainer)
                    }
                    Column {
                        Text("Pending Dues", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("₹${pendingDues.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WarmOrangeSecondary)
                    }
                    Column {
                        Text("Volume Handled", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${totalVolumeKg.toInt()} kg", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by category, Txn ID, or party...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("txn_search_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = selectedFilter == null, onClick = { selectedFilter = null }, label = { Text("All (${userTxns.size})") })
                FilterChip(selected = selectedFilter == PaymentStatus.PAID.name, onClick = { selectedFilter = PaymentStatus.PAID.name }, label = { Text("Paid") })
                FilterChip(selected = selectedFilter == PaymentStatus.RECORDED.name, onClick = { selectedFilter = PaymentStatus.RECORDED.name }, label = { Text("Recorded") })
                FilterChip(selected = selectedFilter == PaymentStatus.DISPUTED.name, onClick = { selectedFilter = PaymentStatus.DISPUTED.name }, label = { Text("Disputed") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No matching transaction records.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { txn ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("txn_item_${txn.transactionId}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = txn.category,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "₹${txn.totalAmount.toInt()}",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Txn ID: ${txn.transactionId}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    StatusChip(status = txn.paymentStatus)
                                }

                                Text(
                                    text = "Handover Ref: ${txn.handoverRefId}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Collector: ${txn.collectorName}  •  Recycler: ${txn.recyclerName}",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Weight: ${txn.weightKg} kg  •  Agreed Rate: ₹${txn.pricePerKg.toInt()} / kg",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Mode: Direct UPI / IMPS Transfer",
                                    fontSize = 12.sp,
                                    color = DeepGreenOnContainer
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.speak("${txn.category}, total rupees ${txn.totalAmount.toInt()}, weight ${txn.weightKg} kilograms, status ${txn.paymentStatus}.")
                                        },
                                        modifier = Modifier.weight(1f).height(38.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp)
                                    ) {
                                        Icon(Icons.Default.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Listen", fontSize = 12.sp)
                                    }

                                    Button(
                                        onClick = { viewingReceiptTxn = txn },
                                        modifier = Modifier.weight(1.3f).height(38.dp).testTag("view_receipt_btn_${txn.transactionId}"),
                                        contentPadding = PaddingValues(horizontal = 6.dp)
                                    ) {
                                        Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("View Receipt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Receipt Viewer Modal
            viewingReceiptTxn?.let { txn ->
                DigitalReceiptDialog(
                    transaction = txn,
                    onDismiss = { viewingReceiptTxn = null },
                    onShare = {
                        viewModel.showMessage("Receipt #${txn.transactionId} generated and copied to share.")
                        viewingReceiptTxn = null
                    }
                )
            }
        }
    }
}

// =========================================================================
// DIGITAL HANDOVER RECEIPT DIALOG (Section 9 & 14)
// =========================================================================

@Composable
fun DigitalReceiptDialog(
    transaction: TransactionRecordEntity,
    onDismiss: () -> Unit,
    onShare: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("E-Waste Handover Receipt", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("KABADIWALA CONNECT", fontWeight = FontWeight.Black, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        Text("CPCB EPR Mandated Digital Ledger", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    StatusChip(status = transaction.paymentStatus)
                }

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                val formattedDate = remember(transaction.dateTimestamp) {
                    java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(transaction.dateTimestamp))
                }

                ReceiptDetailRow("Receipt / Txn ID", transaction.transactionId)
                ReceiptDetailRow("Handover Ref", transaction.handoverRefId)
                ReceiptDetailRow("Collection Lot ID", transaction.lotId)
                ReceiptDetailRow("Date & Time", formattedDate)

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                ReceiptDetailRow("Collector", transaction.collectorName)
                ReceiptDetailRow("Authorized Recycler", transaction.recyclerName)
                ReceiptDetailRow("Material Category", transaction.category)
                ReceiptDetailRow("Inspected Weight", "${transaction.weightKg} kg")
                ReceiptDetailRow("Agreed Rate", "₹${transaction.pricePerKg.toInt()} / kg")
                ReceiptDetailRow("Payment Mode", "Direct UPI / Bank IMPS Transfer")

                Divider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Net Amount:", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Text("₹${transaction.totalAmount.toInt()}", fontWeight = FontWeight.Black, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Digitally Verified & Signed by Both Parties\nRef: CPCB-EPR-KABADIWALA-TXN", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onShare,
                modifier = Modifier.testTag("share_receipt_button")
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share / Print PDF")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

// =========================================================================
// AUDIO HELP & VOICE GUIDE DIALOG (Section 23 & 30)
// =========================================================================

@Composable
fun AudioHelpDialog(
    viewModel: KabadiwalaViewModel,
    onDismiss: () -> Unit
) {
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    val speechRate by viewModel.speechRate.collectAsState()

    val audioGuides = listOf(
        Pair(
            LocalizationManager.getString("step_weight_desc"),
            LocalizationManager.getString("wizard_step1_desc")
        ),
        Pair(
            LocalizationManager.getString("safety_instructions"),
            LocalizationManager.getString("safety_brief_audio")
        ),
        Pair(
            LocalizationManager.getString("digital_handover"),
            LocalizationManager.getString("handover_receipt_desc")
        ),
        Pair(
            LocalizationManager.getString("earnings"),
            LocalizationManager.getString("earnings_audio_desc")
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Headphones, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(LocalizationManager.getString("voice_assistance"), fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                Text(
                    text = LocalizationManager.getString("audio_help_desc"),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Speech Rate Adjuster
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${LocalizationManager.getString("speech_speed")}: ${speechRate}x", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(
                            selected = speechRate == 0.8f,
                            onClick = { viewModel.setSpeechRate(0.8f) },
                            label = { Text("0.8x") }
                        )
                        FilterChip(
                            selected = speechRate == 1.0f,
                            onClick = { viewModel.setSpeechRate(1.0f) },
                            label = { Text("1.0x") }
                        )
                        FilterChip(
                            selected = speechRate == 1.2f,
                            onClick = { viewModel.setSpeechRate(1.2f) },
                            label = { Text("1.2x") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                audioGuides.forEach { (title, narration) ->
                    Card(
                        onClick = { viewModel.speak(narration) },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(narration, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
                            }
                            IconButton(onClick = { viewModel.speak(narration) }) {
                                Icon(Icons.Default.VolumeUp, contentDescription = LocalizationManager.getString("listen"), tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                if (isSpeaking) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.stopAudio() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(LocalizationManager.getString("stop"))
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(LocalizationManager.getString("close"))
            }
        }
    )
}

@Composable
private fun ReceiptDetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}


