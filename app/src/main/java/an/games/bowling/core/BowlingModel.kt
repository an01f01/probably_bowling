package an.games.bowling.core

/**
 * A type of frame.
 * @author Alessandro Negri.
 * @since version 0.1.
 *
 * The enum class defines a type of frame.
 */
enum class FrameType {
    NORMAL, SPARE, STRIKE, LAST
}

/**
 * A Frame for a bowling game.
 * @author Alessandro Negri.
 * @since version 0.1.
 *
 * The class represents a frame of a bowling game.
 */
class Frame {

    /**
     * @property points the fixed array of size 3 for the points for each roll, depending on the frame and roll value there can be up to three rolls.
     */
    var points: IntArray = IntArray(3)

    /**
     * @property bonus the bonus points for the frame.
     */
    var bonus: Int = 0

    /**
     * @property frameType the type of frame.
     */
    var frameType: FrameType = FrameType.NORMAL
}

/**
 * A player.
 * @author Alessandro Negri.
 * @since version 0.1.
 *
 * The class represents a player for the bowling game.
 *
 * @param _name: A string containing the name of the player.
 */
class Player(_name: String) {

    /**
     * @property name the String containing the name of the player.
     */
    var name: String

    /**
     * @property frames the fixed array containing 10 frames required for a bowling game.
     */
    var frames: Array<Frame>

    /**
     * @property finalScore the final score calculated for reference.
     */
    var finalScore: Int

    /**
     * Initializes the [name] and the array frames to a fixes size of 10.
     */
    init {
        this.name = _name
        this.frames =  Array<Frame>(10) { Frame() }
        this.finalScore = 0
    }

    /**
     * Resets the frames for the player.
     */
    fun reset() {
        this.frames =  Array<Frame>(10) { Frame() }
        this.finalScore = 0
    }

    /**
     * Sets the score for the player.
     *
     * @param score: sets the current score for the player.
     */
    fun setScore(score: Int) {
        this.finalScore = score
    }

    /**
     * @return the final score.
     */
    fun getScore(): Int {
        return this.finalScore
    }

}