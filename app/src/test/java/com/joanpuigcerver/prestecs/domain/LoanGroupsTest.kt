package com.joanpuigcerver.prestecs.domain

import com.joanpuigcerver.prestecs.data.model.LoanKind
import com.joanpuigcerver.prestecs.data.model.LoanStatus
import com.joanpuigcerver.prestecs.data.repository.InMemoryLoanRepository
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Proves de l'agrupació i la cerca de préstecs.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
class LoanGroupsTest {

    private val today = LocalDate.of(2026, 11, 6)
    private val loans = InMemoryLoanRepository(today).getLoans()
    private val groups = groupByRoom(loans)

    @Test
    fun `els prestecs retornats no apareixen a les targetes`() {
        assertTrue(groups.flatMap { it.loans }.none { it.isReturned })
    }

    @Test
    fun `les aules s'ordenen en ordre natural`() {
        val rooms = groupByRoom(
            loans.map { it.copy(room = if (it.id == 1L) "10B" else if (it.id == 2L) "2A1" else it.room) },
        ).map { it.room }
        assertTrue(rooms.indexOf("2A1") < rooms.indexOf("10B"))
    }

    @Test
    fun `els prestecs d'un dia anterior compten com a no retornats`() {
        val overdue = groups.flatMap { it.loans }.filter { it.isOverdue }
        assertTrue(overdue.isNotEmpty())
        assertTrue(overdue.all { it.ageDays > 0 })
    }

    @Test
    fun `la cerca no distingeix majuscules i troba l'estudiant`() {
        val result = filterGroups(groups, "GARCIA", onlyOverdue = false)
        assertEquals(1, result.flatMap { it.loans }.size)
    }

    @Test
    fun `el filtre de no retornats elimina els grups buits`() {
        val result = filterGroups(groups, "", onlyOverdue = true)
        assertTrue(result.all { group -> group.loans.all { it.isOverdue } })
        assertTrue(result.size < groups.size)
    }

    @Test
    fun `naturalCompare compara les xifres com a numeros`() {
        assertTrue(naturalCompare("2A1", "10B") < 0)
        assertTrue(naturalCompare("10B", "2A1") > 0)
        assertEquals(0, naturalCompare("2a1", "2A1"))
    }

    @Test
    fun `el tipus de prestec es dedueix del carro`() {
        val fixed = loans.first { it.cart == it.room }
        val cart = loans.first { it.cart != it.room }
        assertEquals(LoanKind.FixedInRoom, fixed.kind)
        assertEquals(LoanKind.FromCart(cart.cart), cart.kind)
    }

    @Test
    fun `copy marca un prestec com a retornat sense mutar l'original`() {
        val loan = loans.first { it.status == LoanStatus.NOT_RETURNED }
        val returned = loan.copy(status = LoanStatus.RETURNED, returnedAt = today.atTime(10, 0))
        assertTrue(returned.isReturned)
        assertFalse(loan.isReturned)
    }
}
