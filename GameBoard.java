package org.cis1200.othello;

// import org.cis1200.othello.Othello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Othello othello;
    private JLabel status; // current status
    private JLabel player1Count;
    private JLabel player2Count;

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;
    public static final int GRID_SIZE = 8; // number of rows and columns
    public static final int CELL_SIZE = BOARD_WIDTH / GRID_SIZE; // size of a single cell

    /**
     * Initializes the game board.
     */

    // This is the constructor

    public GameBoard(JLabel statusInit, JLabel player1CountInit, JLabel player2CountInit) {

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // Initializes JLabel
        status = statusInit;
        player1Count = player1CountInit;
        player2Count = player2CountInit;

        // Initialize game model
        othello = new Othello();

        /*
         * Listens for mouse clicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / CELL_SIZE;
                int row = e.getY() / CELL_SIZE;

                if (othello.playTurn(col, row)) {
                    updateStatus();
                    updatePieceCount();
                    repaint();
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        othello.reset();

        // update status
        status.setText("Player 1's Turn");

        // update piece count
        player1Count.setText("Player 1 (Black) Pieces: 2");
        player2Count.setText("Player 2 (Gray) Pieces: 2");

        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (othello.getCurrentPlayer()) {
            status.setText("Player 1's Turn");
        } else {
            status.setText("Player 2's Turn");
        }

        int winner = othello.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        } else if (winner == 3) {
            status.setText("It's a tie.");
        }
    }

    /* updatePieceCount() - if a move has been successful,
    update the number of piece for the respective player
    Problem: How do you verify if a move is successful?
     */
    private void updatePieceCount() {
        int player1Pieces = 0;
        int player2Pieces = 0;

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (othello.getCell(row, col) == 1) {
                    player1Pieces++;
                } else if (othello.getCell(row, col) == 2) {
                    player2Pieces++;
                }
            }
        }

        player1Count.setText("Player 1 (Black) Pieces: " + player1Pieces);
        player2Count.setText("Player 2 (Gray) Pieces: " + player2Pieces);
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw grid lines
        g.setColor(Color.BLACK);
        for (int i = 0; i <= GRID_SIZE; i++) {
            // draw the vertical lines
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, BOARD_HEIGHT);
            // draw horizontal lines
            g.drawLine(0, i * CELL_SIZE, BOARD_WIDTH, i * CELL_SIZE);
        }

        //draw the pieces
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int cell = othello.getCell(col, row);
                if (cell == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(col * CELL_SIZE,
                            row * CELL_SIZE,
                            CELL_SIZE,
                            CELL_SIZE);
                } else if (cell == 2) {
                    g.setColor(Color.GRAY);
                    g.fillOval(col * CELL_SIZE,
                            row * CELL_SIZE,
                            CELL_SIZE,
                            CELL_SIZE);
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
