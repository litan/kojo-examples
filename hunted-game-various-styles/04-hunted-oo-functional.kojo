cleari()
drawStage(cm.darkGreen)
val cb = canvasBounds

class Player(x0: Double, y0: Double) {
    val speed = 5
    private var x = x0
    private var y = y0
    private var pic = Picture.rectangle(50, 50)
        .withPenColor(green)
        .withFillColor(green)

    pic.setPosition(x, y)

    def viewPic = pic

    def step() {
        if (isKeyPressed(Kc.VK_RIGHT)) {
            x += speed
        }
        if (isKeyPressed(Kc.VK_LEFT)) {
            x -= speed
        }
        if (isKeyPressed(Kc.VK_UP)) {
            y += speed
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
            y -= speed
        }

        if (pic.collidesWith(stageBorder)) {
            gameLost = true
        }
    }

    def view() {
        if (pic.isDrawn) {
            pic.setPosition(x, y)
        }
        else {
            pic.draw()
        }
    }

}

class Hunter(x0: Double, y0: Double, vel0: Vector2D) {
    private var x = x0
    private var y = y0
    private var vel = vel0
    private var pic = Picture.rectangle(50, 50)
        .withPenColor(red)
        .withFillColor(red)

    pic.setPosition(x, y)

    def step() {
        if (pic.collidesWith(stageBorder)) {
            vel = bouncePicOffStage(pic, vel)
        }

        if (pic.collidesWith(player.viewPic)) {
            gameLost = true
        }
        x += vel.x
        y += vel.y
    }

    def view() {
        if (pic.isDrawn) {
            pic.setPosition(x, y)
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