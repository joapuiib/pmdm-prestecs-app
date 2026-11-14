package com.joanpuigcerver.prestecs.ui.components

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Previsualitzacions habituals: mode clar, mode fosc i cada idioma.
 *
 * S'aplica amb `@PrestecsPreviews` sobre qualsevol funció de previsualització.
 */
@Preview(name = "Clar", showBackground = true)
@Preview(name = "Fosc", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Valencià", showBackground = true, locale = "ca")
@Preview(name = "Castellà", showBackground = true, locale = "es")
@Preview(name = "Lletra gran", showBackground = true, fontScale = 1.6f)
annotation class PrestecsPreviews
