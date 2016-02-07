/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package server;
import static common.CommonInformation.clientInfos;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class GameEngine {
    
    private final int totalPlayer;
    
    private int currentPlayer;
    
    private boolean won = false;
    
    public GameEngine(int totalPlayer){
        this.totalPlayer = totalPlayer;        
        
        currentPlayer = 0;
        
        startGame();
    }
    
    private void startGame(){
        while(!won){
            try {
                clientInfos.get(currentPlayer).getDataOutputStream().writeUTF("roll");
                clientInfos.stream().forEach((clientInfo)->{
                    if(clientInfo.getId()!=currentPlayer){
                        try {
                            clientInfo.getDataOutputStream().writeUTF("wait");
                        } catch (IOException ex) {
                            Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                String result = clientInfos.get(currentPlayer).getDataInputStream().readUTF();
                int rolledNumber = Integer.parseInt(result.split(":")[1]);
                calculate(rolledNumber);
                currentPlayer++;
                if(currentPlayer == totalPlayer){
                    currentPlayer = 0;
                }
            } catch (IOException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void calculate(int rolledNumber) {
        if(clientInfos.get(currentPlayer).getCurrentPosition() == 0){
            if(rolledNumber == 1){
                int oldPosition = clientInfos.get(currentPlayer).getCurrentPosition();
                int newPosition = clientInfos.get(currentPlayer).getCurrentPosition()+rolledNumber;
                clientInfos.get(currentPlayer).setCurrentPosition(newPosition);
                clientInfos.stream().forEach((clientInfo)->{
                    try {
                        clientInfo.getDataOutputStream().writeUTF("reposition:"+currentPlayer+":"+newPosition+":"+oldPosition);
                    } catch (IOException ex) {
                        Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }else{
                try {
                    clientInfos.get(currentPlayer).getDataOutputStream().writeUTF("show:You need 1 to start");
                } catch (IOException ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            int oldPosition = clientInfos.get(currentPlayer).getCurrentPosition();
            int newPosition = clientInfos.get(currentPlayer).getCurrentPosition()+rolledNumber;
            if(newPosition>100){
                try {
                    clientInfos.get(currentPlayer).getDataOutputStream().writeUTF("show:You are out of luck!");
                } catch (IOException ex) {
                    Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(newPosition == 100){
                clientInfos.get(currentPlayer).setCurrentPosition(newPosition);
                clientInfos.stream().forEach((clientInfo)->{
                    try {
                        clientInfo.getDataOutputStream().writeUTF("reposition:"+currentPlayer+":"+newPosition+":"+oldPosition);
                    } catch (IOException ex) {
                        Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                win(currentPlayer);
            }else{
                clientInfos.get(currentPlayer).setCurrentPosition(newPosition);
                clientInfos.stream().forEach((clientInfo)->{
                    try {
                        clientInfo.getDataOutputStream().writeUTF("reposition:"+currentPlayer+":"+newPosition+":"+oldPosition);
                    } catch (IOException ex) {
                        Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
    }

    private void win(int winner) {
        clientInfos.stream().forEach((clientInfo)->{
            try {
                clientInfo.getDataOutputStream().writeUTF("show:Player "+winner+" won!");
            } catch (IOException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        won = true;
    }
    
}
