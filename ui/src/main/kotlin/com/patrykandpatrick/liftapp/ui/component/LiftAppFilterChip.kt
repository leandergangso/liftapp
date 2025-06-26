package com.patrykandpatrick.liftapp.ui.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.semantics.Role
import com.patrykandpatrick.liftapp.ui.dimens.dimens
import com.patrykandpatrick.liftapp.ui.icons.Check
import com.patrykandpatrick.liftapp.ui.icons.LiftAppIcons
import com.patrykandpatrick.liftapp.ui.modifier.interactiveButtonEffect
import com.patrykandpatrick.liftapp.ui.preview.ComponentPreview
import com.patrykandpatrick.liftapp.ui.preview.GridPreviewSurface
import com.patrykandpatrick.liftapp.ui.theme.LiftAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LiftAppFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    colors: LiftAppFilterChipColors = LiftAppFilterChipDefaults.colors,
    contentPadding: PaddingValues =
        PaddingValues(dimens.chip.horizontalPadding, dimens.chip.verticalPadding),
    interactionSource: MutableInteractionSource? = null,
) {
    val containerColors = animateContainerColorsAsState(colors.getColors(selected)).value

    CardBase(
        enabled = enabled,
        colors = containerColors,
        interactionSource = interactionSource,
        modifier = modifier,
        textStyle = MaterialTheme.typography.labelLarge,
    ) { interactionSource ->
        val shape = CircleShape
        LookaheadScope {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.chip.spacing),
                modifier =
                    Modifier.animateBounds(this)
                        .interactiveButtonEffect(
                            colors = containerColors.interactiveBorderColors,
                            onClick = onClick,
                            enabled = enabled,
                            role = Role.Checkbox,
                            shape = shape,
                            checked = selected,
                        )
                        .background(
                            color =
                                if (enabled) containerColors.backgroundColor
                                else containerColors.disabledBackgroundColor,
                            shape = shape,
                        )
                        .padding(contentPadding)
                        .align(Alignment.Center)
                        .fillMaxWidth(),
            ) {
                leadingIcon?.invoke()

                label()
            }
        }
    }
}

@Composable
fun LiftAppChipRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimens.padding.itemHorizontalSmall),
        modifier = modifier,
    ) {
        LookaheadScope { content() }
    }
}

data class LiftAppFilterChipColors(
    val selectedColors: ContainerColors,
    val unselectedColors: ContainerColors,
) {
    fun getColors(selected: Boolean): ContainerColors =
        if (selected) selectedColors else unselectedColors
}

object LiftAppFilterChipDefaults {
    val colors: LiftAppFilterChipColors
        @Composable
        get() =
            LiftAppFilterChipColors(
                selectedColors = LiftAppCardDefaults.tonalCardColors,
                unselectedColors = LiftAppCardDefaults.outlinedColors,
            )

    @Composable
    fun Icon(
        vector: ImageVector,
        modifier: Modifier = Modifier,
        contentDescription: String? = null,
    ) {
        Icon(
            imageVector = vector,
            contentDescription = contentDescription,
            modifier = modifier.size(dimens.chip.iconSize),
        )
    }
}

@ComponentPreview
@Composable
fun LiftAppFilterChipPreview() {
    LiftAppTheme {
        GridPreviewSurface(
            content =
                listOf(
                    "Selected Chip" to { FilterChipPreview(selected = true) },
                    "Unselected Chip" to { FilterChipPreview(selected = false) },
                    "Selected Chip with leading icon" to
                        {
                            FilterChipPreview(
                                selected = true,
                                leadingIcon = { LiftAppFilterChipDefaults.Icon(LiftAppIcons.Check) },
                            )
                        },
                    "Chip with leading icon" to
                        {
                            FilterChipPreview(
                                selected = false,
                                leadingIcon = { LiftAppFilterChipDefaults.Icon(LiftAppIcons.Check) },
                            )
                        },
                    "ChipRow" to
                        {
                            LiftAppChipRow {
                                FilterChipPreview(selected = true)
                                FilterChipPreview(selected = false)
                            }
                        },
                )
        )
    }
}

@Composable
private fun FilterChipPreview(
    selected: Boolean,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val (selected, setSelected) = remember { mutableStateOf(selected) }
    LiftAppFilterChip(
        selected = selected,
        onClick = { setSelected(!selected) },
        label = { Text(if (selected) "Selected" else "Unselected") },
        modifier = modifier,
        leadingIcon = leadingIcon,
    )
}
