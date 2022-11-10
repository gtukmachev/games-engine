package tga.gaming.engine.model

interface Actionable {
    fun onPrepareAct(ctx: TurnContext) { }
    fun onDoAct(ctx: TurnContext) { }
    fun onFinishAct(ctx: TurnContext) { }
}
