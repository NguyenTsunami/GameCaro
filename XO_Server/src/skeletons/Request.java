/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skeletons;

import ifunctions.IRequest;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import mainscreen.AdminScreen;
import model.Player;
import model.PlayerInfo;
import model.Room;

/**
 *
 * @author thuy
 */
public class Request extends UnicastRemoteObject implements IRequest {

    AdminScreen mainscreen;

    public Request(AdminScreen m) throws RemoteException {
        super();
        mainscreen = m;
    }

    @Override
    public void signUp(Player p) throws RemoteException {
        mainscreen.pda.insert(p);
        mainscreen.loadPlayerList();
    }

    @Override
    public boolean isExist(String acc) throws RemoteException {
        ArrayList<Player> list = mainscreen.pda.getPlayerList();
        for (Player p : list) {
            if (p.getAcc().equals(acc)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Player signIn(String acc, String pass) throws RemoteException {
        ArrayList<Player> list = mainscreen.pda.getPlayerList();
        for (Player p : list) {
            if (p.getAcc().equals(acc) && p.getPass().equals(pass)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public int getRank(int id) throws RemoteException {
        return mainscreen.pda.getRank(id);
    }

    @Override
    public Room createRoom(int p1id) throws RemoteException {
        mainscreen.ROOMID++;
        Room room = new Room(mainscreen.ROOMID, p1id, 0, 0, 0);
        mainscreen.roomList.add(room);
        mainscreen.loadRoomList();
        return room;
    }

    @Override
    public Room visitRoom(Room room, int p2id) throws RemoteException {
        for (Room r : mainscreen.roomList) {
            if (r.equals(room)) {
                if (r.getP1id() == 0) {
                    r.setP1id(p2id);
                } else if (r.getP2id() == 0) {
                    r.setP2id(p2id);
                }
                mainscreen.loadRoomList();
                return r;
            }
        }
        return null;
    }

    @Override
    public PlayerInfo getPlayerInfoById(int id) throws RemoteException {
        ArrayList<Player> list = mainscreen.pda.getPlayerList();
        for (Player p : list) {
            if (p.getId() == id) {
                PlayerInfo pinfo = new PlayerInfo();
                pinfo.setId(p.getId());
                pinfo.setName(p.getName());
                pinfo.setAva(p.getAva());
                pinfo.setScore(p.getScore());
                pinfo.setRank(mainscreen.pda.getRank(p.getId()));
                return pinfo;
            }
        }
        return null;
    }

    @Override
    public void outRoom(int roomid, int pid) throws RemoteException {
        for (Room r : mainscreen.roomList) {
            if (r.getRoomID() == roomid) {
                if (r.getP1id() == pid) {
                    r.setP1id(0);
                } else if (r.getP2id() == pid) {
                    r.setP2id(0);
                }
                if (r.getP1id() == 0 && r.getP2id() == 0) {
                    mainscreen.roomList.remove(r);
                    break;
                }
            }
        }
        mainscreen.loadRoomList();
    }

    @Override
    public void sendStateReady(int roomid, int pid, int state) throws RemoteException {
        for (Room r: mainscreen.roomList) {
            if (r.getRoomID() == roomid) {
                if (r.getP1id() == pid) {
                    r.setState1(state);
                } else if (r.getP2id() == pid) {
                    r.setState2(state);
                }
                break;
            }
        }
        mainscreen.loadRoomList();
    }

    @Override
    public void sendCellPick(int roomid, int index) throws RemoteException {
        for (Room r: mainscreen.roomList) {
            if (r.getRoomID() == roomid) {
                r.setCurrentCellPickIndex(index);
                break;
            }
        }
        mainscreen.loadRoomList();
    }

    @Override
    public void updateScore(int pid, int score) throws RemoteException {
        mainscreen.pda.updateScore(pid, score);
        mainscreen.loadPlayerList();
    }

}
