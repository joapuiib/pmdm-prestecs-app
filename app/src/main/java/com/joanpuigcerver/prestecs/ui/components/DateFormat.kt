package com.joanpuigcerver.prestecs.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/** Dona format a una data i hora segons l'idioma de l'aplicació. */
@Composable
fun formatDateTime(value: LocalDateTime): String {
    val locale = LocalConfiguration.current.locales[0]
    val formatter = remember(locale) {
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(locale)
    }
    return value.format(formatter)
}
