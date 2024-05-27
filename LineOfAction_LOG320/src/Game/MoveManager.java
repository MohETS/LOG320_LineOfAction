package Game;

import java.util.ArrayList;

public class MoveManager {

    private static ArrayList<Move> listMove;
    private static Board gameBoard;

    private static Position pawnPosition;
    private static int pawnColor, pawnOpponentColor;

    public MoveManager(Board board) {
        gameBoard = board;
    }

    public static ArrayList<Move> generateMoves(Pawn pawn) {
        listMove = new ArrayList<>();
        pawnPosition = pawn.getPosition();
        pawnColor = pawn.getColor();
        pawnOpponentColor = pawn.getOpponentColor();
        Move moveHolder;


        //Generate Move North and South
        int length = calculateVerticalMoveLength();
        moveHolder = generateNorthMove(length);
        if (moveHolder != null) {
            listMove.add(moveHolder);
        }
        moveHolder = generateSouthMove(length);
        if (moveHolder != null) {
            listMove.add(moveHolder);
        }

        //Generate Move East and West
        length = calculateHorizontalMoveLength();
        moveHolder = generateEastMove(length);
        if (moveHolder != null) {
            listMove.add(moveHolder);
        }
        moveHolder = generateWestMove(length);
        if (moveHolder != null) {
            listMove.add(moveHolder);
        }


        //Generate Move NorthWest and SouthEast
        if (pawnPosition.getRow() - pawnPosition.getCol() >= 0) {
            //Calculate from the left
            length = calculateDiagonalTopBottomMoveLength(true);
            moveHolder = generateNorthWestMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
            moveHolder = generateSouthEastMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }

        } else {
            //Calculate from the top
            length = calculateDiagonalTopBottomMoveLength(false);
            moveHolder = generateNorthWestMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
            moveHolder = generateSouthEastMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
        }

