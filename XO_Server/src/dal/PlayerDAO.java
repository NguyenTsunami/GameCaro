/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;

/**
 *
 * @author thuy
 */
public class PlayerDAO extends BaseDAO {

    public ArrayList<Player> getPlayerList() {
        ArrayList<Player> list = new ArrayList<>();

        String sql = "SELECT id, name, acc, pass, ava, score FROM [Player] ORDER BY score DESC\n";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Player a = new Player();
                a.setId(rs.getInt("id"));
                a.setName(rs.getString("name"));
                a.setAcc(rs.getString("acc"));
                a.setPass(rs.getString("pass"));
                a.setAva(rs.getString("ava"));
                a.setScore(rs.getInt("score"));
                list.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public void insert(Player player) {
        try {
            String sql = "INSERT INTO [Player](name, acc, pass, ava, score)\n"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player.getName());
            statement.setString(2, player.getAcc());
            statement.setString(3, player.getPass());
            statement.setString(4, player.getAva());
            statement.setInt(5, player.getScore());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getRank(int id) {
        int rank = -1;
        try {
            String sql = "WITH temp AS (\n"
                    + "	SELECT id, RANK () OVER (ORDER BY score DESC) rank_no\n"
                    + "	FROM Player\n"
                    + ") \n"
                    + "SELECT rank_no\n"
                    + "FROM temp\n"
                    + "WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            rank = rs.getInt("rank_no");
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rank;
    }
    
    public void updateScore(int id, int score) {
        try {
            String sql = "UPDATE Player SET score = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, score);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PlayerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
