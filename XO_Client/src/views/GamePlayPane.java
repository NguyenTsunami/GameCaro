/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import ifunctions.ICellPickGetter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import mainscreen.PlayerScreen;
import sun.applet.Main;

/**
 *
 * @author thuy
 */
public class GamePlayPane extends javax.swing.JPanel {

    PlayerScreen mainscreen;
    int roomid;
    int currentPID;
    GameRoomPane gameroom;

    //X - player1 ; Y - player2
    //X always go first
    private char whoseTurn = 'X';

    //cells (>25)
    int rows = 10;
    int cols = 10;
    private Cell[][] cells = new Cell[rows][cols];

    public GamePlayPane(GameRoomPane gr) {
        initComponents();
        gameroom = gr;
        mainscreen = gr.mainscreen;
        roomid = gr.room.getRoomID();
        currentPID = gr.currentPID;
        lblReport.setText("X's Turn");

        // Panel contain all cells
        pnlController.setLayout(new GridLayout(rows, cols, 0, 0));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pnlController.add(cells[i][j] = new Cell(index(i, j)));
            }
        }

        //Thread listens cell that enemy picked
        CellListener cellListener = new CellListener();
        cellListener.start();
    }

    //Set background and set pick-ability for player of turn
    public void setTurn() {
        if (whoseTurn == 'X' && currentPID == 1) { // 1-X turn; 1-you
            gameroom.player2Pane.setBackground(new Color(0, 0, 0, 64)); //2- black
            gameroom.player1Pane.setBackground(new Color(255, 255, 255, 64)); // 1-white
            gameroom.lblTurnP1.setText("Your turn..."); //1-Your turn
            gameroom.lblTurnP2.setText(""); //2-""
            mainscreen.repaint();
            setEnablePick(true);
        } else if (whoseTurn == 'X' && currentPID == 2) { //1-X turn; 2-you
            gameroom.player2Pane.setBackground(new Color(0, 0, 0, 64)); // 2- black
            gameroom.player1Pane.setBackground(new Color(255, 255, 255, 64)); //1-white
            gameroom.lblTurnP1.setText("Enemy turn..."); //1-enemy turn
            gameroom.lblTurnP2.setText(""); //2-""
            mainscreen.repaint();
            setEnablePick(false);
        } else if (whoseTurn == 'O' && currentPID == 1) { // 2-O turn; 1-you
            gameroom.player1Pane.setBackground(new Color(0, 0, 0, 64)); //1- black
            gameroom.player2Pane.setBackground(new Color(255, 255, 255, 64)); //2-white
            gameroom.lblTurnP2.setText("Enemy turn..."); //2-enemy turn
            gameroom.lblTurnP1.setText(""); //1-""
            mainscreen.repaint();
            setEnablePick(false);
        } else if (whoseTurn == 'O' && currentPID == 2) { //2-O turn; 2-you
            gameroom.player1Pane.setBackground(new Color(0, 0, 0, 64)); //1-black
            gameroom.player2Pane.setBackground(new Color(255, 255, 255, 64)); // 2-white
            gameroom.lblTurnP1.setText(""); //1-""
            gameroom.lblTurnP2.setText("Your turn..."); //2-your turn
            mainscreen.repaint();
            setEnablePick(true);
        }
    }

    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j].getToken() == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public int xpos(int index) {
        return index / cols;
    }

    public int ypos(int index) {
        return index % cols;
    }

    public int index(int i, int j) {
        return i * cols + j;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    int min(int a, int b) {
        return (a < b) ? a : b;
    }

    public boolean isWon(int xpos, int ypos) {
        char token = cells[xpos][ypos].getToken();
        char oppositeToken = (token == 'X') ? 'O' : 'X';

        //Check Vertical
        for (int i = max(0, xpos - 4); i <= min(xpos, rows - 5); i++) {
            boolean checkLine = true;
            for (int j = i; j < i + 5; j++) {
                if (cells[j][ypos].getToken() != token) {
                    checkLine = false;
                    break;
                }
            }
            if (checkLine) {
                boolean check2Head = true;
                if (i - 1 >= 0 && cells[i - 1][ypos].getToken() == oppositeToken
                        && i + 5 < rows && cells[i + 5][ypos].getToken() == oppositeToken) {
                    check2Head = false;
                }
                if (check2Head) {
                    for (int j = i; j < i + 5; j++) {
                        cells[j][ypos].setBackground(new Color(255, 255, 255, 64));
                    }
                    return true;
                }
            }
        }

        //Check Horizontal
        for (int i = max(0, ypos - 4); i <= min(ypos, cols - 5); i++) {
            boolean checkLine = true;
            for (int j = i; j < i + 5; j++) {
                if (cells[xpos][j].getToken() != token) {
                    checkLine = false;
                    break;
                }
            }
            if (checkLine) {
                boolean check2Head = true;
                if (i - 1 >= 0 && cells[xpos][i - 1].getToken() == oppositeToken
                        && i + 5 < cols && cells[xpos][i + 5].getToken() == oppositeToken) {
                    check2Head = false;
                }
                if (check2Head) {
                    for (int j = i; j < i + 5; j++) {
                        cells[xpos][j].setBackground(new Color(255, 255, 255, 64));
                    }
                    return true;
                }
            }
        }

        //Check Diagonal 1
        for (int i = max(0, xpos - 4); i <= min(xpos, rows - 5); i++) {
            boolean checkLine = true;
            for (int j = i; j < i + 5; j++) {
                int k = ypos - (j - xpos);
                if (k < 0 || k >= cols || cells[j][k].getToken() != token) {
                    checkLine = false;
                    break;
                }
            }
            if (checkLine) {
                boolean check2Head = true;
                if (i - 1 >= 0 && ypos - (i - 1 - xpos) < cols
                        && cells[i - 1][ypos - (i - 1 - xpos)].getToken() == oppositeToken
                        && i + 5 < rows && ypos - (i + 5 - xpos) >= 0
                        && cells[i + 5][ypos - (i + 5 - xpos)].getToken() == oppositeToken) {
                    check2Head = false;
                }
                if (check2Head) {
                    for (int j = i; j < i + 5; j++) {
                        cells[j][ypos - (j - xpos)].setBackground(new Color(255, 255, 255, 64));
                    }
                    return true;
                }
            }
        }

        //Check Diagonal 2
        for (int i = max(0, xpos - 4); i <= min(xpos, rows - 5); i++) {
            boolean checkLine = true;
            for (int j = i; j < i + 5; j++) {
                int k = ypos + (j - xpos);
                if (k < 0 || k >= cols || cells[j][k].getToken() != token) {
                    checkLine = false;
                    break;
                }
            }
            if (checkLine) {
                boolean check2Head = true;
                if (i - 1 >= 0 && ypos + (i - 1 - xpos) >= 0
                        && cells[i - 1][ypos + (i - 1 - xpos)].getToken() == oppositeToken
                        && i + 5 < rows && ypos + (i + 5 - xpos) < cols
                        && cells[i + 5][ypos + (i + 5 - xpos)].getToken() == oppositeToken) {
                    check2Head = false;
                }
                if (check2Head) {
                    for (int j = i; j < i + 5; j++) {
                        cells[j][ypos + (j - xpos)].setBackground(new Color(255, 255, 255, 64));
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public void closeGamePlayPane() throws RemoteException {
        gameroom.removeGamePlayPane(this);
    }

    //set enble pick
    public void setEnablePick(boolean enable) {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].setEnable(enable);
            }
        }
    }

    //thread cell listener
    class CellListener extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    ICellPickGetter stub = (ICellPickGetter) Naming.lookup("rmi://localhost:8787/cellPickGetter");
                    int index = stub.getCellEnemyPick(roomid);
                    //index = -1: stop room game
                    //index >=0: playing
                    //index =-2: enemy out
                    if (index >= 0) {
                        int i = xpos(index);
                        int j = ypos(index);
                        cells[i][j].pick();
                    } else if (index == -2) {
                        //close this game play pane
                        closeGamePlayPane();
                        //stop this thread
                        break;
                    }
                    setTurn();
                    sleep(500);
                } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GamePlayPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public class Cell extends JPanel {

        private boolean enable = true;
        private char token = ' ';
        public int index = -1;

        public Cell(int idx) {
            setOpaque(false);
            setBorder(new LineBorder(Color.white, 3));

            //Add mouse listener
            addMouseListener(new MyMouseListener());
            this.index = idx;
        }

        public char getToken() {
            return token;
        }

        public void setToken(char c) {
            token = c;
            repaint();
        }

        public void setEnable(boolean e) {
            enable = e;
        }

        //Handle Case Won
        public void hanldeWon() throws RemoteException {
            int confirmContinue;
            lblReport.setText(whoseTurn + " won! The game is over");

            //update score
            if (whoseTurn == 'X') {
                if (currentPID == 1) {
                    //update score for player
                    mainscreen.requestStub.updateScore(mainscreen.player.getId(), mainscreen.player.getScore() + 10);
                    mainscreen.player.setScore(mainscreen.player.getScore() + 10);
                    //confirm dialolg
                    confirmContinue = JOptionPane.showConfirmDialog(mainscreen, "You won! Do you want to continue?");
                } else {
                    //confirm dialolg
                    confirmContinue = JOptionPane.showConfirmDialog(mainscreen, "You lose! Do you want to continue?");
                }
            } else {
                if (currentPID == 2) {
                    //update score for player
                    mainscreen.requestStub.updateScore(mainscreen.player.getId(), mainscreen.player.getScore() + 10);
                    mainscreen.player.setScore(mainscreen.player.getScore() + 10);
                    //confirm dialolg
                    confirmContinue = JOptionPane.showConfirmDialog(mainscreen, "You won! Do you want to continue?");
                } else {
                    //confirm dialolg
                    confirmContinue = JOptionPane.showConfirmDialog(mainscreen, "You lose! Do you want to continue?");
                }
            }
            //handle confirm
            if (confirmContinue == JOptionPane.YES_OPTION) {
                mainscreen.requestStub.sendStateReady(roomid, mainscreen.player.getId(), 0);
            } else {
                gameroom.outRoom();
            }
            //End game
            whoseTurn = ' ';
        }

        //Handle Draw Case
        public void hanleDraw() throws RemoteException {
            int confirmContinue;
            lblReport.setText("Draw! The game is over");
            //handle confirm
            confirmContinue = JOptionPane.showConfirmDialog(mainscreen, "Draw! Do you want to continue?");
            if (confirmContinue == JOptionPane.YES_OPTION) {
                mainscreen.requestStub.sendStateReady(roomid, mainscreen.player.getId(), 0);
            } else {
                gameroom.outRoom();
            }
            //End game
            whoseTurn = ' ';
        }

        //When this cell is picked
        public void pick() throws RemoteException {

            // If has a cell empty and game is continuing
            if (token == ' ' && whoseTurn != ' ') {
                try {
                    //Send cell picked
                    mainscreen.requestStub.sendCellPick(roomid, index);
                } catch (RemoteException ex) {
                    Logger.getLogger(GamePlayPane.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Change token
                setToken(whoseTurn);

                // Check state of game
                if (isWon(xpos(index), ypos(index))) {
                    hanldeWon();
                } else if (isFull()) {
                    hanleDraw();
                } else {
                    // flip turn
                    whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                    // report turn
                    lblReport.setText(whoseTurn + "'s turn");
                }

                //picked -> set disable
                setEnablePick(false);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            if (token == 'X') {
                g.drawImage(new ImageIcon("images/xxx.png").getImage(), 0, 0, getWidth(), getHeight(), null);
            } else if (token == 'O') {
                g.drawImage(new ImageIcon("images/ooo.png").getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        }

        private class MyMouseListener extends MouseAdapter {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (enable == true) {
                    try {
                        pick();
                    } catch (RemoteException ex) {
                        Logger.getLogger(GamePlayPane.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

        pnlController = new javax.swing.JPanel();
        lblReport = new javax.swing.JLabel();

        setOpaque(false);

        pnlController.setOpaque(false);

        javax.swing.GroupLayout pnlControllerLayout = new javax.swing.GroupLayout(pnlController);
        pnlController.setLayout(pnlControllerLayout);
        pnlControllerLayout.setHorizontalGroup(
            pnlControllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlControllerLayout.setVerticalGroup(
            pnlControllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 359, Short.MAX_VALUE)
        );

        lblReport.setFont(new java.awt.Font("Ink Free", 3, 36)); // NOI18N
        lblReport.setForeground(new java.awt.Color(255, 255, 255));
        lblReport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblReport.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlController, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReport, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
                .addGap(71, 71, 71))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(lblReport, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(pnlController, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(98, 98, 98))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblReport;
    private javax.swing.JPanel pnlController;
    // End of variables declaration//GEN-END:variables
}
