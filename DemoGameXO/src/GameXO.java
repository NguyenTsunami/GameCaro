
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author thuy
 */
public class GameXO extends javax.swing.JFrame {

    /**
     * Creates new form GameXO
     */
    public GameXO() {
        initComponents();
        setLocationRelativeTo(null);
        setSize(300, 300);

        // Panel p chứa các ô
        pnlController.setLayout(new GridLayout(3, 3, 0, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pnlController.add(cells[i][j] = new Cell());
            }

            // Thay đổi đường viền của các ô và label trạng thái
            pnlController.setBorder(new LineBorder(Color.red, 1));
            lblReport.setBorder(new LineBorder(Color.yellow, 1));
        }
    }

    // Chỉ ra lượt chơi của người chơi, giá trị ban đầu là X
    private char whoseTurn = 'X';

    // Khởi tạo các ô
    private Cell[][] cells = new Cell[3][3];

    /**
     * Xác định xem liệu tất cả các ô đều đã được đánh hay chưa
     */
    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getToken() == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Xác định xem liệu người chơi với ký hiện token đã thắng hay chưa
     */
    public boolean isWon(char token) {
        for (int i = 0; i < 3; i++) {
            if ((cells[i][0].getToken() == token)
                    && (cells[i][1].getToken() == token)
                    && (cells[i][2].getToken() == token)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if ((cells[0][j].getToken() == token)
                    && (cells[1][j].getToken() == token)
                    && (cells[2][j].getToken() == token)) {
                return true;
            }
        }

        if ((cells[0][0].getToken() == token)
                && (cells[1][1].getToken() == token)
                && (cells[2][2].getToken() == token)) {
            return true;
        }

        if ((cells[0][2].getToken() == token)
                && (cells[1][1].getToken() == token)
                && (cells[2][0].getToken() == token)) {
            return true;
        }

        return false;
    }
    // Một inner class đại diện cho một ô

    public class Cell extends JButton {
        // Ký hiệu của ô này

        private char token = ' ';

        public Cell() {
            setBorder(new LineBorder(Color.black, 1)); // Thay đổi đường viền của ô
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Nếu một ô còn trống và trò chơi vẫn chưa kết thúc
                if (token == ' ' && whoseTurn != ' ') {
                    setToken(whoseTurn); // Thay đổi ký hiệu cho ô

                    // Kiểm tra trạng thái của trò chơi
                    if (isWon(whoseTurn)) {
                        lblReport.setText(whoseTurn + "Won! The game is over");
                        whoseTurn = ' '; // Trò chơi kết thúc
                    } else if (isFull()) {
                        lblReport.setText("Draw! The game is over");
                        whoseTurn = ' '; // Trò chơi kết thúc
                    } else {
                        // Thay đổi lượt chơi
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Hiển thị lượt chơi
                        lblReport.setText(whoseTurn + "'s turn");

                    }
                }
                }
            } );  // Đăng ký listener
        }

        /**
         * Trả về ký hiệu của ô
         */
        public char getToken() {
            return token;
        }

        /**
         * Nhập một ký hiệu mới cho ô
         */
        public void setToken(char c) {
            token = c;
            repaint();
        }

        /**
         * Vẽ ô
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (token == 'X') {
                g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
                g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
            } else if (token == 'O') {
                g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
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

        lblReport = new javax.swing.JLabel();
        pnlController = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblReport.setText("X's turn");
        getContentPane().add(lblReport, java.awt.BorderLayout.PAGE_START);

        pnlController.setOpaque(false);

        javax.swing.GroupLayout pnlControllerLayout = new javax.swing.GroupLayout(pnlController);
        pnlController.setLayout(pnlControllerLayout);
        pnlControllerLayout.setHorizontalGroup(
            pnlControllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 928, Short.MAX_VALUE)
        );
        pnlControllerLayout.setVerticalGroup(
            pnlControllerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 694, Short.MAX_VALUE)
        );

        getContentPane().add(pnlController, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameXO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameXO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameXO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameXO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameXO().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblReport;
    private javax.swing.JPanel pnlController;
    // End of variables declaration//GEN-END:variables
}
