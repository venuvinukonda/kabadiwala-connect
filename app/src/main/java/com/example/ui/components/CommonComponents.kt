package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
                Column {
                    Text(
                        text = LocalizationManager.getString("select_language"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "मराठी, English, हिन्दी, తెలుగు & more",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    val orderedLanguages = listOf(
                        AppLanguage.ENGLISH,
                        AppLanguage.MARATHI,
                        AppLanguage.HINDI,
                        AppLanguage.TELUGU,
                        AppLanguage.TAMIL,
                        AppLanguage.KANNADA,
                        AppLanguage.MALAYALAM,
                        AppLanguage.GUJARATI,
                        AppLanguage.BENGALI,
                        AppLanguage.PUNJABI,
                        AppLanguage.ODIA
                    )

                    orderedLanguages.forEach { lang ->
                        val isSelected = currentLang == lang
                        val isMarathi = lang == AppLanguage.MARATHI
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
                                containerColor = when {
                                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                                    isMarathi -> DeepGreenContainer.copy(alpha = 0.35f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                }
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
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = lang.nativeName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            color = if (isSelected)
                                                MaterialTheme.colorScheme.onPrimaryContainer
                                            else
                                                MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isMarathi) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = DeepGreenPrimary.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = "महाराष्ट्र",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = DeepGreenPrimary,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${lang.displayName} • ${lang.localeTag}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isSelected) {
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

@Composable
fun FloatingAudioPlayerBar(
    currentText: String,
    isSpeaking: Boolean,
    isPaused: Boolean,
    speechRate: Float,
    currentLang: AppLanguage,
    onPlayPause: () -> Unit,
    onReplay: () -> Unit,
    onStop: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("floating_audio_player_bar"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isSpeaking) DeepGreenContainer else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Default.Hearing else Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = if (isSpeaking) DeepGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isSpeaking) LocalizationManager.getString("now_speaking") else LocalizationManager.getString("pause"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSpeaking) DeepGreenPrimary else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.clickable { onLanguageClick() }
                            ) {
                                Text(
                                    text = "${currentLang.nativeName} (${currentLang.code.uppercase()})",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = currentText,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Speed chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable {
                            val nextSpeed = when {
                                speechRate < 0.9f -> 1.0f
                                speechRate < 1.15f -> 1.25f
                                else -> 0.75f
                            }
                            onSpeedChange(nextSpeed)
                        }
                    ) {
                        Text(
                            text = "${speechRate}x",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        )
                    }

                    // Replay button
                    IconButton(
                        onClick = onReplay,
                        modifier = Modifier.size(36.dp).testTag("audio_player_replay_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = LocalizationManager.getString("replay"),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Play/Pause button
                    IconButton(
                        onClick = onPlayPause,
                        modifier = Modifier.size(36.dp).testTag("audio_player_play_pause_btn")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isSpeaking) LocalizationManager.getString("pause") else LocalizationManager.getString("play"),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Stop/Close button
                    IconButton(
                        onClick = onStop,
                        modifier = Modifier.size(36.dp).testTag("audio_player_stop_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = LocalizationManager.getString("stop"),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceUnavailableDialog(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.VolumeOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = LocalizationManager.getString("voice_unavailable_title"),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = message,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onRetry,
                modifier = Modifier.testTag("voice_unavailable_retry_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(LocalizationManager.getString("retry"), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("voice_unavailable_dismiss_btn")
            ) {
                Text(LocalizationManager.getString("close"), fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Custom smooth Material 3 vertical scrollbar for regular scrollable columns.
 * Displays a visible track and rounded thumb when content overflows.
 */
fun Modifier.verticalScrollbar(
    scrollState: ScrollState,
    color: Color = DeepGreenPrimary,
    thickness: Dp = 6.dp,
    padding: Dp = 3.dp
): Modifier = this.drawWithContent {
    drawContent()
    val maxScroll = scrollState.maxValue.toFloat()
    if (maxScroll > 0) {
        val viewportHeight = size.height
        val scroll = scrollState.value.toFloat()
        val contentHeight = viewportHeight + maxScroll
        val thumbHeight = (viewportHeight / contentHeight * viewportHeight).coerceIn(48f, viewportHeight)
        val scrollRatio = (scroll / maxScroll).coerceIn(0f, 1f)
        val thumbOffset = scrollRatio * (viewportHeight - thumbHeight)

        // Semi-transparent track
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.08f),
            topLeft = Offset(size.width - thickness.toPx() - padding.toPx(), 0f),
            size = Size(thickness.toPx(), viewportHeight),
            cornerRadius = CornerRadius(thickness.toPx() / 2, thickness.toPx() / 2)
        )

        // Active thumb
        drawRoundRect(
            color = color,
            topLeft = Offset(size.width - thickness.toPx() - padding.toPx(), thumbOffset),
            size = Size(thickness.toPx(), thumbHeight),
            cornerRadius = CornerRadius(thickness.toPx() / 2, thickness.toPx() / 2),
            alpha = 0.9f
        )
    }
}

/**
 * Custom smooth Material 3 vertical scrollbar for LazyColumn lists.
 * Displays a visible track and rounded thumb when content overflows.
 */
fun Modifier.verticalScrollbar(
    lazyListState: LazyListState,
    color: Color = DeepGreenPrimary,
    thickness: Dp = 6.dp,
    padding: Dp = 3.dp
): Modifier = this.drawWithContent {
    drawContent()
    val layoutInfo = lazyListState.layoutInfo
    val totalItems = layoutInfo.totalItemsCount
    val visibleItems = layoutInfo.visibleItemsInfo
    if (totalItems > 0 && visibleItems.isNotEmpty()) {
        val firstVisible = visibleItems.first().index
        val visibleCount = visibleItems.size
        if (visibleCount < totalItems) {
            val viewportHeight = size.height
            val thumbHeight = ((visibleCount.toFloat() / totalItems) * viewportHeight).coerceIn(48f, viewportHeight)
            val scrollRatio = (firstVisible.toFloat() / (totalItems - visibleCount)).coerceIn(0f, 1f)
            val thumbOffset = scrollRatio * (viewportHeight - thumbHeight)

            // Semi-transparent track
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.08f),
                topLeft = Offset(size.width - thickness.toPx() - padding.toPx(), 0f),
                size = Size(thickness.toPx(), viewportHeight),
                cornerRadius = CornerRadius(thickness.toPx() / 2, thickness.toPx() / 2)
            )

            // Active thumb
            drawRoundRect(
                color = color,
                topLeft = Offset(size.width - thickness.toPx() - padding.toPx(), thumbOffset),
                size = Size(thickness.toPx(), thumbHeight),
                cornerRadius = CornerRadius(thickness.toPx() / 2, thickness.toPx() / 2),
                alpha = 0.9f
            )
        }
    }
}
