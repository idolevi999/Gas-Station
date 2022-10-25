/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package verFinal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author idolevi
 */
public class GasStation 
{
    public static MyPanel pnlCanvas; //עצם לשמירת פאנל הסימולציה
    public static JFrame win; //עצם לשמירת החלון
    public static JButton btnPause, enterCar, fuelTanker; //כפתורים לשימוש מלא של הסימולציה
    public static ArrayList<Car> listTurns; //מערך דינמי השומר את המכוניות הרוצות לתדלק
    public static boolean pause; //משתנה בוליאני המעודכן במצב העצירה
    public static String pause_text; //משתנה המולבד על כפתור העצירה בהתאם לשימוש
    public static JProgressBar jp; //משתנה השומר את העצם המציג כמות האחוזים של הדלק בתצוגה
    public static int counter; //משתנה הסופר את כמות המכוניות הנכנסות
    public static Gas gas;
    public static final Object lock  = new Object();
    
    /**
     * פעולה המתאחלת את הסימולציה על יד בניית חלון מתאים ותהליך ראשוני של הסימולציה
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException 
    {
        pause = false;
        pause_text = "Pause";
        listTurns = new ArrayList<>();
        counter = 1;
        gas = new Gas(30);
        
        win = new JFrame();
        win.setLayout(new BorderLayout());
        win.setTitle("Gas Station");
        win.setSize(800, 600);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setLocationRelativeTo(null);

        // האזנה לאירוע שינוי גודל החלון
        win.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt)
            {
                System.out.println("********** componentResized **********");
            }
        });
        
        // כפתורי בקרה ושליטה
        enterCar = new JButton("Enter a Car");
        fuelTanker = new JButton("Fuel Tanker");
        btnPause = new JButton(pause_text);
        
        JPanel pnlButtons = new JPanel(new GridLayout(1, 4, 10, 20));
        pnlButtons.add(enterCar);
        pnlButtons.add(fuelTanker);
        pnlButtons.add(btnPause);

        jp = new JProgressBar(0, 100);
        jp.setSize(25, 50);
        jp.setForeground(Color.GREEN);
        jp.setStringPainted(true);
         
        // משטח ציור
        pnlCanvas = new MyPanel();
        pnlCanvas.setBackground(Color.WHITE);
        jp.setValue(gas.getGus());
        pnlCanvas.add(jp);
        
        win.add(pnlButtons, BorderLayout.NORTH);
        win.add(pnlCanvas, BorderLayout.CENTER);
        win.setVisible(true);
        win.setResizable(false);

        pnlCanvas.setFocusable(true);
        pnlCanvas.grabFocus();
        
        enterCar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                enterCarti();
                
            }
        });
        
        fuelTanker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                Station.fuelTanker();
            }
        });
        
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                if(pause == true)
                    pause = false;
                else
                    pause = true;
                
                try {
                    setPause(pause);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });

        while(true)
        {
            Random r = new Random();
            int num1 = r.nextInt(2000);
            num1 += 2300;
            Thread.sleep(num1);
            int num = r.nextInt(3);
            Car car  = new Car(700, 430, num,0);
            //System.out.println("car");
            pnlCanvas.enterCar(car);
            new Thread(new Runnable() {
                @Override
                public void run() 
                {
                    while(true){
                        if(car.getCarX() == -50)
                        {
                            pnlCanvas.getCars().remove(car);
                            break;
                        }
                    try{
                        Thread.sleep(100);
                        pnlCanvas.moveCar(car);}
                    catch(InterruptedException ex){}}}
            }).start();
        }
    };
    
    /**
     * פעולה המבצעת את תהליך ההקפאה ותהליך היציאה ממצב עצירה
     * @param pause משתנה בולאיאני המייצג את מצב העצירה
     * @throws InterruptedException 
     */
    public static void setPause(boolean pause) throws InterruptedException
    {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                boolean b1 = pnlCanvas.sta1;
                boolean b2 = pnlCanvas.sta2;
                if(pause)
                {
                    pause_text = "Continue";
                    btnPause.setText(pause_text);
                    enterCar.setEnabled(false);
                    fuelTanker.setEnabled(false);
                    pnlCanvas.sta1 = false;
                    pnlCanvas.sta2 = false;
                    pnlCanvas.repainte();
                }
                else
                {
                    pause_text = "Pause";
                    btnPause.setText(pause_text);
                    enterCar.setEnabled(true);
                    fuelTanker.setEnabled(true);
//                    synchronized(pnlCanvas.getCars()) {pnlCanvas.getCars().notifyAll();}
                    if(gas.getGus() != 0)
                        synchronized(gas) {gas.notifyAll();}
                    synchronized(lock){lock.notifyAll();}
                    
                }
            }
        }).start();
        
    }
    
    public static void checkPause() throws InterruptedException
    {
        synchronized(lock)
        {
            while(pause)
            {
                lock.wait();
            }
        }
    }
    
    /**
     * פעולה להכנסת מכונית חדשה לתהליך התדלוק
     */
    public static void enterCarti() 
    {
        if(checkIfIsHere(3) != null)
        {
            JOptionPane.showConfirmDialog(win ,"No longer place for another car \n Try again later" ,"", JOptionPane.CLOSED_OPTION);
        }
        else
        {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {

               Random r = new Random();
               int num = r.nextInt(3);
               Car car  = new Car(750, 145, num,0);
               //counter++;
               pnlCanvas.enterCar(car);
               pnlCanvas.plusTurn();
               listTurns.add(car);
             
               if(checkIfIsHere(1) == null)
               {
                       car.setPlaceTurn(1);
                   
                   try {
                       car.setFarGo(500);
                       tryGo(car, 1);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);}
                   return;
               }
               else if(checkIfIsHere(2) == null)
               {
                       car.setPlaceTurn(2);
                   
                   try {
                       car.setFarGo(590);
                       tryGo(car, 2);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);}
                   return;
               }
               else
               {
                   if(checkIfIsHere(3) != null)
                       System.out.println("ERORR");
                   
                    car.setPlaceTurn(3);
                   
                   try {
                       car.setFarGo(670);
                       tryGo(car, 3);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);}
               }
            }
            
        }).start();
        }
    }
    
    /**
     * פעולה המבצעת את תהליך התדלוק בעמדה
     * @param car עצם המייצג את המכונית
     * @param numOfStation מספר המייצג את מספר העמדה בה רוצים לתדלק
     */
    public static void refuelingCar(Car car, int numOfStation)
    {
        new Thread(new Runnable() {
            @Override
            public void run() 
            {
                pnlCanvas.setStations(numOfStation,false);
                if(numOfStation == 0)
                   car.setCarY(90);    
                else
                   car.setCarY(260);

                while(true)
                {
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        pnlCanvas.moveCar(car);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if(car.getCarX() == 70)
                    {
                        pnlCanvas.stopCar(car);
//                        if(numOfStation == 0)
//                        {
                        pnlCanvas.getStations().get(numOfStation).refueling(car, numOfStation);
//                        }
//                        pnlCanvas.stations.get(numOfStation).refueling(car, numOfStation, pnlCanvas.getGas());
                        break;
                    }
                }
            }
        }).start();
        
    }
    
    /**
     * פעולה הבודקת האם אחת העמדות פנויות ואם כן פועלת בהתאם
     * @param car רכב
     * @return מוחזר אמת או שקר לגבי האם העמדה פנויה
     * @throws InterruptedException 
     */
    public static boolean checkIsClear(Car car) throws InterruptedException
    {
        
        if(pnlCanvas.getStations().get(0).getEmpty() == true)
        {
            car.setPlaceTurn(-1);
            refuelingCar(car, 0);
            pnlCanvas.setTurnMinus();
            return true;
        }

        if(pnlCanvas.getStations().get(1).getEmpty() == true)
        {
            car.setPlaceTurn(-1);
            refuelingCar(car, 1);
            pnlCanvas.setTurnMinus();
            return true;
        }
        
        return false;
    }
    
    /**
     * פעולה המקדמת את המכונית בהתאם למיקומה בתור
     * @param car מכונית
     * @param number מספר המיקום בתור
     * @throws InterruptedException 
     */
    public static void tryGo(Car car, int number) throws InterruptedException
    {
       new Thread(new Runnable() {
           @Override
           public void run() 
           {
                if (number == 1) 
                 {
                     while (true) 
                     {
                         
                         try {
                             pnlCanvas.moveCar(car);
                             Thread.sleep(100);
                         } catch (InterruptedException ex) {
                             Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         if (car.getCarX() == car.getFarGo()) {
                             pnlCanvas.stopCar(car);
                             try {
                                 checkIfCan(car);
                             } catch (InterruptedException ex) {
                                 Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                             }
                             return;
                         }
                     }
                 }

                 if (number == 2) 
                 {
                     while (true) 
                     {
                         System.out.print("");
                         
                         try {
                             pnlCanvas.moveCar(car);
                             Thread.sleep(100);
                         } catch (InterruptedException ex) {
                             Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         if (car.getCarX() == car.getFarGo()) {
                             pnlCanvas.stopCar(car);
                             return;
                         }
                     }
                 }

                 if (number == 3) 
                 {
                     while (true) 
                     {
                         System.out.print("");
                       
                         try {
                             pnlCanvas.moveCar(car);
                             Thread.sleep(100);
                         } catch (InterruptedException ex) {
                             Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         if (car.getCarX() == car.getFarGo()) {
                             pnlCanvas.stopCar(car);
                             return;
                         }
                     }
                 }
           }
       }).start();
        
    }
    
    /**
     * פעולה האחראית לקידום המכוניות בהתאם לתור הממתינים
     * @throws InterruptedException 
     */
    public static void forwardTurns() throws InterruptedException
    {
        if(checkIfIsHere(2) != null)
        {
            Car car = checkIfIsHere(2);
            car.setPlaceTurn(1);
            car.setFarGo(500);
            tryGo(car, 1);
        }
        
        if(checkIfIsHere(3) != null)
        {
            Car car = checkIfIsHere(3);
            car.setPlaceTurn(2);
            car.setFarGo(590);
            tryGo(car, 2);
        }
    }

    /**
     * פעולה האחראית לביצוע בדיקה האם אחת מהעמדות פנויות לתדלוק
     * @param car מכונית בה מתבצעת הבדיקה
     * @throws InterruptedException 
     */
    public static void checkIfCan(Car car) throws InterruptedException 
    {
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                boolean is = false;
                while(is == false)
                {
                    try {
                        is = checkIsClear(car);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.print("");
                }
                try {
                    forwardTurns();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GasStation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
        
    }
    
    /**
     * פעולה הבודקת האם קיים רכב במקום מסויים בתור
     * @param number מספר המיקום שנכנס לבדיקה
     * @return מוחזר רכב הנמצא במיקום המבוקש או ערך זבל
     */
    public static Car checkIfIsHere(int number)
    {
        for (int i = 0; i < listTurns.size(); i++) 
        {
            if(listTurns.get(i).getPlaceTurn() == number)
                return listTurns.get(i);
        }
        
        return null;
    }
    
}