package an.games.bowling.core

/**
 * A bowling game.
 * @author Alessandro Negri.
 * @since version 0.1.
 *
 * The class controls the game of bowling for set number of players.
 *
 * @param _playersp: A string array containing all the player names for the game.
 */
class BowlingGame(_players: Array<String>) {

    /* Static variables and functions for the bowling game */
    companion object {
        /* The maximum and initial number of pins for a bowling frame */
        const val PINS: Int = 10
    }

    /**
     * @property players is the list of players for the bowling game.
     */
    private var players: MutableList<Player> = mutableListOf<Player>()

    /**
     * @property gameFinished is a Boolean contains the state of the bowling game.
     */
    private var gameFinished: Boolean = false

    /**
     * @property frame is the current frame for the current player.
     */
    private var frame: Int = 1

    /**
     * @property roll is the number roll for the current frame.
     */
    private var roll: Int = 1

    /**
     * @property pins contains the number of pins left on the current frame.
     */
    private var pins: Int = PINS

    /**
     * @property lastScore retains the last score calculcated
     */
    private var lastScore: Int = 0

    /**
     * @property message contains information on the calculation.
     */
    private var message: String = ""

    init {
        _players.iterator().forEach {
            players.add(Player(_name = it))
        }
        println("Total players: ${players.size}")
    }

    /**
     * Adds a player to the bowling game.
     *
     * @param name is the String to name the player.
     *
     * @return the index of the new player.
     */
    fun addPlayer(name: String): Int {
        players.add(Player(_name = name))
        return players.size - 1
    }

    /**
     * Renames a player.
     *
     * @param playerIdx  is the index of the player from the list.
     * @param newName is the String to rename the player.
     *
     * @return the index of the player renamed.
     */
    fun renamePlayer(playerIdx: Int, newName: String): Int {
        if (playerIdx < 0 || playerIdx > players.size - 1) throw IllegalArgumentException("Player index must be within the range of existing players")
        players[playerIdx].name = newName
        return playerIdx
    }

    /**
     * Gets all the players available for the game.
     *
     * @return all the players for the bowling game.
     */
    fun getPlayers() : Array<Player> {
        return players.toTypedArray()
    }

    /**
     * Gets the message of the bowling game.
     *
     * @return String containing the message of the calculation score.
     */
    fun getMessage(): String {
        return message
    }

    /**
     * Calculates the score of a player from the [playerIdx].
     *
     * @throws IllegalArgumentException if the [playerIdx] is not in the list of players.
     * @throws IllegalArgumentException if the [frameNumber] is not in a valid range of 1 - 10.
     * @throws IllegalArgumentException if the [rollNumber] is not valid for the frame 1-2 or 1-3 on the last frame.
     * @throws IllegalArgumentException if the [pinsKnocked] is not in a valid range of 0 - 10.
     *
     * @return the final score given the [frameNumber] and [rollNumber] for a [playerIdx] given the [pinsKnocked].
     */
    fun calculateScore(playerIdx: Int, frameNumber: Int, rollNumber: Int, pinsKnocked: Int) : Int {
        if (playerIdx < 0 || playerIdx > players.size - 1) throw IllegalArgumentException("A valid player index must be provided")
        if (frameNumber < 1 || frameNumber > 10) throw IllegalArgumentException("Frame number must be between 1 - 10, instead $frameNumber was provided")
        if (rollNumber < 1 || rollNumber > 3) throw IllegalArgumentException("Roll number must be between 1 - 3, instead $rollNumber was provided")
        if (pinsKnocked < 0 || pinsKnocked > 10) throw IllegalArgumentException("Pins knocked must be between 0 - 10, instead $pinsKnocked was provided")

        val frameIdx = frameNumber - 1
        val rollIdx = rollNumber - 1

        var finalScore: Int = 0 //players[playerIdx].getScore()
        message = ""
        for (i in 0..frameIdx) { //frameIdx downTo 0) {
            message += " frameIdx $i : \n"
            println(" frameIdx $i : ")
            // There are 3 cases, for calculating the rolls
            // 1. the last frame played with the current roll
            // 2. the previous frames, which have two rolls
            // 3. the last frame of the game with a possibility of 3 rolls
            if (i == 9) {
                for (j in 0..rollIdx) { //rollIdx downTo 0) {
                    message += "   rollIdx $j = ${players[playerIdx].frames[i].points[j]}\n"
                    println("   rollIdx $j = ${players[playerIdx].frames[i].points[j]}")
                    finalScore += players[playerIdx].frames[i].points[j]
                }
            } else {
                if (i == frameIdx) {
                    for (j in 0..rollIdx) { //rollIdx downTo 0) {
                        message += "   rollIdx $j = ${players[playerIdx].frames[i].points[j]}\n"
                        println("   rollIdx $j = ${players[playerIdx].frames[i].points[j]}")
                        finalScore += players[playerIdx].frames[i].points[j]
                    }
                } else {
                    when (players[playerIdx].frames[i].frameType) {
                        FrameType.STRIKE -> {
                            players[playerIdx].frames[i].bonus = players[playerIdx].frames[i + 1].points[0] + players[playerIdx].frames[i + 1].points[1]
                            // This calculates if there are three strikes in a row
                            if (frameIdx - i > 1 && players[playerIdx].frames[i + 1].frameType == FrameType.STRIKE) {
                                players[playerIdx].frames[i].bonus += players[playerIdx].frames[i + 2].points[0]
                            }
                        }
                        FrameType.SPARE -> {
                            players[playerIdx].frames[i].bonus = players[playerIdx].frames[i + 1].points[0]
                        }
                    }
                    var score = players[playerIdx].frames[i].points[0]
                    score += players[playerIdx].frames[i].points[1]
                    score += players[playerIdx].frames[i].bonus
                    finalScore += score
                    message += " [${players[playerIdx].frames[i].points[0]}, ${players[playerIdx].frames[i].points[1]}]  score = $score | $finalScore | frame type: ${players[playerIdx].frames[i].frameType}\n"
                    println(" [${players[playerIdx].frames[i].points[0]}, ${players[playerIdx].frames[i].points[1]}]  score = $score | $finalScore | frame type: ${players[playerIdx].frames[i].frameType}")
                }
            }
        }
        players[playerIdx].setScore(finalScore)
        message += "FINAL SCORE[$frameNumber]: $finalScore\n"
        println("FINAL SCORE[$frameNumber]: $finalScore")
        return finalScore
    }

