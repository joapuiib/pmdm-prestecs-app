package com.joanpuigcerver.prestecs.data.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Estat d'un préstec.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
enum class LoanStatus {
    /** Prestat hui. */
    LENT,

    /** Prestat un dia anterior i encara sense retornar. */
    NOT_RETURNED,

    /** Portàtil ja retornat. */
    RETURNED,
}

/**
 * Tipus de préstec: portàtil fix de l'aula o portàtil d'un carro.
 */
sealed interface LoanKind {
    /** El portàtil és fix de l'aula. */
    data object FixedInRoom : LoanKind

    /** El portàtil viu en el carro [cart]. */
    data class FromCart(val cart: String) : LoanKind
}

/**
 * Préstec d'un portàtil a una persona.
 */
data class Loan(
    val id: Long,
    val room: String,
    val cart: String,
    val laptop: String,
    val student: String,
    val teacher: String,
    val lentAt: LocalDateTime,
    val returnedAt: LocalDateTime? = null,
    val status: LoanStatus,
) {
    /** Tipus de préstec, deduït de si el carro coincideix amb l'aula. */
    val kind: LoanKind
        get() = if (cart == room) LoanKind.FixedInRoom else LoanKind.FromCart(cart)

    /** Indica si el portàtil ja ha tornat. */
    val isReturned: Boolean get() = status == LoanStatus.RETURNED

    /** Indica si és d'un dia anterior i no s'ha retornat. */
    val isOverdue: Boolean get() = status == LoanStatus.NOT_RETURNED

    /** Dies que fa que es va fer el préstec (0 = hui); mai és negatiu. */
    val ageDays: Int
        get() = ChronoUnit.DAYS.between(lentAt.toLocalDate(), LocalDate.now()).toInt().coerceAtLeast(0)

    /** Text en minúscules sobre el qual actua la cerca. */
    val searchText: String
        get() = listOf(room, cart, laptop, student, teacher.orEmpty()).joinToString(" ").lowercase()
}
