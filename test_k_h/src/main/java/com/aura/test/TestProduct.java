package com.aura.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class TestProduct extends Thread{
    private static final Random random = new Random();
   private Producer<Integer,String> producer;
   private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    //private static final String[] sbcs = new String[]{"A","B","C","D","E"};
    public TestProduct(){

        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "172.16.22.236:9092,172.16.22.242:9092,172.16.22.244:9092");//,
        //kafkaProps.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");

        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<Integer, String>(kafkaProps);

        //producer = new KafkaProducer<Integer, String>(properties);
    }

public void run(){
    int aa =1;
    int nn =0;
    String date01 = null;//HH24时区，hh 是12时区
    int ce1   =0;
    int ce2=1;
    int ce3=2;
    int ce4 =4;
    DecimalFormat mFormat = new DecimalFormat("000000000000");
    /*String [] ce ={"8477900020","8477900030","8477900040","8477900050","8477900060","8477900070"};
    String [] arr = {"8477900010","8477900020","8477900030","8477900040","8477900050"
            ,"8477900060","8477900070","8477900080","8477900090","8477900000"} ;*/
    String [] ce ={"5001","5002","5003","5004","5005","5006"};
    String [] arr = {"5001","5002","5003","5004","5005","5006"
            ,"5007","5018","5019","5011"} ;
        while (true){
           date01 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            /*long date02 = new Date().getTime();
            date01 = String.valueOf(date02);*/

            //String shebei =  sbcs[random.nextInt(sbcs.length)];
            nn = random.nextInt(100);
            if(nn >80 || nn < 30){
                nn = random.nextInt(100);
            }
            int r = random.nextInt(10);
            String log = null;




               for(int j=0;j<6;j++){

                   if(ce[j].equals(arr[r])){
                       r=random.nextInt(9);
                       j=0;
                   }

                        }

            log= ce[0]+ " " + date01 + " "+nn+" "+aa;//mFormat.format(aa);
            for(int i=1;i<6;i++){
                ce[i-1]=ce[i];
            }

            ce[5]=arr[r];




            aa+=1;
            producer.send(new ProducerRecord<Integer, String>(topic,log));
          /*  for(String ss : ce){
                System.out.print(ss+" ");
            }*/
            //System.out.println();
            System.out.println(log);

            try {

                Thread.sleep(500);
               // aa=aa+1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
}
    public static void main(String[] args) {
        TestProduct test = new TestProduct();
        test.setTopic(args[0]);
        test.start();
        //ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        test.getTopic();

    }

}
