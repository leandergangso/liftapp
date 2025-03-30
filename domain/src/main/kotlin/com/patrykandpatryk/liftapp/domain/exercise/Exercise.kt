package com.patrykandpatryk.liftapp.domain.exercise

import com.patrykandpatryk.liftapp.domain.model.Name
import com.patrykandpatryk.liftapp.domain.muscle.Muscle
import com.patrykandpatryk.liftapp.domain.muscle.MuscleContainer

data class Exercise(
    val id: Long,
    val displayName: String,
    val name: Name,
    val exerciseType: ExerciseType,
    override val primaryMuscles: List<Muscle>,
    override val secondaryMuscles: List<Muscle>,
    override val tertiaryMuscles: List<Muscle>,
) : MuscleContainer {

    fun update(
        id: Long = this.id,
        name: Name = this.name,
        mainMuscles: List<Muscle> = this.primaryMuscles,
        secondaryMuscles: List<Muscle> = this.secondaryMuscles,
        tertiaryMuscles: List<Muscle> = this.tertiaryMuscles,
    ): Update =
        Update(
            id = id,
            name = name,
            mainMuscles = mainMuscles,
            secondaryMuscles = secondaryMuscles,
            tertiaryMuscles = tertiaryMuscles,
        )

    data class Insert(
        val name: Name,
        val exerciseType: ExerciseType,
        val mainMuscles: List<Muscle>,
        val secondaryMuscles: List<Muscle> = emptyList(),
        val tertiaryMuscles: List<Muscle> = emptyList(),
    )

    data class Update(
        val id: Long,
        val name: Name,
        val mainMuscles: List<Muscle>,
        val secondaryMuscles: List<Muscle>,
        val tertiaryMuscles: List<Muscle>,
    )
}
