package com.joanpuigcerver.prestecs.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.data.model.LoanStatus

/** Text de l'estat del préstec. */
@Composable
fun LoanStatus.label(): String = stringResource(
    when (this) {
        LoanStatus.LENT -> R.string.estat_prestat
        LoanStatus.NOT_RETURNED -> R.string.estat_no_retornat
        LoanStatus.RETURNED -> R.string.estat_retornat
    },
)

private data class BadgeColors(val container: Color, val content: Color)

private val LightBadges = mapOf(
    LoanStatus.LENT to BadgeColors(Color(0xFFD1E7DD), Color(0xFF0F5132)),
    LoanStatus.NOT_RETURNED to BadgeColors(Color(0xFFF8D7DA), Color(0xFF842029)),
    LoanStatus.RETURNED to BadgeColors(Color(0xFFE2E3E5), Color(0xFF41464B)),
)

private val DarkBadges = mapOf(
    LoanStatus.LENT to BadgeColors(Color(0xFF1E3A2B), Color(0xFFA3CFBB)),
    LoanStatus.NOT_RETURNED to BadgeColors(Color(0xFF4A1F24), Color(0xFFF1AEB5)),
    LoanStatus.RETURNED to BadgeColors(Color(0xFF2B2F33), Color(0xFFC4C8CB)),
)

/** Etiqueta de color amb l'estat d'un préstec. */
@Composable
fun StatusBadge(status: LoanStatus, modifier: Modifier = Modifier) {
    val dark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val colors = (if (dark) DarkBadges else LightBadges).getValue(status)
    Surface(
        modifier = modifier,
        color = colors.container,
        contentColor = colors.content,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = status.label(),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}
