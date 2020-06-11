package views;

import ifunctions.IRoomListGetter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JViewport;
import mainscreen.PlayerScreen;
import model.Player;
import model.PlayerInfo;
import model.Room;
import sun.applet.Main;
import ifunctions.IPlayerListGetter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thuy
 */
public class OverviewPane extends javax.swing.JPanel {

    /**
     * Creates new form OverviewPane
     */
    PlayerScreen mainscreen;
    public IPlayerListGetter plistStub;
    public IRoomListGetter rlistStub;
    ArrayList<PlayerInfo> playerList = new ArrayList<>();
    ArrayList<Room> roomList = new ArrayList<>();

    public OverviewPane(PlayerScreen fr) throws RemoteException {
        initComponents();
        mainscreen = fr;
        loadInfo();
        loadRoomList();
        loadPlayerList();

        //Start Thread
        Thread rankLoader = new RankLoader();
        Thread roomLoader = new RoomLoader();
        rankLoader.start();
        roomLoader.start();
    }

    public void loadInfo() throws RemoteException {
        Player player = mainscreen.player;
        if (player == null) {
            return;
        }
        ImagePane avaPane = new ImagePane(new ImageIcon(player.getAva()).getImage());
        AvatarView.add(avaPane, BorderLayout.CENTER);
        lblName.setText("Name: " + player.getName());
        lblScore.setText("Score: " + player.getScore());
        lblRank.setText("Rank: " + mainscreen.requestStub.getRank(player.getId()));
    }

    public void loadPlayerList() throws RemoteException {

        JPanel container = new JPanel(new GridLayout(0, 1, 10, 10));
        container.setOpaque(false);

        for (PlayerInfo p : playerList) {
            PlayerInfoPane pane = new PlayerInfoPane(p);
            container.add(pane);
        }

        JViewport viewport = new JViewport();
        viewport.setOpaque(false);
        viewport.setView(container);

        rankPaneScroll.setViewport(viewport);
    }

    public void loadRoomList() throws RemoteException {

        JPanel container = new JPanel(new GridLayout(0, 1, 10, 10));
        container.setOpaque(false);
        for (Room r : roomList) {
            PlayerInfo p1 = mainscreen.requestStub.getPlayerInfoById(r.getP1id());
            PlayerInfo p2 = mainscreen.requestStub.getPlayerInfoById(r.getP2id());
            RoomInfoPane pane = new RoomInfoPane(r, p1, p2, mainscreen);
            container.add(pane);
        }
        JViewport viewport = new JViewport();
        viewport.setOpaque(false);
        viewport.setView(container);

        roomPaneScroll.setViewport(viewport);
    }

    class RankLoader extends Thread {

        boolean isSame(ArrayList<PlayerInfo> l1, ArrayList<PlayerInfo> l2) {
            if (l1.size() != l2.size()) {
                return false;
            }
            for (int i = 0; i < l1.size(); i++) {
                PlayerInfo p1 = l1.get(i);
                PlayerInfo p2 = l2.get(i);
                if (!p1.equals(p2)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    plistStub = (IPlayerListGetter) Naming.lookup("rmi://localhost:8787/playerListGetter");
                    ArrayList<PlayerInfo> newList = plistStub.getPlayerList();
                    if (!isSame(playerList, newList)) {
                        playerList = newList;
                        loadPlayerList();
                    }
                    sleep(1000);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    class RoomLoader extends Thread {

        boolean isSame(ArrayList<Room> l1, ArrayList<Room> l2) {
            if (l1.size() != l2.size()) {
                return false;
            }
            for (int i = 0; i < l1.size(); i++) {
                Room p1 = l1.get(i);
                Room p2 = l2.get(i);
                if (!p1.equals(p2)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    rlistStub = (IRoomListGetter) Naming.lookup("rmi://localhost:8787/roomListGetter");
                    ArrayList<Room> newList = rlistStub.getRoomList();
                    if (!isSame(roomList, newList)) {
                        roomList = newList;
                        loadRoomList();
                    }
                    sleep(1000);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        backgroundPane1 = new views.BackgroundPane();
        jPanel1 = new javax.swing.JPanel();
        AvatarView = new javax.swing.JPanel();
        InfoView = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();
        lblRank = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        OptionView = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        RoomTableView = new javax.swing.JPanel();
        roomPaneScroll = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        btnCreateRoom = new javax.swing.JButton();
        btnFindRoom = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        RankTableView = new javax.swing.JPanel();
        rankPaneScroll = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        btnFindPlayer = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 10, 10));

        AvatarView.setBackground(new java.awt.Color(255, 204, 255));
        AvatarView.setOpaque(false);
        AvatarView.setLayout(new java.awt.BorderLayout());
        jPanel1.add(AvatarView);

        InfoView.setOpaque(false);
        InfoView.setLayout(new java.awt.GridLayout(0, 1, 10, 10));

        lblName.setFont(new java.awt.Font("Ink Free", 1, 18)); // NOI18N
        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setText("Name");
        InfoView.add(lblName);

        lblScore.setFont(new java.awt.Font("Ink Free", 1, 18)); // NOI18N
        lblScore.setForeground(new java.awt.Color(255, 255, 255));
        lblScore.setText("SCore");
        InfoView.add(lblScore);

        lblRank.setFont(new java.awt.Font("Ink Free", 1, 18)); // NOI18N
        lblRank.setForeground(new java.awt.Color(255, 255, 255));
        lblRank.setText("Rank");
        InfoView.add(lblRank);

        jPanel1.add(InfoView);

        jPanel6.setOpaque(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 95, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6);

        jPanel3.setOpaque(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 212, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 95, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);

        OptionView.setOpaque(false);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        jButton1.setText("Volumn");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnLogOut.setBackground(new java.awt.Color(255, 255, 255));
        btnLogOut.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnLogOut.setText("Log Out");
        btnLogOut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout OptionViewLayout = new javax.swing.GroupLayout(OptionView);
        OptionView.setLayout(OptionViewLayout);
        OptionViewLayout.setHorizontalGroup(
            OptionViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, OptionViewLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(OptionViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46))
        );
        OptionViewLayout.setVerticalGroup(
            OptionViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(OptionViewLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogOut)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel1.add(OptionView);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.BorderLayout(10, 10));

        RoomTableView.setBackground(new java.awt.Color(204, 255, 204));
        RoomTableView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ROOM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Ink Free", 1, 36), new java.awt.Color(255, 255, 255))); // NOI18N
        RoomTableView.setForeground(new java.awt.Color(255, 255, 255));
        RoomTableView.setOpaque(false);
        RoomTableView.setLayout(new java.awt.BorderLayout());

        roomPaneScroll.setOpaque(false);
        RoomTableView.add(roomPaneScroll, java.awt.BorderLayout.CENTER);

        jPanel2.add(RoomTableView, java.awt.BorderLayout.CENTER);

        jPanel7.setOpaque(false);
        jPanel7.setLayout(new java.awt.GridBagLayout());

        btnCreateRoom.setBackground(new java.awt.Color(255, 255, 255));
        btnCreateRoom.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnCreateRoom.setText("Create New Room");
        btnCreateRoom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnCreateRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRoomActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 76;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 132, 40, 0);
        jPanel7.add(btnCreateRoom, gridBagConstraints);

        btnFindRoom.setBackground(new java.awt.Color(255, 255, 255));
        btnFindRoom.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnFindRoom.setText("Find Room");
        btnFindRoom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnFindRoom.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                btnFindRoomMouseWheelMoved(evt);
            }
        });
        btnFindRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindRoomActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 12, 40, 120);
        jPanel7.add(btnFindRoom, gridBagConstraints);

