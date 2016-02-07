/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package server;

import common.ClientInfo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class ClientAdder extends Thread {
    
    private final ServerSocket server;
    private final Server gameServer;
    
    private int id = 0;
    
    public ClientAdder(ServerSocket server,Server gameServer){
        this.server = server;
        this.gameServer = gameServer;
    }
    
    @Override
    public void run() {
        while(!interrupted()){
            try {
                Socket client = server.accept();
                DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
                ClientInfo clientInfo = new ClientInfo(id, dataInputStream.readUTF(), dataInputStream, dataOutputStream);
                common.CommonInformation.clientInfos.add(clientInfo);
                clientInfo.getDataOutputStream().writeUTF("Welcome "+clientInfo.getName()+"\nYou are connected:"+id);
                if(id == 0){
                    new ClientAdderStopper(clientInfo).start();
                    clientInfo.getDataOutputStream().writeUTF("enableStart");
                }else{
                    clientInfo.getDataOutputStream().writeUTF("none");
                }
                id++;
            } catch (IOException ex) {
                Logger.getLogger(ClientAdder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void stopClientAdder() {
        this.interrupt();
    }
    
    private class ClientAdderStopper extends Thread{

        private final ClientInfo clientInfo;
        
        public ClientAdderStopper(ClientInfo clientInfo){
            this.clientInfo = clientInfo;
        }
        
        @Override
        public void run() {
            try {
                clientInfo.getDataInputStream().readUTF();
                gameServer.startGame();
                stopClientAdder();
                this.interrupt();
            } catch (IOException ex) {
                Logger.getLogger(ClientAdder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
