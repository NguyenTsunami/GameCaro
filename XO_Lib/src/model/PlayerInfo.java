/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.rmi.Remote;

/**
 *
 * @author thuy
 */
public class PlayerInfo implements Remote, Serializable {

    private int id;
    private String name;
    private String ava;
    private int score;
    private int rank;

    public PlayerInfo() {
    }

    public PlayerInfo(int id, String name, String ava, int score, int rank) {
        this.id = id;
        this.name = name;
        this.ava = ava;
        this.score = score;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAva() {
        return ava;
    }

    public void setAva(String ava) {
        this.ava = ava;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean equals(PlayerInfo p2) {
        if (this == null && p2 == null) {
            return true;
        }
        if (this != null && p2 != null) {
            return (this.getId() == p2.getId() && this.getName().equals(p2.getName())
                    && this.getRank() == p2.getRank() && this.getScore() == p2.getScore()
                    && this.getAva().equals(p2.getAva()));
        }
        return false;
    }
}
