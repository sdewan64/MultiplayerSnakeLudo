/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package server;

import static common.CommonInformation.clientInfos;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class Server {
    
    private ServerSocket server;
    
    private void start(){
        try {
            server = new ServerSocket(common.CommonInformation.PORT);
            ClientAdder clientAdder = new ClientAdder(server,this);
            clientAdder.start();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void startGame(){
        clientInfos.stream().forEach((clientInfo) -> {
            try {
                clientInfo.getDataOutputStream().writeUTF("startGame");
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        new GameEngine(clientInfos.size());
    }
    
    public static void main(String args[]){
        new Server().start();
    }
    
}
