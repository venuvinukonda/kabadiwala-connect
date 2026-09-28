package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: KabadiwalaViewModel,
    onUserApprovalsClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allLots by viewModel.allLots.collectAsState()
    val allPickups by viewModel.allPickups.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val recyclerProfiles by viewModel.recyclerProfiles.collectAsState()

    val pendingUsersCount = allUsers.count { it.status == UserStatus.PENDING.name }
    val totalWeightCollected = allLots.sumOf { it.weightKg }
    val totalValue = allTransactions.sumOf { it.totalAmount }
    val disputedCount = allTransactions.count { it.paymentStatus == PaymentStatus.DISPUTED.name }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Admin Oversight Console",
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
            Text(
                text = "System Operations & Compliance",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Manage CPCB/SPCB authorizations, collectors, and resolve disputes",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pending Approvals Alert Banner
            if (pendingUsersCount > 0) {
                Card(
                    onClick = onUserApprovalsClick,
                    modifier = Modifier.fillMaxWidth().testTag("pending_approvals_alert"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmOrangeContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PendingActions, contentDescription = null, tint = WarmOrangeSecondary, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$pendingUsersCount Registrations Pending Approval",
                                fontWeight = FontWeight.Bold,
                                color = WarmOrangeOnContainer,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Collectors & Recyclers waiting for license verification",
                                fontSize = 12.sp,
                                color = WarmOrangeOnContainer.copy(alpha = 0.8f)
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = WarmOrangeOnContainer)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Quick Stats Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard("Collected Scrap", "${totalWeightCollected.toInt()} kg", Icons.Default.Scale, DeepGreenContainer, DeepGreenOnContainer, Modifier.weight(1f))
                AdminStatCard("Total Value", "₹${totalValue.toInt()}", Icons.Default.CurrencyRupee, WarmOrangeContainer, WarmOrangeOnContainer, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard("Active Users", "${allUsers.size}", Icons.Default.People, TechBlueContainer, MaterialTheme.colorScheme.onTertiaryContainer, Modifier.weight(1f))
                AdminStatCard("Disputes", "$disputedCount", Icons.Default.Gavel, if (disputedCount > 0) DangerRed.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant, if (disputedCount > 0) DangerRed else MaterialTheme.colorScheme.onSurfaceVariant, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Admin Controls", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            AdminActionCard(
                title = "Registration Approvals",
                subtitle = "Verify collector applications and recycler CPCB certificates",
                icon = Icons.Default.HowToReg,
                badge = if (pendingUsersCount > 0) "$pendingUsersCount Pending" else null,
                onClick = onUserApprovalsClick,
                testTag = "admin_user_approvals_nav"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdminActionCard(
                title = "All Transactions & Payments",
                subtitle = "Inspect digital handover receipts, investigate disputes & record payments",
                icon = Icons.Default.ReceiptLong,
                badge = if (disputedCount > 0) "$disputedCount Disputed" else null,
                onClick = onTransactionsClick,
                testTag = "admin_transactions_nav"
            )

            Spacer(modifier = Modifier.height(10.dp))

            AdminActionCard(
                title = "Analytics & Audit Logs",
                subtitle = "Category distribution, collection trends & compliance logs",
                icon = Icons.Default.BarChart,
                badge = null,
                onClick = onAnalyticsClick,
                testTag = "admin_analytics_nav"
            )
        }
    }
}

@Composable
fun AdminStatCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGreenOutlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = contentColor)
            Text(label, fontSize = 12.sp, color = contentColor.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun AdminActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badge: String?,
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
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = WarmOrangeSecondary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = badge,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
}

// =========================================================================
// REGISTRATION APPROVALS SCREEN (Section 19)
// =========================================================================

@Composable
fun AdminUserApprovalsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val allUsers by viewModel.allUsers.collectAsState()
    val collectors by viewModel.collectorProfiles.collectAsState()
    val recyclers by viewModel.recyclerProfiles.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Collectors, 1: Recyclers

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Registration Approvals",
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Collectors (${collectors.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Recyclers (${recyclers.size})") }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedTab == 0) {
                    items(collectors) { col ->
                        val user = allUsers.find { it.id == col.userId }
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_col_card_${col.userId}"),
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
                                    Text(col.fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    StatusChip(status = user?.status ?: "PENDING")
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Phone: ${col.phone}  •  City: ${col.city}", fontSize = 13.sp)
                                Text("Govt ID: ${col.govtIdNumber}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                Text("Address: ${col.address}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Preferred Area: ${col.preferredArea}", fontSize = 12.sp)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { viewModel.approveCollector(col.userId) },
                                        modifier = Modifier.weight(1f).height(40.dp).testTag("approve_collector_${col.userId}")
                                    ) {
                                        Text("Approve")
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.rejectCollector(col.userId) },
                                        modifier = Modifier.weight(1f).height(40.dp)
                                    ) {
                                        Text("Reject")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    items(recyclers) { rec ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_rec_card_${rec.userId}"),
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
                                    Text(rec.orgName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    StatusChip(status = rec.certStatus)
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Cert No: ${rec.certNumber}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                                Text("Authority: ${rec.certAuthority}  •  Valid till ${rec.certExpiryDate}", fontSize = 12.sp)
                                Text("Authorized Contact: ${rec.authorizedPerson} (${rec.phone})", fontSize = 12.sp)
                                Text("Categories: ${rec.acceptedCategoriesCsv}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = { viewModel.approveRecycler(rec.userId) },
                                        modifier = Modifier.weight(1f).height(40.dp).testTag("approve_recycler_${rec.userId}")
                                    ) {
                                        Text("Verify & Approve")
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.rejectRecycler(rec.userId) },
                                        modifier = Modifier.weight(1f).height(40.dp)
                                    ) {
                                        Text("Reject")
                                    }
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
// ADMIN TRANSACTIONS & DISPUTES (Section 20)
// =========================================================================

@Composable
fun AdminTransactionsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val txns by viewModel.allTransactions.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var filterStatus by remember { mutableStateOf<String?>(null) }

    val filteredList = if (filterStatus == null) txns else txns.filter { it.paymentStatus == filterStatus }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Transaction Audit & Disputes",
                onBack = onBack,
                onLanguageClick = onLanguageClick,
                currentLang = currentLang
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            // Filter chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(selected = filterStatus == null, onClick = { filterStatus = null }, label = { Text("All") })
                FilterChip(selected = filterStatus == PaymentStatus.RECORDED.name, onClick = { filterStatus = PaymentStatus.RECORDED.name }, label = { Text("Recorded") })
                FilterChip(selected = filterStatus == PaymentStatus.PAID.name, onClick = { filterStatus = PaymentStatus.PAID.name }, label = { Text("Paid") })
                FilterChip(selected = filterStatus == PaymentStatus.DISPUTED.name, onClick = { filterStatus = PaymentStatus.DISPUTED.name }, label = { Text("Disputed") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions match this filter.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filteredList) { txn ->
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("admin_txn_${txn.transactionId}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(txn.transactionId, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    StatusChip(status = txn.paymentStatus)
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Handover Ref: ${txn.handoverRefId}  •  Lot: ${txn.lotId}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Collector: ${txn.collectorName}  •  Recycler: ${txn.recyclerName}", fontSize = 13.sp)
                                Text("Material: ${txn.category} (${txn.weightKg} kg @ ₹${txn.pricePerKg.toInt()}/kg)", fontSize = 13.sp)
                                Text("Total Amount: ₹${txn.totalAmount.toInt()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)

                                if (txn.notes.isNotBlank()) {
                                    Text("Notes: ${txn.notes}", fontSize = 12.sp, color = WarmOrangeSecondary)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    if (txn.paymentStatus != PaymentStatus.PAID.name) {
                                        Button(
                                            onClick = { viewModel.markTransactionPaid(txn.transactionId) },
                                            modifier = Modifier.weight(1f).height(38.dp)
                                        ) {
                                            Text("Mark Paid")
                                        }
                                    }
                                    if (txn.paymentStatus != PaymentStatus.DISPUTED.name) {
                                        OutlinedButton(
                                            onClick = { viewModel.markTransactionDisputed(txn.transactionId, "Investigating weight discrepancy") },
                                            modifier = Modifier.weight(1f).height(38.dp)
                                        ) {
                                            Text("Dispute")
                                        }
                                    }
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
// ADMIN ANALYTICS & AUDIT LOGS (Section 21)
// =========================================================================

@Composable
fun AdminAnalyticsScreen(
    viewModel: KabadiwalaViewModel,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val auditLogs by viewModel.auditLogs.collectAsState()
    val allLots by viewModel.allLots.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    val categoriesCount = allLots.groupBy { it.category }.mapValues { it.value.size }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "Analytics & Audit Logs",
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
            Text("Category Breakdown", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    categoriesCount.forEach { (cat, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(cat, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text("$count lots", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("System Audit Logs (Real-Time)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            if (auditLogs.isEmpty()) {
                Text("No audit events recorded.")
            } else {
                auditLogs.forEach { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(log.action, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                                Text(log.actorRole, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(log.details, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
