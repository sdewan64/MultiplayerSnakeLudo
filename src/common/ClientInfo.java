/*
 * Shaheed Ahmed Dewan Sagar
 * AUST-12-01-04-085
 * sdewan64@gmail.com
 */

package common;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Shaheed Ahmed Dewan Sagar
 *         AUST-12.01.04.085
 *         sdewan64@gmail.com
 */
public class ClientInfo {
    
    private final int id;
    private final String name;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    
    private int currentPosition = 0;

    public ClientInfo(int id, String name,DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.id = id;
        this.name = name;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
    
    public int getCurrentPosition(){
        return currentPosition;
    }
    
    public void setCurrentPosition(int number){
        currentPosition = number;
    }
}
