package salt

import java.io.File
import java.util.*

data class Player(
        val id            : Int,
        var healthy       : Int,
        var infected      : Int,
        var dead          : Int,
        var infectionRate : Int,
        var contagionRate : Int,
        var lethalityRate : Int,
        var migrationRate : Int)

fun main(args : Array<String>) {
    @Suppress("NAME_SHADOWING")
    val args = args[0].split(';')

    val round = args[0].toInt()
    val myId = args[1].toInt()

    val players : MutableList<Player> = ArrayList()

    for ( i in 2..args.size-1) {
        val data = args[i].split('_')
        players.add(Player(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt(), data[4].toInt(), data[5].toInt(), data[6].toInt(), data[7].toInt()))
    }

    if (round == 50) {
        println("CCC")  //Extra 30 at end of game.
        return
    }

    var actionsLeft = 3

    val me = players.first { it.id == myId }
    val dataFile = File("Salt.txt")
    val lastRoundInfected : Int
    var roundsInHole : Int
    if (round == 1) {
        lastRoundInfected = 1
        roundsInHole = 0
    } else {
        val lines = dataFile.readLines()
        lastRoundInfected = lines[0].toInt()
        roundsInHole = lines[1].toInt()
    }

    val wantedInfected = lastRoundInfected * Math.pow(1/1.5, 1.0/5) * (if (round % 5 == 0 && round != 0) 1.5 else 1.0)

    while (me.migrationRate > 0) {
        print('B')          //Close borders
        me.migrationRate = Math.max(0, me.migrationRate - 10)
        actionsLeft--
    }

    if (me.infected <= wantedInfected) {   //Our infected are dieing too quickly
        roundsInHole++
    } else {
        roundsInHole = Math.max(0, roundsInHole - 1)
    }

    if (me.lethalityRate > 0) {
        var lethalityRateDelta = roundsInHole * 2
        while (lethalityRateDelta > 0 && me.lethalityRate > 0 && actionsLeft > 0) {
            if (lethalityRateDelta == 2 || me.lethalityRate <= 2) {
                lethalityRateDelta -= 2
                print('V')  //Research vaccines
                me.infectionRate = Math.max(0, me.infectionRate - 1)
                me.contagionRate = Math.max(0, me.contagionRate - 4)
                me.lethalityRate = Math.max(0, me.lethalityRate - 2)
                actionsLeft--
            } else {
                lethalityRateDelta -= 4
                print('I')
                me.lethalityRate = Math.max(0, me.lethalityRate - 4)
                actionsLeft--
            }
        }
    }

    dataFile.writeText("${me.infected}\n$roundsInHole")

    while (actionsLeft > 0) {
        if (me.infectionRate + me.contagionRate * me.infected / 100 <= 0) {
            break
        }
        val mWeight = Math.min(me.infectionRate, 4)
        val eWeight = Math.min(me.contagionRate, 8) * me.infected / 100
        val vWeight = Math.min(me.contagionRate, 4) * me.infected / 100 + Math.min(me.infectionRate, 1)
        if (mWeight > eWeight && mWeight > vWeight) {
            print('M')      //Research microbiology
            me.infectionRate = Math.max(0, me.infectionRate - 4)
        } else if (eWeight > vWeight){
            print('E')      //Research epidemiology
            me.contagionRate = Math.max(0, me.contagionRate - 8)
        } else {
            print('V')      //Research vaccines
            me.infectionRate = Math.max(0, me.infectionRate - 1)
            me.contagionRate = Math.max(0, me.contagionRate - 4)
            me.lethalityRate = Math.max(0, me.lethalityRate - 2)
        }
        actionsLeft--
    }

    while (actionsLeft > 0) {
        if (me.infected <= 0) {
            break
        }
        print('C')          //Cure
        val cured = Math.min(me.infected, 10)
        me.infected -= cured
        me.healthy += cured
        actionsLeft--
    }

    while (actionsLeft > 0) {
        print('N')          //Do nothing
        actionsLeft--
    }

    return
}