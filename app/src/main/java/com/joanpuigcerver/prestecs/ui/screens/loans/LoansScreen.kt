package com.joanpuigcerver.prestecs.ui.screens.loans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.data.repository.InMemoryLoanRepository
import com.joanpuigcerver.prestecs.data.model.Loan
import com.joanpuigcerver.prestecs.domain.RoomGroup
import com.joanpuigcerver.prestecs.domain.filterGroups
import com.joanpuigcerver.prestecs.domain.groupByRoom
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.screens.detail.LoanDetailBody
import com.joanpuigcerver.prestecs.ui.components.formatDateTime
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme
import java.time.LocalDate
import kotlinx.coroutines.launch

/**
 * Pantalla principal, amb l'aspecte de l'aplicació de préstecs: cerca, comptadors, una targeta per
 * aula amb els seus préstecs oberts i la barra d'accions inferior.
 *
 * L'estat de la cerca i del filtre es guarda amb `rememberSaveable`. Els botons de nou préstec, nova
 * devolució i històric encara no tenen pantalla: mostren un avís (fase 2).
 *
 * @author Joan Puigcerver
 * @version 0.4.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoansScreen(
    groups: List<RoomGroup>,
    twoPane: Boolean,
    onLoanClick: (Long) -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var onlyOverdue by rememberSaveable { mutableStateOf(false) }
    var selectedId by rememberSaveable { mutableStateOf<Long?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val comingSoon = stringResource(R.string.placeholder_coming)
    val showComingSoon: () -> Unit = { scope.launch { snackbarHostState.showSnackbar(comingSoon) } }

    val visible = filterGroups(groups, query, onlyOverdue)
    val allLoans = groups.flatMap { it.loans }
    val selected = allLoans.firstOrNull { it.id == selectedId }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    OverflowMenu(
                        onHistory = showComingSoon,
                        onProfile = onProfile,
                        onSettings = onSettings,
                        onLogOut = onLogOut,
                        onAbout = onAbout,
                    )
                },
            )
        },
        bottomBar = { ActionsBar(onNewLoan = showComingSoon, onNewReturn = showComingSoon) },
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.weight(if (twoPane) 0.55f else 1f)) {
                Cards(
                    // El cercador i els comptadors es desplacen amb les targetes.
                    header = {
                        SearchField(query = query, onQueryChange = { query = it })
                        StatsRow(
                            activeCount = allLoans.count { !it.isOverdue },
                            overdueCount = allLoans.count { it.isOverdue },
                            onlyOverdue = onlyOverdue,
                            onToggleOverdue = { onlyOverdue = !onlyOverdue },
                        )
                    },
                    groups = visible,
                    isFiltering = query.isNotBlank() || onlyOverdue,
                    selectedId = if (twoPane) selectedId else null,
                    onLoanClick = { id -> if (twoPane) selectedId = id else onLoanClick(id) },
                )
            }
            if (twoPane) {
                VerticalDivider(modifier = Modifier.fillMaxHeight())
                Box(modifier = Modifier.weight(0.45f).fillMaxHeight()) {
                    if (selected != null) {
                        LoanDetailBody(selected, modifier = Modifier.padding(16.dp))
                    } else {
                        Text(
                            text = stringResource(R.string.detail_select),
                            modifier = Modifier.align(Alignment.Center).padding(32.dp),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text(stringResource(R.string.loans_search_hint)) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.loans_search_clear),
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
    )
}

/** Comptador de préstecs actius i xip que filtra els no retornats. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatsRow(activeCount: Int, overdueCount: Int, onlyOverdue: Boolean, onToggleOverdue: () -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.loans_stat_active, activeCount), style = MaterialTheme.typography.bodyMedium)
        FilterChip(
            selected = onlyOverdue,
            onClick = onToggleOverdue,
            label = {
                Text(
                    stringResource(R.string.loans_stat_overdue, overdueCount),
                    color = if (overdueCount > 0) MaterialTheme.colorScheme.error else Color.Unspecified,
                )
            },
        )
    }
}

@Composable
private fun Cards(
    header: @Composable () -> Unit,
    groups: List<RoomGroup>,
    isFiltering: Boolean,
    selectedId: Long?,
    onLoanClick: (Long) -> Unit,
) {
    // Una columna al mòbil, més en una tauleta.
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        item(key = "header", span = StaggeredGridItemSpan.FullLine) {
            Column { header() }
        }
        if (groups.isEmpty()) {
            item(key = "empty", span = StaggeredGridItemSpan.FullLine) {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(if (isFiltering) R.string.loans_no_results else R.string.loans_none_active),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
        items(groups, key = { it.room }) { group ->
            RoomCard(group = group, selectedId = selectedId, onLoanClick = onLoanClick)
        }
    }
}

@Composable
private fun RoomCard(group: RoomGroup, selectedId: Long?, onLoanClick: (Long) -> Unit) {
    OutlinedCard(Modifier.fillMaxWidth()) {
        RoomHeader(group)
        if (group.loans.isEmpty()) {
            HorizontalDivider()
            Text(
                stringResource(R.string.room_empty),
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        group.loans.forEach { loan ->
            HorizontalDivider()
            LoanRow(loan = loan, selected = loan.id == selectedId, onClick = { onLoanClick(loan.id) })
        }
    }
}

@Composable
private fun RoomHeader(group: RoomGroup) {
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_school),
            contentDescription = stringResource(R.string.room_image_description, group.room),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp).align(Alignment.CenterVertically),
        )
        Text(
            group.room.ifEmpty { stringResource(R.string.loans_no_aula) },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
        )
        if (group.overdue > 0) {
            Text(
                pluralStringResource(R.plurals.loans_aula_overdue, group.overdue, group.overdue),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.error,
            )
        }
        if (group.overdue > 0 && group.active > 0) {
            Text("·", style = MaterialTheme.typography.labelMedium, color = muted)
        }
        if (group.active > 0) {
            Text(
                pluralStringResource(R.plurals.loans_aula_active, group.active, group.active),
                style = MaterialTheme.typography.labelMedium,
                color = muted,
            )
        }
    }
}

/** Fila d'un préstec: portàtil, estudiant, professorat i antiguitat. És una peça sense estat. */
@Composable
private fun LoanRow(loan: Loan, selected: Boolean, onClick: () -> Unit) {
    val prestec = loan
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                prestec.laptop,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified,
            )
            PersonLine(R.drawable.ic_school, stringResource(R.string.loans_student), prestec.student)
            PersonLine(R.drawable.ic_person, stringResource(R.string.loans_professor), prestec.teacher)
        }
        Column(horizontalAlignment = Alignment.End) {
            // En roig, tot el que és anterior a hui: està sense retornar.
            Text(
                if (loan.ageDays <= 0) {
                    stringResource(R.string.loans_age_today)
                } else {
                    pluralStringResource(R.plurals.loans_age_days, loan.ageDays, loan.ageDays)
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (loan.ageDays > 0) FontWeight.SemiBold else FontWeight.Normal,
                color = if (loan.ageDays > 0) MaterialTheme.colorScheme.error else muted,
            )
            Text(formatDateTime(prestec.lentAt), style = MaterialTheme.typography.bodySmall, color = muted)
        }
    }
}

