package com.patrykandpatrick.liftapp.ui.modifier

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.liftapp.ui.InteractiveBorderColors
import com.patrykandpatrick.liftapp.ui.interaction.extendedInteractions

fun Modifier.interactiveButtonEffect(
    colors: InteractiveBorderColors,
    onClick: (() -> Unit)? = null,
    borderWidth: Dp = 1.dp,
    enabled: Boolean = true,
    checked: Boolean = false,
    shape: Shape = RectangleShape,
    indicationScale: IndicationScale = IndicationScale(),
    role: Role? = null,
    scaleAnimationSpec: AnimationSpec<Float> =
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
    colorAnimationSpec: AnimationSpec<Color> = spring(stiffness = Spring.StiffnessLow),
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()

    then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    enabled = enabled,
                    role = role,
                    onClick = onClick,
                )
            } else {
                Modifier
            }
        )
        .extendedInteractions(
            enabled = enabled && onClick != null,
            interactionSource = interactionSource,
            coroutineScope = scope,
        )
        .interactiveScale(
            interactionSource = interactionSource,
            animationSpec = scaleAnimationSpec,
            scale = indicationScale,
        )
        .interactiveBorder(
            interactionSource = interactionSource,
            colors = colors,
            width = borderWidth,
            shape = shape,
            checked = checked,
            animationSpec = colorAnimationSpec,
        )
}