        //Generate Move NorthEast and SouthWest
        if (pawnPosition.getRow() + pawnPosition.getCol() <= 7) {
            length = calculateDiagonalBottomTopMoveLength(true);
            moveHolder = generateNorthEastMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
            moveHolder = generateSouthWestMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }

        } else {
            length = calculateDiagonalBottomTopMoveLength(false);
            moveHolder = generateNorthEastMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
            moveHolder = generateSouthWestMove(length);
            if (moveHolder != null) {
                listMove.add(moveHolder);
            }
        }

        return listMove;
    }

    //** Start North and South move methods**//
    /**
     * This method calculates the length for a vertical move
     */
    private static int calculateVerticalMoveLength() {
        int length = 0;
        //Calculate length of move North South
        for (int i = 0; i < gameBoard.boardPawn.length; i++) {
            if (gameBoard.boardPawn[i][pawnPosition.getCol()] != null) {
                length++;
            }
        }
        return length;
    }

    private static Move generateNorthMove(int length) {
        //Game.Game.Move North - Check if case if empty or case is occupied by opponent
        if ((pawnPosition.getRow() - length) >= 0) {
            Pawn northBoardCase = gameBoard.boardPawn[(pawnPosition.getRow() - length)][pawnPosition.getCol()];
            if (northBoardCase == null || northBoardCase.getColor() != pawnColor) {
                if (!northMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position((pawnPosition.getRow() - length), pawnPosition.getCol()));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the north move of the pawn is blocked by an opponent in its path
     */
    private static boolean northMoveBlocked(int length) {
        for (int i = 1; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol()] != null && gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol()].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    private static Move generateSouthMove(int length) {
        //Game.Game.Move South - Check if case if empty or case is occupied by opponent
        if ((pawnPosition.getRow() + length) <= 7) {
            Pawn southBoardCase = gameBoard.boardPawn[(pawnPosition.getRow() + length)][pawnPosition.getCol()];
            if (southBoardCase == null || southBoardCase.getColor() != pawnColor) {
                if (!southMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position((pawnPosition.getRow() + length), pawnPosition.getCol()));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the south move of the pawn is blocked by an opponent in its path
     */
    private static boolean southMoveBlocked(int length) {
        for (int i = 1; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol()] != null && gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol()].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    //** End North and South move methods**//


    //** Start East and West move methods**//
    /**
     * This method calculates the length for a horizontal move
     */
    private static int calculateHorizontalMoveLength() {
        //Calculate length of move West East
        int length = 0;
        for (int i = 0; i < gameBoard.boardPawn.length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow()][i] != null) {
                length++;
            }
        }
        return length;
    }

    private static Move generateEastMove(int length) {
        //Game.Game.Move East
        if ((pawnPosition.getCol() + length) <= 7) {
            Pawn eastBoardCase = gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() + length];
            if (eastBoardCase == null || eastBoardCase.getColor() != pawnColor) {
                if (!eastMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position(pawnPosition.getRow(), (pawnPosition.getCol() + length)));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the east move of the pawn is blocked by an opponent in its path
     */
    private static boolean eastMoveBlocked(int length) {
        for (int i = 1; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() + i] != null && gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() + i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    private static Move generateWestMove(int length) {
        //Game.Game.Move West
        if ((pawnPosition.getCol() - length) >= 0) {
            Pawn westBoardCase = gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() - length];
            if (westBoardCase == null || westBoardCase.getColor() != pawnColor) {
                if (!westMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position(pawnPosition.getRow(), (pawnPosition.getCol() - length)));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the west move of the pawn is blocked by an opponent in its path
     */
    private static boolean westMoveBlocked(int length) {
        for (int i = 1; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() - i] != null && gameBoard.boardPawn[pawnPosition.getRow()][pawnPosition.getCol() - i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    //** End East and West move methods**//


    //** Start NorthWest and SouthEast move methods**//
    /**
     * This method calculates the length for a diagonal move with the direction going from the top left of the
     * board to the bottom right of the board.
     */
    private static int calculateDiagonalTopBottomMoveLength(boolean startLeft) {
        //Calculate length of move North-West South-East
        int length = 0;

        if (startLeft) {
            int row = pawnPosition.getRow() - pawnPosition.getCol();
            int i = row;
            for (int j = 0; j < gameBoard.boardPawn.length - row; j++) {
                if (gameBoard.boardPawn[i][j] != null) {
                    length++;
                }
                i++;
            }
        } else {
            int col = pawnPosition.getCol() - pawnPosition.getRow();
            int j = col;
            for (int i = 0; i < gameBoard.boardPawn.length - col; i++) {
                if (gameBoard.boardPawn[i][j] != null) {
                    length++;
                }
                j++;
            }
        }

        return length;
    }

    private static Move generateNorthWestMove(int length) {
        if ((pawnPosition.getRow() - length) >= 0 && (pawnPosition.getCol() - length) >= 0) {
            Pawn northWestBoardCase = gameBoard.boardPawn[pawnPosition.getRow() - length][pawnPosition.getCol() - length];
            if (northWestBoardCase == null || northWestBoardCase.getColor() != pawnColor) {
                if (!northWestMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position((pawnPosition.getRow() - length), (pawnPosition.getCol() - length)));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the north-west move of the pawn is blocked by an opponent in its path
     */
    private static boolean northWestMoveBlocked(int length) {
        for (int i = 0; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol() - i] != null && gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol() - i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    private static Move generateSouthEastMove(int length) {
        if ((pawnPosition.getRow() + length) <= 7 && (pawnPosition.getCol() + length) <= 7) {
            Pawn southEastBoardCase = gameBoard.boardPawn[pawnPosition.getRow() + length][pawnPosition.getCol() + length];
            if (southEastBoardCase == null || southEastBoardCase.getColor() != pawnColor) {
                if (!southEastMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position((pawnPosition.getRow() + length), (pawnPosition.getCol() + length)));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the south-east move of the pawn is blocked by an opponent in its path
     */
    private static boolean southEastMoveBlocked(int length) {
        for (int i = 0; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol() + i] != null && gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol() + i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    //** End NorthWest and SouthEast move methods**//


    //** Start NorthEast and SouthWest move methods**//

    /**
     * This method calculates the length for a diagonal move with the direction going from the bottom left of the
     * board to the top right of the board.
     */
    private static int calculateDiagonalBottomTopMoveLength(boolean startLeft) {
        //Calculate length of move South-West North-East
        int length = 0;
        if (startLeft) {
            int row = pawnPosition.getRow() + pawnPosition.getCol();
            int i = row;
            for (int j = 0; j < row + 1; j++) {
                if (gameBoard.boardPawn[i][j] != null) {
                    length++;
                }
                i--;
            }
        } else {
            int col = (pawnPosition.getRow() + pawnPosition.getCol()) - gameBoard.boardPawn.length + 1;
            int i = 7;
            for (int j = col; j < gameBoard.boardPawn.length; j++) {
                if (gameBoard.boardPawn[i][j] != null) {
                    length++;
                }
                i--;
            }
        }
        return length;
    }

    private static Move generateNorthEastMove(int length) {
        if ((pawnPosition.getRow() - length) >= 0 && (pawnPosition.getCol() + length) <= 7) {
            Pawn northEastBoardCase = gameBoard.boardPawn[pawnPosition.getRow() - length][pawnPosition.getCol() + length];
            if (northEastBoardCase == null || northEastBoardCase.getColor() != pawnColor) {
                if (!northEastMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position(pawnPosition.getRow() - length, pawnPosition.getCol() + length));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the north-east move of the pawn is blocked by an opponent in its path
     */
    private static boolean northEastMoveBlocked(int length) {
        for (int i = 0; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol() + i] != null && gameBoard.boardPawn[pawnPosition.getRow() - i][pawnPosition.getCol() + i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    private static Move generateSouthWestMove(int length) {
        if ((pawnPosition.getRow() + length) <= 7 && (pawnPosition.getCol() - length) >= 0) {
            Pawn southWestBoardCase = gameBoard.boardPawn[pawnPosition.getRow() + length][pawnPosition.getCol() - length];
            if (southWestBoardCase == null || southWestBoardCase.getColor() != pawnColor) {
                if (!southWestMoveBlocked(length)) {
                    return new Move(length, pawnPosition, new Position(pawnPosition.getRow() + length, pawnPosition.getCol() - length));
                }
            }
        }
        return null;
    }

    /**
     * This method checks is the south-west move of the pawn is blocked by an opponent in its path
     */
    private static boolean southWestMoveBlocked(int length) {
        for (int i = 0; i < length; i++) {
            if (gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol() - i] != null && gameBoard.boardPawn[pawnPosition.getRow() + i][pawnPosition.getCol() - i].getColor() == pawnOpponentColor) {
                return true;
            }
        }
        return false;
    }

    //** Start NorthEast and SouthWest move methods**//


}
