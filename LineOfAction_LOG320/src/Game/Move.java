package Game;

import Server.PositionDecoder;

public class Move {
    private int length;
    private Position startingPosition;
    private Position endingPosition;
    boolean moveRemovedPawn = false;
    // This variable contains the pawn that was deleted if the move deleted a pawn
    public Pawn deletedPawn;

    public Move(int length, Position startingPosition, Position endingPosition) {
        this.length = length;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
    }

    public Move(Position startingPosition, Position endingPosition) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public Position getEndingPosition() {
        return endingPosition;
    }

    /**
     * Tells if the move is a move that deleted a pawn
     */
    public boolean isMoveDeletingPawn() {
        return moveRemovedPawn;
    }

    /**
     * This method sets if this move was a move that deleted a pawn or not. The deleted pawn is saved as a variable
     * int the move.
     * @param pawnRemoved
     * @param deletedPawn
     */
    public void setPawnDeleted(boolean pawnRemoved, Pawn deletedPawn) {
        this.moveRemovedPawn = pawnRemoved;
        this.deletedPawn = deletedPawn;
    }

    @Override
    public String toString() {
        return "Move{" +
                "length=" + length +
                ", Game.Game.Move=" + PositionDecoder.decode(startingPosition) + PositionDecoder.decode(endingPosition) +
                '}';
    }
}
