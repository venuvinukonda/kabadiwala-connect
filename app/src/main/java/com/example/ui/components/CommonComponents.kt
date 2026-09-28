package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.service.localization.AppLanguage
import com.example.service.localization.LocalizationManager
import com.example.ui.theme.*

@Composable
fun ListenButton(
    textToSpeak: String,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit,
    isSpeaking: Boolean,
    modifier: Modifier = Modifier,
    label: String = LocalizationManager.getString("listen")
) {
    FilledTonalButton(
        onClick = {
            if (isSpeaking) onStop() else onSpeak(textToSpeak)
        },
        modifier = modifier
            .minimumInteractiveComponentSize()
            .testTag("listen_button"),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isSpeaking) WarmOrangeContainer else DeepGreenContainer,
            contentColor = if (isSpeaking) WarmOrangeOnContainer else DeepGreenOnContainer
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeUp,
            contentDescription = LocalizationManager.getString("listen"),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isSpeaking) LocalizationManager.getString("playing") else label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ScreenVoiceBanner(
    title: String,
    narration: String,
    onSpeak: (String) -> Unit,
    onStop: () -> Unit,
    isSpeaking: Boolean,
    speechRate: Float,
    onRateChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onPause: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onReplay: (() -> Unit)? = null
) {
    var showSpeedDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("screen_voice_banner"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Hearing,
                            contentDescription = LocalizationManager.getString("voice_assistance"),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isSpeaking) LocalizationManager.getString("playing") else LocalizationManager.getString("listen"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(
                    onClick = { showSpeedDialog = true },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text(
                        text = "${speechRate}x",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Replay button if available
                if (onReplay != null) {
                    IconButton(
                        onClick = onReplay,
                        modifier = Modifier.size(36.dp).minimumInteractiveComponentSize().testTag("banner_replay_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = LocalizationManager.getString("replay"),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Play / Stop main button
                FilledIconButton(
                    onClick = {
                        if (isSpeaking) onStop() else onSpeak(narration)
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .minimumInteractiveComponentSize()
                        .testTag("banner_speak_button"),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isSpeaking) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                        contentDescription = if (isSpeaking) LocalizationManager.getString("stop") else LocalizationManager.getString("play"),
                        tint = Color.White
                    )
                }
            }
        }
    }

    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = { Text(LocalizationManager.getString("speech_speed"), fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    listOf(
                        0.8f to "0.8x (${LocalizationManager.getString("speed")})",
                        1.0f to "1.0x (${LocalizationManager.getString("speech_speed")})",
                        1.25f to "1.25x (${LocalizationManager.getString("speed")})"
                    ).forEach { (rate, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onRateChange(rate)
                                    showSpeedDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = speechRate == rate, onClick = {
                                onRateChange(rate)
                                showSpeedDialog = false
                            })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, fontWeight = if (speechRate == rate) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) { Text(LocalizationManager.getString("close")) }
            }
        )
    }
}

@Composable
fun StatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.uppercase()) {
        "APPROVED", "COMPLETED", "PAID", "SYNCED" -> SuccessGreen.copy(alpha = 0.15f) to SuccessGreen
        "PENDING", "PENDING_VERIFICATION", "REQUESTED", "DRAFT" -> WarningAmber.copy(alpha = 0.2f) to Color(0xFFB76400)
        "ACCEPTED", "SCHEDULED", "RECORDED" -> TechBlueTertiary.copy(alpha = 0.15f) to TechBlueTertiary
        "REJECTED", "SUSPENDED", "FAILED", "EXPIRED", "DISPUTED" -> DangerRed.copy(alpha = 0.15f) to DangerRed
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    val displayLabel = LocalizationManager.getStatusLabel(status)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = displayLabel,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun OfflineNotificationBar(
    isOnline: Boolean,
    pendingSyncCount: Int,
    onToggleOnline: () -> Unit,
    onSyncNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(visible = !isOnline || pendingSyncCount > 0) {
        Surface(
            color = if (!isOnline) WarningAmber.copy(alpha = 0.9f) else SuccessGreen.copy(alpha = 0.9f),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (!isOnline) Icons.Default.CloudOff else Icons.Default.CloudSync,
                        contentDescription = "Sync State",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (!isOnline)
                            "${LocalizationManager.getString("offline_mode")} ($pendingSyncCount)"
                        else
                            "$pendingSyncCount ${LocalizationManager.getString("sync")}",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = onToggleOnline,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                    ) {
                        Text(if (!isOnline) "Online" else "Offline", fontSize = 11.sp)
                    }
                    if (isOnline && pendingSyncCount > 0) {
                        Button(
                            onClick = onSyncNow,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = SuccessGreen),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(LocalizationManager.getString("sync"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KabadiwalaTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    onLanguageClick: () -> Unit,
    currentLang: AppLanguage,
    onLogoutClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PureWhiteSurface,
            titleContentColor = DeepGreenPrimary,
            navigationIconContentColor = DeepGreenPrimary,
            actionIconContentColor = DeepGreenPrimary
        ),
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("topbar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(start = 12.dp, end = 4.dp).size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Recycling,
                            contentDescription = "Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        actions = {
            // Language selector button
            FilledTonalButton(
                onClick = onLanguageClick,
                modifier = Modifier
                    .height(36.dp)
                    .minimumInteractiveComponentSize()
                    .testTag("topbar_language_button"),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Change Language",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentLang.nativeName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            actions()

            if (onLogoutClick != null) {
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.minimumInteractiveComponentSize().testTag("topbar_logout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

@Composable
fun LanguageDialog(
    currentLang: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = LocalizationManager.getString("select_language"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    AppLanguage.entries.forEach { lang ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onSelectLanguage(lang)
                                    onDismiss()
                                }
                                .testTag("lang_option_${lang.code}"),
                            colors = CardDefaults.cardColors(
                                containerColor = if (currentLang == lang)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = lang.nativeName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = if (currentLang == lang)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = lang.displayName,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (currentLang == lang) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("language_dialog_cancel_button")
            ) {
                Text(LocalizationManager.getString("cancel"), fontWeight = FontWeight.Bold)
            }
        }
    )
}
