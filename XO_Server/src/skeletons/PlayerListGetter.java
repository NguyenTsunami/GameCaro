/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skeletons;

import dal.PlayerDAO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import model.Player;
import model.PlayerInfo;
import ifunctions.IPlayerListGetter;

/**
 *
 * @author thuy
 */
public class PlayerListGetter extends UnicastRemoteObject implements IPlayerListGetter {

    public PlayerListGetter() throws RemoteException {
        super();
    }

    @Override
    public ArrayList<PlayerInfo> getPlayerList() throws RemoteException {
        PlayerDAO pda = new PlayerDAO();
        ArrayList<Player> playerList = pda.getPlayerList();
        ArrayList<PlayerInfo> list = new ArrayList<>();
        for (Player p : playerList) {
            PlayerInfo pinfo = new PlayerInfo();
            pinfo.setId(p.getId());
            pinfo.setName(p.getName());
            pinfo.setAva(p.getAva());
            pinfo.setScore(p.getScore());
            pinfo.setRank(pda.getRank(p.getId()));
            list.add(pinfo);
        }
        return list;
    }

}
