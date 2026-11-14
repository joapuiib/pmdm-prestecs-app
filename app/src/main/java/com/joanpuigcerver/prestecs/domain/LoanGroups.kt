package com.joanpuigcerver.prestecs.domain

import com.joanpuigcerver.prestecs.data.model.Loan
import com.joanpuigcerver.prestecs.data.model.LoanStatus

/** Préstecs oberts d'una aula, el més antic primer. */
data class RoomGroup(val room: String, val loans: List<Loan>) {
    /** Prestats hui. */
    val active: Int get() = loans.count { !it.isOverdue }

    /** Prestats un dia anterior i sense retornar. */
    val overdue: Int get() = loans.count { it.isOverdue }
}

/**
 * Targetes de la pantalla principal: un grup per aula en ordre alfabètic natural (`2A1` abans de
 * `10B`) i, dins de cada grup, el préstec més antic primer. Els préstecs retornats s'ometen.
 */
fun groupByRoom(loans: List<Loan>): List<RoomGroup> = loans
    .filter { it.status != LoanStatus.RETURNED }
    .groupBy { it.room }
    .map { (room, open) ->
        RoomGroup(room, open.sortedWith(compareBy<Loan> { it.lentAt }.thenBy { it.id }))
    }
    .sortedWith { a, b -> naturalCompare(a.room, b.room) }

/**
 * Filtra els grups pel text de cerca i, si [onlyOverdue], només els no retornats.
 */
fun filterGroups(groups: List<RoomGroup>, query: String, onlyOverdue: Boolean): List<RoomGroup> {
    val needle = query.trim().lowercase()
    if (needle.isEmpty() && !onlyOverdue) return groups
    return groups.mapNotNull { group ->
        val loans = group.loans.filter {
            (needle.isEmpty() || needle in it.searchText) && (!onlyOverdue || it.isOverdue)
        }
        if (loans.isEmpty()) null else group.copy(loans = loans)
    }
}

private val CHUNK = Regex("\\d+|\\D+")

/** Ordre sense distingir majúscules on les xifres consecutives es comparen com a números. */
fun naturalCompare(a: String, b: String): Int {
    val left = CHUNK.findAll(a).map { it.value }.toList()
    val right = CHUNK.findAll(b).map { it.value }.toList()
    for (i in 0 until minOf(left.size, right.size)) {
        val x = left[i]
        val y = right[i]
        val result = if (x[0].isDigit() && y[0].isDigit()) {
            x.toBigInteger().compareTo(y.toBigInteger())
        } else {
            x.compareTo(y, ignoreCase = true)
        }
        if (result != 0) return result
    }
    return left.size.compareTo(right.size)
}
