package com.patrykandpatryk.liftapp.feature.exercises.model

import com.patrykandpatrick.liftapp.navigation.data.ExerciseListRouteData
import com.patrykandpatryk.liftapp.core.text.StringTextFieldState
import com.patrykandpatryk.liftapp.feature.exercises.ui.ExercisesItem

data class ScreenState(
    val mode: ExerciseListRouteData.Mode,
    val query: StringTextFieldState,
    val exercises: List<ExercisesItem> = emptyList(),
    val groupBy: GroupBy = GroupBy.Name,
    val selectedItemCount: Int = 0,
) {
    val pickingMode: Boolean = mode is ExerciseListRouteData.Mode.Pick
}
