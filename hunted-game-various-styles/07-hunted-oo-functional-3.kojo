cleari()
drawStage(cm.darkGreen)
val cb = canvasBounds

case class PlayerState(x: Double, y: Double) {
    def withUpdatedX(speed: Double): PlayerState = copy(x = x + speed)
    def withUpdatedY(speed: Double): PlayerState = copy(y = y + speed)
}

case class HunterState(x: Double, y: Double, vel: Vector2D) {
    def withVel(vel2: Vector2D): HunterState = copy(vel = vel2)
    def withVelUpdatedXY: HunterState = copy(x = x + vel.x, y = y + vel.y)
}

class Player(x0: Double, y0: Double) {
    val speed = 5
    private var state = PlayerState(x0, y0)
    private var pic = Picture.rectangle(50, 50)
    pic.setPenColor(green)
    pic.setFillColor(green)
    pic.setPosition(state.x, state.y)

    def viewPic = pic

    def nextState(prevState: PlayerState, pressedKeys: collection.Set[Int]): PlayerState = {
        var newState = state
        if (pressedKeys.contains(Kc.VK_RIGHT)) {
            newState = newState.withUpdatedX(speed)
        }
        if (pressedKeys.contains(Kc.VK_LEFT)) {
            newState = newState.withUpdatedX(-speed)
        }
        if (pressedKeys.contains(Kc.VK_UP)) {
            newState = newState.withUpdatedY(speed)
        }
        if (pressedKeys.contains(Kc.VK_DOWN)) {
            newState = newState.withUpdatedY(-speed)
        }

        if (pic.collidesWith(stageBorder)) {
            gameLost = true
        }
        newState
    }

    def step() {
        state = nextState(state, pressedKeys)
    }

    def view() {
        if (pic.isDrawn) {
            pic.setPosition(state.x, state.y)
        }
        else {
            pic.draw()
        }
    }

}

class Hunter(x0: Double, y0: Double, vel0: Vector2D) {
    private var state = HunterState(x0, y0, vel0)
    private var pic = Picture.rectangle(50, 50)
    pic.setPenColor(red)
    pic.setFillColor(red)

    pic.setPosition(state.x, state.y)

    def step() {
        var newState = state
        if (pic.collidesWith(stageBorder)) {
            val newVel = bouncePicOffStage(pic, newState.vel)
            newState = newState.withVel(newVel)
        }

        if (pic.collidesWith(player.viewPic)) {
            gameLost = true
        }
        newState = newState.withVelUpdatedXY
        state = newState
    }

    def view() {
        if (pic.isDrawn) {
            pic.setPosition(state.x, state.y)
        }
        else {
            pic.draw()
        }
    }
}

val player = new Player(cb.x + cb.width / 2, cb.y + 20)

val nh = 5
val hunters = (1 to nh).map { n =>
    val hv = Vector2D(random(1, 4), random(1, 4))
    new Hunter(
        cb.x + cb.width / (nh + 2) * n,
        cb.y + randomDouble(100, cb.height - 200),
        hv
    )
}

var gameLost = false

def showGameLost() {
    stopAnimation()
    drawCenteredMessage("You Lost", white, 30)
}

def updateState() {
    repeatFor(hunters) { h => h.step() }
    player.step()
}

def viewState() {
    if (gameLost) {
        showGameLost()
    }
    else {
        player.view()
        repeatFor(hunters) { h => h.view() }
    }
}

viewState()
animate {
    updateState()
    viewState()
}

showGameTime(10, "You Win", black, 25)
activateCanvas()