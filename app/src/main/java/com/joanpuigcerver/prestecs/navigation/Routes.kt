package com.joanpuigcerver.prestecs.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Rutes de l'aplicació. Cada una és una clau de navegació serialitzable, de manera que la pila
 * de navegació sobreviu a la recreació de l'activitat (per exemple, en girar el mòbil).
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
@Serializable
data object Splash : NavKey

/** Presentació de 3 pàgines del primer arrancament. */
@Serializable
data object Onboarding : NavKey

/** Inici de sessió (simulat). */
@Serializable
data object Login : NavKey

/** Pantalla principal: préstecs oberts per aula. */
@Serializable
data object Home : NavKey

/** Detall d'un préstec; porta l'identificador com a paràmetre. */
@Serializable
data class Detail(val loanId: Long) : NavKey

@Serializable
data object Profile : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object About : NavKey
