package org.mefetran.munchkinmaster.presentation.ui.uikit.util

class WindowSizeClass(
    val minWidthDp: Int,
    val minHeightDp: Int,
) {
    constructor(widthDp: Float, heightDp: Float) : this(widthDp.toInt(), heightDp.toInt())

    init {
        require(minWidthDp >= 0) {
            "Expected minWidthDp to be at least 0, minWidthDp: $minWidthDp."
        }
        require(minHeightDp >= 0) {
            "Expected minHeightDp to be at least 0, minHeightDp: $minHeightDp."
        }
    }

    fun isHeightAtLeastBreakpoint(heightDpBreakpoint: Int): Boolean {
        return minHeightDp >= heightDpBreakpoint
    }

    fun isWidthAtLeastBreakpoint(widthDpBreakpoint: Int): Boolean {
        return minWidthDp >= widthDpBreakpoint
    }

    fun isAtLeastBreakpoint(widthDpBreakpoint: Int, heightDpBreakpoint: Int): Boolean {
        return isWidthAtLeastBreakpoint(widthDpBreakpoint) &&
                isHeightAtLeastBreakpoint(heightDpBreakpoint)
    }

    override fun hashCode(): Int {
        var result = minWidthDp
        result = 31 * result + minHeightDp
        return result
    }

    override fun toString(): String {
        return "WindowSizeClass(minWidthDp=$minWidthDp, minHeightDp=$minHeightDp)"
    }

    companion object {
        const val HEIGHT_DP_EXPANDED_LOWER_BOUND = 900
        const val HEIGHT_DP_MEDIUM_LOWER_BOUND = 480
        const val WIDTH_DP_EXPANDED_LOWER_BOUND = 840
        const val WIDTH_DP_EXTRA_LARGE_LOWER_BOUND = 1600
        const val WIDTH_DP_LARGE_LOWER_BOUND = 1200
        const val WIDTH_DP_MEDIUM_LOWER_BOUND = 600
        private val WIDTH_DP_BREAKPOINTS = listOf(0, WIDTH_DP_MEDIUM_LOWER_BOUND, WIDTH_DP_EXPANDED_LOWER_BOUND, WIDTH_DP_LARGE_LOWER_BOUND, WIDTH_DP_EXTRA_LARGE_LOWER_BOUND)
        private val HEIGHT_DP_BREAKPOINTS = listOf(0, HEIGHT_DP_MEDIUM_LOWER_BOUND, HEIGHT_DP_EXPANDED_LOWER_BOUND)
        private fun createBreakpointSet(
            widthBreakpoints: List<Int>,
            heightBreakpoints: List<Int>,
        ): Set<WindowSizeClass> {
            return widthBreakpoints
                .flatMap { widthBp ->
                    heightBreakpoints.map { heightBp ->
                        WindowSizeClass(minWidthDp = widthBp, minHeightDp = heightBp)
                    }
                }
                .toSet()
        }

        @JvmField
        val BREAKPOINTS: Set<WindowSizeClass> = createBreakpointSet(WIDTH_DP_BREAKPOINTS, HEIGHT_DP_BREAKPOINTS)
    }
}

fun Set<WindowSizeClass>.computeWindowSizeClass(
    widthDp: Int,
    heightDp: Int,
): WindowSizeClass {
    var maxWidth = 0
    forEach { bucket ->
        if (bucket.minWidthDp <= widthDp && bucket.minWidthDp > maxWidth) {
            maxWidth = bucket.minWidthDp
        }
    }
    var match = WindowSizeClass(0, 0)
    forEach { bucket ->
        if (
            bucket.minWidthDp == maxWidth &&
            bucket.minHeightDp <= heightDp &&
            match.minHeightDp <= bucket.minHeightDp
        ) {
            match = bucket
        }
    }
    return match
}

fun Set<WindowSizeClass>.computeWindowSizeClassPreferHeight(
    widthDp: Int,
    heightDp: Int,
): WindowSizeClass {
    var maxHeight = 0
    forEach { bucket ->
        if (bucket.minHeightDp <= heightDp && bucket.minHeightDp > maxHeight) {
            maxHeight = bucket.minHeightDp
        }
    }
    var match = WindowSizeClass(0, 0)
    forEach { bucket ->
        if (
            bucket.minHeightDp == maxHeight &&
            bucket.minWidthDp <= widthDp &&
            match.minWidthDp <= bucket.minWidthDp
        ) {
            match = bucket
        }
    }
    return match
}
