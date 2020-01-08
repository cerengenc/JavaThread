package deneme1;

import static deneme1.gui.frame;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

  class subServerThread implements Runnable {

    @Override
    public void run() {
        
        try {
            Thread.currentThread().setName("subServer"+ Thread.currentThread().getId());
            subServer server=new subServer(Deneme1.getMainServer());
            server.flag=true;
            Deneme1.addList(server);
            gui.createProgressBar(server.bound);
            server.producerAndConsumer(Thread.currentThread().getId());
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(subServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}


public class Deneme1 {
   
    static CopyOnWriteArrayList <subServer> sub_servers= new CopyOnWriteArrayList<subServer>();
    static mainServer mainServer=new mainServer();
    static boolean flag=false;
    static JFrame f;
    
    
    public static void main(String[] args) throws InterruptedException {
        
       mainServer.size=0;
       mainServer.subSize=0;
        gui arayuz=new gui();
       f=new JFrame("Kullanıcı Arayüzü");
       gui.function(f);
       f.setVisible(true);
       f.setSize(500,500);
       f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       mainServerProducer producer=new mainServerProducer(mainServer);       
       mainServerConsumer consumer=new mainServerConsumer(mainServer);
       
        Thread t1=new Thread(producer);
        Thread t2=new Thread(consumer);
        Thread t3=new Thread(new subServerThread());   
        Thread t4=new Thread(new subServerThread());
        
        Thread t5=new Thread(new Runnable(){
           Deneme1 temp=new Deneme1();
           @Override
           public void run() {
               
               try {
                   Thread.currentThread().setName("subServer-check");
                   temp.checkSubServer();
               } catch (InterruptedException ex) {
                   Logger.getLogger(Deneme1.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           
       });
        
        
        Thread t6 = new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("watch-MainServer");
                    Thread.sleep(1000);
                    watchMainServer();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Deneme1.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
        
         Thread t7 = new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    Thread.currentThread().setName("watch-subServers");
                    Thread.sleep(1000);
                    watchSubServers();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Deneme1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
     
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        
     
       
    }
    
    public static synchronized void addList(subServer newServer){
        sub_servers.add(newServer);
    }
    
    public static synchronized mainServer getMainServer(){
        return mainServer;
    }
    
    public static synchronized int ListSize(){
        return sub_servers.size();
    }
    
    /*public static synchronized CopyOnWriteArrayList<subServer> getList(){
        return sub_servers;
    }*/
    
    public void checkSubServer() throws InterruptedException{
        
       while(true){  
        
          for (int i = 0; i < Deneme1.sub_servers.size(); i++) {
            synchronized(mainServer){
              if(Deneme1.sub_servers.get(i) != null){
              if(Deneme1.sub_servers.get(i).size>=((Deneme1.sub_servers.get(i).bound/100)*70)){
                  System.out.println("Yeni subSserver oluşturulmalı!");
                  double newSize=(Deneme1.sub_servers.get(i).size);
                  subServer server=new subServer(Deneme1.getMainServer());
                  server.setSize(Deneme1.sub_servers.get(i).size);
                  System.out.println("Yeni üretilen server size:"+server.size);
                  Deneme1.sub_servers.get(i).setSize(Deneme1.sub_servers.get(i).size);
                  System.out.println("Bölünen server size:"+Deneme1.sub_servers.get(i).size);
                  server.flag=true;
                  createNewSubServer(server);
          } else if(Deneme1.sub_servers.size() > 2 && Deneme1.sub_servers.get(i).size==0){
                    System.out.println("Subserver silindi!");
                    gui.deleteProgressBar(i);
                    deleteServer(Deneme1.sub_servers.get(i),i);
                    
          }
              
          }
          }
       
      } 
       }
      
    }
    
       public synchronized static void deleteServer(subServer server,int i){
        server.deleteSubServer(i); 
    }
    
    public synchronized static void createNewSubServer(subServer server) throws InterruptedException{
        
        Thread t1=new Thread(() -> {
            
            try {
                Thread.currentThread().setName("subServer2" + " "+ Thread.currentThread().getId());
                Deneme1.addList(server);
                System.out.println("kac tane alt server var?"+Deneme1.sub_servers.size());
                server.flag=true;
                gui.createProgressBar(server.bound);
                server.producerAndConsumer(Thread.currentThread().getId());
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Deneme1.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        t1.start();
       
    }
    
    public static void watchMainServer(){
        
            while(true){
                
                double percent=(mainServer.size*100)/mainServer.bound;
                gui.setMainServer(percent,mainServer.size,mainServer.bound);
            
            }
       }

    
    public static void watchSubServers() throws InterruptedException{
        
        double percent=0;
      
        while(true){
            for (int i = 0; i < Deneme1.sub_servers.size(); i++) {
                   percent=(Deneme1.sub_servers.get(i).size*100)/sub_servers.get(i).bound;
                   gui.setSubServer(i,percent,sub_servers.get(i).size,sub_servers.get(i).bound);  
            }
            Thread.sleep(200);
         }
        }
    }
