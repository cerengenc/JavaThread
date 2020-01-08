package deneme1;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainServerProducer implements Runnable {
    
    mainServer mainServer ;
    int bound=500 ;
    
    public mainServerProducer(mainServer temp){
    this.mainServer=temp;
}
    
  
    @Override
    public void run() {
          try {
              Thread.currentThread().setName("mainServerProducer");
              producer();
          } catch (InterruptedException ex) {
              Logger.getLogger(mainServerProducer.class.getName()).log(Level.SEVERE, null, ex);
          }
    }
    
    
    public void producer() throws InterruptedException{
        int value;
        while(true){
         synchronized(mainServer){
             
           Random value1=new Random(); //random sınıfı
           value=value1.nextInt(100)+1;
          
           
             if(mainServer.size+value>= this.bound){
                mainServer.size=this.bound; 
                 
             }
             if(mainServer.size==this.bound){
                 System.out.println("mainServer producer random value :"+value);
            System.out.println("mainServer buffer size:"+ mainServer.size);
                 System.out.println("mainServer producer is waiting");
                 mainServer.wait();
                 
             }
             else{
            System.out.println("mainServer producer random value:"+value);
            mainServer.size=mainServer.size+value; 
            System.out.println("mainServer buffer size:"+ mainServer.size);
             }
               mainServer.notify();               
    }
         
            Thread.sleep(500); 
    }
    }
}