    /**
     * Bowls a roll for the given frame of the specified [playerIdx], it uses a random number depending on the frame, roll, and previous roll.
     *
     * @throws IllegalArgumentException if the [playerIdx] is not in the list of players.
     *
     * @return the score of the bowl action as a Triple containing the total score, frame number, and roll number.
     */
    fun bowl(playerIdx: Int): Triple<Int, Int, Int> {
        if (playerIdx < 0 || playerIdx > players.size - 1) throw IllegalArgumentException("A valid player index must be provided")
        if (!gameFinished) {

            val randomPinsKnocked: Int = (0..pins).random()
            pins -= randomPinsKnocked
            players[playerIdx].frames[frame-1].points[roll-1] = randomPinsKnocked

            if (frame == 10) {
                // NO BONUS POINTS: The final frame is a special frame. Strikes and spares in this frame scores no bonus points, but do allow you to have an additional shot.
                players[playerIdx].frames[frame-1].frameType = FrameType.LAST
            } else {
                if (randomPinsKnocked == 10 && roll == 1) {
                    // STRIKE
                    players[playerIdx].frames[frame-1].frameType = FrameType.STRIKE
                } else if (roll == 2) {
                    if (players[playerIdx].frames[frame-1].points[0] + players[playerIdx].frames[frame-1].points[1] == 10) {
                        // SPARE
                        players[playerIdx].frames[frame-1].frameType = FrameType.SPARE
                    }
                }
            }

            lastScore = calculateScore(playerIdx = playerIdx, frameNumber = frame, rollNumber = roll, pinsKnocked = randomPinsKnocked)

            if (frame != 10 && players[playerIdx].frames[frame-1].frameType == FrameType.STRIKE) {
                ++roll
            } else if (frame == 10) {
                // corner case for the last frame
                when(roll) {
                    1 -> {
                        if (randomPinsKnocked == 10) pins = 10
                    }
                    2 -> {
                        if (randomPinsKnocked == 10) {
                            pins = 10
                        }
                        else if (players[playerIdx].frames[frame-1].points[0] + players[playerIdx].frames[frame-1].points[1] == 10) {
                            pins = 10
                        }
                        else if (players[playerIdx].frames[frame-1].points[0] + players[playerIdx].frames[frame-1].points[1] < 10) {
                            roll = 3
                        }
                    }
                }
            }

            ++roll
            if (frame != 10 && roll == 3) {
                roll = 1
                ++frame
                pins = PINS
                // next player...
            } else if (frame == 10 && roll >= 4) {
                gameFinished = true
            }
        }

        return Triple(lastScore, frame, roll)
    }

    /**
     * Resets the bowling game for all players.
     */
    fun resetGame() {
        frame = 1
        roll = 1
        pins = PINS
        for (i in 0..players.size-1) {
            players[i].reset()
        }
        gameFinished = false
    }

}