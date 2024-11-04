# Cards-Game
Cards Game in Java implementing features from Gwent and HeartStone. The whole
game runs a mocker that simulates games using commands stored in test files.
There is no actual way of playing the game.

This project is a homework for my OOP course, which I took in my 3rd semester.

## Overview
Cards game resembles a pseudo game engine like Unity and UnrealEngine for easily
defining `GameObject` specific logic. The GameManager ensures that this logic
is taking place, while also representing an entry point for different commands.
These commands are the driving factor of the game. They specify what should
happen next, for example if the player ends their turn i.e. `endPlayerTurn`.
The game has no actual way of being played, all the interactions being mocked.

## Design

Tests are provided in form of JSON files. Then, the program outputs other JSON
files representing the output. The logic behind this happens with the help of
the following structures.

### Mocker
`Mocker` is a handler class that builds all input data into game data objects
i.e. `Card Data` or `Player Data`. It then runs each game and pushes forward the
commands into the `ActionHandler`.

### ActionHandler
`ActionHandler` is a handler class that maps each command to a function from
the `GameManager`. This is where the output JSON nodes are built.

### GameManager
`GameManager` is a manager class, therefore singleton, that is an unique object
that manages the overall game logic. This represents the entry point for
commands, but it also is the place where the main game loop takes place i.e.
functions like `beginPlay`, `tickRound`, `tickTurn` are called on every
`GameObject`.

### GameObject
`GameObject` is an abstract class that defines basic generic functions like
`beginPlay`. This is the base class from which all game related objects should
inherit.

### Player
`Player` is a `GameObject` which defines a player in the game. This is the owner
of the playing cards. Each game has exactly two players, enforced by the
`GameManager`.

Players have different methods which get called from the `GameManager`. These
represents different game decisions i.e. `useMinionAttack` which after some
self-specific checks get propagated to the `Game` class.

### Game
`Game` is a `GameObject` that represents the main brain of the actual played
game. It is the middle-man between the two players attacking eachother. Its
purpose is to perform different type of checks and return `Status` objects
accordingly or to propagate the logic to the next player i.e. the enemy of the
caller or even the caller in case of self-inflicted spells.

### Warrior
`Warrior` is the base `GameObject` for all inherited cards i.e. `Minion`,
`CasterMinion` and `Hero`. It defines specific logic for cards like taking
damage i.e. event `onAttacked`, whilist keeping it generic for other inherited
cards. They will be defining their own specific logic i.e. `CasterMinion`
implements interface `Caster` which gives possibility of using spells.

### Army
`Army` is a `GameObject` that stores a `Player`'s placed cards. It is the object
through which active `Warrior` objects can be obtained. It also features some
events that get triggered on `Minion` or `Hero` death.

### AbilityHandler
`AbilityHandler` is a handler class that implements all game abilities/spells as
`Ability` objects. This way, objects that contain an ability function can be
assigned as members of `CasterMinion` or `Hero`.

## Conclusion
Being the first Java project I develop, I think that there are a lot of things
that could be improved. One would be to make better use of interfaces and
separate logic in a more accurate manner. Overall I am pretty contempt with
how the project turned out.
