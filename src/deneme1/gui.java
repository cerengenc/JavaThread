package deneme1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class gui extends javax.swing.JFrame{

    static JFrame frame=new JFrame();
    static JPanel panel=new JPanel();
    static JProgressBar pbarMainServer;
    static JProgressBar pbarSubServer1;
    static JProgressBar pbarSubServer2;
    static mainServer mainServer;
    static JLabel mainServerSize;
    static CopyOnWriteArrayList <JProgressBar> progressBars=new CopyOnWriteArrayList();
    static CopyOnWriteArrayList <JLabel> labels=new CopyOnWriteArrayList();
    static CopyOnWriteArrayList <JLabel> labels2=new CopyOnWriteArrayList();
     
    static Container mainContainer ;
     static JButton b;
    public gui(){
       
         setContentPane(panel);
        
    }
    public static void function(JFrame temp) {
          
        frame=temp;
        panel=new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        pbarMainServer=new JProgressBar();
        pbarMainServer.setStringPainted(true);
       
        
        JLabel t1=new JLabel("Main Server");
        mainServerSize=new JLabel("0/3000");
        //pbarMainServer.setValue(0);
        panel.add(pbarMainServer);
        panel.add(t1);
        panel.add(mainServerSize);
      
        frame.add(panel); 
        
        
    }
    
    
    
    public synchronized static void setMainServer(double percent,int size,int bound){
       pbarMainServer.setValue((int) percent); 
       mainServerSize.setText(size+" / "+bound);
       
       
    }
    
    public synchronized static void setSubServer(int index,double percent,double size,int bound){
        
        if(percent<50){
            progressBars.get(index).setForeground(Color.GREEN);
        } else if( percent>=50 && percent<60){
           progressBars.get(index).setForeground(Color.ORANGE);
        } else {
            progressBars.get(index).setForeground(Color.RED);
        }
       progressBars.get(index).setValue((int) percent);
       labels2.get(index).setText((int)size+ " / "+ bound);
    
    }
    
    public synchronized static void createProgressBar(int bound){
        
        JProgressBar newPBar=new JProgressBar();
        JLabel temp=new JLabel("SubServer "+Thread.currentThread().getId());
        JLabel temp2=new JLabel("0/"+bound);
        newPBar.setValue(0);
        newPBar.setStringPainted(true);
        panel.add(newPBar);
        panel.add(temp);
        panel.add(temp2);
        panel.revalidate();
        panel.repaint();
        progressBars.add(newPBar);
        labels.add(temp);
        labels2.add(temp2);
        
        
        System.out.println("progressBar sayisi:"+progressBars.size()); 
    }
    
    public synchronized static void deleteProgressBar(int index){
        
        panel.remove(progressBars.get(index));
        panel.remove(labels.get(index));
        panel.remove(labels2.get(index));
        progressBars.remove(progressBars.get(index));
        labels.remove(index);
        labels2.remove(index);
        panel.revalidate();
        panel.repaint();
       
    }

    
    
}
