package com.joanpuigcerver.prestecs.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joanpuigcerver.prestecs.R
import com.joanpuigcerver.prestecs.ui.components.BackButton
import com.joanpuigcerver.prestecs.ui.components.PrestecsPreviews
import com.joanpuigcerver.prestecs.ui.theme.PrestecsTheme

/**
 * Pantalla de perfil: nom i resum dels préstecs de la persona.
 *
 * @author Joan Puigcerver
 * @version 0.3.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    activeLoans: Int,
    overdueLoans: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.profile_title)) }, navigationIcon = { BackButton(onBack) }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ic_avatar),
                        contentDescription = stringResource(R.string.profile_avatar),
                        modifier = Modifier.size(72.dp),
                    )
                    Column {
                        Text(
                            text = stringResource(
                                R.string.profile_greeting,
                                userName,
                            ),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = pluralStringResource(R.plurals.my_active_loans, activeLoans, activeLoans),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = stringResource(R.string.loans_stat_overdue, overdueLoans),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (overdueLoans > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@PrestecsPreviews
@Composable
private fun ProfilePreview() {
    PrestecsTheme {
        ProfileScreen(
            userName = "Joan",
            activeLoans = 2,
            overdueLoans = 1,
            onBack = {},
        )
    }
}
