package ArtificialInteligence;

import Game.*;
import Server.PositionDecoder;

import java.util.ArrayList;

public class CpuPlayer implements Runnable {

    private int cpuColor;
    private int serverColor;
    private Game game;
    private Board boardPawn;
    private Pawn actualPawn;
    private String bestMove;
    private boolean activeSearch;

    public CpuPlayer(int cpuColor, Game game) {
        this.game = game;
        this.boardPawn = game.gameBoard;
        this.cpuColor = cpuColor;
        if (cpuColor == 2) {
            serverColor = 4;
        } else {
            serverColor = 2;
        }
        activeSearch = false;
    }

    @Override
    public void run() {
        play();
    }

    public void stopSearchAlphaBeta() {
        this.activeSearch = false;
    }

    public int getCpuColor() {
        return cpuColor;
    }

    public String getBestMove() {
        return bestMove;
    }

    public void play() {
        getNextMoveAB();
    }

    int numberOfMoves;
    int tempNumberOfMoves;
    /**
     * This method is called by the game to find the best move for the AI
     */
    public void getNextMoveAB() {
        activeSearch = true;
        float highestValue = 0;

        ArrayList<Move> playersMove = boardPawn.getPlayerMoves(cpuColor);
        numberOfMoves = playersMove.size();

        for (int i = 0; i < playersMove.size() && activeSearch; i++) {
            Move move = playersMove.get(i);
            //Play
            game.tempPlay(cpuColor, move);
            actualPawn = game.gameBoard.getPawn(move.getEndingPosition());
            //AB result
            float result = alphaBeta(serverColor, 5, Float.MIN_VALUE, Float.MAX_VALUE);

            /*  Check if the value of the alphaBeta pruning is equal to a winning move. If it's the case we evaluate
                the state of the board after the move to see if the winning move would make us win right now or not.
                If it's the case then we pick this move and end the search for the best move */
            if (result == Float.MAX_VALUE) {
                if(game.gameBoard.evaluate(cpuColor, actualPawn) == Float.MAX_VALUE){
                    System.out.println("Winning move :"+PositionDecoder.decode(move.getStartingPosition()) + PositionDecoder.decode(move.getEndingPosition()));
                    bestMove = PositionDecoder.decode(move.getStartingPosition()) + PositionDecoder.decode(move.getEndingPosition());
                    activeSearch = false;
                    game.unPlay(cpuColor);
                    return;
                }
            }
            //Get the number of moves that would be generated if this move was played
            tempNumberOfMoves =  boardPawn.getPlayerMoves(cpuColor).size();

            //Undoplay
            game.unPlay(cpuColor);

            /*  Checks if the value is better than the previous value. If it's not the case and the value is equal,
                check if the state of the game after the move being played generates more or less move than the actual
                state of the board */
            if (result > highestValue) {
                bestMove = PositionDecoder.decode(move.getStartingPosition()) + PositionDecoder.decode(move.getEndingPosition());
                highestValue = result;
            } else if (result == highestValue && tempNumberOfMoves >= numberOfMoves) {
                bestMove = PositionDecoder.decode(move.getStartingPosition()) + PositionDecoder.decode(move.getEndingPosition());
                highestValue = result;
            }
        }
    }

    public float alphaBeta(int color, int depth, float alpha, float beta) {
        float result = boardPawn.evaluate(cpuColor, actualPawn);

        if (result == Float.MAX_VALUE || result == Float.MIN_VALUE || depth == 1 || !activeSearch) {
            return result;
        }

        if (color == cpuColor) {
            //Max
            float alphaT = Float.MIN_VALUE;
            ArrayList<Move> playersMove = boardPawn.getPlayerMoves(cpuColor);
            for (Move move : playersMove) {
                //Play
                game.tempPlay(cpuColor, move);
                actualPawn = game.gameBoard.getPawn(move.getEndingPosition());
                //AB result
                result = alphaBeta(serverColor, depth - 1, alpha, beta);
                //Undoplay
                game.unPlay(cpuColor);
                alphaT = Math.max(result, alphaT);
                alpha = Math.max(alpha, alphaT);
                if (beta < alpha) {
                    return alphaT;
                }
            }
            return alphaT;
        } else {
            //Min
            float betaT = Float.MAX_VALUE;
            ArrayList<Move> playersMove = boardPawn.getPlayerMoves(serverColor);
            for (Move move : playersMove) {
                //Play
                game.tempPlay(serverColor, move);
                actualPawn = game.gameBoard.getPawn(move.getEndingPosition());
                //AB result
                result = alphaBeta(cpuColor, depth - 1, alpha, beta);
                //Undoplay
                game.unPlay(serverColor);
                betaT = Math.min(result, betaT);
                beta = Math.min(beta, betaT);
                if (beta < alpha) {
                    return betaT;
                }
            }
            return betaT;
        }
    }

}
