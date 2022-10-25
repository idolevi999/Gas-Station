package verFinal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Guard;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static ver2.GasStation.pnlCanvas;

/**
 * Document : MyPanel Created on : 03/05/2017, 13:40:51 Author : ilan
 */
public class MyPanel extends JPanel
{
    private ImageIcon station,road,car,stop,car1,car2,tanker; //משתנים האחראים לשמירת האייקונים המוצגים בפאנל
    private Graphics2D g2d; //משתנה האחראי לגפריקת הסימולציה
    private ArrayList<Car> cars,turns; //מערך דינמי האחראי לשמירת אובייקטים מסויימים
    private ArrayList<Station> stations; //מערך דינאמי השומר את עמדות התדלוק
    //public Gas gas; //משתנה המייצג את כמות הדלק באחוזים
    private int turn; //משתנה השומר את כמות הממתינים בתור ברגע נתון
    public boolean isTanker, sta1, sta2, isGas; //משתנים בוליאנים האחראים לשמירת מצבים מסויימים
    
    /**
     * פעולה המאתחלת את לוח הפאנל
     */
    public MyPanel()
    {
       isTanker = false;
       sta1 = false;
       sta2 = false;
       isGas = false;
       turn = 0;
       loadImages();
       //gas = new Gas(30);
       Station sta1 = new Station();
       Station sta2 = new Station();
       cars = new ArrayList<Car>();
       stations = new ArrayList<Station>();
       stations.add(sta1);
       stations.add(sta2);
    }

    /**
     * פעולה המציירת על הפאנל גרפיקה מסויימת
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.

        grabFocus();
        // מאפשר גרפיקה חדה ברזולוציה גבוהה
        g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ציור הוראות
        g2d.setFont(new Font ("Courier New", 1, 20));
        g2d.setColor(Color.BLACK);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(4));  // קביעת עובי הקווים
        g2d.drawRect(3, 3, 560, 63);
        g2d.setColor(Color.YELLOW);
        g2d.drawImage(road.getImage(), 0, 400, null);
        for (int i = 0; i < cars.size(); i++) {
            switch(cars.get(i).getNumCar())
            {
                case 0:
                    g2d.drawImage(car.getImage(), cars.get(i).getCarX(), cars.get(i).getCarY(), null);
                    break;
                case 1:
                    g2d.drawImage(car1.getImage(), cars.get(i).getCarX(), cars.get(i).getCarY(), null);
                    break;
                case 2:
                    g2d.drawImage(car2.getImage(), cars.get(i).getCarX(), cars.get(i).getCarY(), null);
                    break;
            }
        }
        for (int i = 0; i < stations.size(); i++) 
        {
            if(i == 0)
                g2d.drawImage(station.getImage(), 70, 30, null);
            else
                g2d.drawImage(station.getImage(), 71 ,200, null);
        }
        g2d.setColor(Color.BLACK);
        g2d.drawImage(stop.getImage(), 420, 145, null);
       
        g2d.drawString("waiting : "+ turn,500,110);
        
        g2d.setColor(Color.blue);
        g2d.drawString("Main Gus - " + GasStation.gas.getGus() + "%", 320, 50);
        
        if(isTanker)
        {
            g2d.drawImage(tanker.getImage(), 410, 75, null);
            g2d.drawString("Fuel Tanker Now", 320, 75);
            g2d.drawString("station: " + 1 + ", Now: wait", 50, 350);
            g2d.drawString("station: " + 2 + ", Now: wait", 50, 370);
        }
        
        if(sta1)
            g2d.drawString("Working...", 64, 25);
        
        if(sta2)
            g2d.drawString("Working...", 64, 195);
        
        if(isGas)
        {
            g2d.drawString("No Gas!!", 330, 70);
        }
        
    }

    /**
     * פעולה הטוענת את כל האייקונים
     */
    private void loadImages()
    {
        station = new ImageIcon(MyPanel.class.getResource("/assets/dalkan.png"));
        station = new ImageIcon(station.getImage().getScaledInstance(60, 75, Image.SCALE_SMOOTH));        
        
        road = new ImageIcon(MyPanel.class.getResource("/assets/road.PNG"));
        road = new ImageIcon(road.getImage().getScaledInstance(800, 100, Image.SCALE_SMOOTH));
      
        car = new ImageIcon(MyPanel.class.getResource("/assets/car.png"));
        car = new ImageIcon(car.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH));
        
        car1 = new ImageIcon(MyPanel.class.getResource("/assets/car1.png"));
        car1 = new ImageIcon(car1.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH));
        
        car2 = new ImageIcon(MyPanel.class.getResource("/assets/car2.png"));
        car2 = new ImageIcon(car2.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH));
        
        tanker = new ImageIcon(MyPanel.class.getResource("/assets/tanker1.png"));
        tanker = new ImageIcon(tanker.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH));
        
        stop = new ImageIcon(MyPanel.class.getResource("/assets/stope.png"));
        stop = new ImageIcon(stop.getImage().getScaledInstance(70, 80, Image.SCALE_SMOOTH));
    }
 
   
    /**
     * פעולה המכניסה מכונית למסך
     * @param c מכונית
     */
    public void enterCar(Car c)
    {
        cars.add(c);
        repaint();
    }
    
    /**
     * פעולה המזיזה את הרכב בציר האורך
     * @param c רכב 
     * @param num מספר המייצג את כמות הפיקסלים להזזה
     * @throws InterruptedException 
     */
    public void moveCarY(Car c , int num) throws InterruptedException
    {
        c.setCarX(c.getCarY()+ num);
        repaint();
    }
    
    /**
     * פעולה המזיזה את המוכנית בציר הרוחב
     * @param c מכונית
     * @throws InterruptedException 
     */
    public void moveCar(Car c) throws InterruptedException
    {
        GasStation.checkPause();
        c.setCarX(c.getCarX() - 5);
        this.repaint();
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn() {
        this.turn++;
    }
    
    public void setTurnMinus() {
        this.turn--;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public ArrayList<Car> getTurns() {
        return turns;
    }

    public void setTurns(ArrayList<Car> turns) {
        this.turns = turns;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(int num,boolean b) 
    {
        this.stations.get(num).setEmpty(b);
    }

    public void stopCar(Car car) 
    {
        repaint();
    }

    
    
    /**
     * פעולה המשחררת את העמדה אחרי סיום תדלוק מוצלח של מכונית
     * @param car מכונית
     * @param numOfStation מספר עמדה 
     */
    public void releaseStation(Car car, int numOfStation) 
    {
        stations.get(numOfStation).setEmpty(true);
        if(numOfStation == 0)
            sta1 = false;
        else
            sta2 = false;
    }

    public void plusTurn() 
    {
        turn++;
    }

    public void repainte() 
    {
        repaint();
    }

    public void switchSta() 
    {
//        boolean b1 = sta1;
//        boolean b2  = sta2;
        sta1 = false;
        sta2 = false;
    }

    public void isTanker(boolean b) 
    {
        isTanker = b;
    }

    public void switchStaBol(int i, boolean b) 
    {
        if(i == 1)
            sta1 = b;
        if(i == 2)
            sta2 = b;
    }

    public boolean isIsGas() {
        return isGas;
    }

    public void setIsGas(boolean isGas) {
        this.isGas = isGas;
    }
    
    
}
