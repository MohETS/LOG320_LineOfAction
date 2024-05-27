package Game;

import java.util.ArrayList;

public class Pawn {

    //2 for black | 4 for red
    private int color, opponentColor;
    //Variable to know if the pawn is linked to another pawn
    private boolean linked;
    //Variable to know if the pawn is alive
    private boolean alive = true;
    //Position of the pawn on the board
    private Position position;
    //Board of the game
    private Board gameBoard;
    //Should always contain 8 moves | (Concept of direction north, north-east, east, etc...)

    public Pawn(int color, int row, int col, Board board) {
        this.color = color;
        if (color == 2) {
            opponentColor = 4;
        } else {
            opponentColor = 2;
        }
        linked = false;
        position = new Position(row, col);
        this.gameBoard = board;
    }

    public ArrayList<Move> generatePawnMoves() {
        ArrayList<Move> temp = MoveManager.generateMoves(this);
        return temp;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getColor() {
        return color;
    }

    public int getOpponentColor() {
        return opponentColor;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean getLinked() {
        return linked;
    }

    /**
     * This method unlinks the pawn by setting its linked value back to false.
     */
    public void setUnlinked() {
        this.linked = false;
    }

    /**
     * Checks if the square is inside the bounds of the board.
     */
    private boolean insideBounds(int i, int j) {
        int row = position.getRow() - 1;
        int col = position.getCol() - 1;
        return (0 <= row + i && row + i <= 7) && (0 <= col + j && col + j <= 7);
    }

    /**
     * Checks if the square is empty.
     */
    private boolean emptySquare(int row, int col) {
        return gameBoard.boardPawn[row][col] == null;
    }

    /**
     * Checks if the pawn in the square is the same color as the Pawn object.
     */
    private boolean coloredSquare(int row, int col) {
        return gameBoard.boardPawn[row][col].getColor() == color;
    }

    /**
     * Checks if the pawn in the square is already linked.
     */
    private boolean linkedSquare(int row, int col) {
        return gameBoard.boardPawn[row][col].getLinked() == false;
    }

    /**
     * Checks if the pawn in the square is alive.
     */
    private boolean aliveSquare(int row, int col) {
        return gameBoard.boardPawn[row][col].isAlive();
    }

    /**
     * This method recursively checks if the pawns are linked together.
     * The surroundings squares of the first pawn are checked and if another pawn is there the method is called on
     * the new pawn until there is no more pawns surrounding each other.
     */
    public void isLinked() {
        if (alive) {
            if(color == 2){
                gameBoard.blackPawnConnected();
            }else{
                gameBoard.redPawnConnected();
            }
            linked = true;
        }

        int row = position.getRow() - 1;
        int col = position.getCol() - 1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //Checks if it's inside the board and if the nearby squares are not empty
                if (insideBounds(i, j) && !emptySquare(row + i, col + j)) {
                    //Checks if the nearby square is the same color, if the square is not already linked and if the pawn on the square is alive
                    if (coloredSquare(row + i, col + j) && linkedSquare(row + i, col + j) && aliveSquare(row + i, col + j)) {
                        gameBoard.boardPawn[row + i][col + j].isLinked();
                    }
                }
            }
        }
    }

    /**
     *  This method tells if the pawn is on the sides of the board.
     * @return
     */
    public boolean isOnSides() {
        if (position.getRow() == 0 || position.getRow() == 7) {
            return true;
        }
        if (position.getCol() == 0 || position.getCol() == 7) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "color=" + color +
                "," +
                "" + position +
                '}';
    }
}
