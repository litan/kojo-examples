cleari()
drawStage(cm.darkGreen)
val cb = canvasBounds

val player = Picture.rectangle(50, 50)
player.setFillColor(green)
player.setPenColor(green)
player.setPosition(cb.x + cb.width / 2, cb.y + 20)
draw(player)

val nh = 5
val hunters = ArrayBuffer.empty[Picture]
val huntersVel = HashMap.empty[Picture, Vector2D]
repeatFor(1 to nh) { n =>
    val pic = Picture.rectangle(50, 50)
    pic.setFillColor(red)
    pic.setPenColor(red)
    pic.setPosition(cb.x + cb.width / (nh + 2) * n, cb.y + randomDouble(100, cb.height - 200))
    hunters.append(pic)
    val hv = Vector2D(random(1, 4), random(1, 4))
    huntersVel(pic) = hv
    draw(pic)
}

def gameLost() {
    stopAnimation()
    drawCenteredMessage("You Lost", white, 30)
}

val speed = 5
animate {
    repeatFor(hunters) { h =>
        var hv = huntersVel(h)
        h.translate(hv)
        if (h.collidesWith(stageBorder)) {
            hv = bouncePicOffStage(h, hv)
            huntersVel(h) = hv
        }

        if (h.collidesWith(player)) {
            gameLost()
        }
    }

    if (isKeyPressed(Kc.VK_RIGHT)) {
        player.translate(speed, 0)
    }
    if (isKeyPressed(Kc.VK_LEFT)) {
        player.translate(-speed, 0)
    }
    if (isKeyPressed(Kc.VK_UP)) {
        player.translate(0, speed)
    }
    if (isKeyPressed(Kc.VK_DOWN)) {
        player.translate(0, -speed)
    }

    if (player.collidesWith(stageBorder)) {
        gameLost()
    }
}
showGameTime(10, "You Win", black, 25)
activateCanvas()