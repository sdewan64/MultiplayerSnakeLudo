/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class Client {
    
    private int id;
    
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    
    private Board board;
    
    private void start(){
        try {
            Socket client = new Socket(common.CommonInformation.HOST, common.CommonInformation.PORT);
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            
            board = new Board(id,dataInputStream,dataOutputStream);
            String welcomeMsg = readData();
            JOptionPane.showMessageDialog(board, welcomeMsg.split(":")[0]);
            id = Integer.parseInt(welcomeMsg.split(":")[1]);
            board.connected();
            
            new ClientActionHandler().start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not connect to the server\nThe game has already begun");
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class ClientActionHandler extends Thread{

        @Override
        public void run() {
            while(true){
                try {
                    String msg = readData();
                    handleActions(msg);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    private String readData() throws IOException{
        return dataInputStream.readUTF();
    }
    
    private void handleActions(String action){
        if(action.contains("enableStart")){
            board.enableStartGameButton();
        }else if(action.contains("startGame")){
            board.setStatus("Game Starting...");
        }else if(action.contains("roll")){
            board.roll();
        }else if(action.contains("wait")){
            board.setStatus("Wait for your turn...");
        }else if(action.contains("show")){
            board.setStatus(action.split(":")[1]);
        }else if(action.contains("reposition")){
            board.reposition(Integer.parseInt(action.split(":")[1]),Integer.parseInt(action.split(":")[2]),Integer.parseInt(action.split(":")[3]));
        }
    }
    
    public static void main(String[] args){
        new Client().start();
    }
    
}
