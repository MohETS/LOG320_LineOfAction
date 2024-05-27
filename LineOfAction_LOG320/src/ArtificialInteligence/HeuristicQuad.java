package ArtificialInteligence;

import Game.*;

/**
 * This Class follows the heuristics principles found in the research paper "THE QUAD HEURISTIC IN LINES OF ACTION"
 * Written by Mark H.M. Winands, Jos W.H.M. Uiterwijk and H. Jaap van den Herik
 * A detailed explanation can be found on the paper for the logic behind the heuristic and its implementation.
 *
 * *DISCLAIMER*
 * I did not come up with the logic or the discovery of that heuristic but simply implemented it
 * Link to the paper : https://citeseerx.ist.psu.edu/document?repid=rep1&type=pdf&doi=3e5d2be2314798993a22621880fbad4da529ee76#:~:text=The%20heuristic%20is%20based%20on%20the%20use%20of%20quads%2C%20an,the%20board%20along%20the%20edges.
 */


public class HeuristicQuad {
    private Board board;

    //Euleur value for each player
    private float eulerNumberBlack = 0;
    private float eulerNumberRed = 0;

    /* Variable used for the initialization of the quad Heuristic - Start*/
    private int blackQ1 = 0;
    private int blackQ3 = 0;
    private int blackQd = 0;
    private int redQ1 = 0;
    private int redQ3 = 0;
    private int redQd = 0;

    /* Variable used for the initialization of the quad Heuristic - End*/

    //Value of the quad concerning the starting position of the move
    private float movedPawnValueStartingQuad = 0;
    //Value of the quad concerning the ending position of the move
    private float movedPawnValueEndingQuad = 0;
    //Value of the quad concerning the deleted pawn from the move
    private float deletedPawnValueEndingQuad = 0;


    public HeuristicQuad(Board board) {
        this.board = board;
        initHeuristic();
    }

    /**
     * This method refreshes the Euleur value after being updated.
     * @param color - pawn color
     * @param undo - true if the refresh came from an undo move or false if it came from a move
     */
    public void refreshEuleurValue(int color, boolean undo) {
        if (undo) {
            if (color == 2) {
                this.eulerNumberBlack += (movedPawnValueStartingQuad - movedPawnValueEndingQuad);
                this.eulerNumberRed += (deletedPawnValueEndingQuad);
            } else {
                this.eulerNumberRed += (movedPawnValueStartingQuad - movedPawnValueEndingQuad);
                this.eulerNumberBlack += (deletedPawnValueEndingQuad);
            }
        } else {
            if (color == 2) {
                this.eulerNumberBlack -= (movedPawnValueStartingQuad - movedPawnValueEndingQuad);
                this.eulerNumberRed -= deletedPawnValueEndingQuad;
            } else {
                this.eulerNumberRed -= (movedPawnValueStartingQuad - movedPawnValueEndingQuad);
                this.eulerNumberBlack -= deletedPawnValueEndingQuad;
            }
        }

        movedPawnValueStartingQuad = 0;
        movedPawnValueEndingQuad = 0;
        deletedPawnValueEndingQuad = 0;
    }

    /**
     * This method updates the Euleur value of the player after a move is being made
     * @param color - pawn color
     * @param move - pawn move
     */
    public void eulerValueUpdate(int color, Move move) {
        movedPawnValueStartingQuad = quadValue(color, move.getStartingPosition());
        movedPawnValueEndingQuad = quadValue(color,move.getEndingPosition());
        if(move.isMoveDeletingPawn()){
            deletedPawnValueEndingQuad = quadValue(move.deletedPawn.getColor(),move.getEndingPosition());
        }else{
            deletedPawnValueEndingQuad = quadValue(0,move.getEndingPosition());
        }

    }

