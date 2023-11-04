package tga.gaming.engine.stat

import kotlinx.html.currentTimeMillis
import kotlin.math.roundToInt

class Metric(
    val shortName: String,
    val name: String,
    val deep: Int
) {

    private var startedAt: Long = 0L
    private var prevSecond: Int = 0
    private val statisticBySeconds: Array<Stat> = Array(deep){ Stat() }


    fun add(addingValue: Int) {
        val nowSecond: Int = (((currentTimeMillis() - startedAt) / 1000) % deep).toInt()
        if (nowSecond != prevSecond) {
            statisticBySeconds[nowSecond].reset()
            prevSecond = nowSecond
        }
        statisticBySeconds[nowSecond].add(addingValue)


    }

    fun reset() {
        prevSecond = 0
        for (stat in statisticBySeconds) stat.reset()
        startedAt = currentTimeMillis()
    }

    fun aggregateAsString(): String {
        val aggregated = aggregate()
        return  "$shortName: [${aggregated.avg}] ... $name: $aggregated"
    }

    private fun aggregate(): AggregatedSumStat {
        val aggregated = AggregatedSumStat()

        var i = prevSecond - 1
        if (i<0) i = deep-1
        repeat(deep-2) {
            i--; if (i < 0) i = deep-1
            aggregated.add(statisticBySeconds[i])

        }
        return aggregated
    }

}

data class Stat(
    var sum: Long = 0,
    var count: Long = 0,
    var max: Long = Long.MIN_VALUE,
    var min: Long = Long.MAX_VALUE,
) {

    val avg: Int get() = if (count == 0L) 0 else (sum.toDouble() / count).roundToInt()

    fun add(addingValue: Int) {
        sum += addingValue
        count += 1
        if (addingValue > max) max = addingValue.toLong()
        if (addingValue < min) min = addingValue.toLong()
    }

    fun reset() { sum = 0; count = 0; max = Long.MIN_VALUE; min = Long.MAX_VALUE }

    override fun toString() = "Stat{avg=$avg, max=$max, min=$min, sum=$sum, count=$count}"
}


data class AggregatedSumStat(
    var sum: Long = 0,
    var count: Int = 0,
    var max: Long = Long.MIN_VALUE,
    var min: Long = Long.MAX_VALUE,
) {

    val avg: Int get() = if (count == 0) 0 else (sum.toDouble() / count).roundToInt()

    fun add(statItem: Stat) {
        sum += statItem.sum
        count += 1
        if (statItem.sum > max) max = statItem.sum
        if (statItem.sum < min) min = statItem.sum
    }

    override fun toString() = "AggregatedSumStat{avg=$avg, max=$max, min=$min, sum=$sum, count=$count}"
}