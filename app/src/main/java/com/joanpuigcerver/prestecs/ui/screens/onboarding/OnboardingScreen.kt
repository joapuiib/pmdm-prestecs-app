package com.joanpuigcerver.prestecs.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import kotlinx.coroutines.launch

private const val PAGE_COUNT = 3

/**
 * Presentació de 3 pàgines que s'ensenya la primera vegada. Es pot saltar en qualsevol moment i, tant si
 * s'acaba com si se salta, es continua amb la pantalla d'inici de sessió.
 *
 * @author Joan Puigcerver
 * @version 0.6.0
 */
@Composable
fun OnboardingScreen(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val scope = rememberCoroutineScope()
    val isLast = pagerState.currentPage == PAGE_COUNT - 1

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onFinished) { Text(stringResource(R.string.button_skip)) }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
                OnboardingPage(page = page)
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PageIndicator(current = pagerState.currentPage, count = PAGE_COUNT)
                Button(onClick = {
                    if (isLast) {
                        onFinished()
                    } else {
                        scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                    }
                }) {
                    Text(stringResource(if (isLast) R.string.button_start else R.string.button_next))
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(page: Int, modifier: Modifier = Modifier) {
    val (title, text) = when (page) {
        0 -> R.string.onboarding_1_title to R.string.onboarding_1_text
        1 -> R.string.onboarding_2_title to R.string.onboarding_2_text
        else -> R.string.onboarding_3_title to R.string.onboarding_3_text
    }
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(title), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.size(16.dp))
        Text(stringResource(text), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}

@Composable
private fun PageIndicator(current: Int, count: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    ),
            )
        }
    }
}

@PrestecsPreviews
@Composable
private fun OnboardingPreview() {
    PrestecsTheme { OnboardingScreen(onFinished = {}) }
}
