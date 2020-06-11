/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifunctions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.PlayerInfo;

/**
 *
 * @author thuy
 */
public interface IPlayerListGetter extends Remote{
    public ArrayList<PlayerInfo> getPlayerList() throws RemoteException;
}
