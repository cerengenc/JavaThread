package deneme1;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class subServer {
    
    double size=0;
    final int bound=250;
    mainServer anaveri;
    boolean flag=true;
   
    public subServer(mainServer gelenveri){
    this.anaveri=gelenveri;   
    this.size=0;
    this.flag=true;
   
}
    
 
    public void setSize(double size){
        this.size=size/2;
    }

  
        
   public void producerAndConsumer(long id) throws InterruptedException{
       
       System.out.println(id+" subServer çalışıyor.");
       subServer temp=this;
       
       Thread t1=new Thread(new Runnable(){

           @Override
           public void run() {
               try {
                   Thread.currentThread().setName(id + "subServer-producer");
                   temp.subServerProducer(id); 
               } catch (InterruptedException ex) {
                   Logger.getLogger(subServer.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           
       });
       
       Thread t2=new Thread(new Runnable(){

           @Override
           public void run() {
               try {
                   Thread.currentThread().setName(id +"subServer-consumer");
                   temp.subServerConsumer(id);
                  
               } catch (InterruptedException ex) {
                   Logger.getLogger(subServer.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           
       });
       
       t1.start(); //thread başladı
       t2.start();
       
      
       t1.join(); //t1 t2'den önce bitecek.
       t2.join();
       
   }

   
   public void subServerProducer(long id) throws InterruptedException{
       
         int value;
         
        while(this.flag==true){    
         synchronized(anaveri){
             
             Random value1=new Random(); //random sınıfı
             value=value1.nextInt(50);
          
           
             if(this.size+value>= this.bound){
                anaveri.size=(int) (anaveri.size+this.size+value-this.bound);
                this.size=this.bound;
                
             }
             
             if(this.size==this.bound){
                 System.out.println(id + " "+"producer random value:"+value);                 
                 System.out.println(id + " "+"buffer size"+(int)this.size);
                 System.out.println(id + " "+"subServer producer is waiting");
                 anaveri.wait();
                 
             }
             else{
            System.out.println(id + " "+"producer random value:"+value);
            if(anaveri.size-value<=0){
                
                this.size=this.size+anaveri.size;
                anaveri.size=0;
            }
            else{
                anaveri.size=anaveri.size-value;//random sayi üretip anaserverin yükünü azaltttı
                this.size=this.size+value;
            }
           
                System.out.println(id + " "+"buffer size:"+(int)this.size);
                System.out.println(id + " "+"mainServer buffer size:"+anaveri.size);
             }
             
               anaveri.notify();    
    }
         
            Thread.sleep(500); 
    } 
    }
   
   public void subServerConsumer(long id) throws InterruptedException{
       int value=0;
       while(this.flag==true){
        
         synchronized(this){
         value= new Random().nextInt(50);
           
           if(this.size-value<=0){
               this.size=0;
               System.out.println(id + " "+ "consumer random value:"+value);
               System.out.println(id + " "+ "buffer size:"+this.size); 
           } else {
               System.out.println(id+" "+"consumer random value:"+value);
               this.size=this.size-value;
               System.out.println(id + " "+"buffer size:"+(int)this.size);   
           }
           
           if(this.size==0 && Deneme1.sub_servers.size() == 2 ){
                System.out.println(id + " "+"consumer is waiting");
                while(this.size == 0){
                wait();    
           }
           }
   
            notify();      
      } 
        Thread.sleep(300);
       }
  
   }
   
     public  synchronized void deleteSubServer(int i){
       this.flag=false;
       Deneme1.sub_servers.remove(i);   
   }

    
}