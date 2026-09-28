package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Distinctive Light Green and White Color Scheme
private val LightGreenAndWhiteColorScheme = lightColorScheme(
  primary = DeepGreenPrimary,
  onPrimary = DeepGreenOnPrimary,
  primaryContainer = DeepGreenContainer,
  onPrimaryContainer = DeepGreenOnContainer,
  secondary = WarmOrangeSecondary,
  onSecondary = WarmOrangeOnSecondary,
  secondaryContainer = WarmOrangeContainer,
  onSecondaryContainer = WarmOrangeOnContainer,
  tertiary = TechBlueTertiary,
  onTertiary = TechBlueOnTertiary,
  tertiaryContainer = TechBlueContainer,
  onTertiaryContainer = TechBlueOnContainer,
  background = LightGreenBackground,
  onBackground = LightGreenOnBackground,
  surface = PureWhiteSurface,
  onSurface = PureWhiteOnSurface,
  surfaceVariant = LightGreenSurfaceVariant,
  onSurfaceVariant = LightGreenOnSurfaceVariant,
  surfaceTint = DeepGreenPrimary,
  outline = LightGreenOutline,
  outlineVariant = LightGreenOutlineVariant
)

private val DarkColorScheme = darkColorScheme(
  primary = DarkGreenPrimary,
  onPrimary = DarkGreenOnPrimary,
  primaryContainer = DarkGreenContainer,
  onPrimaryContainer = DarkGreenOnContainer,
  secondary = DarkOrangeSecondary,
  onSecondary = DarkOrangeOnSecondary,
  secondaryContainer = DarkOrangeContainer,
  onSecondaryContainer = DarkOrangeOnContainer,
  tertiary = DarkBlueTertiary,
  onTertiary = DarkBlueOnTertiary,
  tertiaryContainer = DarkBlueContainer,
  onTertiaryContainer = DarkBlueOnContainer,
  background = DarkBackground,
  onBackground = DarkOnBackground,
  surface = DarkSurface,
  onSurface = DarkOnSurface,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkOnSurfaceVariant,
)

@Composable
fun KabadiwalaConnectTheme(
  darkTheme: Boolean = false, // Set to false by default to showcase the Light Green & White combination
  dynamicColor: Boolean = false, // Preserve brand light green & white palette consistently
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightGreenAndWhiteColorScheme
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      window?.let {
        it.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
