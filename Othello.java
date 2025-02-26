//package org.cis1200.tictactoe;

package org.cis1200.othello;

import java.util.ArrayList;
import java.util.List;

import java.io.*;

/**
 * This class is a model for Othello.
 *
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 *
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 *
 * Run this file to see the main method play a game of Othello,
 * visualized with Strings printed to the console.
 */
public class Othello {

    private int[][] board;
    private int numTurns;
    private boolean player1;
    private boolean gameOver;

    /* initialize two arrays of directions:
    1. each cell matches a direction
    2. for example: (1, -1) means moving South West
    */
    int[] dRow = {1, 0, -1, -1, -1, 0, 1, 1};
    int[] dCol = {-1, -1, -1, 0, 1, 1, 1, 0};

    int opponent;

    /**
     * Constructor sets up game state.
     */
    public Othello() {
        reset();
    }

    /* Player Denotations:
    1. Player One (Black Piece): 1
    2. Player Two (White Piece): 2
     */

    /* Coordinates Denotations
    1. x-axis => column
    2. y-axis => row
     */

    /**
     * (Re)sets the game state to start a new game.
     */
    public void reset() {

        // Create an 8:8 board (standard Othello board size)
        board = new int[8][8];

        // Return every variable back to its original state
        numTurns = 0;
        player1 = true;
        gameOver = false;

        /* Start the game by setting 4 pieces of Black (Player 1)
        and White (Player 2) in the middle => Hardcoded
         */
        board[3][3] = 2;
        board[3][4] = 1;
        board[4][3] = 1;
        board[4][4] = 2;
    }

    /* decide who is making a move:
    1. if the player being passed in (the one currently making
    a move) is player 1 => opponent is player 2
    2. else if the opposite then opponent is player 1
    */
    public void setPlayer(int player) {
        if (player == 1) {
            opponent = 2;
        } else {
            opponent = 1;
        }
    }

    /**
     * Method isValid check if a move is valid. A move is valid if:
     * 1. The cell has not been occupied
     * 2. Flips at least one opponent's piece: If a cell is chosen,
     * check in all directions to see if there is at least one other cell
     * apart from the one right adjacent to it that is of the same color
     *
     * Note:
     * 1. This method does not perform any action
     * 2. Boolean here is different from the boolean in playTurn: Here,
     * it is checking if a move could be made - in playTurn, boolean checks
     * if a move has been made successfully.
     */
    public boolean isValid(int c, int r, int player) {

        // Check if cell is empty and game is not over
        if (board[r][c] == 0 && !gameOver) {

            setPlayer(player);

            // for loop to: go through one direction at a time
            for (int dir = 0; dir < 8; dir++) {
                int x = c + dCol[dir];
                int y = r + dRow[dir];
                // control whether there has at least been one opponent detected in between
                boolean hasOpponent = false;

                // control if coordinates x and y are out of bound
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {

                    /* if the adjacent piece is an opponent, switch
                    hasOpponent to true and move on
                     */
                    if (board[y][x] == opponent) {
                        hasOpponent = true;
                    /* if the adjacent piece is player AND hasOpponent
                    is true (meaning there has been at least one opponent
                    detected some time before this, then return true
                     */
                    } else if (board[y][x] == player && hasOpponent) {
                        return true;
                    /* if none of those things are true, meaning adjacent
                    piece is either player or empty, then return false
                     */
                    } else {
                        break;
                    }

                    // keep moving the coordinates in the current direction
                    x += dCol[dir];
                    y += dRow[dir];
                }

            }
        }
        return false;
    }