    /**
     * This method calculates the quad value for the move of a pawn.
     * This method is based on the equation found in the research paper mentioned in the top of the class file.
     * Refer to it for a better understanding of the logic behind the quad evaluation
     * @param color - pawn color
     * @param position - pawn position
     * @return - value of the quad
     */
    public float quadValue(int color, Position position) {
        if (color == 0) {
            return 0;
        }
        int row = position.getRow();
        int column = position.getCol();

        Pawn[][] boardPawn = this.board.boardPawn;
        boolean quad[][] = new boolean[2][2];
        int numberpawn = 0;

        checkDiagonals(quad, row, column, boardPawn, color);
        int lineSegment = checkSides(quad, row, column, boardPawn, color);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (!quad[i][j]) {
                    numberpawn++;
                }
            }
        }

        return numberpawn - (lineSegment * 0.5f) + 1;
    }

    /**
     * This method checks the square of the quads on the 4 diagonals. (North-East, South-East, North-West, South-West)
     */
    private void checkDiagonals(boolean[][] quad, int row, int column, Pawn[][] boardPawn, int color) {
        if (quadIsInBounds(row - 1, column - 1) && boardPawn[row - 1][column - 1] != null) {
            if (boardPawn[row - 1][column - 1].getColor() == color) {
                quad[0][0] = true;
            }
        }

        if (quadIsInBounds(row - 1, column + 1) && boardPawn[row - 1][column + 1] != null) {
            if (boardPawn[row - 1][column + 1].getColor() == color) {
                quad[0][1] = true;
            }
        }

        if (quadIsInBounds(row + 1, column - 1) && boardPawn[row + 1][column - 1] != null) {
            if (boardPawn[row + 1][column - 1].getColor() == color) {
                quad[1][0] = true;
            }
        }

        if (quadIsInBounds(row + 1, column + 1) && boardPawn[row + 1][column + 1] != null) {
            if (boardPawn[row + 1][column + 1].getColor() == color) {
                quad[1][1] = true;
            }
        }
    }

    /**
     * This method checks the square of the quads on the 4 sides. (North, South, West, East)
     */
    private int checkSides(boolean[][] quad, int row, int column, Pawn[][] boardPawn, int color) {
        int lineSegment = 0;
        if (quadIsInBounds(row - 1, column) && boardPawn[row - 1][column] != null && boardPawn[row - 1][column].getColor() == color) {
            quad[0][0] = true;
            quad[0][1] = true;
        } else {
            lineSegment += 2;
        }

        if (quadIsInBounds(row + 1, column) && boardPawn[row + 1][column] != null && boardPawn[row + 1][column].getColor() == color) {
            quad[1][0] = true;
            quad[1][1] = true;
        } else {
            lineSegment += 2;
        }

        if (quadIsInBounds(row, column - 1) && boardPawn[row][column - 1] != null && boardPawn[row][column - 1].getColor() == color) {
            quad[0][0] = true;
            quad[1][0] = true;
        } else {
            lineSegment += 2;
        }

        if (quadIsInBounds(row, column + 1) && boardPawn[row][column + 1] != null && boardPawn[row][column + 1].getColor() == color) {
            quad[0][1] = true;
            quad[1][1] = true;
        } else {
            lineSegment += 2;
        }

        return lineSegment;
    }

    /**
     * This method initializes the Euleur value for each player.
     */
    public void initHeuristic() {
        Pawn[][] boardPawn = this.board.boardPawn;

        for (int row = 0; row <= 8; row++) {
            for (int column = 0; column <= 8; column++) {
                int[][] quad = new int[2][2];

                if (quadIsInBounds(row, column) && boardPawn[row][column] != null) {
                    quad[0][0] = boardPawn[row][column].getColor();
                }

                if (quadIsInBounds(row - 1, column) && boardPawn[row - 1][column] != null) {
                    quad[1][0] = boardPawn[row - 1][column].getColor();
                }

                if (quadIsInBounds(row, column - 1) && boardPawn[row][column - 1] != null) {
                    quad[0][1] = boardPawn[row][column - 1].getColor();
                }

                if (quadIsInBounds(row - 1, column - 1) && boardPawn[row - 1][column - 1] != null) {
                    quad[1][1] = boardPawn[row - 1][column - 1].getColor();
                }

                quadValueBlack(quad);
                quadValueRed(quad);
            }
        }

        eulerNumberBlack = (blackQ1 * 0.25f) + (blackQ3 * -0.25f) + (blackQd * -0.5f);
        eulerNumberRed = (redQ1 * 0.25f) + (redQ3 * -0.25f) + (redQd * -0.5f);

    }

     /**
     * This method calculates the value of the quad for the black players. The method is used for the initialization only
     */
    private void quadValueBlack(int[][] quad) {
        int numberBlackpawn = 0;
        for (int i = 0; i < quad.length; i++) {
            for (int j = 0; j < quad.length; j++) {
                if (quad[i][j] == 2) {
                    numberBlackpawn++;
                }
            }
        }

        //Q1
        if (numberBlackpawn == 1) {
            blackQ1++;
        }
        //Q3
        if (numberBlackpawn == 3) {
            blackQ3++;
        }
        //Qd
        if (numberBlackpawn == 2 && ((quad[0][0] == 2 && quad[1][1] == 2) || (quad[0][1] == 2 && quad[1][0] == 2))) {
            blackQd++;
        }

    }

    /**
     * This method calculates the value of the quad for the red players. The method is used for the initialization only
     */
    private void quadValueRed(int[][] quad) {
        int numberRedpawn = 0;
        for (int i = 0; i < quad.length; i++) {
            for (int j = 0; j < quad.length; j++) {
                if (quad[i][j] == 4) {
                    numberRedpawn++;
                }
            }
        }

        //Q1
        if (numberRedpawn == 1) {
            redQ1++;
        }
        //Q3
        if (numberRedpawn == 3) {
            redQ3++;
        }
        //Qd
        if (numberRedpawn == 2 && ((quad[0][0] == 4 && quad[1][1] == 4) || (quad[0][1] == 4 && quad[1][0] == 4))) {
            redQd++;
        }

    }

    /**
     * This method checks if the quad in inside the bound of the board
     */
    private boolean quadIsInBounds(int row, int column) {
        if ((0 <= row && row <= 7) && (0 <= column && column <= 7)) {
            return true;
        }
        return false;
    }

    public float getEulerNumberBlack() {
        return eulerNumberBlack;
    }

    public float getEulerNumberRed() {
        return eulerNumberRed;
    }
}
