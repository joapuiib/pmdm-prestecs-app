package com.joanpuigcerver.prestecs.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla de benvinguda: el logo apareix amb una animació d'escala i opacitat i, en acabar,
 * es crida [onFinished].
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
@Composable
fun SplashScreen(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    val currentOnFinished by rememberUpdatedState(onFinished)

    LaunchedEffect(Unit) {
        // Es llança en paral·lel: l'escala i l'opacitat comencen al mateix temps.
        launch { scale.animateTo(1f, tween(durationMillis = 700, easing = FastOutSlowInEasing)) }
        alpha.animateTo(1f, tween(durationMillis = 700))
        delay(500)
        currentOnFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.alpha(alpha.value).scale(scale.value),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(160.dp),
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@PrestecsPreviews
@Composable
private fun SplashPreview() {
    PrestecsTheme { SplashScreen(onFinished = {}) }
}
