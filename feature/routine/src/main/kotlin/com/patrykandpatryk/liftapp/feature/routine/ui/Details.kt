package com.patrykandpatryk.liftapp.feature.routine.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.patrykandpatryk.liftapp.core.model.Unfold
import com.patrykandpatryk.liftapp.core.ui.ListItem
import com.patrykandpatryk.liftapp.core.ui.dimens.LocalDimens
import com.patrykandpatryk.liftapp.feature.routine.model.ScreenState

@Composable
internal fun Details(modifier: Modifier = Modifier) {
    val viewModel: RoutineViewModel = hiltViewModel()

    val loadableState by viewModel.screenState.collectAsState()

    loadableState.Unfold { state -> Details(modifier = modifier, state = state) }
}

@Composable
private fun Details(state: ScreenState, modifier: Modifier = Modifier) {
    val dimens = LocalDimens.current
    val muscleDimens = dimens.muscle

    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = muscleDimens.gridCellMinSize),
        contentPadding =
            PaddingValues(
                horizontal = dimens.padding.contentHorizontal,
                vertical = dimens.padding.contentVertical,
            ),
        horizontalArrangement = Arrangement.spacedBy(muscleDimens.listItemHorizontalMargin),
    ) {
        item(key = state.imagePath, span = { GridItemSpan(maxLineSpan) }) {
            AsyncImage(
                modifier =
                    Modifier.aspectRatio(ratio = 1f)
                        .fillMaxWidth()
                        .padding(vertical = dimens.padding.contentVertical),
                model = state.imagePath,
                contentDescription = null,
            )
        }

        items(items = state.muscles, key = { it.muscle }) { muscleModel ->
            ListItem(
                title = { Text(stringResource(id = muscleModel.nameRes)) },
                description = { Text(stringResource(id = muscleModel.type.nameRes)) },
                icon = {
                    Box(
                        modifier =
                            Modifier.size(muscleDimens.tileSize)
                                .background(
                                    color = colorResource(id = muscleModel.type.colorRes),
                                    shape = RoundedCornerShape(muscleDimens.tileCornerSize),
                                )
                    )
                },
                paddingValues =
                    PaddingValues(vertical = dimens.padding.itemVertical, horizontal = 0.dp),
            )
        }
    }
}
