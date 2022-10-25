/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verFinal;

import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * מחלקה המגדירה את כמות הדלק
 * @author idole
 */
public class Gas {
    
    private int gus; //משתנה מסוג מספר שלם השומר את כמות הדלק באחוזים

    /**
     * פעולה בונה היוצרת מיכל דלק
     * @param gus כמות הדלק באחוזים
     */
    public Gas(int gus) {
        this.gus = gus;
    }

    public int getGus() {
        return gus;
    }

    public void setGus(int gus) {
        this.gus = gus;
    }
    
    
    /**
     * פעולה המבצעת הצגה על המסך במקרה שנגמר הדלק
     */
    public void switchErrorGas()
    {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                
                while(GasStation.gas.getGus() == 0)
                {
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(GasStation.pnlCanvas.isGas == true)
                        GasStation.pnlCanvas.setIsGas(false);
                    else
                        GasStation.pnlCanvas.setIsGas(true);
                    
                    
                }
                GasStation.pnlCanvas.setIsGas(false);
                
            }
        }).start();
    }
    
}
