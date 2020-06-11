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
public class Room implements Remote, Serializable {

    private int roomID;
    private int p1id;
    private int p2id;
    private int state1 = 0;
    private int state2 = 0;
    private int currentCellPickIndex = -2;

    public Room() {
    }

    public Room(int roomID, int p1id, int p2id, int state1, int state2) {
        this.roomID = roomID;
        this.p1id = p1id;
        this.p2id = p2id;
        this.state1 = state1;
        this.state2 = state2;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getP1id() {
        return p1id;
    }

    public void setP1id(int p1id) {
        this.p1id = p1id;
    }

    public int getP2id() {
        return p2id;
    }

    public void setP2id(int p2id) {
        this.p2id = p2id;
    }

    public int getState1() {
        return state1;
    }

    public void setState1(int state1) {
        this.state1 = state1;
    }

    public int getState2() {
        return state2;
    }

    public void setState2(int state2) {
        this.state2 = state2;
    }

    public int getCurrentCellPickIndex() {
        return currentCellPickIndex;
    }

    public void setCurrentCellPickIndex(int currentCellPickIndex) {
        this.currentCellPickIndex = currentCellPickIndex;
    }

    public boolean equals(Room r2) {
        return (this.getRoomID() == r2.getRoomID()
                && this.getP1id() == r2.getP1id()
                && this.getP2id() == r2.getP2id());
    }
}
