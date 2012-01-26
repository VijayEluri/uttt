import java.io.*;
import java.util.Scanner;
import java.util.Random;

class Uttt {
    private Board[] smallBoards;
    private Board masterBoard;

    private int currentPlayer;
    private int currentBoard;
    private boolean shouldPlay;

    private static final char[] chars = {' ', 'X', 'O'};
    private static final int MAX_MOVES = 81;
    private static final int NUM_ROWS = 27;

    public Uttt() {
        this.smallBoards = new Board[9];
        for (int i = 0; i < this.smallBoards.length; i++) {
            this.smallBoards[i] = new Board();
        }
        this.masterBoard = new Board();

        this.currentPlayer = 1;
        this.currentBoard = 0;
        this.shouldPlay = true;
        return;
    }

    public void initializeBoards() {
        for (int i = 0; i < this.smallBoards.length; i++) {
            this.smallBoards[i].initialize();
        }
        return;
    }

    public void saluteWinner(int winner) {
        if (winner == 0) {
            System.out.println("A draw, then?  You know, back in my day, they didn't have draws.  This game isn't what it used to be...");
        } else {
            System.out.println("Congratulations, player " + winner + "! You are the winner!  You have a bright future ahead of you, son."); 
        }
    }

    public boolean promptToPlayAgain() {
        System.out.println("Play another? (Y or N)");
        Scanner s = new Scanner(System.in);
        boolean answerSatisfactory = false;
        while (!answerSatisfactory) {
            String response = s.next();
            char firstChar = response.toLowerCase().charAt(0);
            if (firstChar == 'y') {
                System.out.println("And they're going at it again!  Get your popcorn, ladies and gents, it's gonna be a hell of a show!");
                return true;
            } else if (firstChar == 'n') {
                System.out.println("Right then.  Cheerio.  Well-played all around.  I'll see you down at the pub then, eh?");    
                return false;
            }
        }
        return false;
    }

    // print the current state of the game to the console
    public void printBoards() {
        for (int i = 0; i < NUM_ROWS; i++) {
            if (i % 3 == 0) {
                printBasicLine();
            } else if (i % 9 == 8) {
                printBasicLine();
                if (i != 26) {
                    printLargeDivider();
                }
            } else if (i % 3 == 2) {
                printSmallDivider();
            } else if (i % 3 == 1) {
                printBoardRow(i);
            }
        }
        return;
    }

    // helper method to this.printBoards()
    private void printBasicLine() {
        System.out.println("   |   |   |/|   |   |   |/|   |   |   ");
        return;
    }

    // helper method to this.printBoards()
    private void printLargeDivider() {
        System.out.println("///|///|///|/|///|///|///|/|///|///|///");
        return;
    }

    // helper method to this.printBoards()
    private void printSmallDivider() {
        System.out.println("___|___|___|/|___|___|___|/|___|___|___");
        return;
    }

    // helper method to this.printBoards().
    private void printBoardRow(int row){
        char[] marks = new char[9];
        int masterBoardRow = row / 9;
        int subRow = (row % 9) / 3;
        for (int i = 0; i < 3; i++) {
            int board = masterBoardRow * 3 + i;
            Board b = this.smallBoards[board];
            for (int j = 0; j < 3; j++) {
                int value = b.getValueInSpace(subRow * 3 + j); 
                marks[i * 3 + j] = this.chars[value];
            }
        }

        System.out.println(" " + marks[0] + " | " + marks[1] + " | " + marks[2] + " |/| " + marks[3] + " | " + marks[4] + " | " + marks[5] + " |/| "+ marks[6] + " | " + marks[7] + " | " + marks[8] + " ");

        return;                         
    }

    public int play() {
        int winner = 0;
        Scanner s = new Scanner(System.in);

        for (int i = 0; i < MAX_MOVES; i++) {
            int move = 0;
            boolean moveSuccessful = false;

            // if the board that the current player must move in is full, other player gets to choose the new current board
            while (this.smallBoards[this.currentBoard].isFull()) {
                System.out.println("Player " + (this.currentPlayer % 2 + 1) + ", choose the current board");
                this.currentBoard = s.nextInt() - 1;
            }

            System.out.println("Current Board: " + (this.currentBoard + 1));
            System.out.println("Player " + this.currentPlayer + ", what is your move? (Type an integer, 1-9)");
                
            while (!moveSuccessful) {
                move = s.nextInt();
                // player can input '10' to offer a draw. this block of code handles a draw offer.
                if (move == 10) {
                    boolean drawOfferResolved = false;
                    while (!drawOfferResolved) {
                        System.out.println("Player " + this.currentPlayer + " offers a draw. Player " + (this.currentPlayer % 2 + 1) + ", do you accept? (Y or N)");
                        String line = s.next();
                        char firstChar = line.toLowerCase().charAt(0);
                        if (firstChar == 'y') {
                            System.out.println("Player " + (this.currentPlayer % 2 + 1) + " accepts.");
                            return 0;
                        } else if (firstChar == 'n') {
                            System.out.println("Oh it's on, baby.  It is ON. Player " + this.currentPlayer + ", it's your move.");
                            move = s.nextInt();
                            if (move != 10) {
                                drawOfferResolved = true;
                            }
                        } 
                    }
                }

                // player will treat a board as 1-indexed, but behind the scenes it's 0-indexed
                move -= 1;

                moveSuccessful = this.smallBoards[this.currentBoard].setValueInSpace(move, this.currentPlayer); 
            }

            // if no one has won this board yet, check to see if someone won it on this turn
            if (this.masterBoard.getValueInSpace(this.currentBoard) == 0) {
                int smallBoardWinner = this.smallBoards[this.currentBoard].checkForWinner();
                // if the small board has a winner, update the master board and check if someone won the game. if yes, return the winner.
                if (smallBoardWinner > 0) {
                    System.out.println("We have a smallboard winner! Player " + this.currentPlayer + " has won board " + (this.currentBoard + 1));
                    this.masterBoard.setValueInSpace(this.currentBoard, this.currentPlayer);
                    int realWinner = this.masterBoard.checkForWinner();
                    if (realWinner > 0) {
                        return realWinner; 
                    }
                }
            }
            this.currentBoard = move;
            this.currentPlayer = this.currentPlayer % 2 + 1;
            printBoards();
        }
        return 0;
    }

    public void move() {
        System.out.println("Player " + this.currentPlayer + ", it's your move");

        return;
    }

    public boolean shouldPlay() {
        return this.shouldPlay;
    }

    public void setCurrentBoard(int board) {
        this.currentBoard = board;
    }

    public void setShouldPlay(boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    public static void main (String[] args) {
        Uttt game = new Uttt();

        game.printBoards();

        while(game.shouldPlay()) {
            game.initializeBoards();

            Random generator = new Random();

            System.out.println("Randomly assigning a starting board:");
            game.setCurrentBoard(generator.nextInt(9) + 1);
            int winner = game.play();
            game.saluteWinner(winner);
            game.setShouldPlay(game.promptToPlayAgain());
        }
    }
}
