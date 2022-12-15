package tga.gaming.engine.movers

import org.w3c.dom.CanvasRenderingContext2D
import tga.gaming.engine.PI2
import tga.gaming.engine.model.*
import kotlin.math.PI

fun CompositeMover.addConstantSpeedMover(
    speed: Double = 1.0,
    rotationSpeed: Double = PI / 180,
    bounds: Frame,
    targetFunction: VectorFunction
): ConstantSpeedMover {
    val mover = ConstantSpeedMover(this as Obj, speed, rotationSpeed, bounds, targetFunction)
    this.movers.add(mover)
    return mover
}

inline fun <reified T: CompositeMover> T.withConstantSpeedMover(
    speed: Double = 1.0,
    rotationSpeed: Double = PI / 180,
    bounds: Frame,
    noinline targetFunction: VectorFunction
): T {
    val mover = ConstantSpeedMover(this as Obj, speed, rotationSpeed, bounds, targetFunction)
    this.movers.add(mover)
    return this
}

class ConstantSpeedMover(
    override val obj: Obj,
    private val speed: Double = 1.0,
    private val rotationSpeed: Double = PI / 180,
    val bounds: Frame,
    val targetFunction: VectorFunction
): Mover {

    var prevTarget: Vector? = null

    private var isRotationComplete: Boolean = true
    private var desiredDirectionVector = v(1,0)
    private var speedVector: Vector = desiredDirectionVector * speed

    private var desiredSpeedAngle: Double = 0.0
    private var turnDirection = "no"
    private var va  = 0.0
    private var currSpeedAngle = 0.0
    private var desiredLeftTurn = 0.0
    private var desiredRightTurn = 0.0


    override fun move() {
        val target = targetFunction()
        target?.let{ rotateToTarget(it) }
        obj.p += speedVector
        obj.p.restrictWithFrame(bounds)
    }

    private fun rotateToTarget(target: Vector) {
        if (prevTarget != target) { isRotationComplete = false }
        if (isRotationComplete) return
        prevTarget = target.copy()
        desiredDirectionVector = (target - obj.p).norm()


        desiredSpeedAngle = desiredDirectionVector.angle()
        currSpeedAngle = speedVector.norm().angle()

        if (desiredSpeedAngle < currSpeedAngle) desiredSpeedAngle += PI2

        desiredLeftTurn = desiredSpeedAngle - currSpeedAngle
        desiredRightTurn = PI2 - desiredLeftTurn

        if (desiredLeftTurn <= desiredRightTurn) {
            // turn to the left
            turnDirection = "left"
            if ( desiredLeftTurn <= rotationSpeed )  { speedVector.set(desiredDirectionVector); isRotationComplete = true}
            else                                     { va = currSpeedAngle + rotationSpeed; speedVector.setToAngle(va) }
        } else {
            // turn to the right
            turnDirection = "right"
            if ( desiredRightTurn <= rotationSpeed ) { speedVector.set(desiredDirectionVector); isRotationComplete = true}
            else                                     { va = currSpeedAngle - rotationSpeed; speedVector.setToAngle(va) }

        }
        speedVector.timesAssign(speed)
    }

    private fun Double.asDegree() = "${(this / PI2 * 360).toInt()}"

    fun draw(ctx: CanvasRenderingContext2D) {
        ctx.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        //ctx.translate(obj.p.x, obj.p.y)
        val aColor = if (isRotationComplete) "grey" else "orange"


        prevTarget?.let{
            ctx.beginPath()
            ctx.strokeStyle = "red"
            ctx.arc(it.x, it.y, 7.0, 0.0, PI2)
            ctx.lineWidth = 1.5
            ctx.stroke()
            ctx.fill()
            ctx.beginPath()
            ctx.lineWidth = 1.0
            ctx.strokeText(desiredSpeedAngle.asDegree(), it.x + 10, it.y)
            ctx.strokeText(turnDirection, it.x + 10, it.y+20)
            ctx.strokeText(va.asDegree(), it.x + 10, it.y+40)

        }


        ctx.beginPath()
        ctx.moveTo(obj.p.x, obj.p.y)
        val dV = desiredDirectionVector.norm() * 50
        ctx.lineTo(obj.p.x + dV.x, obj.p.y + dV.y)
        ctx.strokeStyle = aColor
        ctx.fillStyle = aColor
        ctx.stroke()
        ctx.beginPath()
        ctx.arc(obj.p.x, obj.p.y, 3.0, 0.0, PI2)
        ctx.fill()

        ctx.beginPath()
        ctx.moveTo(obj.p.x, obj.p.y)
        val dS = speedVector.norm() * 40
        ctx.lineTo(obj.p.x + dS.x, obj.p.y + dS.y)
        ctx.strokeStyle = "yellow"
        ctx.fillStyle = "yellow"
        ctx.stroke()
        ctx.strokeText(currSpeedAngle.asDegree(), obj.p.x+20, obj.p.y)

        ctx.strokeStyle = "lightblue"
        ctx.strokeText("left: ${desiredLeftTurn.asDegree()}", obj.p.x+20, obj.p.y+25)
        ctx.strokeText("right:${desiredRightTurn.asDegree()}", obj.p.x+20, obj.p.y+50)
    }
}