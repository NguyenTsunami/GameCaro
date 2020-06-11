/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skeletons;

import ifunctions.IRoomListGetter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import mainscreen.AdminScreen;
import model.Room;

/**
 *
 * @author thuy
 */
public class RoomListGetter extends UnicastRemoteObject implements IRoomListGetter {

    AdminScreen mainscreen;
    public RoomListGetter(AdminScreen m) throws RemoteException {
        super();
        mainscreen = m;
    }

    @Override
    public ArrayList<Room> getRoomList() throws RemoteException {
        return mainscreen.roomList;
    }

}
