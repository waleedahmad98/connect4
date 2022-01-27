import javax.swing.*;
import java.awt.*;

public class GUI {
    private final int WIDTH = 70;
    private final int HEIGHT = 70;


    public JFrame frame;
    public JLabel[][] grid;
    public JButton[] b_row;
    public ImageIcon main = new ImageIcon(new ImageIcon(this.getClass().getResource("add_btn.png")).getImage().getScaledInstance(60, 30, Image.SCALE_DEFAULT));
    public ImageIcon hover = new ImageIcon(new ImageIcon(this.getClass().getResource("add_btn_invert.png")).getImage().getScaledInstance(60, 30, Image.SCALE_DEFAULT));
    public ImageIcon press = new ImageIcon(new ImageIcon(this.getClass().getResource("add_btn_invert.png")).getImage().getScaledInstance(50, 20, Image.SCALE_DEFAULT));
    public ImageIcon disabled = new ImageIcon(new ImageIcon(this.getClass().getResource("add_btn_disabled.png")).getImage().getScaledInstance(60, 30, Image.SCALE_DEFAULT));
    public ImageIcon unfilled = new ImageIcon(new ImageIcon(this.getClass().getResource("unfilled.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    public ImageIcon red = new ImageIcon(new ImageIcon(this.getClass().getResource("filled_red.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    public ImageIcon green = new ImageIcon(new ImageIcon(this.getClass().getResource("filled_green.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    public ImageIcon redWin = new ImageIcon(new ImageIcon(this.getClass().getResource("red_win.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    public ImageIcon greenWin = new ImageIcon(new ImageIcon(this.getClass().getResource("green_win.png")).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT));
    public ImageIcon winImg = new ImageIcon(new ImageIcon(this.getClass().getResource("winImg.png")).getImage());
    public ImageIcon loseImg = new ImageIcon(new ImageIcon(this.getClass().getResource("loseImg.png")).getImage());

    public void start(){
        JFrame frame = new JFrame("ConnectFour.java");
        JPanel panel = new JPanel(new GridLayout(8, 7,25,5));
        panel.setBackground(new Color(5, 43, 98));
        grid = new JLabel[7][7];
        b_row = new JButton[7];
        for(int i=0; i<7; i++){ // add top layer buttons
            b_row[i] = new JButton();
            b_row[i].setName(Integer.toString(i));
            b_row[i].setBorderPainted(false);
            //b_row[i].setBorder(null);
            b_row[i].setFocusable(false);
            b_row[i].setMargin(new Insets(0, 0, 0, 0));
            b_row[i].setContentAreaFilled(false);
            b_row[i].setIcon(main);
            b_row[i].setRolloverIcon(hover);
            b_row[i].setPressedIcon(press);
            b_row[i].setDisabledIcon(disabled);
            panel.add(b_row[i]);
        }
        for (int i = 0; i < 7; i++){ // add cells in the grid
            for (int j = 0; j < 7; j++){
                grid[i][j] = new JLabel();
                grid[i][j].setIcon(unfilled);
                panel.add(grid[i][j]);
            }
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
