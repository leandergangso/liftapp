package com.patrykandpatryk.liftapp.feature.main.navigation

import kotlinx.serialization.Serializable

@Serializable object Home

sealed interface HomeRoute {
    @Serializable data object Dashboard : HomeRoute

    @Serializable data object Plan : HomeRoute

    @Serializable data object Exercises : HomeRoute

    @Serializable data object BodyMeasurements : HomeRoute

    @Serializable data object More : HomeRoute
}
