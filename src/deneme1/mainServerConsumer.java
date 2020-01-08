package deneme1;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainServerConsumer implements Runnable{
    
    mainServer mainServer ;
    int bound=500;
    
  public mainServerConsumer(mainServer temp){
     this.mainServer=temp;
  }
  
   @Override
    
    public void run() {
        try {
            Thread.currentThread().setName("mainServerConsumer");
            consumer();
        } catch (InterruptedException ex) {
            Logger.getLogger(mainServerConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
    
     public void consumer() throws InterruptedException{
        int value;
        while(true){
             synchronized(mainServer){
              Random value1=new Random(); //random sınıfı
              value=value1.nextInt(50)+1;
             
             if(mainServer.size-value<=0){
                 mainServer.size=0;
             }
                 if(mainServer.size==0){
                     System.out.println("mainServer consumer random value:"+value);
                     
                      System.out.println("mainServer buffer size:"+mainServer.size);
                      System.out.println("mainServer consumer is waiting");
                      mainServer.wait();
                 }else{
                 System.out.println("mainServer consumer random value:"+value);
                 mainServer.size=mainServer.size-value;
                 System.out.println("mainServer buffer size:"+mainServer.size);
                                    

                 }   
                 mainServer.notify();
             }   
            Thread.sleep(200); 
        }
    }
}

