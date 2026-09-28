package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserRole
import com.example.service.localization.LocalizationManager
import com.example.ui.components.KabadiwalaTopBar
import com.example.ui.components.ListenButton
import com.example.ui.components.ScreenVoiceBanner
import com.example.ui.theme.*
import com.example.ui.viewmodel.KabadiwalaViewModel

@Composable
fun RoleSelectionScreen(
    viewModel: KabadiwalaViewModel,
    onRoleSelected: (UserRole) -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = LocalizationManager.getString("app_name"),
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = LocalizationManager.getString("select_role"),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = LocalizationManager.getString("select_role_desc"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Collector Card
            RoleCard(
                title = LocalizationManager.getString("role_collector"),
                subtitle = LocalizationManager.getString("role_collector_desc"),
                icon = Icons.Default.Recycling,
                containerColor = PureWhiteSurface,
                contentColor = DeepGreenPrimary,
                onClick = { onRoleSelected(UserRole.COLLECTOR) },
                testTag = "role_collector_card"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Recycler Card
            RoleCard(
                title = LocalizationManager.getString("role_recycler"),
                subtitle = LocalizationManager.getString("role_recycler_desc"),
                icon = Icons.Default.PrecisionManufacturing,
                containerColor = PureWhiteSurface,
                contentColor = TechBlueTertiary,
                onClick = { onRoleSelected(UserRole.RECYCLER) },
                testTag = "role_recycler_card"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Admin Card
            RoleCard(
                title = LocalizationManager.getString("role_admin"),
                subtitle = LocalizationManager.getString("role_admin_desc"),
                icon = Icons.Default.AdminPanelSettings,
                containerColor = PureWhiteSurface,
                contentColor = WarmOrangeSecondary,
                onClick = { onRoleSelected(UserRole.ADMIN) },
                testTag = "role_admin_card"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, LightGreenOutlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = contentColor.copy(alpha = 0.15f),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.85f),
                    lineHeight = 16.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = contentColor
            )
        }
    }
}

