/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifunctions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Room;

/**
 *
 * @author thuy
 */
public interface IRoomStateGetter extends Remote{
    public Room getRoom(int roomid) throws RemoteException;
}
