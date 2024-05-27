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
public class HeuristicCentralization {

    private Board board;
    private float valueBlackPawn = 0;
    private float valueRedPawn = 0;


    public HeuristicCentralization(Board board) {
        this.board = board;
    }

    /**
     * This method updates the centralization value after moves have been made.
     * @param move - pawn move
     * @param color - pawn color
     * @param undo -  true if the update is called when a move is undone or false if a move is being played
     */
    public void update(Move move, int color, boolean undo) {
        if (undo) {
            if (color == 2) {
                this.valueBlackPawn += (centralizationValue(move.getStartingPosition()) - centralizationValue(move.getEndingPosition()));
                if (move.isMoveDeletingPawn()) {
                    this.valueRedPawn += centralizationValue(move.getEndingPosition());
                }
            } else {
                this.valueRedPawn += (centralizationValue(move.getStartingPosition()) - centralizationValue(move.getEndingPosition()));
                if (move.isMoveDeletingPawn()) {
                    this.valueBlackPawn += centralizationValue(move.getEndingPosition());
                }
            }
        } else {
            if (color == 2) {
                this.valueBlackPawn += (centralizationValue(move.getEndingPosition()) - centralizationValue(move.getStartingPosition()));
                if (move.isMoveDeletingPawn()) {
                    this.valueRedPawn -= centralizationValue(move.getEndingPosition());
                }
            } else {
                this.valueRedPawn += (centralizationValue(move.getEndingPosition()) - centralizationValue(move.getStartingPosition()));
                if (move.isMoveDeletingPawn()) {
                    this.valueBlackPawn -= centralizationValue(move.getEndingPosition());
                }
            }
        }
    }

    /**
     * This method calculates the centralization value based on its position on the board. The closer to the center
     * (3,3 coordinates for example) the greater the value of the centralization heuristic. For more details see the
     * paper mentioned in the beginning of the class file.
     * @param position
     * @return
     */
    public float centralizationValue(Position position) {
        int row = position.getRow();
        int column = position.getCol();

        //First border value
        if (row == 0 || column == 0) {
            return 0;
        } else if (row == 7 || column == 7) {
            return 0;
        }

        //Second border value
        if (row == 1 || column == 1) {
            return 1;
        } else if (row == 6 || column == 6) {
            return 1;
        }

        //Third border value
        if (row == 2 || column == 2) {
            return 2;
        } else if (row == 5 || column == 5) {
            return 2;
        }

        //Fourth border value
        if (row == 3 || column == 3) {
            return 3;
        } else if (row == 4 || column == 4) {
            return 3;
        }

        return 0;
    }


    public float getValueBlackPawn() {
        return valueBlackPawn;
    }

    public float getValueRedPawn() {
        return valueRedPawn;
    }
}
