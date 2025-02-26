package org.cis1200.othello;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RunOthello implements Runnable {
    public void run() {

        // Top-level frame containing all components
        final JFrame frame = new JFrame("Othello"); // Title Othello appears on top
        frame.setLocation(800, 800);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        // Status panel initially displays "Setting Up"
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Count panel
        final JPanel count_panel = new JPanel();
        count_panel.setLayout(new BoxLayout(count_panel, BoxLayout.Y_AXIS));
        frame.add(count_panel, BorderLayout.EAST);
        final JLabel player1Count = new JLabel("Player 1 (Black) pieces: 2");
        final JLabel player2Count = new JLabel("Player 2 (Gray) pieces: 2");
        count_panel.add(player1Count);
        count_panel.add(player2Count);

        // Game board
        final GameBoard board = new GameBoard(status, player1Count, player2Count);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> {
            String instructionText =
                    """
                            Game Instructions:
                            1. The game is played on an 8x8 board with black and gray pieces.
                            2. The game starts with two black and two gray pieces in the\s
                            center of the board.
                            3. Place your piece so that your opponent's pieces are\s
                            bordered at the ends by
                            pieces of your color. Black always moves first.
                            4. Players take turns placing a piece of their color\s
                            on an empty square.
                            5. A move is only legal if it surrounds one or more of\s
                            the opponent's pieces.
                            6. When you outflank your opponent, their pieces are\s
                            flipped to your color.
                            7. The game ends when no more moves are possible.
                   \s""";

            JOptionPane.showMessageDialog(frame, instructionText, "Othello Game Instructions",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        control_panel.add(instructions);

        // Game panel
        final JPanel game_state_panel = new JPanel();
        frame.add(game_state_panel, BorderLayout.WEST);
        Othello game = new Othello();

        // Save option
        final JButton save = new JButton("Save");
        save.addActionListener(e -> {

            // Create a JFileChoose
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Othello Game");

            /* Pops up a "Save File" file choose dialogue. The int
            represent the user option */
            int userSelection = fileChooser.showSaveDialog(frame); // parameter is parents

            // if user approve the saved file, gets the chosen file
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Get selected file returns the selected file
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    game.saveGame(fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(frame,
                            "Game saved successfully!",
                            "Save Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error saving game: " + ex.getMessage(),
                            "Save Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        game_state_panel.add(save);

        // Load option - follows similar logic to Save
        final JButton load = new JButton("Load");
        load.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Othello Game");

            int userSelection = fileChooser.showOpenDialog(frame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                try {
                    game.loadGame(fileToLoad.getAbsolutePath());
                    // Update the board to reflect loaded game state
                    game.updateBoard(game.getBoard());
                    JOptionPane.showMessageDialog(frame,
                            "Game loaded successfully!",
                            "Load Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Error loading game: " + ex.getMessage(),
                            "Load Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        game_state_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}