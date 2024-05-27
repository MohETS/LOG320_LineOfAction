package Game;

import ArtificialInteligence.*;

import java.util.ArrayList;

public class Board {

    private static String[] column = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static String[] row = {"8", "7", "6", "5", "4", "3", "2", "1"};

    //List containing all the black pawns of the board
    private ArrayList<Pawn> blackPawnsList = new ArrayList<>();
    private ArrayList<Move> blackPawnsMovesList = new ArrayList<>();
    //List containing all the red pawns of the board
    private ArrayList<Pawn> redPawnsList = new ArrayList<>();
    private ArrayList<Move> redPawnsMovesList = new ArrayList<>();

    //Number of pawns that are linked for each player
    private int blackPawnsConnected;
    private int redPawnsConnected;

    //Number of pawns alive for each player
    private int blackPawnsAlive;
    private int redPawnsAlive;

    public Pawn[][] boardPawn = new Pawn[8][8];
    //Centralization Heuristic *Check heuristic class for sources*
    public HeuristicQuad heuristicQuad;
    //Centralization Heuristic *Check heuristic class for sources*
    public HeuristicCentralization heuristicCentralization;

    public Board(int[][] board) {
        initBoard(board);
        blackPawnsAlive = blackPawnsList.size();
        redPawnsAlive = redPawnsList.size();
        heuristicQuad = new HeuristicQuad(this);
        heuristicCentralization = new HeuristicCentralization(this);
    }


