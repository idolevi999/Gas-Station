/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verFinal;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * מחלקה המגדירה מכונית
 * @author idole
 */
public class Car
{
    private int carX; //משתנה לשמירת מיקום הרוחב של המכונית
    private int carY; //משתנה לשמירת מיקום האורך של המכונית
    private int numCar; //משתנה לשמירת מספר המכונית לפי צבע
    private int placeTurn; //משתנה השומר את מיקום המכונית בתור הממתינים
    private int FarGo; //משתנה השומר את מיקום המכונית

    /**
     * פעולה בונה היוצרת מכונית בסימולצייה
     * @param carX מיקום האורך המכונית בחלון
     * @param carY מיקום ברוחב המכונית בחלון
     * @param numCar מספר מכונית לפי צבע
     * @param placeTurn מיקום המכונית בתור
     */
    public Car(int carX, int carY, int numCar, int placeTurn) {
        this.carX = carX;
        this.carY = carY;
        this.numCar = numCar;
        this.placeTurn = placeTurn;
        this.FarGo = 0;
    }

    public int getNumCar() {
        return numCar;
    }

    public void setNumCar(int numCar) {
        this.numCar = numCar;
    }

    public int getFarGo() {
        return FarGo;
    }

    public void setFarGo(int FarGo) {
        this.FarGo = FarGo;
    }
    
    public int getCarX() {
        return carX;
    }

    public void setCarX(int carX) {
        this.carX = carX;
    }

    public int getCarY() {
        return carY;
    }

    public void setCarY(int carY) {
        this.carY = carY;
    }

    public int getPlaceTurn() {
        return placeTurn;
    }

    public void setPlaceTurn(int placeTurn) {
        this.placeTurn = placeTurn;
    }
   
}
