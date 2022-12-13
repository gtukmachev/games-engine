package tga.gaming.engine.stat

import kotlinx.html.currentTimeMillis

class Metric(
    val name: String
) {
    private val statistic = HashMap<Long, Stat>()

    fun add(addingValue: Int) {
        val t = currentTimeMillis() / 1000
        val stat = when(val st = statistic[t]) {
            null -> { Stat().also { statistic[t] = it } }
            else -> st
        }

        stat.add(addingValue)
    }

    fun getForSeveralLastSecondsAsString(nSecond: Int): String {
        return name + ": " + getForSeveralLastSeconds(nSecond).toString()
    }

    fun getForSeveralLastSeconds(nSecond: Int): Stat {
        val t = currentTimeMillis() / 1000
        val tPast = t - nSecond

        val s = Stat()
        for (i in tPast until t ) {
            val stat = statistic[i]
            if (stat != null) {
                s.sum += stat.sum
                s.count += stat.count
                if (s.max < stat.max) s.max = stat.max
                if (s.min > stat.min) s.min = stat.min
            }
            if (s.count > 0) s.avg = s.sum.toDouble() / s.count
        }

        return s
    }

}

data class Stat(
    var sum: Long = 0,
    var count: Int = 0,
    var avg: Double = 0.0,
    var max: Int = 0,
    var min: Int = 0,
) {
    fun add(addingValue: Int) {
        sum += addingValue
        count += 1
        avg = sum.toDouble() / count
        if (max < addingValue) max = addingValue
        if (min > addingValue) max = min
    }
}