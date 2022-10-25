/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verFinal;

import java.util.logging.Level;
import java.util.logging.Logger;
import static verFinal.GasStation.checkPause;



/**
 * מחלקה המייצגת עמדת תדלוק בתחנת הדלק
 * @author idole
 */
public class Station
{
    
    private boolean isEmpty; //משתנה השומר את מצב התחחנה האם היא פנויה או לא

    /**
     * פעולה בונה היוצרת עמדת תדלוק פנויה
     */
    public Station() 
    {
        this.isEmpty = true;
    }

    public boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isfull) {
        this.isEmpty = isfull;
    }

    /**
     * פעולה המבצעת את תהליך התדלוק בעמדה
     * @param car מכונית בה מתבצע התדלוק
     * @param numOfStation מספר עמדה בה מתבצע התדלוק
     */
    public void refueling(Car car, int numOfStation)
    {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                
                for (int i = 0; i < 5; i++) 
                {
                   
                        //                    if(GasStation.pause == true)
//                    {
//                        synchronized(GasStation.gas)
//                        {
//                            try {
//                                
//                                GasStation.gas.wait();
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }
                    try {
                    GasStation.checkPause();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    synchronized(GasStation.gas)
                    {
                        if(GasStation.gas.getGus() == 0)
                        {
                            try {
                                GasStation.pnlCanvas.switchStaBol(1,false);
                                GasStation.pnlCanvas.switchStaBol(2,false);
                                if(GasStation.pnlCanvas.isGas == false)
                                    GasStation.pnlCanvas.setIsGas(true);
                                //fuelTanker();
                                
                                GasStation.gas.switchErrorGas();
                                System.out.println("station: " + numOfStation + ", Now: wait");
                                GasStation.gas.wait();
                                
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }}
                        if(numOfStation == 0)
                            GasStation.pnlCanvas.switchStaBol(1,true);
                        else
                            GasStation.pnlCanvas.switchStaBol(2,true);
                        synchronized(GasStation.gas)
                        {
                            GasStation.gas.setGus(GasStation.gas.getGus() - 2);
                            GasStation.jp.setValue(GasStation.gas.getGus());
                        }
                    }
                    
                    GasStation.pnlCanvas.repainte();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                GasStation.pnlCanvas.releaseStation(car,numOfStation);
                //stations.get(numOfStation).setEmpty(true);
                
                while(true)
                {
                    if(car.getCarX() == -50)
                    {
                        GasStation.pnlCanvas.getCars().remove(car);
                        break;
                    }
                    try {
                        GasStation.pnlCanvas.moveCar(car);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
    };
    
    
    /**
     * פעולה הקוראת למשאית התדלוק להגיע ולמלא את המיכל הראשי של התחנה
     */
    public static void fuelTanker() 
    {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                GasStation.pnlCanvas.switchSta();
                
                synchronized(GasStation.gas)
                {
                GasStation.pnlCanvas.isTanker(true);
                GasStation.btnPause.setEnabled(false);
                while(GasStation.gas.getGus() < 100)
                {
                   
                        //                    if(GasStation.pause == true)
//                    {
//                        synchronized(GasStation.gas)
//                        {
//                            try {
//                                GasStation.gas.wait();
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//}
                    try {
                    GasStation.checkPause();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Station.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    GasStation.gas.setGus(GasStation.gas.getGus() + 5);
                    GasStation.jp.setValue(GasStation.gas.getGus());
                    
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
//                sta1 = b1;
//                sta2  = b2;
                GasStation.gas.notifyAll();
                GasStation.btnPause.setEnabled(true);
                GasStation.pnlCanvas.isTanker(false);
                }
            }
        }).start();
        
        
    }
    
    
}
