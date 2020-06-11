/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifunctions;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author thuy
 */
public interface ICellPickGetter extends Remote {
    public int getCellEnemyPick(int roomid) throws RemoteException;
}
