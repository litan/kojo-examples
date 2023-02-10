Three versions are available:
* fireworks.kojo - uses retained pictures for the view.
* fireworks-redraw.kojo - uses erased/redrawn pictures for the view.
* fireworks-canvas.kojo - uses a canvas for the view.

Note - retained pictures are meant to be used in games/simulations in the following cases:
* when you want to do collision detection via pictures.
* when you want to attach mouse handlers to pictures.
* for slightly better performance (especially with a large number of pictures).

Also, in a game/simulation, use pic.draw() instead of draw(pic) to avoid the overhead of copy-on-share value semantics for pictures (which are triggered by draw(pic)). For functional gen-art, use draw(pic).

Note 2 - the canvas version shows firework traces.  
In general, the canvas based stuff is available in Kojo for situations where:
* there are more than 5-10K visual elements or 
* you need special effects like traces etc.