@Composable
fun LoginScreen(
    role: UserRole,
    viewModel: KabadiwalaViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var identifier by remember {
        mutableStateOf(
            when (role) {
                UserRole.COLLECTOR -> "collector"
                UserRole.RECYCLER -> "recycler"
                UserRole.ADMIN -> "admin"
            }
        )
    }
    var password by remember {
        mutableStateOf(
            when (role) {
                UserRole.ADMIN -> "admin123"
                else -> "password123"
            }
        )
    }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val roleLabel = when (role) {
        UserRole.COLLECTOR -> LocalizationManager.getString("role_collector")
        UserRole.RECYCLER -> LocalizationManager.getString("role_recycler")
        UserRole.ADMIN -> LocalizationManager.getString("role_admin")
    }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "$roleLabel ${LocalizationManager.getString("login")}",
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (role) {
                            UserRole.COLLECTOR -> Icons.Default.Recycling
                            UserRole.RECYCLER -> Icons.Default.PrecisionManufacturing
                            UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = LocalizationManager.getWelcomeGreeting(roleLabel),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = LocalizationManager.getString("username_or_phone"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            ListenButton(
                textToSpeak = "${LocalizationManager.getString("login")} - $roleLabel. ${LocalizationManager.getString("username_or_phone")}, ${LocalizationManager.getString("password")}.",
                onSpeak = { viewModel.speak(it) },
                onStop = { viewModel.stopAudio() },
                isSpeaking = isSpeaking,
                label = LocalizationManager.getString("listen")
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = identifier,
                onValueChange = { identifier = it },
                label = { Text(LocalizationManager.getString("username_or_phone")) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_identifier_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(LocalizationManager.getString("password")) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_password_input"),
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { showForgotPasswordDialog = true },
                    modifier = Modifier.testTag("forgot_password_button")
                ) {
                    Text(LocalizationManager.getString("forgot_password_title"), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (identifier.isBlank() || password.isBlank()) {
                        errorMessage = LocalizationManager.getString("enter_id_password_error")
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    viewModel.login(
                        identifier = identifier,
                        pass = password,
                        onSuccess = {
                            isLoading = false
                            onLoginSuccess()
                        },
                        onError = { err ->
                            isLoading = false
                            errorMessage = err
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(14.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(LocalizationManager.getString("login"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (role != UserRole.ADMIN) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(LocalizationManager.getString("dont_have_account"), fontSize = 14.sp)
                }
            }

            if (showForgotPasswordDialog) {
                ForgotPasswordDialog(
                    initialIdentifier = identifier,
                    onDismiss = { showForgotPasswordDialog = false },
                    onReset = { userIdentifier, newPass ->
                        viewModel.resetPassword(userIdentifier, newPass) { success, msg ->
                            if (success) {
                                password = newPass
                                identifier = userIdentifier
                                showForgotPasswordDialog = false
                                viewModel.showMessage(msg)
                            } else {
                                viewModel.showMessage(msg)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CollectorRegisterScreen(
    viewModel: KabadiwalaViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Mumbai") }
    var state by remember { mutableStateOf("Maharashtra") }
    var pincode by remember { mutableStateOf("") }
    var govtId by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("Central Suburbs") }
    var languages by remember { mutableStateOf("Hindi, Marathi, English") }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "${LocalizationManager.getString("role_collector")} ${LocalizationManager.getString("register")}",
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
            if (submitted) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("pending_approval_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = LocalizationManager.getString("reg_status_pending_title"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = LocalizationManager.getString("reg_status_pending_desc"),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onRegistered,
                            modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                        ) {
                            Text(LocalizationManager.getString("reg_back_to_role"))
                        }
                    }
                }
            } else {
                Text(
                    text = LocalizationManager.getString("reg_collector_title"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = LocalizationManager.getString("reg_collector_desc"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                ListenButton(
                    textToSpeak = LocalizationManager.getString("reg_voice_collector"),
                    onSpeak = { viewModel.speak(it) },
                    onStop = { viewModel.stopAudio() },
                    isSpeaking = isSpeaking,
                    label = LocalizationManager.getString("listen")
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(LocalizationManager.getString("reg_full_name")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_fullname_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(LocalizationManager.getString("reg_phone_number")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_phone_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(LocalizationManager.getString("username")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_username_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(LocalizationManager.getString("password")) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_password_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = govtId,
                    onValueChange = { govtId = it },
                    label = { Text(LocalizationManager.getString("reg_govt_id")) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("reg_govtid_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(LocalizationManager.getString("reg_address")) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_address_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text(LocalizationManager.getString("reg_city")) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { pincode = it },
                        label = { Text(LocalizationManager.getString("reg_pincode")) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text(LocalizationManager.getString("reg_preferred_area")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = languages,
                    onValueChange = { languages = it },
                    label = { Text(LocalizationManager.getString("reg_spoken_languages")) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (fullName.isNotBlank() && phone.isNotBlank() && username.isNotBlank()) {
                            viewModel.registerCollector(
                                fullName = fullName,
                                phone = phone,
                                username = username,
                                pass = password.ifBlank { "password123" },
                                address = address.ifBlank { "Local area" },
                                city = city,
                                state = state,
                                pincode = pincode.ifBlank { "400001" },
                                govtId = govtId.ifBlank { "GOVT-REF-1092" },
                                area = area,
                                languages = languages,
                                onSuccess = { submitted = true }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("submit_collector_registration"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(LocalizationManager.getString("reg_submit_collector"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RecyclerRegisterScreen(
    viewModel: KabadiwalaViewModel,
    onRegistered: () -> Unit,
    onBack: () -> Unit,
    onLanguageClick: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    var orgName by remember { mutableStateOf("") }
    var personName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Mumbai") }
    var state by remember { mutableStateOf("Maharashtra") }
    var pincode by remember { mutableStateOf("400701") }
    var certNumber by remember { mutableStateOf("CPCB/EW-REG/2026/099") }
    var certAuthority by remember { mutableStateOf("Central Pollution Control Board") }
    var certIssueDate by remember { mutableStateOf("2025-01-10") }
    var certExpiryDate by remember { mutableStateOf("2028-01-09") }
    var acceptedCategories by remember { mutableStateOf("Laptops, Mobile Phones, Circuit Boards, Batteries, Cables, Computers") }
    var capacity by remember { mutableStateOf("5000") }
    var minQty by remember { mutableStateOf("10") }
    var pickupRadius by remember { mutableStateOf("40") }
    var submitted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            KabadiwalaTopBar(
                title = "${LocalizationManager.getString("role_recycler")} ${LocalizationManager.getString("register")}",
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
            if (submitted) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("pending_verification_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = LocalizationManager.getString("reg_auth_verification_title"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = LocalizationManager.getString("reg_auth_verification_desc"),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onRegistered,
                            modifier = Modifier.fillMaxWidth().minimumInteractiveComponentSize()
                        ) {
                            Text(LocalizationManager.getString("reg_back_to_role"))
                        }
                    }
                }
            } else {
                Text(
                    text = LocalizationManager.getString("reg_recycler_title"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = LocalizationManager.getString("reg_recycler_desc"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                ListenButton(
                    textToSpeak = LocalizationManager.getString("reg_voice_recycler"),
                    onSpeak = { viewModel.speak(it) },
                    onStop = { viewModel.stopAudio() },
                    isSpeaking = isSpeaking,
                    label = LocalizationManager.getString("listen")
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = orgName,
                    onValueChange = { orgName = it },
                    label = { Text(LocalizationManager.getString("reg_business_name")) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_orgname_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = personName,
                    onValueChange = { personName = it },
                    label = { Text(LocalizationManager.getString("reg_authorized_person")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(LocalizationManager.getString("reg_contact_phone")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(LocalizationManager.getString("username")) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text(LocalizationManager.getString("password")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = certNumber,
                    onValueChange = { certNumber = it },
                    label = { Text(LocalizationManager.getString("reg_cpcb_license")) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_certnum_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = certAuthority,
                    onValueChange = { certAuthority = it },
                    label = { Text(LocalizationManager.getString("reg_cert_authority")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = certIssueDate,
                        onValueChange = { certIssueDate = it },
                        label = { Text(LocalizationManager.getString("reg_issue_date")) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = certExpiryDate,
                        onValueChange = { certExpiryDate = it },
                        label = { Text(LocalizationManager.getString("reg_expiry_date")) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = acceptedCategories,
                    onValueChange = { acceptedCategories = it },
                    label = { Text(LocalizationManager.getString("reg_accepted_categories")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it },
                        label = { Text(LocalizationManager.getString("reg_daily_capacity")) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = minQty,
                        onValueChange = { minQty = it },
                        label = { Text(LocalizationManager.getString("reg_min_lot_qty")) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (orgName.isNotBlank() && phone.isNotBlank()) {
                            val ratesJson = "{\"Laptops\": 285.0, \"Mobile Phones\": 350.0, \"Circuit Boards\": 430.0, \"Batteries\": 145.0, \"Cables\": 195.0}"
                            viewModel.registerRecycler(
                                orgName = orgName,
                                personName = personName.ifBlank { "Manager" },
                                phone = phone,
                                username = username.ifBlank { "recycler_${System.currentTimeMillis() % 10000}" },
                                pass = password.ifBlank { "password123" },
                                address = address.ifBlank { "Industrial Area" },
                                city = city,
                                state = state,
                                pincode = pincode,
                                certNumber = certNumber,
                                certAuthority = certAuthority,
                                certIssueDate = certIssueDate,
                                certExpiryDate = certExpiryDate,
                                acceptedCategories = acceptedCategories,
                                capacity = capacity.toDoubleOrNull() ?: 5000.0,
                                minQty = minQty.toDoubleOrNull() ?: 10.0,
                                pickupRadius = pickupRadius.toDoubleOrNull() ?: 40.0,
                                ratesJson = ratesJson,
                                onSuccess = { submitted = true }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("submit_recycler_registration"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(LocalizationManager.getString("reg_submit_recycler"), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordDialog(
    initialIdentifier: String,
    onDismiss: () -> Unit,
    onReset: (String, String) -> Unit
) {
    var identifier by remember { mutableStateOf(initialIdentifier) }
    var otpSent by remember { mutableStateOf(false) }
    var enteredOtp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LockReset,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset Password", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                Text(
                    text = "Verify your registered phone or username to reset your access credentials.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text("Phone or Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("forgot_pass_identifier_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (!otpSent) {
                    Button(
                        onClick = {
                            if (identifier.isBlank()) {
                                localError = "Please enter your username or phone number"
                            } else {
                                localError = null
                                otpSent = true
                                enteredOtp = "7829" // Preloaded for immediate validation
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(44.dp).testTag("send_otp_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Verification OTP")
                    }
                } else {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "OTP sent to registered mobile. Verification Code: 7829",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { enteredOtp = it },
                        label = { Text("4-digit OTP") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("forgot_pass_otp_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("forgot_pass_new_password_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("forgot_pass_confirm_password_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                if (localError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = localError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            if (otpSent) {
                Button(
                    onClick = {
                        if (enteredOtp.length < 4) {
                            localError = "Please enter valid 4-digit OTP"
                        } else if (newPassword.isBlank() || newPassword.length < 4) {
                            localError = "Password must be at least 4 characters"
                        } else if (newPassword != confirmPassword) {
                            localError = "Passwords do not match"
                        } else {
                            onReset(identifier, newPassword)
                        }
                    },
                    modifier = Modifier.testTag("submit_reset_password_button")
                ) {
                    Text("Update Password")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
