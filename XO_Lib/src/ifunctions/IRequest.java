/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifunctions;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Player;
import model.PlayerInfo;
import model.Room;

/**
 *
 * @author thuy
 */
public interface IRequest extends Remote {

    public void signUp(Player p) throws RemoteException;

    public boolean isExist(String acc) throws RemoteException;

    public Player signIn(String acc, String pass) throws RemoteException;

    public int getRank(int id) throws RemoteException;

    public Room createRoom(int p1id) throws RemoteException;

    public Room visitRoom(Room room, int p2id) throws RemoteException;

    public PlayerInfo getPlayerInfoById(int id) throws RemoteException;

    public void outRoom(int roomid, int pid) throws RemoteException;

    public void sendStateReady(int roomid, int pid, int state) throws RemoteException;

    public void sendCellPick(int roomid, int index) throws RemoteException;
    
    public void updateScore(int pid, int score) throws RemoteException;
}
