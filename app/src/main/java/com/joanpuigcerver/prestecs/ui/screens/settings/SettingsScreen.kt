package com.joanpuigcerver.prestecs.ui.screens.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.BackButton
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import com.joanpuigcerver.prestecs.ui.theme.ThemeMode

/**
 * Pantalla d'ajustos: tema clar, fosc o del sistema; accés a l'idioma i a «Quant a».
 *
 * L'ajust del tema es manté en memòria durant aquesta fase.
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
    onAboutClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.settings_title)) }, navigationIcon = { BackButton(onBack) }) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(stringResource(R.string.theme_title), style = MaterialTheme.typography.titleLarge)
            Column(modifier = Modifier.selectableGroup()) {
                ThemeOption(R.string.theme_system, themeMode == ThemeMode.SYSTEM, onClick = { onThemeModeChange(ThemeMode.SYSTEM) })
                ThemeOption(R.string.theme_light, themeMode == ThemeMode.LIGHT, onClick = { onThemeModeChange(ThemeMode.LIGHT) })
                ThemeOption(R.string.theme_dark, themeMode == ThemeMode.DARK, onClick = { onThemeModeChange(ThemeMode.DARK) })
            }

            // L'idioma per aplicació s'ajusta des del sistema (Android 13 o superior).
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Text(stringResource(R.string.language_title), style = MaterialTheme.typography.titleLarge)
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(R.string.language_open)) }
            }

            TextButton(onClick = onAboutClick) { Text(stringResource(R.string.about_open)) }
        }
    }
}

@Composable
private fun ThemeOption(labelRes: Int, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick, role = Role.RadioButton)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(stringResource(labelRes), modifier = Modifier.padding(start = 16.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@PrestecsPreviews
@Composable
private fun SettingsPreview() {
    PrestecsTheme { SettingsScreen(themeMode = ThemeMode.SYSTEM, onThemeModeChange = {}, onAboutClick = {}, onBack = {}) }
}
