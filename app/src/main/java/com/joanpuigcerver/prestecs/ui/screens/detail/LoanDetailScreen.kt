package com.joanpuigcerver.prestecs.ui.screens.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.data.model.LoanKind
import com.joanpuigcerver.prestecs.data.repository.InMemoryLoanRepository
import com.joanpuigcerver.prestecs.data.model.Loan
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.components.StatusBadge
import com.joanpuigcerver.prestecs.ui.components.formatDateTime
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import java.time.LocalDate

/**
 * Pantalla de detall d'un préstec, amb barra superior i botó de tornada.
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(loan: Loan, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = stringResource(R.string.action_back))
                    }
                },
            )
        },
    ) { padding ->
        LoanDetailBody(loan, modifier = Modifier.padding(padding).padding(16.dp))
    }
}

/** Contingut del detall, sense barra superior: es reutilitza en el panell dret del disseny adaptatiu. */
@Composable
fun LoanDetailBody(loan: Loan, modifier: Modifier = Modifier) {
    val data = loan
    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState()).animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text(data.laptop, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.weight(1f))
            StatusBadge(data.status)
        }
        Text(
            text = when (val kind = data.kind) {
                LoanKind.FixedInRoom -> stringResource(R.string.kind_fixed)
                is LoanKind.FromCart -> stringResource(R.string.kind_cart, kind.cart)
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        HorizontalDivider()
        Field(R.string.detail_room, data.room)
        Field(R.string.loans_student, data.student)
        data.teacher.takeIf { it.isNotBlank() }?.let { Field(R.string.loans_professor, it) }
        HorizontalDivider()
        Text(stringResource(R.string.loans_lent_at, formatDateTime(data.lentAt)), style = MaterialTheme.typography.bodyLarge)
        Text(
            text = if (loan.ageDays <= 0) {
                stringResource(R.string.loans_age_today)
            } else {
                pluralStringResource(R.plurals.loans_age_days, loan.ageDays, loan.ageDays)
            },
            color = if (loan.ageDays > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = data.returnedAt?.let { stringResource(R.string.loans_returned_at, formatDateTime(it)) }
                ?: stringResource(R.string.detail_not_returned_yet),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

/** Parella etiqueta-valor. */
@Composable
private fun Field(labelRes: Int, value: String) {
    Column {
        Text(
            stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@PrestecsPreviews
@Composable
private fun LoanDetailPreview() {
    val today = LocalDate.of(2026, 11, 6)
    val loan = InMemoryLoanRepository(today).getLoans().first { it.id == 2L }
    PrestecsTheme { LoanDetailScreen(loan, onBack = {}) }
}
