package com.patrykandpatryk.liftapp.testing

import com.patrykandpatryk.liftapp.domain.date.HourFormat
import com.patrykandpatryk.liftapp.domain.model.AllPreferences
import com.patrykandpatryk.liftapp.domain.repository.PreferenceRepository
import com.patrykandpatryk.liftapp.domain.unit.DistanceUnit
import com.patrykandpatryk.liftapp.domain.unit.MassUnit
import com.patrykmichalik.opto.domain.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestPreferenceRepository : PreferenceRepository {

    override val massUnit: Preference<MassUnit, String, *> = preference(MassUnit.Kilograms)

    override val distanceUnit: Preference<DistanceUnit, String, *> = preference(DistanceUnit.Kilometers)

    override val hourFormat: Preference<HourFormat, String, *> = preference(HourFormat.H24)

    override val is24H: Flow<Boolean> = hourFormat.get()
        .map { hourFormat ->
            when (hourFormat) {
                HourFormat.H12 -> false
                HourFormat.Auto,
                HourFormat.H24 -> true
            }
        }

    override val allPreferences: Flow<AllPreferences> = combine(
        massUnit.get(),
        distanceUnit.get(),
        hourFormat.get()
    ) { massUnit, distanceUnit, hourFormat ->
        AllPreferences(
            massUnit = massUnit,
            distanceUnit = distanceUnit,
            hourFormat = hourFormat,
        )
    }

    private fun <T> preference(defaultValue: T): Preference<T, String, Unit> =
        object : Preference<T, String, Unit> {

            private val impl = MutableStateFlow(defaultValue)

            override val defaultValue: T = defaultValue

            override val key: Unit = Unit

            override val parse: (String) -> T = { TODO() }

            override val save: (T) -> String = { TODO() }

            override fun get(): Flow<T> = impl

            override suspend fun update(block: (T) -> T) {
                impl.update(block)
            }

            override suspend fun set(value: T) {
                impl.value = value
            }
        }
}
