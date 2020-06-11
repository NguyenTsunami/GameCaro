
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GameController extends JFrame {
    // Chỉ ra lượt chơi của người chơi, giá trị ban đầu là X

    private char whoseTurn = 'X';

    // Khởi tạo các ô
    private Cell[][] cells = new Cell[3][3];

    // Khởi tạo label trạng thái của trò chơi
    private JLabel jlblStatus = new JLabel("X's turn to play");

    /**
     * Initialize UI
     */
    public GameController() {
        //Khởi tạo frame
        setLocationRelativeTo(null);
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        //Status Label
        add(jlblStatus);
        jlblStatus.setBounds(400, 400, 100, 100);

        // Panel p chứa các ô
        JPanel p = new JPanel(new GridLayout(3, 3, 0, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                p.add(cells[i][j] = new Cell());
            }

            // Thay đổi đường viền của các ô và label trạng thái
            p.setBorder(new LineBorder(Color.red, 1));
            jlblStatus.setBorder(new LineBorder(Color.yellow, 1));

            // Đưa panel và label vào trong frame
            add(p, BorderLayout.CENTER);
            add(jlblStatus, BorderLayout.SOUTH);
        }
    }

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

    public class Cell extends JPanel {
        // Ký hiệu của ô này

        private char token = ' ';

        public Cell() {
            setBorder(new LineBorder(Color.black, 1)); // Thay đổi đường viền của ô
            addMouseListener(new MyMouseListener());  // Đăng ký listener
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

        private class MyMouseListener extends MouseAdapter {

            /**
             * Xử lý sự kiện nhấn chuột trong một ô
             */
            public void mouseClicked(MouseEvent e) {
                // Nếu một ô còn trống và trò chơi vẫn chưa kết thúc
                if (token == ' ' && whoseTurn != ' ') {
                    setToken(whoseTurn); // Thay đổi ký hiệu cho ô

                    // Kiểm tra trạng thái của trò chơi
                    if (isWon(whoseTurn)) {
                        jlblStatus.setText(whoseTurn + "Won! The game is over");
                        whoseTurn = ' '; // Trò chơi kết thúc
                    } else if (isFull()) {
                        jlblStatus.setText("Draw! The game is over");
                        whoseTurn = ' '; // Trò chơi kết thúc
                    } else {
                        // Thay đổi lượt chơi
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Hiển thị lượt chơi
                        jlblStatus.setText(whoseTurn + "'s turn");

                    }
                }
            }
        }
    }

    /**
     * Phương thức main() cho phép applet này chạy như là một ứng dụng
     */
    public static void main(String[] args) {
        // Tạo một frame
        JFrame frame = new JFrame("Game");
    }
}
