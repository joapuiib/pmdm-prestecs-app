package com.joanpuigcerver.prestecs.data

import com.joanpuigcerver.prestecs.data.model.LoanStatus
import com.joanpuigcerver.prestecs.data.repository.InMemoryLoanRepository
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Proves del model de dades i del repositori en memòria.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
class InMemoryLoanRepositoryTest {

    private val repository = InMemoryLoanRepository(LocalDate.of(2026, 11, 6))

    @Test
    fun `hi ha almenys 5 aules i 10 prestecs`() {
        assertTrue(repository.getLoans().map { it.room }.distinct().size >= 5)
        assertTrue(repository.getLoans().size >= 10)
    }



    @Test
    fun `els codis de portatil tenen el format CARRO - PORTATIL`() {
        assertTrue(repository.getLoans().all { " - " in it.laptop })
    }

    @Test
    fun `hi ha prestecs en els tres estats`() {
        val statuses = repository.getLoans().map { it.status }.toSet()
        assertEquals(LoanStatus.entries.toSet(), statuses)
    }

    @Test
    fun `els prestecs actius no inclouen els retornats`() {
        val active = repository.getActiveLoans()
        assertTrue(active.isNotEmpty())
        assertTrue(active.none { it.status == LoanStatus.RETURNED })
    }

    @Test
    fun `l'estat es coherent amb les dates`() {
        repository.getLoans().forEach { loan ->
            assertEquals(loan.returnedAt != null, loan.isReturned)
        }
    }
}
