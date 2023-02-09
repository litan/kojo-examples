### The "hunted" game in various styles

#### Core Ideas behing the different versions

Here are some definitions of core terms, as used in the Kojo/Scala world.  
You might see different definitions elsewhere (so don't get hung up on the words; what's important is the ideas behind the words).

* Everything in Scala is an object.
* Objects contain data/fields and code/methods.
* Objects can be of the following kinds:
  * Value objects - these are made of immutable data. They represent quantities/facts/passive-information in the world being modelled.
  * Mutable data objects - these provide a way of storing changable data in a program.
  * Entity objects - these contain mutable data (their state) and behavior (via methods) that acts on the state. They represent active entities in the world being modelled.  
  (Note - entity state can also be modelled as a mutable/changable reference to a value).
* Functions compute new values from old values. They map values to values. They take in values as inputs and return values as outputs.
* Commands carry out actions (or indirectly affect future actions).
* Object methods can be functions or commands.
* State in a system is the thing that changes as the system runs.

With the above definitions in place, here's an overview of the various versions of hunted:  
* imperative/structured - todo
* functional - todo
* object oriented - todo
* object-functional - todo
