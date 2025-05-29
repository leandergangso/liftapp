package com.patrykandpatrick.liftapp.plan.model

import com.patrykandpatrick.liftapp.plan.ui.PlanState
import com.patrykandpatryk.liftapp.domain.plan.ActivePlan
import com.patrykandpatryk.liftapp.domain.plan.GetActivePlanUseCase
import com.patrykandpatryk.liftapp.domain.plan.Plan
import jakarta.inject.Inject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPlanStateUseCase
@Inject
constructor(private val getActivePlanUseCase: GetActivePlanUseCase) {
    operator fun invoke(): Flow<PlanState> =
        getActivePlanUseCase().map { pair ->
            if (pair == null) {
                PlanState.NoActivePlan
            } else {
                val (activePlan, plan) = pair
                PlanState.ActivePlan(
                    plan = plan,
                    cycleNumber = getCycleNumber(activePlan, plan),
                    cycleCount = activePlan.cycleCount,
                    currentPlanItemIndex = getCurrentItem(activePlan, plan),
                    cycleDates =
                        PlanState.getAllCycleDates(
                            activePlan.startDate.plusDays(activePlan.dayOffset),
                            activePlan.cycleCount,
                            plan.items.size.toLong(),
                        ),
                )
            }
        }

    private fun getDayCountBetweenStartAndToday(activePlan: ActivePlan): Int {
        val startDate = activePlan.startDate.plusDays(activePlan.dayOffset)
        val today = LocalDate.now()
        return ChronoUnit.DAYS.between(startDate, today).toInt()
    }

    private fun getCycleNumber(activePlan: ActivePlan, plan: Plan): Int {
        val dayCount = plan.items.size
        val daysDifference = getDayCountBetweenStartAndToday(activePlan)
        return daysDifference / dayCount
    }

    private fun getCurrentItem(activePlan: ActivePlan, plan: Plan): Int =
        getDayCountBetweenStartAndToday(activePlan) % plan.items.size
}
