package com.patrykandpatryk.liftapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.patrykandpatrick.liftapp.navigation.Routes
import com.patrykandpatryk.liftapp.core.navigation.ComposeNavigationResultListener
import com.patrykandpatryk.liftapp.core.navigation.NavigationResultListener
import com.patrykandpatryk.liftapp.domain.Constants
import com.patrykandpatryk.liftapp.domain.Constants.Database.ID_NOT_SET
import com.patrykandpatryk.liftapp.feature.bodymeasurementdetails.navigation.BodyMeasurementDetailsNavigator
import com.patrykandpatryk.liftapp.feature.exercise.navigation.ExerciseDetailsNavigator
import com.patrykandpatryk.liftapp.feature.exercises.navigation.ExerciseListNavigator
import com.patrykandpatryk.liftapp.feature.newexercise.navigation.NewExerciseNavigator
import com.patrykandpatryk.liftapp.feature.newroutine.navigation.NewRoutineNavigator
import com.patrykandpatryk.liftapp.feature.onerepmax.OneRepMaxNavigator
import com.patrykandpatryk.liftapp.feature.routine.navigator.RoutineNavigator
import com.patrykandpatryk.liftapp.feature.settings.navigator.SettingsNavigator

@Stable
class MainNavigator(private val navController: NavController) :
    NavigationResultListener by ComposeNavigationResultListener(navController),
    RoutineNavigator,
    NewRoutineNavigator,
    ExerciseDetailsNavigator,
    ExerciseListNavigator,
    NewExerciseNavigator,
    BodyMeasurementDetailsNavigator,
    OneRepMaxNavigator,
    SettingsNavigator {
    override fun back() {
        navController.popBackStack()
    }

    override fun editRoutine(routineId: Long) {
        navController.navigate(Routes.Routine.edit(routineId))
    }

    override fun exercise(exerciseID: Long) {
        navController.navigate(Routes.Exercise.details(exerciseID))
    }

    override fun onExercisesPicked(exerciseIDs: List<Long>) {
        val previousRoute: String = requireNotNull(navController.previousBackStackEntry?.destination?.route)
        navController.getBackStackEntry(previousRoute).savedStateHandle[Constants.Keys.PICKED_EXERCISE_IDS] = exerciseIDs
        navController.popBackStack()
    }

    override fun newExercise() {
        navController.navigate(Routes.Exercise.new())
    }

    override fun editExercise(exerciseID: Long) {
        navController.navigate(Routes.Exercise.edit(exerciseID))
    }

    override fun pickExercises(disabledExerciseIDs: List<Long>) {
        navController.navigate(Routes.Exercise.pick(disabledExerciseIDs))
    }

    override fun newBodyMeasurement(bodyMeasurementId: Long, bodyEntryMeasurementId: Long?) {
        navController.navigate(Routes.BodyMeasurement.newMeasurement(bodyMeasurementId, bodyEntryMeasurementId ?: ID_NOT_SET))
    }
}

@Composable
fun rememberMainNavigator(navController: NavController): MainNavigator =
    remember(navController) { MainNavigator(navController) }
