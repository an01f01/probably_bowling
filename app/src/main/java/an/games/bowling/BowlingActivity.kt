package an.games.bowling

import an.games.bowling.core.BowlingGame
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BowlingActivity : AppCompatActivity() {

    /**
     * @property bowlingGame object that manages the bowling game.
     */
    private var bowlingGame: BowlingGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bowling)

        var players = Array<String>(1){"John Doe"}
        bowlingGame = BowlingGame(_players = players)

        val logView: TextView = findViewById<TextView>(R.id.logText)
        findViewById<Button>(R.id.bowl).setOnClickListener { view ->
             // textView.text = "Internet Access";
            var score: Triple<Int, Int, Int>? = bowlingGame?.bowl(0)
            println("BOWL $score")
            logView.text = bowlingGame?.getMessage()
        }
        findViewById<Button>(R.id.reset).setOnClickListener { view ->
            // textView.text = "Internet Access";
            println("RESET GAME")
            bowlingGame?.resetGame()
            logView.text = "RESET GAME"
        }
    }
}