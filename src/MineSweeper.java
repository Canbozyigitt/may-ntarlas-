import java.util.Scanner;
import java.util.Random;

public class MineSweeper {
    private char[][] board;
    private char[][] mineLocations;
    private int numRows;
    private int numCols;
    private int numMines;
    private int remainingCells;

    public MineSweeper(int numRows, int numCols, int numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numMines = numMines;
        this.remainingCells = numRows * numCols;
        this.board = new char[numRows][numCols];
        this.mineLocations = new char[numRows][numCols];

        initializeBoard();
        placeMines();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;

        while (!gameOver) {
            printBoard();
            System.out.print("Satır ve Sütun girin (örnek: 1 2): ");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            if (isValidMove(row, col)) {
                if (mineLocations[row][col] == 'X') {
                    gameOver = true;
                    revealMines();
                    printBoard();
                    System.out.println("Mayına bastınız! Oyun bitti.");
                } else {
                    revealCell(row, col);
                    remainingCells--;

                    if (remainingCells == numMines) {
                        gameOver = true;
                        printBoard();
                        System.out.println("Tebrikler! Oyunu kazandınız.");
                    }
                }
            } else {
                System.out.println("Geçersiz hamle. Lütfen tekrar deneyin...");
            }
        }

        scanner.close();
    }

    private void initializeBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                board[i][j] = ' ';
                mineLocations[i][j] = ' ';
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < numMines) {
            int row = random.nextInt(numRows);
            int col = random.nextInt(numCols);

            if (mineLocations[row][col] != 'X') {
                mineLocations[row][col] = 'X';
                minesPlaced++;
            }
        }
    }

    private void printBoard() {
        System.out.println("  | 1 2 3 4 5 6 7 8 9");
        System.out.println("--+-------------------");
        for (int i = 0; i < numRows; i++) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < numCols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isValidMove(int row, int col) {
        return (row >= 0 && row < numRows && col >= 0 && col < numCols && board[row][col] == ' ');
    }

    private void revealCell(int row, int col) {
        if (mineLocations[row][col] == 'X') {
            return;
        }

        int minesAround = countMinesAround(row, col);
        if (minesAround > 0) {
            board[row][col] = (char) (minesAround + '0');
        } else {
            board[row][col] = ' ';
            revealAdjacentCells(row, col);
        }
    }

    private void revealAdjacentCells(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < numRows && j >= 0 && j < numCols && board[i][j] == ' ') {
                    revealCell(i, j);
                    remainingCells--;
                    if (remainingCells == numMines) {
                        return;
                    }
                }
            }
        }
    }

    private int countMinesAround(int row, int col) {
        int minesAround = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < numRows && j >= 0 && j < numCols && mineLocations[i][j] == 'X') {
                    minesAround++;
                }
            }
        }
        return minesAround;
    }

    private void revealMines() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (mineLocations[i][j] == 'X') {
                    board[i][j] = 'X';
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Satır sayısını girin: ");
        int numRows = scanner.nextInt();
        System.out.print("Sütun sayısını girin: ");
        int numCols = scanner.nextInt();
        System.out.print("Mayın sayısını girin: ");
        int numMines = scanner.nextInt();

        MineSweeper game = new MineSweeper(numRows, numCols, numMines);
        game.run();
    }
}