        jPanel2.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel4.setBackground(new java.awt.Color(255, 255, 204));
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel4.setOpaque(false);
        jPanel4.setLayout(new java.awt.BorderLayout(10, 10));

        RankTableView.setBackground(new java.awt.Color(204, 255, 204));
        RankTableView.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RANK", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Ink Free", 1, 36), new java.awt.Color(255, 255, 255))); // NOI18N
        RankTableView.setForeground(new java.awt.Color(255, 255, 255));
        RankTableView.setOpaque(false);
        RankTableView.setLayout(new java.awt.BorderLayout());

        rankPaneScroll.setBackground(new java.awt.Color(255, 204, 204));
        rankPaneScroll.setMinimumSize(new java.awt.Dimension(100, 100));
        rankPaneScroll.setPreferredSize(new java.awt.Dimension(200, 200));
        RankTableView.add(rankPaneScroll, java.awt.BorderLayout.CENTER);

        jPanel4.add(RankTableView, java.awt.BorderLayout.CENTER);

        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.GridBagLayout());

        btnFindPlayer.setBackground(new java.awt.Color(255, 255, 255));
        btnFindPlayer.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnFindPlayer.setText("Find Player");
        btnFindPlayer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnFindPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindPlayerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.ipady = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 217, 36, 173);
        jPanel8.add(btnFindPlayer, gridBagConstraints);

        jPanel4.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout backgroundPane1Layout = new javax.swing.GroupLayout(backgroundPane1);
        backgroundPane1.setLayout(backgroundPane1Layout);
        backgroundPane1Layout.setHorizontalGroup(
            backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(backgroundPane1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        backgroundPane1Layout.setVerticalGroup(
            backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPane1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(backgroundPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnFindRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindRoomActionPerformed

    private void btnFindPlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindPlayerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindPlayerActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        // TODO add your handling code here:
        mainscreen.player = null;
        mainscreen.remove(this);
        mainscreen.displaySigninView();
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnFindRoomMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_btnFindRoomMouseWheelMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFindRoomMouseWheelMoved

    private void btnCreateRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRoomActionPerformed
        try {
            // create room
            Room room = mainscreen.requestStub.createRoom(mainscreen.player.getId());

            // create and display game pane
            mainscreen.displayGameRoomView(room, 1);
        } catch (RemoteException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCreateRoomActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AvatarView;
    private javax.swing.JPanel InfoView;
    private javax.swing.JPanel OptionView;
    private javax.swing.JPanel RankTableView;
    private javax.swing.JPanel RoomTableView;
    private views.BackgroundPane backgroundPane1;
    private javax.swing.JButton btnCreateRoom;
    private javax.swing.JButton btnFindPlayer;
    private javax.swing.JButton btnFindRoom;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblRank;
    private javax.swing.JLabel lblScore;
    private javax.swing.JScrollPane rankPaneScroll;
    private javax.swing.JScrollPane roomPaneScroll;
    // End of variables declaration//GEN-END:variables
}