    /**
     * Method flipPiece
     * 1. Return a boolean: true if a piece could be flipped, false otherwise
     * 2. Flip opponent's piece in every available direction
     */
    public void flipPiece(int c, int r, int player) {

        setPlayer(player);

        // the logic here is similar to isValid
        for (int dir = 0; dir < 8; dir++) {
            int x = c + dCol[dir];
            int y = r + dRow[dir];
            boolean hasOpponent = false;
            // holds all the pieces needed to be flipped
            List<int[]> piecesToFlip = new ArrayList<>();

            while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                if (board[y][x] == opponent) {
                    hasOpponent = true;
                    // if an opponent is detected, add the coordinate to the list
                    piecesToFlip.add(new int[]{y, x});
                } else if (board[y][x] == player && hasOpponent) {
                    /* when reach a player piece after opponent pieces
                    are detected, flip all the opponent pieces.
                     */
                    for (int[] i : piecesToFlip) {
                        board[i[0]][i[1]] = player;
                    }
                    break;
                } else {
                    break;
                }
                x += dCol[dir];
                y += dRow[dir];
            }

        }
    }

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     *
     * Note: so playTurn should
     * 1. Validate if a move is valid by calling isValid
     * 2. Flip the piece between it by calling flipPiece
     * 3. Return true of a move has been successfully made, false otherwise
     */
    public boolean playTurn(int c, int r) {

        int currentPlayer;
        if (player1) {
            currentPlayer = 1;
        } else {
            currentPlayer = 2;
        }

        if (isValid(c, r, currentPlayer)) {
            board[r][c] = currentPlayer;
            flipPiece(c, r, currentPlayer);
            if (checkWinner() == 0) {
                player1 = !player1;
            }
            return true;
        }
        return false;
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won, 3 if the game hits stalemate
     */
    public int checkWinner() {

        int numPlayer1 = 0;
        int numPlayer2 = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == 1) {
                    numPlayer1++;
                } else if (board[row][col] == 2) {
                    numPlayer2++;
                }
            }
        }

        /* If we are out of move (total moves = 64) check the winner
        If not, return 0 and continue the game
         */
        if (numPlayer1 + numPlayer2 >= 64 || !hasValidMove()) {
            gameOver = true;
            if (numPlayer1 > numPlayer2) {
                return 1;
            } else if (numPlayer1 < numPlayer2) {
                return 2;
            } else {
                return 3;
            }
        }
        return 0;
    }

    public boolean hasValidMove() {
        int currentPlayer;
        if (player1) {
            currentPlayer = 1;
        } else {
            currentPlayer = 2;
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (isValid(col, row, currentPlayer)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            if (i < 2) {
                System.out.println("\n---------");
            }
        }
    }

    // Getter and Setter methods

    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     *
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = empty, 1 = Player 1, 2 = Player 2
     */
    public int getCell(int c, int r) {
        return board[r][c];
    }

    public int[][] getBoard() {
        return board;
    }

    public void updateBoard(int[][] board) {
        this.board = board;
    }

    public boolean getGameStatus() {
        return gameOver;
    }

    public void setGameStatus(boolean gameOver) {
        this.gameOver = gameOver;
    }

    // Saving game state
    public void saveGame(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Save the board state
            for (int[] row : board) {
                for (int cell : row) {
                    writer.print(cell + " ");
                }
                writer.println(); // new line after each row
            }

            // Save additional game state variables
            writer.println(numTurns);
            writer.println(player1);
            writer.println(gameOver);
        }
    }

    public void loadGame(String fileName) throws IOException {

        // Create a buffered reader
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Reset board
            board = new int[8][8];
            // Load board state
            for (int r = 0; r < 8; r++) {
                // Read the line first
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Error reading file");
                }
                // separate into tokens to loop through
                String[] tokens = line.trim().split("\\s+");
                for (int c = 0; c < 8; c++) {
                    board[r][c] = Integer.parseInt(tokens[c]);
                }
            }

            // Load additional game state variables
            numTurns = Integer.parseInt(reader.readLine());
            player1 = Boolean.parseBoolean(reader.readLine());
            gameOver = Boolean.parseBoolean(reader.readLine());
        } catch (IOException e) {
            System.err.println("Error loading game");
        }
    }
}
