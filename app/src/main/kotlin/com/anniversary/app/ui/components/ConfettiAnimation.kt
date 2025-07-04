package com.anniversary.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    onAnimationEnd: (() -> Unit)? = null
) {
    val density = LocalDensity.current
    var isAnimating by remember { mutableStateOf(true) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            kotlinx.coroutines.delay(3000) // Animation duration
            isAnimating = false
            onAnimationEnd?.invoke()
        }
    }

    if (isAnimating) {
        Box(modifier = modifier) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        spread = 45,
                        colors = listOf(
                            Color(0xFFFF69B4), // Hot pink
                            Color(0xFFFF1493), // Deep pink
                            Color(0xFFDC143C), // Crimson
                            Color(0xFFFFB6C1), // Light pink
                            Color(0xFFFFC0CB), // Pink
                            Color(0xFFFF6347)  // Tomato
                        ),
                        emitter = Emitter(duration = 3, TimeUnit.SECONDS).max(100),
                        position = Position.Relative(0.5, 0.0)
                    ),
                    // Side confetti
                    Party(
                        speed = 0f,
                        maxSpeed = 25f,
                        damping = 0.85f,
                        spread = 60,
                        colors = listOf(
                            Color(0xFFFF69B4),
                            Color(0xFFFF1493),
                            Color(0xFFDC143C)
                        ),
                        emitter = Emitter(duration = 2, TimeUnit.SECONDS).max(50),
                        position = Position.Relative(0.0, 0.3)
                    ),
                    Party(
                        speed = 0f,
                        maxSpeed = 25f,
                        damping = 0.85f,
                        spread = 60,
                        colors = listOf(
                            Color(0xFFFF69B4),
                            Color(0xFFFF1493),
                            Color(0xFFDC143C)
                        ),
                        emitter = Emitter(duration = 2, TimeUnit.SECONDS).max(50),
                        position = Position.Relative(1.0, 0.3)
                    )
                )
            )
        }
    }
}