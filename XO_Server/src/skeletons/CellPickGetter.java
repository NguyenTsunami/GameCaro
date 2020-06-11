/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skeletons;

import ifunctions.ICellPickGetter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import mainscreen.AdminScreen;
import model.Room;

/**
 *
 * @author thuy
 */
public class CellPickGetter extends UnicastRemoteObject implements ICellPickGetter{

    AdminScreen mainscreen;

    public CellPickGetter(AdminScreen m) throws RemoteException {
        super();
        mainscreen = m;
    }
    
    @Override
    public int getCellEnemyPick(int roomid) throws RemoteException {
        for (Room r: mainscreen.roomList) {
            if (r.getRoomID() == roomid) {
                return r.getCurrentCellPickIndex();
            }
        }
        return -1;
    }
    
}
