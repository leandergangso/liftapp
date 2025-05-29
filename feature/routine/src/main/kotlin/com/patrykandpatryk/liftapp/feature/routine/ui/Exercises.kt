package com.patrykandpatryk.liftapp.feature.routine.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.liftapp.ui.dimens.dimens
import com.patrykandpatryk.liftapp.core.R
import com.patrykandpatryk.liftapp.core.extension.thenIf
import com.patrykandpatryk.liftapp.core.gestures.onItemYRange
import com.patrykandpatryk.liftapp.core.gestures.rememberItemRanges
import com.patrykandpatryk.liftapp.core.gestures.reorderable
import com.patrykandpatryk.liftapp.core.model.Unfold
import com.patrykandpatryk.liftapp.core.model.getPrettyStringLong
import com.patrykandpatryk.liftapp.core.ui.ListItem
import com.patrykandpatryk.liftapp.core.ui.swipe.SwipeContainer
import com.patrykandpatryk.liftapp.core.ui.swipe.SwipeableDeleteBackground
import com.patrykandpatryk.liftapp.domain.extension.addOrSet
import com.patrykandpatryk.liftapp.domain.routine.RoutineExerciseItem
import com.patrykandpatryk.liftapp.feature.routine.model.Action
import com.patrykandpatryk.liftapp.feature.routine.model.ScreenState
import kotlin.math.roundToInt

@Composable
internal fun Exercises(modifier: Modifier = Modifier) {
    val viewModel: RoutineViewModel = hiltViewModel()

    val loadableState by viewModel.screenState.collectAsStateWithLifecycle()

    loadableState.Unfold { state ->
        Exercises(state = state, onAction = viewModel::handleAction, modifier = modifier)
    }
}

@Composable
private fun Exercises(
    state: ScreenState,
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemRanges = rememberItemRanges()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        itemsIndexed(items = state.exercises, key = { _, exercise -> exercise.id }) {
            index,
            exercise ->
            ListItem(
                index = index,
                itemRanges = itemRanges,
                exercise = exercise,
                exercises = state.exercises,
                onIntent = onAction,
                onItemClick = { onAction(Action.NavigateToExercise(it)) },
                onGoalClick = { onAction(Action.NavigateToExerciseGoal(it)) },
            )
        }
    }
}

@Composable
fun LazyItemScope.ListItem(
    index: Int,
    itemRanges: ArrayList<IntRange>,
    exercise: RoutineExerciseItem,
    exercises: List<RoutineExerciseItem>,
    onIntent: (Action) -> Unit,
    onItemClick: (exerciseID: Long) -> Unit,
    onGoalClick: (exerciseID: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lastDragDelta = remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var yOffset by remember(index) { mutableFloatStateOf(0f + lastDragDelta.floatValue) }

    val dragElevation = MaterialTheme.dimens.elevation.dragElevation
    val swipeElevation = MaterialTheme.dimens.swipe.swipeElevation

    val dragShadow by animateDpAsState(targetValue = if (isDragging) dragElevation else 0.dp)

    SwipeContainer(
        background = { swipeProgress, swipeOffset ->
            SwipeableDeleteBackground(swipeProgress = swipeProgress, swipeOffset = swipeOffset)
        },
        dismissContent = { swipeProgress, _ ->
            val swipeShadow by
                animateDpAsState(targetValue = if (swipeProgress != 0f) swipeElevation else 0.dp)

            ListItem(
                title = { Text(exercise.name) },
                modifier =
                    modifier.shadow(swipeShadow).background(MaterialTheme.colorScheme.surface),
                description = {
                    Text(
                        buildAnnotatedString {
                            append(exercise.goal.getPrettyStringLong(exercise.type))
                            append("\n")
                            append(exercise.muscles)
                        }
                    )
                },
                actions = {
                    IconButton(onClick = { onGoalClick(exercise.id) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_target),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }

                    Icon(
                        modifier =
                            Modifier.padding(start = 8.dp)
                                .reorderable(
                                    itemIndex = index,
                                    itemYOffset = yOffset,
                                    itemRanges = itemRanges,
                                    onIsDragging = { isDragging = it },
                                    onDelta = { delta ->
                                        yOffset += delta
                                        lastDragDelta.floatValue = delta
                                    },
                                    onItemReordered = { from, to ->
                                        onIntent(
                                            Action.Reorder(
                                                exercises = exercises,
                                                from = from,
                                                to = to,
                                            )
                                        )
                                    },
                                ),
                        painter = painterResource(id = R.drawable.ic_drag_handle),
                        contentDescription = null,
                    )
                },
            ) {
                onItemClick(exercise.id)
            }
        },
        onDismiss = { onIntent(Action.DeleteExercise(exercise.id)) },
        modifier =
            Modifier.thenIf(isDragging.not()) { animateItem() }
                .offset { IntOffset(x = 0, y = yOffset.roundToInt()) }
                .shadow(dragShadow)
                .zIndex(if (isDragging) 1f else 0f)
                .background(color = MaterialTheme.colorScheme.surface)
                .onItemYRange { yRange -> itemRanges.addOrSet(index, yRange) },
    )
}
