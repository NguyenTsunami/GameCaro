/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skeletons;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import mainscreen.AdminScreen;
import model.Room;
import ifunctions.IRoomStateGetter;

/**
 *
 * @author thuy
 */
public class RoomStateGetter extends UnicastRemoteObject implements IRoomStateGetter {

    AdminScreen mainscreen;

    public RoomStateGetter(AdminScreen m) throws RemoteException {
        super();
        mainscreen = m;
    }

    @Override
    public Room getRoom(int roomid) throws RemoteException {
        for (Room r: mainscreen.roomList) {
            if (r.getRoomID() == roomid) {
                return r;
            }
        }
        return null;
    }

}