    /**
     * This method is responsible for generating the board of the game
     *
     * @param board - 2D int array containing the board state
     */
    private void initBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 2) {
                    //pawns noir
                    Pawn tempPawnNoir = new Pawn(2, i, j, this);
                    blackPawnsList.add(tempPawnNoir);
                    this.boardPawn[i][j] = tempPawnNoir;
                } else if (board[i][j] == 4) {
                    //pawn rouge
                    Pawn tempPawnRouge = new Pawn(4, i, j, this);
                    redPawnsList.add(tempPawnRouge);
                    this.boardPawn[i][j] = tempPawnRouge;
                } else {
                    //vide
                    this.boardPawn[i][j] = null;
                }
                System.out.print(column[j] + row[i] + " " + board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * This method returns a pawn according to the position by in parameters
     *
     * @param position - Game.Game.Position of the wanted pawn
     * @return - Pawn based on the position or null if there is no pawn there
     */
    public Pawn getPawn(Position position) {
        if (boardPawn[position.getRow()][position.getCol()] != null) {
            return boardPawn[position.getRow()][position.getCol()];
        }
        return null;
    }

    /**
     * This method moves the pawn. If a pawn is removed during the move, the pawn is set as the deletedPawn inside the
     * move object.
     * @param color - pawn color
     */
    public void movePawn(int color, Move move) {

        if (getPawn(move.getEndingPosition()) != null) {
            move.setPawnDeleted(true, boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()]);
            boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()].setAlive(false);
            if (color == 2) {
                this.redPawnsAlive--;
            } else {
                this.blackPawnsAlive--;
            }
        }
        heuristicQuad.eulerValueUpdate(color, move);

        Pawn temp = boardPawn[move.getStartingPosition().getRow()][move.getStartingPosition().getCol()];
        temp.setPosition(move.getEndingPosition());
        boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()] = temp;
        boardPawn[move.getStartingPosition().getRow()][move.getStartingPosition().getCol()] = null;

        //Update our heuristics
        heuristicQuad.refreshEuleurValue(color, false);
        heuristicCentralization.update(move, color, false);

    }

    /**
     * This method undoes a move by setting back the moved pawn and then check if this moved deleted a pawn or not.
     * If it is the case the removed pawn is recovered from the pawn object inside the move object.
     * @param color - pawn color
     */
    public void undoMovePawn(int color, Move move) {

        boardPawn[move.getStartingPosition().getRow()][move.getStartingPosition().getCol()] = boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()];
        boardPawn[move.getStartingPosition().getRow()][move.getStartingPosition().getCol()].setPosition(move.getStartingPosition());

        if (move.isMoveDeletingPawn()) {
            boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()] = move.deletedPawn;
            boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()].setAlive(true);
            if (color == 2) {
                this.redPawnsAlive++;
            } else {
                this.blackPawnsAlive++;
            }
        } else {
            boardPawn[move.getEndingPosition().getRow()][move.getEndingPosition().getCol()] = null;
        }

        //Update our heuristics
        heuristicQuad.eulerValueUpdate(color, move);
        heuristicQuad.refreshEuleurValue(color, true);
        heuristicCentralization.update(move, color, true);
    }

    /**
     * This methods return an Arraylist of the black pawns
     */
    public ArrayList<Pawn> getBlackPawnsList() {
        return blackPawnsList;
    }

    /**
     * This methods return an Arraylist of the red pawns
     */
    public ArrayList<Pawn> getRedPawnsList() {
        return redPawnsList;
    }

    /**
     * This method gets all the move for the black player
     */
    public ArrayList<Move> getBlackPawnsMovesList() {
        blackPawnsMovesList.clear();
        for (Pawn pawn : blackPawnsList) {
            if (pawn.isAlive()) {
                for (Move move : pawn.generatePawnMoves()) {
                    blackPawnsMovesList.add(move);
                }
            }
        }
        return blackPawnsMovesList;
    }

    /**
     * This method gets all the move for the red player
     */
    public ArrayList<Move> getRedPawnsMovesList() {
        redPawnsMovesList.clear();
        for (Pawn pawn : redPawnsList) {
            if (pawn.isAlive()) {
                for (Move move : pawn.generatePawnMoves()) {
                    redPawnsMovesList.add(move);
                }
            }
        }
        return redPawnsMovesList;
    }

    /**
     * This method returns the set of moves for the color of the player given in parameters
     * @param color
     * @return
     */
    public ArrayList<Move> getPlayerMoves(int color) {
        if (color == 2) {
            return new ArrayList<>(getBlackPawnsMovesList());
        } else {
            return new ArrayList<>(getRedPawnsMovesList());
        }
    }

    /**
     *  This method is called to increment the number of black pawn connected.
     */
    public void blackPawnConnected() {
        this.blackPawnsConnected++;
    }

    /**
     *  This method is called to increment the number of red pawn connected.
     */
    public void redPawnConnected() {
        this.redPawnsConnected++;
    }

    /**
     * This method is used to evaluate the value of the move played. The greater the value the better the move is.
     * @param cpuColor - Color of the ai for the game
     * @param actualPawn - Pawn that is currently moved to ca
     * @return - value of the move
     */
    public float evaluate(int cpuColor, Pawn actualPawn) {
        float eulerNumberBlack = this.heuristicQuad.getEulerNumberBlack();
        float eulerNumberRed = this.heuristicQuad.getEulerNumberRed();

        float centralizationBlack = this.heuristicCentralization.getValueBlackPawn();
        float centralizationRed = this.heuristicCentralization.getValueRedPawn();

        //Checks if the move is a winning move or not based on the quad heuristic
        if (cpuColor == 2) {
            if (eulerNumberBlack <= 1) {
                actualPawn.isLinked();
                if (blackPawnsConnected == blackPawnsAlive) {
                    resetEvaluate();
                    return Float.MAX_VALUE;
                }
            }
            if (eulerNumberRed <= 1) {
                actualPawn.isLinked();
                if (redPawnsConnected == redPawnsAlive) {
                    resetEvaluate();
                    return Float.MIN_VALUE;
                }

            }
        }

        if (cpuColor == 4) {
            if (eulerNumberRed <= 1) {
                actualPawn.isLinked();
                if (redPawnsConnected == redPawnsAlive) {
                    resetEvaluate();
                    return Float.MAX_VALUE;
                }

            }
            if (eulerNumberBlack <= 1) {
                actualPawn.isLinked();
                if (blackPawnsConnected == blackPawnsAlive) {
                    resetEvaluate();
                    return Float.MIN_VALUE;
                }
            }
        }

        resetEvaluate();

        /*Give a better value to moves where the moved pawn is on the sides. We want to avoid having pawns
        stay on the side of the board.*/
        int resultat = 0;
        if (actualPawn.isOnSides()) {
            resultat += 100;
        }

        if (cpuColor == 2) {
            return resultat + (eulerNumberRed - eulerNumberBlack) + (centralizationBlack - centralizationRed);
        } else {
            return resultat + (eulerNumberBlack - eulerNumberRed) + (centralizationRed - centralizationBlack);
        }
    }

    /**
     * This method resets the different variables used for the "evaluate" function.
     */
    private void resetEvaluate() {
        blackPawnsList.stream().forEach(pawn -> pawn.setUnlinked());
        redPawnsList.stream().forEach(pawn -> pawn.setUnlinked());
        blackPawnsConnected = 0;
        redPawnsConnected = 0;
    }

    public void showBoard() {
        for (int i = 0; i < boardPawn.length; i++) {
            for (int j = 0; j < boardPawn.length; j++) {
                if (boardPawn[i][j] != null) {
                    System.out.print(column[j] + row[i] + " " + boardPawn[i][j].getColor() + " ");
                } else {
                    System.out.print(column[j] + row[i] + " " + 0 + " ");
                }
            }
            System.out.println();
        }
    }
}
