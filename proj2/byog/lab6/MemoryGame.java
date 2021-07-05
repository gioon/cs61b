package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Initialize random number generator
        this.rand = new Random(seed);
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        gameOver = false;
        playerTurn = false;
        round = 1;

        // Establish Game loop
        while (!gameOver) {
            drawFrame("Round " + round + "!");
            StdDraw.pause(1000);

            String randStr = generateRandomString(round);
            flashSequence(randStr);

            playerTurn = true;
            String answer = solicitNCharsInput(round);
            playerTurn = false;

            if (answer.equals(randStr)) {
                drawFrame("Correct!");
                StdDraw.pause(1000);
                round++;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }
    }

    public void drawFrame(String s) {
        // Take the string and display it in the center of the screen
        // If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        if (!gameOver) {
            Font font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);

            StdDraw.textLeft(1, height - 1, "Round: " + round);

            if (playerTurn) {
                StdDraw.text(width / 2.0, height - 1, "Type!");
            } else {
                StdDraw.text(width / 2.0, height - 1, "Watch!");
            }

            String e = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            StdDraw.textRight(width - 1, height - 1, e);

            StdDraw.line(0, height - 2, width, height - 2);
        }

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(width / 2.0, height / 2.0, s);

        StdDraw.show();
    }

    public String generateRandomString(int n) {
        // Generate random string of letters of length n
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            s.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
        }
        return s.toString();
    }

    public void flashSequence(String letters) {
        // Display each character in letters, making sure to blank the screen between letters
        for (char letter: letters.toCharArray()) {
            drawFrame(String.valueOf(letter));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        // Read n letters of player input
        String s = "";
        while (s.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                s += c;
                drawFrame(s);
            }
        }

        StdDraw.pause(1000);
        return s;
    }
}
