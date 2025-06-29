package com.patrykandpatryk.liftapp.feature.bodymeasurementlist.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.liftapp.ui.component.LiftAppScaffold
import com.patrykandpatryk.liftapp.core.R
import com.patrykandpatryk.liftapp.core.ui.CompactTopAppBar
import com.patrykandpatryk.liftapp.core.ui.ListItem
import com.patrykandpatryk.liftapp.core.ui.resource.iconRes
import com.patrykandpatryk.liftapp.feature.bodymeasurementlist.model.Action

@Composable
fun BodyMeasurementListScreen(modifier: Modifier = Modifier) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val viewModel: BodyMeasurementListViewModel = hiltViewModel()
    val items by viewModel.items.collectAsState()

    LiftAppScaffold(
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            CompactTopAppBar(
                title = { Text(stringResource(id = R.string.route_body)) },
                scrollBehavior = topAppBarScrollBehavior,
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(items = items, key = { item -> item.bodyMeasurementID }) { item ->
                ListItem(
                    title = item.headline,
                    description = item.supportingText,
                    iconPainter = painterResource(id = item.bodyMeasurementType.iconRes),
                    onClick = { viewModel.onAction(Action.OpenDetails(item.bodyMeasurementID)) },
                )
            }
        }
    }
}
