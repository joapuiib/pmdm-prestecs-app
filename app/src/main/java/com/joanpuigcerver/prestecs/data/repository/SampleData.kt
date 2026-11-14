package com.joanpuigcerver.prestecs.data.repository

import com.joanpuigcerver.prestecs.data.model.Loan
import com.joanpuigcerver.prestecs.data.model.LoanStatus
import java.time.LocalDate

/**
 * Dades d'exemple en memòria: 14 préstecs en 6 aules. Les persones són inventades.
 *
 * Les dates es calculen respecte a un dia donat, de manera que sempre hi ha préstecs de hui, d'ahir
 * i d'ara fa uns dies (els que apareixen com a «no retornats»).
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
object SampleData {

    /** Retorna els préstecs d'exemple, amb les dates calculades a partir de [today]. */
    fun loans(today: LocalDate): List<Loan> {
        fun loan(
            id: Long,
            room: String,
            cart: String,
            code: String,
            student: String,
            teacher: String,
            daysAgo: Long,
            hour: Int,
            returnedDaysAgo: Long? = null,
        ): Loan {
            val lentAt = today.minusDays(daysAgo).atTime(hour, 15)
            val returnedAt = returnedDaysAgo?.let { today.minusDays(it).atTime(14, 30) }
            val status = when {
                returnedAt != null -> LoanStatus.RETURNED
                daysAgo > 0 -> LoanStatus.NOT_RETURNED
                else -> LoanStatus.LENT
            }
            return Loan(id, room, cart, "$cart - $code", student, teacher, lentAt, returnedAt, status)
        }

        return listOf(
            loan(1, "2A1", "2A1", "P03", "12345678 - Garcia, Maria", "Joan", daysAgo = 0, hour = 8),
            loan(2, "2A1", "2A1", "P07", "23456789 - Soler, Pau", "Joan", daysAgo = 3, hour = 9),
            loan(3, "2A1", "2A1", "P11", "34567890 - Climent, Laia", "Anna", daysAgo = 0, hour = 11),
            loan(4, "2A4", "C2A4", "P01", "45678901 - Gil, Enric", "Anna", daysAgo = 0, hour = 8),
            loan(5, "2A4", "C2A4", "P02", "56789012 - Vidal, Núria", "Pere", daysAgo = 1, hour = 10),
            loan(6, "2A4", "C2A4", "P09", "67890123 - Pons, Carles", "Vicent", daysAgo = 6, hour = 12),
            loan(7, "3B2", "3B2", "P05", "78901234 - Roig, Aina", "Vicent", daysAgo = 0, hour = 9),
            loan(8, "1A3", "C1A3", "P04", "89012345 - Mas, Joan", "Joan", daysAgo = 2, hour = 8),
            loan(9, "1A3", "C1A3", "P08", "90123456 - Bosch, Clara", "Anna", daysAgo = 0, hour = 10),
            loan(10, "10B", "10B", "P02", "01234567 - Mora, Irene", "Pere", daysAgo = 4, hour = 11),
            loan(11, "Biblioteca", "CBIB", "P06", "11223344 - Navarro, Hugo", "Vicent", daysAgo = 0, hour = 12),
            loan(12, "2A1", "2A1", "P01", "22334455 - Part, Alba", "Joan", daysAgo = 5, hour = 9, returnedDaysAgo = 4),
            loan(13, "3B2", "3B2", "P02", "33445566 - Ferrer, Oriol", "Anna", daysAgo = 2, hour = 10, returnedDaysAgo = 1),
            loan(14, "2A4", "C2A4", "P05", "44556677 - Cano, Sara", "Pere", daysAgo = 1, hour = 8, returnedDaysAgo = 0),
        )
    }
}
