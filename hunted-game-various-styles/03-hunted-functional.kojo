cleari()
drawStage(cm.darkGreen)
val cb = canvasBounds

case class Player(x: Double, y: Double)
case class Hunter(x: Double, y: Double, vel: Vector2D)

case class GameState(
    player:  Player,
    hunters: Seq[Hunter],
    lost:    Boolean
)

case class GamePics(
    playerPic:  Picture,
    hunterPics: Seq[Picture],
    lostPic:    Option[Picture]
) {
    def draw() {
        playerPic.draw()
        repeatFor(hunterPics) { pic => pic.draw() }
        lostPic.foreach { pic => pic.draw() }
    }

    def erase() {
        playerPic.erase()
        repeatFor(hunterPics) { pic => pic.erase() }
        lostPic.foreach { pic => pic.erase() }
    }
}

// this is available in Kojo 2.9.24
def makeCenteredMessage(message: String, color: Color = black, fontSize: Int): Picture = {
    val te = textExtent(message, fontSize)
    PicShape.text(message, fontSize)
        .withTranslation(cb.x + (cb.width - te.width) / 2, cb.y + (cb.height - te.height) / 2 + te.height)
        .withPenColor(color)
}

def gameLostMsg: Picture = {
    makeCenteredMessage("You Lost", white, 30)
}

def initState(nh: Int): GameState = {
    val player = Player(cb.x + cb.width / 2, cb.y + 20)
    val hunters = (1 to nh).map { n =>
        val hv = Vector2D(random(1, 4), random(1, 4))
        Hunter(
            cb.x + cb.width / (nh + 2) * n,
            cb.y + randomDouble(100, cb.height - 200),
            hv
        )
    }
    GameState(player, hunters, false)
}

def nextState(prevState: GameState, gpics: GamePics): GameState = {
    val hunters = prevState.hunters
    val playerPic = gpics.playerPic
    val hunterPics = gpics.hunterPics
    val gameLost =
        hunters.zipWithIndex.find { case (h, idx) =>
            val hp = hunterPics(idx)
            if (hp.collidesWith(playerPic)) true else false
        }.isDefined ||
            playerPic.collidesWith(stageBorder)

    if (gameLost) {
        GameState(prevState.player, prevState.hunters, true)
    }
    else {
        val newHunters = hunters.zipWithIndex.map { case (h, idx) =>
            val hv = h.vel
            val hp = hunterPics(idx)

            val hv2 = if (hp.collidesWith(stageBorder))
                bouncePicOffStage(hp, hv)
            else
                hv

            val hx2 = h.x + hv2.x
            val hy2 = h.y + hv2.y
            Hunter(hx2, hy2, hv2)
        }

        var newPlayer = prevState.player

        if (isKeyPressed(Kc.VK_RIGHT)) {
            newPlayer = newPlayer.copy(x = newPlayer.x + speed)
        }
        if (isKeyPressed(Kc.VK_LEFT)) {
            newPlayer = newPlayer.copy(x = newPlayer.x - speed)
        }
        if (isKeyPressed(Kc.VK_UP)) {
            newPlayer = newPlayer.copy(y = newPlayer.y + speed)
        }
        if (isKeyPressed(Kc.VK_DOWN)) {
            newPlayer = newPlayer.copy(y = newPlayer.y - speed)
        }
        GameState(newPlayer, newHunters, false)
    }
}

def statePic(gstate: GameState): GamePics = {
    val player = gstate.player
    val hunters = gstate.hunters

    val playerPic = Picture.rectangle(50, 50)
        .withPosition(player.x, player.y)
        .withPenColor(green)
        .withFillColor(green)

    val hunterPics = hunters.map { hunter =>
        Picture.rectangle(50, 50)
            .withPosition(hunter.x, hunter.y)
            .withPenColor(red)
            .withFillColor(red)
    }
    var gpics = GamePics(playerPic, hunterPics, None)

    if (gstate.lost)
        gpics.copy(lostPic = Some(gameLostMsg))
    else
        gpics
}

val speed = 5

val iState = initState(5)
val iPics = statePic(iState)
iPics.draw()

animateWithState(iState, iPics) { case (gameState, gamePics) =>
    val gameState2 = nextState(gameState, gamePics)
    val gamePics2 = statePic(gameState2)
    gamePics.erase()
    gamePics2.draw()
    if (gameState2.lost) {
        stopAnimation()
    }
    (gameState2, gamePics2)

}

showGameTime(10, "You Win", black, 25)
activateCanvas()