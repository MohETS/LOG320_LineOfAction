package Server;

import Game.*;

import java.io.*;
import java.net.*;

/**
 * This class was made by the teachers of the LOG320.
 * Given to us as the client to communicate with the game server.
 */
class Client {
    private static Game game;
    private static String ipAdress;
    private static int port;

    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];

        System.out.println(args[0] + " " + args[1]);
        ipAdress = args[0];
        port = Integer.valueOf(args[1]);

        try {
            MyClient = new Socket(ipAdress, port);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            char cmd = 0;
            while (cmd != '5') {


                cmd = (char) input.read();
                System.out.println(cmd);
                // Debut de la partie en joueur rouge
                if (cmd == '1') {
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer, 0, size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x = 0, y = 0;
                    for (int i = 0; i < boardValues.length; i++) {
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        y++;
                        if (y == 8) {
                            y = 0;
                            x++;
                        }
                    }
                    game = new Game(board, 4);
                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
                    String move = null;
                    //move = console.readLine();
                    move = game.cpuPlay();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();
                }
                // Debut de la partie en joueur Noir
                if (cmd == '2') {
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouges");
                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    //System.out.println("size " + size);
                    input.read(aBuffer, 0, size);
                    String s = new String(aBuffer).trim();
                    System.out.println(s);
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x = 0, y = 0;
                    for (int i = 0; i < boardValues.length; i++) {
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        y++;
                        if (y == 8) {
                            y = 0;
                            x++;
                        }
                    }
                    game = new Game(board, 2);
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if (cmd == '3') {
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer, 0, size);

                    //Game.Game.Move du serveur
                    String s = new String(aBuffer);
                    Position[] positionsServer = getPositionsServerMove(s);
                    game.play(game.getServerPlayer(), new Move(positionsServer[0], positionsServer[1]));

                    //Game.Game.Move du ai
                    System.out.println("Dernier coup :" + s);
                    System.out.println("Entrez votre coup : ");
                    String move = game.cpuPlay();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if (cmd == '4') {
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                }
                // La partie est terminée
                if (cmd == '5') {
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer, 0, size);
                    String s = new String(aBuffer);
                    Position[] positionsServer = getPositionsServerMove(s);
                    System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Position[] getPositionsServerMove(String serverMove) {
        Position[] positions = {PositionDecoder.encode(serverMove.split(" ")[1]), PositionDecoder.encode(serverMove.split(" ")[3].substring(0, 2))};
        return positions;
    }

}