/** Línia xicoteta i atenuada amb una icona que indica el rol. */
@Composable
private fun PersonLine(icon: Int, role: String, name: String) {
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(painterResource(icon), contentDescription = role, tint = muted, modifier = Modifier.size(14.dp))
        Text(name, style = MaterialTheme.typography.bodySmall, color = muted, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

/** Icona al principi del text d'un botó. */
@Composable
private fun RowScope.ButtonIcon(icon: Int) {
    Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(ButtonDefaults.IconSize))
    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
}

@Composable
private fun ActionsBar(onNewLoan: () -> Unit, onNewReturn: () -> Unit) {
    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = onNewLoan, modifier = Modifier.weight(1f)) {
                ButtonIcon(R.drawable.ic_laptop)
                Text(stringResource(R.string.new_loan))
            }
            FilledTonalButton(onClick = onNewReturn, modifier = Modifier.weight(1f)) {
                ButtonIcon(R.drawable.ic_undo)
                Text(stringResource(R.string.new_return))
            }
        }
    }
}

@Composable
private fun OverflowMenu(
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onLogOut: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(painterResource(R.drawable.ic_more_vert), contentDescription = stringResource(R.string.action_more))
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        MenuItem(R.string.loans_history, R.drawable.ic_history, onHistory) { expanded = false }
        MenuItem(R.string.menu_profile, R.drawable.ic_person, onProfile) { expanded = false }
        MenuItem(R.string.menu_settings, null, onSettings) { expanded = false }
        MenuItem(R.string.menu_about, null, onAbout) { expanded = false }
        MenuItem(R.string.action_log_out, null, onLogOut) { expanded = false }
    }
}

@Composable
private fun MenuItem(labelRes: Int, icon: Int?, onClick: () -> Unit, close: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(labelRes)) },
        leadingIcon = icon?.let { { Icon(painterResource(it), contentDescription = null) } },
        onClick = {
            close()
            onClick()
        },
    )
}

@PrestecsPreviews
@Composable
private fun LoansPreview() {
    val today = LocalDate.of(2026, 11, 6)
    PrestecsTheme {
        LoansScreen(
            groups = groupByRoom(InMemoryLoanRepository(today).getActiveLoans()),
            twoPane = false,
            onLoanClick = {},
            onProfile = {},
            onSettings = {},
            onAbout = {},
            onLogOut = {},
        )
    }
}
