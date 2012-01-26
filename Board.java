public class Board {

    private int[] spaces;

    public int getValueInSpace(int space) {
        return spaces[space];    
    }

    public boolean setValueInSpace(int space, int value) {
        if (space < 0 || space > 8) {
            System.out.println("Please choose a space by typing a number between 1 and 9");
            return false;
        }
        if (spaces[space] != 0) {
            System.out.println("Sorry, that space has already been taken! Please try again.");
            return false;
        }

        int humanReadableMove = space + 1;
        System.out.println("So you move " + humanReadableMove + ", eh? Very well.");
        spaces[space] = value;        
        return true;
    }

    public Board() {
        spaces = new int[9];
        initialize();
        return;
    }

    public void initialize() {
        for (int i = 0; i < 9; i++) {
            this.spaces[i] = 0;
        }
        return;
    }

    public int checkForWinner() {
        int winner = 0;
        winner = checkUpwardDiagonal();
        if (winner > 0) return winner;
        winner = checkDownwardDiagonal();
        if (winner > 0) return winner;
        for (int i = 0; i < 3; i++) {
            winner = checkRow(i);
            if (winner > 0) return winner;
        }
        for (int i = 0; i < 3; i++) {
            winner = checkColumn(i); 
            if (winner > 0) return winner;
        }

        return 0;
    }

    public int checkUpwardDiagonal() {
        if (spaces[6] == 0) {
            return 0;
        } else {
            int prospectiveWinner = spaces[6];    
            if (spaces[4] == prospectiveWinner && spaces[2] == prospectiveWinner) {
                return prospectiveWinner;
            }
        }
        return 0;
    }

    public int checkDownwardDiagonal() {
        if (spaces[0] == 0) {
            return 0;
        } else {
            int prospectiveWinner = spaces[0];    
            if (spaces[4] == prospectiveWinner && spaces[8] == prospectiveWinner) {
                return prospectiveWinner;
            }
        }
        return 0;
    }

    public int checkRow(int row) {
        int prospectiveWinner = 0;
        for (int i = 0; i < 3; i++) {
            int space = row * 3 + i;
            // if any space hasn't been played, no winner
            if (spaces[space] == 0) {
                return 0;
            } 
            // if the first space has been played, only that player could be the winner
            if (i == 0) {
                prospectiveWinner = spaces[space];
            // if a subsequent space has been played by the other player, no winner
            } else if (i > 0 && spaces[space] != prospectiveWinner) {
                return 0;
            }
        }
        // if we haven't returned yet, we have a winner!
        return prospectiveWinner;
    }

    public int checkColumn(int column) {
        int prospectiveWinner = 0;
        for (int i = 0; i < 3; i++) {
            int space = column + 3 * i;
            if (spaces[space] == 0) {
                return 0;
            } 
            // if the first space has been played, only that player could be the winner
            if (i == 0) {
                prospectiveWinner = spaces[space];
            // if a subsequent space has been played by the other player, no winner
            } else if (i > 0 && spaces[space] != prospectiveWinner) {
                return 0;
            }
        }
        return prospectiveWinner;
    }

    public boolean isFull() {
        for (int i = 0; i < spaces.length; i++) {
            if (spaces[i] == 0) {
                return false;
            }
        }
        return true;
    }

}
