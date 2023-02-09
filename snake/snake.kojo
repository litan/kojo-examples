// Inspired by
// https://editor.p5js.org/codingtrain/sketches/HkDVpSvDm

cleari()
originBottomLeft()

class Snake() {
    var body = Vector(Point(0, 0))
    private var xdir = 0
    private var ydir = 0
    private var len = 0

    def setDir(xd: Int, yd: Int) {
        xdir = xd
        ydir = yd
    }

    def step() {
        val newHead = body.last + Point(xdir, ydir)
        body = body.tail
        body = body :+ newHead
    }

    def grow() {
        val grownHead = body.last
        len += 1
        body = body :+ grownHead
    }

    def endGame(): Boolean = {
        val x = body.last.x
        val y = body.last.y
        if (x > w - 1 || x < 0 || y > h - 1 || y < 0) {
            true
        }
        else {
            body.take(body.length - 1).find { p =>
                p.x == x && p.y == y
            }.isDefined
        }
    }

    def eat(pos: Point): Boolean = {
        val x = body.last.x
        val y = body.last.y
        if (x == pos.x && y == pos.y) {
            grow()
            true
        }
        else {
            false
        }
    }

    def show() {
        repeatFor(body) { p =>
            val pic = Picture.rectangle(res, res).withFillColor(green)
                .withPenColor(darkGray)
                .withPosition(p.x * res, p.y * res)
            draw(pic)
        }
    }
}

val res = 40
val w = cwidth / res
val h = cheight / res
setRefreshRate(6)
var food: Point = _

val snake = new Snake()
foodLocation()

def foodLocation() {
    food = Point(random(w), random(h))
}

setBackground(cm.lightGray)

onKeyPress { kc =>
    if (kc == Kc.VK_LEFT) {
        snake.setDir(-1, 0)
    }
    if (kc == Kc.VK_RIGHT) {
        snake.setDir(1, 0)
    }
    if (kc == Kc.VK_DOWN) {
        snake.setDir(0, -1)
    }
    if (kc == Kc.VK_UP) {
        snake.setDir(0, 1)
    }
}

def updateState() {
    if (snake.eat(food)) {
        foodLocation()
    }
    snake.step()
}

def viewState() {
    erasePictures()
    snake.show()
    if (snake.endGame()) {
        drawCenteredMessage("You Lost", black, 30)
        stopAnimation()
    }
    val foodPic = Picture.rectangle(res, res).withFillColor(red)
        .withPosition(food.x * res, food.y * res)
    draw(foodPic)
}

animate {
    updateState()
    viewState()
}

activateCanvas()