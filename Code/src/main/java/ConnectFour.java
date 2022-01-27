import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ConnectFour {
    private static final int COMPUTER = 1;
    private static final int HUMAN = 2;
    private static final int DIMENSION = 7;
    private static final int CONNECTIONS = 4;
    private static int[][] winDisks;
    private static int DIFFICULTY;
    private final int[][] board;
    private boolean isMyTurn;
    public static GUI gui;
    public int columnChoice = 0;
    public boolean containment = false;

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public ConnectFour(int difficulty) {
        board = new int[DIMENSION][DIMENSION];
        isMyTurn = false;
        DIFFICULTY = difficulty;
        winDisks = new int[4][2];
    }

    public boolean columnHasSpace(int col) {
        return board[0][col] == 0;
    }

    public boolean isBoardFull() {
        for (int i = 0; i < DIMENSION; ++i)
            if (columnHasSpace(i))
                return false;
        return true;
    }

    public void dropDisk(int column, int value) {
        for (int i = 0; i < DIMENSION && board[i][column] == 0; ++i) {
            board[i][column] = value;

            if (value == COMPUTER)
                gui.grid[i][column].setIcon(gui.red);
            else
                gui.grid[i][column].setIcon(gui.green);
            sleep(10);
            if (i - 1 >= 0) {
                board[i - 1][column] = 0;
                gui.grid[i - 1][column].setIcon(gui.unfilled);
            }

            sleep(30);

        }
    }

    private void popDisk(int column) {
        int i = 0;
        while (board[i][column] == 0)
            ++i;
        board[i][column] = 0;
    }

    private void pushDisk(int column, int value) {
        int i = DIMENSION - 1;
        while (board[i][column] != 0)
            --i;
        board[i][column] = value;
    }

    private int countHorizontal(int x, int y, int val) {
        int count = 0, k = 0;
        for (int i = y; i < DIMENSION && i < y + CONNECTIONS; ++i) {
            if (board[x][i] == val)
                ++count;
            else if (board[x][i] != 0)
                return 0;
            winDisks[k][0] = x;
            winDisks[k++][1] = i;
        }
        return count;
    }

    private int countVertical(int x, int y, int val) {
        int count = 0, k = 0;
        for (int i = x; i >= 0 && i > x - CONNECTIONS; --i) {
            if (board[i][y] == val)
                ++count;
            else if (board[i][y] != 0)
                return 0;
            winDisks[k][0] = i;
            winDisks[k++][1] = y;
        }
        return count;
    }

    private int countForwardDiagonal(int x, int y, int val) {
        int count = 0, k = 0;
        for (int i = x, j = y; i >= 0 && j < DIMENSION && i > x - CONNECTIONS; --i, ++j) {
            if (board[i][j] == val)
                ++count;
            else if (board[i][j] != 0)
                return 0;
            winDisks[k][0] = i;
            winDisks[k++][1] = j;
        }
        return count;
    }

    private int countBackwardDiagonal(int x, int y, int val) {
        int count = 0, k = 0;
        for (int i = x, j = y; i >= 0 && j >= 0 && i > x - CONNECTIONS; --i, --j) {
            if (board[i][j] == val)
                ++count;
            else if (board[i][j] != 0)
                return 0;
            winDisks[k][0] = i;
            winDisks[k++][1] = j;
        }
        return count;
    }

    private int countScore(int x, int y, int val) {
        int[] scores = new int[CONNECTIONS + 1];
        ++scores[countHorizontal(x, y, val)];
        ++scores[countVertical(x, y, val)];
        ++scores[countForwardDiagonal(x, y, val)];
        ++scores[countBackwardDiagonal(x, y, val)];
        return 100000 * scores[4] + 100 * scores[3] + scores[2];
    }

    private boolean isWinning(int val) {
        for (int i = DIMENSION - 1; i >= 0; --i)
            for (int j = 0; j < DIMENSION; ++j)
                if (board[i][j] == val)
                    if (countHorizontal(i, j, val) == CONNECTIONS ||
                            countVertical(i, j, val) == CONNECTIONS ||
                            countForwardDiagonal(i, j, val) == CONNECTIONS ||
                            countBackwardDiagonal(i, j, val) == CONNECTIONS)
                        return true;
        return false;
    }

    private int evaluation(int value) {
        int score = 0;
        for (int i = 0; i < DIMENSION; i++)
            for (int j = 0; j < DIMENSION; j++)
                score += countScore(i, j, value);
        return score;
    }

    private int miniMax(int depth, int max, int min, boolean isMax) {

        int humanScore = evaluation(HUMAN),
                computerScore = evaluation(COMPUTER);
        if (depth == 0 || isBoardFull())
            return computerScore - humanScore;
        if (humanScore >= 100000)
            return -humanScore * depth;
        if (computerScore >= 100000)
            return computerScore * depth;

        if (isMax) {
            for (int i = 0; i < DIMENSION; i++)
                if (columnHasSpace(i)) {
                    pushDisk(i, COMPUTER);
                    int tempValue = miniMax(depth - 1, max, min, false);
                    popDisk(i);
                    if (tempValue > max)
                        max = tempValue;
                    if (max >= min)
                        return max;
                }
            return max;
        } else {
            for (int i = 0; i < DIMENSION; i++)
                if (columnHasSpace(i)) {
                    pushDisk(i, HUMAN);
                    int tempValue = miniMax(depth - 1, max, min, true);
                    popDisk(i);
                    if (tempValue < min)
                        min = tempValue;
                    if (min <= max)
                        return min;
                }
            return min;
        }
    }

    private int findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int bestMove = -1;
        int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
        for (int i = 0; i < DIMENSION; i++)
            if (columnHasSpace(i)) {
                pushDisk(i, COMPUTER);
                int moveVal = miniMax(DIFFICULTY, alpha, beta, false);
                popDisk(i);
                if (moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        return bestMove;
    }

    public void takeTurn() {
        int col, val = HUMAN;
        if (isMyTurn) {
            col = findBestMove();
            sleep(100);
            val = COMPUTER;
        } else {
            while (!containment) {
                sleep(100);
            }
            disableButtons();
            col = columnChoice;
            containment = false;
        }
        dropDisk(col, val);
        if (isMyTurn)
            enableButtons();
        isMyTurn = !isMyTurn;
    }

    private void printWin() {
        if (board[winDisks[0][0]][winDisks[0][1]] == HUMAN) {
            for (int i = 0; i < 4; ++i) {
                gui.grid[winDisks[i][0]][winDisks[i][1]].setIcon(gui.greenWin);
            }
        } else {
            for (int i = 0; i < 4; ++i) {
                gui.grid[winDisks[i][0]][winDisks[i][1]].setIcon(gui.redWin);
            }
        }
    }

    int play() {
        int winner = 0;
        while (!isBoardFull() && winner == 0) {
            if (isWinning(COMPUTER))
                winner = 1;
            else if (isWinning(HUMAN))
                winner = 2;
            else
                takeTurn();
        }
        printWin();
        disableButtons();
        return winner;
    }

    public void disableButtons() {
        for (JButton btn : gui.b_row) {
            btn.setEnabled(false);
        }
    }

    public void enableButtons() {
        for (JButton btn : gui.b_row) {
            if (columnHasSpace(Integer.parseInt(btn.getName())))
                btn.setEnabled(true);
        }
    }

    public void attachEvents() {
        for (JButton btn : gui.b_row) {
            btn.addActionListener(e -> {
                columnChoice = Integer.parseInt(btn.getName());
                containment = true;
            });
        }

    }

    public static String difficultySelector() {
        JPanel fields = new JPanel(new GridLayout(1, 1));
        JComboBox<String> comboBox1 = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

        fields.add(comboBox1);

        int dialog = JOptionPane.showConfirmDialog(null, fields, "Difficulty", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (dialog != 0)
            System.exit(0);
        String result;
        result = Objects.requireNonNull(comboBox1.getSelectedItem()).toString();
        return result;
    }

    public static void showSplash(int winner){
        JDialog d = new JDialog(gui.frame, "Output Box");
        d.setUndecorated(true);
        d.getRootPane ().setOpaque (false);
        d.getContentPane ().setBackground (new Color (0, 0, 0, 0));
        d.setBackground (new Color (0, 0, 0, 0));
        if (winner == 1){
            d.add(new JLabel(gui.loseImg));
            d.pack();
            d.setLocationRelativeTo(gui.frame);
            d.setVisible(true);
        }
        else if(winner == 2){
            d.add(new JLabel(gui.winImg));
            d.pack();
            d.setLocationRelativeTo(gui.frame);
            d.setVisible(true);
        }

    }

    public static void main(String[] args) {
        int difficulty = 1;
        String selection = difficultySelector();
        switch (selection) {
            case "Easy" -> difficulty = 1;
            case "Medium" -> difficulty = 3;
            case "Hard" -> difficulty = 5;
        }
        gui = new GUI();
        gui.start();

        ConnectFour connectFour = new ConnectFour(difficulty);
        connectFour.attachEvents();
        int winner = connectFour.play();
        showSplash(winner);
    }
}