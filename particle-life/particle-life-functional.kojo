// contributed by Anay Kamat

cleari

val minX = canvasBounds.getBounds2D.getMinX.toInt
val maxX = canvasBounds.getBounds2D.getMaxX.toInt

val minY = canvasBounds.getBounds2D.getMinY.toInt
val maxY = canvasBounds.getBounds2D.getMaxY.toInt


case class Atom(
    x:Double, 
    y:Double, 
    color:java.awt.Color, 
    size: Double = 2,
    vx:Double = 0, 
    vy:Double = 0
){
    def draw(){
        Picture.circle(size).thatsStrokeColored(noColor).thatsFilledWith(color).thatsTranslated(x,y).draw        
    }
}

def randomInX = random(minX, maxX)
def randomInY = random(minY, maxY)

def atomsAtRandomLocation(color:Color) = 
    Atom(randomInX, randomInY, color)

def groups(count:Int,color:Color) = 
    Stream.continually(color).take(count).map(atomsAtRandomLocation)


val yellowAtoms = groups(200, yellow)
val redAtoms = groups(200, red)
val greenAtoms = groups(200, green)

case class ForceVector(fx:Double, fy:Double){
    def add(force:ForceVector):ForceVector = 
        ForceVector(fx+force.fx, fy+force.fy)
}

def rule(particles:Seq[Atom], color1:Color, color2:Color, g:Double):Seq[Atom] = {

    val atomsOfColor2 = particles.filter(_.color == color2)
    
    return particles.map { 
        case atom if atom.color != color1 => atom
        case atom => {
            val force = 
                atomsOfColor2.foldLeft(ForceVector(0.0,0.0)){ (forceSum, atom2) =>
                    val dx = atom.x - atom2.x
                    val dy = atom.y - atom2.y
                    val d = Math.sqrt(dx*dx + dy*dy)
                    val forceValue = if (d>0 && d<80) g/d else 0
                    ForceVector(forceValue*dx, forceValue*dy).add(forceSum)
                }
                val vx = (atom.vx + force.fx) * 0.5
                val vy = (atom.vy + force.fy) * 0.5
                val newX = atom.x + vx
                val newY = atom.y + vy
                val finalVx = newX match {
                    case n if n-5 <= minX => vx.abs
                    case n if n+5 >= maxX => vx.abs * -1
                    case n => vx
                }
                val finalVy = newY match {
                    case n if n-5 <= minY => vy.abs
                    case n if n+5 >= maxY => vy.abs * -1
                    case n => vy
                }                
                atom.copy(x=newX, y=newY,vx=finalVx, vy=finalVy)            
        }
    }    
}


def combinedAtoms:Seq[Atom] = (yellowAtoms ++ redAtoms ++ greenAtoms).toSeq

animateWithState(combinedAtoms) { atoms =>

    erasePictures()

    setBackground(black)

    atoms.foreach(_.draw)
    
    val afterRule1 = rule(atoms, green, green, -0.32)
    val afterRule2 = rule(afterRule1, green, red, -0.17)
    val afterRule3 = rule(afterRule2, green, yellow, 0.34)
    val afterRule4 = rule(afterRule3, red, red, -0.1)
    val afterRule5 = rule(afterRule4, red, green, -0.34)
    val afterRule6 = rule(afterRule5, yellow, yellow, 0.15)
    val afterRule7 = rule(afterRule6, yellow, green, -0.2)
    
    afterRule7
    
}