package Server;

import Game.Position;

public class PositionDecoder {
    private static String[] axeX = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static String[] axeY = {"8", "7", "6", "5", "4", "3", "2", "1"};

    /**
     * Return a String positionServer depending on the Position object received in parameters
     * @param position object Position
     * @return position
     */
    public static String decode(Position position) {
        String positionServer;
        positionServer = axeX[position.getCol()] + axeY[position.getRow()];
        return positionServer;
    }

//    public static String decode(int row, int col) {
//        String positionServer;
//        positionServer = axeX[col] + axeY[row];
//        return positionServer;
//    }

    /**
     * Returns a Position object depending on the string position received
     * @param position position following the style of the response/request of the game server ex: A8, F5
     * @return objet Position
     */
    public static Position encode(String position) {
        Position positionServer;
        positionServer = new Position(getValueRow(position.charAt(1)), getValueCol(position.charAt(0)));
        return positionServer;
    }


    /**
     * Returns the position of the column value in a number
     * @param character - Char value of the column ex: A in A8
     * @return number value of the column
     */
    private static int getValueCol(Character character) {
        switch (character) {
            case ('A'):
                return 0;

            case ('B'):
                return 1;

            case ('C'):
                return 2;

            case ('D'):
                return 3;

            case ('E'):
                return 4;

            case ('F'):
                return 5;

            case ('G'):
                return 6;

            case ('H'):
                return 7;
        }
        return 0;
    }

    /**
     * Returns the position of the row value in a number. The server's board is a mirror of my game board hence
     * the reverse conversion of the method.
     * @param number - Char value of the row ex: 8 in A8
     * @return number value of the row
     */
    private static int getValueRow(Character number) {
        switch (number) {
            case ('8'):
                return 0;

            case ('7'):
                return 1;

            case ('6'):
                return 2;

            case ('5'):
                return 3;

            case ('4'):
                return 4;

            case ('3'):
                return 5;

            case ('2'):
                return 6;

            case ('1'):
                return 7;
        }
        return 0;
    }
}
