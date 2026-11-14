package com.joanpuigcerver.prestecs.data.repository

import com.joanpuigcerver.prestecs.data.model.Loan
import java.time.LocalDate

/**
 * Accés a les dades dels préstecs.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
interface LoanRepository {
    /** Retorna tots els préstecs (oberts i retornats). */
    fun getLoans(): List<Loan>

    /** Retorna només els préstecs actius, els que encara no s'han retornat. */
    fun getActiveLoans(): List<Loan>
}

/**
 * Implementació en memòria, basada en [SampleData].
 */
class InMemoryLoanRepository(today: LocalDate = LocalDate.now()) : LoanRepository {
    private val loans = SampleData.loans(today)

    override fun getLoans(): List<Loan> = loans

    override fun getActiveLoans(): List<Loan> = loans.filter { !it.isReturned }
}
