cleari()
drawStage(cm.darkGreen)
val cb = canvasBounds

case class Player(var x: Double, var y: Double)
case class Hunter(var x: Double, var y: Double, var vel: Vector2D)

val player = Player(cb.x + cb.width / 2, cb.y + 20)
val hunters = ArrayBuffer.empty[Hunter]

var playerPic: Picture = Picture.rectangle(0, 0)
val hunterPics = ArrayBuffer.empty[Picture]

var lost = false

val nh = 5
repeatFor(1 to nh) { n =>
    val hv = Vector2D(random(1, 4), random(1, 4))
    val hunter = Hunter(
        cb.x + cb.width / (nh + 2) * n,
        cb.y + randomDouble(100, cb.height - 200),
        hv
    )
    hunters.append(hunter)
}

def gameLost() {
    stopAnimation()
    drawCenteredMessage("You Lost", white, 30)
}

def updateState() {
    var idx = 0
    repeatFor(hunters) { h =>
        val hv = h.vel
        val hp = hunterPics(idx)

        if (hp.collidesWith(stageBorder)) {
            h.vel = bouncePicOffStage(hp, h.vel)
        }

        if (hp.collidesWith(playerPic)) {
            lost = true
        }
        h.x += h.vel.x
        h.y += h.vel.y
        idx += 1
    }

    if (isKeyPressed(Kc.VK_RIGHT)) {
        player.x += speed
    }
    if (isKeyPressed(Kc.VK_LEFT)) {
        player.x -= speed
    }
    if (isKeyPressed(Kc.VK_UP)) {
        player.y += speed
    }
    if (isKeyPressed(Kc.VK_DOWN)) {
        player.y -= speed
    }

    if (playerPic.collidesWith(stageBorder)) {
        lost = true
    }

}

def drawView() {
    if (lost) {
        gameLost()
        return
    }

    playerPic.erase()
    repeatFor(hunterPics) { pic => pic.erase() }

    playerPic = Picture.rectangle(50, 50)
        .withPosition(player.x, player.y)
        .withPenColor(green)
        .withFillColor(green)

    hunterPics.clear()
    repeatFor(hunters) { hunter =>
        val hunterPic = Picture.rectangle(50, 50)
            .withPosition(hunter.x, hunter.y)
            .withPenColor(red)
            .withFillColor(red)

        hunterPics.append(hunterPic)
    }

    playerPic.draw()
    repeatFor(hunterPics) { pic => pic.draw() }
}

val speed = 5
drawView()

animate {
    updateState()
    drawView()
}

showGameTime(10, "You Win", black, 25)
activateCanvas()