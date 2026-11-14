package com.joanpuigcerver.prestecs.ui.screens.about

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.BackButton
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme

/**
 * Pantalla «Quant a»: autoria, versió i crèdits.
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val version = remember {
        try {
            with(context) { packageManager.getPackageInfo(packageName, 0).versionName.orEmpty() }
        } catch (_: PackageManager.NameNotFoundException) {
            ""
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_title)) },
                navigationIcon = { BackButton(onBack) },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(24.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_avatar),
                contentDescription = stringResource(R.string.about_photo),
                modifier = Modifier.size(120.dp),
            )
            Text(stringResource(R.string.about_author), style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Text(stringResource(R.string.about_author_role), style = MaterialTheme.typography.bodyLarge)
            Text(stringResource(R.string.about_version, version), style = MaterialTheme.typography.labelLarge)
            Text(
                stringResource(R.string.about_credits_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            )
            Text(stringResource(R.string.about_credits_text), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.fillMaxWidth())
        }
    }
}

@PrestecsPreviews
@Composable
private fun AboutPreview() {
    PrestecsTheme { AboutScreen(onBack = {}) }
}
