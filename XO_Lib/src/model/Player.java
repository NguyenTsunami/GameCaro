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
public class Player implements Remote, Serializable {

    private int id;
    private String name;
    private String acc;
    private String pass;
    private String ava;
    private int score;

    public Player() {
    }

    public Player(int id, String name, String acc, String pass, String ava, int score) {
        this.id = id;
        this.name = name;
        this.acc = acc;
        this.pass = pass;
        this.ava = ava;
        this.score = score;
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

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

}
