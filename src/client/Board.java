/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package client;

import common.CommonInformation;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import server.ClientAdder;
/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class Board extends JFrame implements ActionListener{
    private final int id;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    
    private static final int BOARD_SIZE = 10;
    
    private Container container;
    private JPanel boardPanel;
    private JPanel dataPanel;
    private final JButton[][] squares = new JButton[BOARD_SIZE][BOARD_SIZE];
    
    private JLabel statusLabel;
    private JLabel showNumberLabel;
    private JLabel showCurrentPosition;
    private JButton rollButton;
    private JButton startGameButton;
    
    int[] numbers = {
        100,99,98,97,96,95,94,93,92,91,
        81,82,83,84,85,86,87,88,89,90,
        80,79,78,77,76,75,74,73,72,71,
        61,62,63,64,65,66,67,68,69,70,
        60,59,58,57,56,55,54,53,52,51,
        41,42,43,44,45,46,47,48,49,50,
        40,39,38,37,36,35,34,33,32,31,
        21,22,23,24,25,26,27,28,29,30,
        20,19,18,17,16,15,14,13,12,11,
        1,2,3,4,5,6,7,8,9,10
    };
    
    public Board(int id,DataInputStream dataInputStream,DataOutputStream dataOutputStream){
        super(CommonInformation.frameHeader);
        this.id = id;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        
        initializeBoard();
        initializeDataPanel();
        setWindowProperties();
    }
    
    private void initializeBoard() {
        container = getContentPane();
        container.setLayout(new GridLayout(1,2,10,10));
        boardPanel = new JPanel(new GridLayout(10,10,0,0));
        
        int index = 0;
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                squares[i][j] = new JButton(String.valueOf(numbers[index]));
                squares[i][j].addActionListener(this);
             
                squares[i][j].setMargin(new Insets(0,0,0,0));
                squares[i][j].setBorder(null);
                squares[i][j].setBorderPainted(false);
                squares[i][j].setContentAreaFilled(false);
                squares[i][j].setFocusPainted(false);
                squares[i][j].setIcon(new ImageIcon("images/board/"+numbers[index++]+".jpg"));
                
                boardPanel.add(squares[i][j]);
            }
        }
        boardPanel.setBorder(BorderFactory.createEmptyBorder());
        container.add(boardPanel);
    }
    
    private void initializeDataPanel() {
        dataPanel = new JPanel(new GridLayout(4,1));
        
        rollButton = new JButton("Roll");
        rollButton.addActionListener(this);
        rollButton.setEnabled(false);
        rollButton.setBorder(null);
        
        JPanel firstPanel = new JPanel(new GridLayout(1,2));
        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(this);
        startGameButton.setEnabled(false);
        startGameButton.setBorder(null);
        
        showCurrentPosition = new JLabel("Current Position : 0");
        firstPanel.add(startGameButton);
        firstPanel.add(showCurrentPosition);
        
        statusLabel = new JLabel("Waiting to Connect...");
        statusLabel.setFont(new Font("Serif", Font.BOLD, 28));
        statusLabel.setForeground(Color.RED);
        
        showNumberLabel = new JLabel("Rolled : ");
        showNumberLabel.setFont(new Font("Serif", Font.BOLD, 14));
        showNumberLabel.setForeground(Color.MAGENTA);
        
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        showNumberLabel.setHorizontalAlignment(JLabel.CENTER);
        rollButton.setHorizontalAlignment(JButton.CENTER);
        startGameButton.setHorizontalAlignment(JButton.CENTER);
        
        dataPanel.add(firstPanel);
        dataPanel.add(statusLabel);
        dataPanel.add(showNumberLabel);
        dataPanel.add(rollButton);
        
        container.add(dataPanel);
    }
    
    private void setWindowProperties() {
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == rollButton){
            rollButton.setEnabled(false);
            int roller = new Random().nextInt(6)+1;
            showNumberLabel.setText("Rolled : "+roller);
            try {
                dataOutputStream.writeUTF("roll:"+roller);
            } catch (IOException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(actionEvent.getSource() == startGameButton){
            try {
                dataOutputStream.writeUTF("Start Game");
                startGameButton.setEnabled(false);
            } catch (IOException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            for(int i=0;i<BOARD_SIZE;i++){
                for(int j=0;j<BOARD_SIZE;j++){
                    if(actionEvent.getSource() == squares[i][j]){
                        System.out.println(squares[i][j].getText());
                        break;
                    }
                }
            }
        }
    } 
    
//    public static void main(String[] args){
//        new Board(1,new DataInputStream(null),new DataOutputStream(null));
//    }
    
    public void enableStartGameButton(){
        startGameButton.setEnabled(true);
    }
    
    public void enableRollButton(){
        rollButton.setEnabled(true);
    }
    
    public void connected(){
        setStatus("Waiting for game to start...");
    }
    
    public void setStatus(String msg){
        statusLabel.setText(msg);
    }
    
    public void roll(){
        setStatus("Your Turn");
        rollButton.setEnabled(true);
    }
    
    public void reposition(int player,int number,int oldPosition){
        System.out.println(id);
        if(player == id){
            showCurrentPosition.setText("Current Position : "+number);
        }
        
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                if(squares[i][j].getText().equals(String.valueOf(number))){
                    squares[i][j].setIcon(new ImageIcon("images/circle/"+player+".png"));
                    break;
                }
            }
        }
        
        for(int i=0;i<BOARD_SIZE;i++){
            for(int j=0;j<BOARD_SIZE;j++){
                if(squares[i][j].getText().equals(String.valueOf(oldPosition))){
                    squares[i][j].setIcon(new ImageIcon("images/board/"+oldPosition+".jpg"));
                    break;
                }
            }
        }
    }
}
