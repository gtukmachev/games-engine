package tga.gaming.engine.stat

object CommonMetrics {

    private val staticSecondsDeep = 9

    val FPS = Metric("FPS", "painted Frames per second", staticSecondsDeep)
    val TPS = Metric("TPS", "game Turns per second", staticSecondsDeep)
    val paintObjectsMetric = Metric("op.CNT", "painted objects per Frame", staticSecondsDeep)
    val paintErrorsMetric = Metric("op.ERR", "paint errors per Frame", staticSecondsDeep)

    fun reset() {
        FPS.reset()
        TPS.reset()
        paintObjectsMetric.reset()
        paintErrorsMetric.reset()
    }

}