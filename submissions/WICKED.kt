package wicked

import java.util.*

fun clamp(value : Double, floor : Double, ceil : Double = Double.POSITIVE_INFINITY) = Math.max(Math.min(value, ceil), floor)
fun clamp(value : Int, floor : Int, ceil : Int = Integer.MAX_VALUE) = Math.max(Math.min(value, ceil), floor)

data class Player(
        val id            : Int,
        var healthy          : Int,
        var infected      : Int,
        var dead          : Int,
        var infectionRate : Int,
        var contagionRate : Double,
        var lethalityRate : Double,
        var migrationRate : Double)

class Game(val players : List<Player>) {

    fun doAction(playerId: Int, a: Char) {
        val player = players.first { it.id == playerId }
        with(player) {
            when (a) {
                'N' -> {}
                'M' -> infectionRate = clamp(infectionRate - 4, 0)
                'E' -> contagionRate = clamp(contagionRate - .08, 0.0, 1.0)
                'I' -> lethalityRate = clamp(lethalityRate - .04, 0.0, 1.0)
                'V' -> {
                    infectionRate = clamp(infectionRate - 1, 0)
                    contagionRate = clamp(contagionRate - .04, 0.0, 1.0)
                    lethalityRate = clamp(lethalityRate - .02, 0.0, 1.0)
                }
                'C' -> {
                    val cured = Math.min(infected, 10)
                    infected -= cured
                    healthy += cured
                }
                'Q' -> infected = clamp(infected - 30, 0)
                'O' -> migrationRate = clamp(migrationRate + .1, 0.0, 1.0)
                'B' -> migrationRate = clamp(migrationRate - .1, 0.0, 1.0)
                'T' -> {
                    players.forEach {
                        val infected = Math.min(it.healthy, 4)
                        it.healthy -= infected
                        it.infected += infected
                    }
                }
                'W' -> {
                    players.forEach {
                        it.infectionRate++
                        it.lethalityRate = clamp(it.lethalityRate + .02, 0.0, 1.0)
                    }
                }
                'D' -> {
                    players.forEach {
                        it.infectionRate++
                        it.contagionRate = clamp(it.contagionRate + .02, 0.0, 1.0)
                    }
                }
                'P' -> {
                    players.forEach {
                        it.infectionRate = clamp(it.infectionRate - 1, 0)
                        it.contagionRate = clamp(it.contagionRate - .01, 0.0, 1.0)
                        it.lethalityRate = clamp(it.lethalityRate - .01, 0.0, 1.0)
                    }
                }
                else -> throw IllegalArgumentException("Invalid action: $a")
            }
        }
    }

    fun copy() = Game(players.map { it.copy() })

    fun migration() {
        var migratingHealthy = 0
        var migratingInfected = 0
        var totalMigratingWeight = 0.0

        players.forEach {
            migratingHealthy += (it.healthy * it.migrationRate).toInt()
            migratingInfected += (it.infected * it.migrationRate).toInt()
            totalMigratingWeight += it.migrationRate

            it.healthy = (it.healthy * (1 - it.migrationRate)).toInt()
            it.infected *= (it.healthy * (1 - it.migrationRate)).toInt()
        }

        players.forEach {
            it.healthy += (migratingHealthy * it.migrationRate / totalMigratingWeight).toInt()
            it.infected += (migratingInfected * it.migrationRate / totalMigratingWeight).toInt()
        }
    }

    fun infection() {
        players.forEach {
            val infected = it.infectionRate //Allow negative healthy.
            it.healthy -= infected
            it.infected += infected
        }
    }

    fun contagion() {
        players.forEach {
            val infected = (it.infected * it.contagionRate).toInt()
            it.healthy -= infected
            it.infected += infected
        }
    }

    fun extinction() {
        players.forEach {
            val killed = (it.infected * it.lethalityRate).toInt()
            it.infected -= killed
            it.dead += killed
        }
    }

    operator fun get(playerId : Int) = players.first { it.id == playerId }

    fun calculateBenefit(action : Char, myId: Int) : Int {

        val copy1 = copy()
        copy1.doAction(myId, action)

        copy1.migration()
        copy1.infection()
        copy1.contagion()
        copy1.extinction()

        return copy1[myId].healthy
    }

}

fun main(args : Array<String>) {
    @Suppress("NAME_SHADOWING")
    val args = args[0].split(';')

    val round = args[0].toInt()
    val myId = args[1].toInt()

    val players : MutableList<Player> = ArrayList()

    for ( i in 2..args.size-1) {
        val data = args[i].split('_')
        players.add(Player(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt(), data[4].toInt(), data[5].toDouble() / 100, data[6].toDouble() / 100, data[7].toDouble() / 100))
    }

    val currentGame = Game(players)

    if (round == 50) {
        println("CCC")  //Extra 30 at end of game.
        return
    }

    for (i in 1..3) {
        val action = determineBestAction(currentGame, myId)
        currentGame.doAction(myId, action)
        print(action)
    }
}

fun determineBestAction(game : Game, myId : Int) : Char {

    if (game[myId].lethalityRate > .02) {        //Save the executives!!!
        return 'I'
    } else if (game[myId].lethalityRate > 0) {
        return 'V'
    }

    val bestAction = "NMEIVQCOBP".maxBy { game.calculateBenefit(it, myId) }!!

    return bestAction

}