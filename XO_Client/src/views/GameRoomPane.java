/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import mainscreen.PlayerScreen;
import model.PlayerInfo;
import model.Room;
import sun.applet.Main;
import ifunctions.IRoomStateGetter;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;

/**
 *
 * @author thuy
 */
public class GameRoomPane extends javax.swing.JPanel implements ActionListener {

    /**
     * Creates new form GameRoomPane
     */
    public PlayerScreen mainscreen;
    public Room room;
    PlayerInfo player1 = null;
    PlayerInfo player2 = null;
    public int currentPID;
    int currentTypePane = -1; //0: wait; 1- ready; 2- play 

    public GameRoomPane(PlayerScreen pc, Room r, int curPID) throws RemoteException {
        initComponents();

        //set init
        mainscreen = pc;
        room = r;
        if (room == null) {
            mainscreen.remove(this);
            return;
        }
        lblRoomID.setText("Room " + room.getRoomID());
        currentPID = curPID;
        loadBackgroundPlayerPane();

        //add close listener
        mainscreen.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                outRoom();
                super.windowClosing(e);
                //do something
            }
        });

        //start thread room state listener
        RoomStateListener visitListener = new RoomStateListener();
        visitListener.start();

        //load info players
        loadPlayer1Info();
        loadPlayer2Info();

        //add card layout for game pane
        GameViewPane.setLayout(new CardLayout());

        JPanel waitingPane = new JPanel();
        waitingPane.setOpaque(false);
        waitingPane.setLayout(new GridBagLayout());
        JLabel lblWait = new JLabel("Wait...", JLabel.CENTER);
        lblWait.setForeground(Color.white);
        lblWait.setFont(new Font("Ink Free", Font.ITALIC, 40));
        waitingPane.add(lblWait);
        GameViewPane.add("waitingPane", waitingPane);

        JPanel readyPane = new JPanel();
        readyPane.setOpaque(false);
        readyPane.setLayout(new GridBagLayout());
        JButton btnStart = new JButton("Start");
        btnStart.setFont(new Font("Ink Free", Font.ITALIC, 40));
        btnStart.setForeground(Color.black);
        btnStart.setBackground(Color.white);
        btnStart.setBorder(new LineBorder(Color.black));
        btnStart.addActionListener(this);
        readyPane.add(btnStart);
        GameViewPane.add("readyPane", readyPane);

        //display
        displayGameViewPane();
    }

    public void loadBackgroundPlayerPane() {
        player1Pane.setBackground(new Color(255, 255, 255, 64));
        player2Pane.setBackground(new Color(255, 255, 255, 64));
        if (currentPID == 1) {
            TitledBorder t1 = (TitledBorder) player1Pane.getBorder();
            t1.setTitle("YOU - X");
            TitledBorder t2 = (TitledBorder) player2Pane.getBorder();
            t2.setTitle("ENEMY - O");
        } else {
            TitledBorder t1 = (TitledBorder) player1Pane.getBorder();
            t1.setTitle("ENEMY - X");
            TitledBorder t2 = (TitledBorder) player2Pane.getBorder();
            t2.setTitle("YOU - O");
        }
    }

    public void loadPlayer1Info() throws RemoteException {
        PlayerInfo p = mainscreen.requestStub.getPlayerInfoById(room.getP1id());
        if (p == null && player1 == null) {
            return;
        }
        if (p != null && player1 != null && p.equals(player1)) {
            return;
        }

        if (room.getState1() == 0 || room.getState2() == 0) {
            player1Pane.setBackground(new Color(255, 255, 255, 64));
            player2Pane.setBackground(new Color(255, 255, 255, 64));
        }

        player1 = p;
        pnlAvaP1.removeAll();
        pnlAvaP1.repaint();

        if (player1 != null) {
            lblNameP1.setText(player1.getName());
            lblScoreP1.setText("Score: " + player1.getScore());
            lblRankP1.setText("Rank: " + player1.getRank());
            pnlAvaP1.add(new ImagePane(new ImageIcon(player1.getAva()).getImage()));
        } else {
            lblNameP1.setText("-----------");
            lblScoreP1.setText("-----------");
            lblRankP1.setText("-----------");
        }

        mainscreen.repaint();
    }

    public void loadPlayer2Info() throws RemoteException {
        PlayerInfo p = mainscreen.requestStub.getPlayerInfoById(room.getP2id());
        if (p == null && player2 == null) {
            return;
        }
        if (p != null && player2 != null && p.equals(player2)) {
            return;
        }

        if (room.getState1() == 0 || room.getState2() == 0) {
            player1Pane.setBackground(new Color(255, 255, 255, 64));
            player2Pane.setBackground(new Color(255, 255, 255, 64));
        }

        player2 = p;
        pnlAvaP2.removeAll();
        pnlAvaP2.repaint();

        if (player2 != null) {
            lblNameP2.setText(player2.getName());
            lblScoreP2.setText("Score: " + player2.getScore());
            lblRankP2.setText("Rank: " + player2.getRank());
            pnlAvaP2.add(new ImagePane(new ImageIcon(player2.getAva()).getImage()));
        } else {
            lblNameP2.setText("-----------");
            lblScoreP2.setText("-----------");
            lblRankP2.setText("-----------");
        }

        mainscreen.repaint();
    }

    public void outRoom() {
        try {
            //send current-picking-index-cell is -2 -> enemy can close game-play-pane 
            mainscreen.requestStub.sendCellPick(room.getRoomID(), -2);

            //send state ready to 0
            if (player1 != null) {
                mainscreen.requestStub.sendStateReady(room.getRoomID(), player1.getId(), 0);
            }
            if (player2 != null) {
                mainscreen.requestStub.sendStateReady(room.getRoomID(), player2.getId(), 0);
            }

            //remove player from room 
            mainscreen.requestStub.outRoom(room.getRoomID(), mainscreen.player.getId());

            //display OverView
            mainscreen.remove(this);
            mainscreen.displayOverView();
        } catch (RemoteException ex) {
            Logger.getLogger(GameRoomPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayWaitingPane() {
        lblTurnP1.setText("Wait Enemy...");
        lblTurnP2.setText("Wait Enemy...");
        CardLayout layout = (CardLayout) GameViewPane.getLayout();
        layout.show(GameViewPane, "waitingPane");
        currentTypePane = 0;
        mainscreen.repaint();
    }

    public void displayReadyPane() {
        lblTurnP1.setText("Wait Enemy...");
        lblTurnP2.setText("Wait Enemy...");
        CardLayout layout = (CardLayout) GameViewPane.getLayout();
        layout.show(GameViewPane, "readyPane");
        currentTypePane = 1;
        mainscreen.repaint();
    }

    public void displayGamePlayPane() throws RemoteException {
        //report this room is stop
        mainscreen.requestStub.sendCellPick(room.getRoomID(), -1);
        //create new game pane
        GamePlayPane gamePlayPane = new GamePlayPane(this);
        GameViewPane.add("gamePlayPane", gamePlayPane);
        //display pane
        CardLayout layout = (CardLayout) GameViewPane.getLayout();
        layout.show(GameViewPane, "gamePlayPane");
        currentTypePane = 2;
    }

    public void removeGamePlayPane(GamePlayPane p) throws RemoteException {
        GameViewPane.remove(p);
        loadPlayer1Info();
        loadPlayer2Info();
        displayGameViewPane();
    }

    public void displayGameViewPane() throws RemoteException {
        //if 1 or 2 player are null
        if (player1 == null || player2 == null) {
            if (currentTypePane != 0) {
                displayWaitingPane();
            }
        }

        //if 2 player ready 
        else if (room.getState1() == 1 && room.getState2() == 1) {
            if (currentTypePane != 2) {
                displayGamePlayPane();
            }
        }

        //if 1 or 2 player not ready
        else if (room.getState1() == 0 || room.getState2() == 0) {
            //if current player is player 1 
            if (currentPID == 1) {
                //if current player are not ready yet
                if (room.getState1() == 0) {
                    if (currentTypePane != 1) {
                        displayReadyPane();
                    }
                    //if current player are ready but enemy not ready yet
                } else if (room.getState2() == 0) {
                    if (currentTypePane != 0) {
                        displayWaitingPane();
                    }
                }
            }
            //if current player is player 2 
            if (currentPID == 2) {
                //if current player are not ready yet
                if (room.getState2() == 0) {
                    if (currentTypePane != 1) {
                        displayReadyPane();
                    }
                    //if current player are ready but enemy not ready yet
                } else if (room.getState1() == 0) {
                    if (currentTypePane != 0) {
                        displayWaitingPane();
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //send state ready
            mainscreen.requestStub.sendStateReady(room.getRoomID(), mainscreen.player.getId(), 1);
            //display pane
            displayGameViewPane();
        } catch (RemoteException ex) {
            Logger.getLogger(GameRoomPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class RoomStateListener extends Thread {

        @Override
        public void run() {

            while (true) {
                try {
                    IRoomStateGetter stub = (IRoomStateGetter) Naming.lookup("rmi://localhost:8787/roomStateGetter");
                    Room r = stub.getRoom(room.getRoomID());

                    //if room is not exist anymore
                    if (r == null) {
                        break;
                    }
                    //set this room
                    room = r;
                    loadPlayer1Info();
                    loadPlayer2Info();
                    displayGameViewPane();
                    sleep(1000);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameRoomPane.class.getName()).log(Level.SEVERE, null, ex);
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

        backgroundPane1 = new views.BackgroundPane();
        jPanel1 = new javax.swing.JPanel();
        notknow = new javax.swing.JPanel();
        btnOutRoom = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lblRoomID = new javax.swing.JLabel();
        optionPane = new javax.swing.JPanel();
        btnVolumn = new javax.swing.JButton();
        player1Pane = new javax.swing.JPanel();
        lblTurnP1 = new javax.swing.JLabel();
        pnlAvaP1 = new javax.swing.JPanel();
        lblNameP1 = new javax.swing.JLabel();
        lblScoreP1 = new javax.swing.JLabel();
        lblRankP1 = new javax.swing.JLabel();
        GameViewPane = new javax.swing.JPanel();
        player2Pane = new javax.swing.JPanel();
        lblTurnP2 = new javax.swing.JLabel();
        pnlAvaP2 = new javax.swing.JPanel();
        lblNameP2 = new javax.swing.JLabel();
        lblScoreP2 = new javax.swing.JLabel();
        lblRankP2 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 10, 10));

        notknow.setOpaque(false);

        btnOutRoom.setBackground(new java.awt.Color(255, 255, 255));
        btnOutRoom.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnOutRoom.setText("Out Room");
        btnOutRoom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnOutRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutRoomActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout notknowLayout = new javax.swing.GroupLayout(notknow);
        notknow.setLayout(notknowLayout);
        notknowLayout.setHorizontalGroup(
            notknowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notknowLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(btnOutRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
        );
        notknowLayout.setVerticalGroup(
            notknowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notknowLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnOutRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel1.add(notknow);

        jPanel7.setOpaque(false);

        lblRoomID.setFont(new java.awt.Font("Ink Free", 1, 48)); // NOI18N
        lblRoomID.setForeground(new java.awt.Color(255, 255, 255));
        lblRoomID.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRoomID.setText("ROOMID");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(jPanel7);

        optionPane.setOpaque(false);

        btnVolumn.setBackground(new java.awt.Color(255, 255, 255));
        btnVolumn.setFont(new java.awt.Font("Ink Free", 0, 18)); // NOI18N
        btnVolumn.setText("Volumn");
        btnVolumn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout optionPaneLayout = new javax.swing.GroupLayout(optionPane);
        optionPane.setLayout(optionPaneLayout);
        optionPaneLayout.setHorizontalGroup(
            optionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optionPaneLayout.createSequentialGroup()
                .addContainerGap(205, Short.MAX_VALUE)
                .addComponent(btnVolumn, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        optionPaneLayout.setVerticalGroup(
            optionPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionPaneLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(btnVolumn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel1.add(optionPane);

        player1Pane.setBackground(new java.awt.Color(204, 204, 255));
        player1Pane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "PLAYER 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Ink Free", 3, 36), new java.awt.Color(255, 255, 255))); // NOI18N

        lblTurnP1.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblTurnP1.setForeground(new java.awt.Color(255, 255, 255));
        lblTurnP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTurnP1.setText("Turn");

        pnlAvaP1.setOpaque(false);
        pnlAvaP1.setLayout(new java.awt.BorderLayout());

        lblNameP1.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblNameP1.setForeground(new java.awt.Color(255, 255, 255));
        lblNameP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameP1.setText("Name");

        lblScoreP1.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblScoreP1.setForeground(new java.awt.Color(255, 255, 255));
        lblScoreP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScoreP1.setText("Score");

        lblRankP1.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblRankP1.setForeground(new java.awt.Color(255, 255, 255));
        lblRankP1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRankP1.setText("Rank");

        javax.swing.GroupLayout player1PaneLayout = new javax.swing.GroupLayout(player1Pane);
        player1Pane.setLayout(player1PaneLayout);
        player1PaneLayout.setHorizontalGroup(
            player1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, player1PaneLayout.createSequentialGroup()
                .addGap(0, 54, Short.MAX_VALUE)
                .addComponent(pnlAvaP1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, player1PaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(player1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRankP1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTurnP1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNameP1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblScoreP1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        player1PaneLayout.setVerticalGroup(
            player1PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player1PaneLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(lblTurnP1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlAvaP1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNameP1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblScoreP1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRankP1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GameViewPane.setBackground(new java.awt.Color(204, 255, 204));
        GameViewPane.setOpaque(false);
        GameViewPane.setLayout(new java.awt.BorderLayout());

        player2Pane.setBackground(new java.awt.Color(204, 204, 255));
        player2Pane.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "PLAYER 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Ink Free", 3, 36), new java.awt.Color(255, 255, 255))); // NOI18N

        lblTurnP2.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblTurnP2.setForeground(new java.awt.Color(255, 255, 255));
        lblTurnP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTurnP2.setText("Turn");

        pnlAvaP2.setOpaque(false);
        pnlAvaP2.setLayout(new java.awt.BorderLayout());

        lblNameP2.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblNameP2.setForeground(new java.awt.Color(255, 255, 255));
        lblNameP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNameP2.setText("Name");

        lblScoreP2.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblScoreP2.setForeground(new java.awt.Color(255, 255, 255));
        lblScoreP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScoreP2.setText("Score");

        lblRankP2.setFont(new java.awt.Font("Ink Free", 1, 24)); // NOI18N
        lblRankP2.setForeground(new java.awt.Color(255, 255, 255));
        lblRankP2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRankP2.setText("Rank");

        javax.swing.GroupLayout player2PaneLayout = new javax.swing.GroupLayout(player2Pane);
        player2Pane.setLayout(player2PaneLayout);
        player2PaneLayout.setHorizontalGroup(
            player2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player2PaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(player2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTurnP2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNameP2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblScoreP2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRankP2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, player2PaneLayout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(pnlAvaP2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        player2PaneLayout.setVerticalGroup(
            player2PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(player2PaneLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(lblTurnP2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlAvaP2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNameP2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblScoreP2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRankP2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout backgroundPane1Layout = new javax.swing.GroupLayout(backgroundPane1);
        backgroundPane1.setLayout(backgroundPane1Layout);
        backgroundPane1Layout.setHorizontalGroup(
            backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(player1Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GameViewPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(player2Pane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1150, Short.MAX_VALUE)
        );
        backgroundPane1Layout.setVerticalGroup(
            backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundPane1Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(backgroundPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(player1Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GameViewPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(player2Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11))
        );

        add(backgroundPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOutRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutRoomActionPerformed
        outRoom();
    }//GEN-LAST:event_btnOutRoomActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GameViewPane;
    private views.BackgroundPane backgroundPane1;
    private javax.swing.JButton btnOutRoom;
    private javax.swing.JButton btnVolumn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblNameP1;
    private javax.swing.JLabel lblNameP2;
    private javax.swing.JLabel lblRankP1;
    private javax.swing.JLabel lblRankP2;
    private javax.swing.JLabel lblRoomID;
    private javax.swing.JLabel lblScoreP1;
    private javax.swing.JLabel lblScoreP2;
    public javax.swing.JLabel lblTurnP1;
    public javax.swing.JLabel lblTurnP2;
    private javax.swing.JPanel notknow;
    private javax.swing.JPanel optionPane;
    public javax.swing.JPanel player1Pane;
    public javax.swing.JPanel player2Pane;
    private javax.swing.JPanel pnlAvaP1;
    private javax.swing.JPanel pnlAvaP2;
    // End of variables declaration//GEN-END:variables
}
