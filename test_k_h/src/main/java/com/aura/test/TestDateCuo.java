package com.aura.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class TestDateCuo extends Thread{
    private static final Random random = new Random();
   private Producer<Integer,String> producer;
   private String topic;
    private String threadName;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    //private static final String[] sbcs = new String[]{"A","B","C","D","E"};
    public TestDateCuo(String name){
        threadName = name ;
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "172.16.22.236:9092,172.16.22.242:9092,172.16.22.244:9092");//,
        //kafkaProps.put("bootstrap.servers", "hadoop01:9092,hadoop02:9092,hadoop03:9092");

        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<Integer, String>(kafkaProps);

        //producer = new KafkaProducer<Integer, String>(properties);
    }

    public void run(){
        int nn =0;
        String [] ce ={"5001","5002","5003","5004","5005","5006"};
        String [] arr = {"5001","5002","5003","5004","5005","5006"
                ,"5017","5018","5019","5010"} ;

            while (true){


                //String shebei =  sbcs[random.nextInt(sbcs.length)];
                nn = random.nextInt(80);
                int x =random.nextInt(10);
                if(nn >70 ){
                    for(int k=0 ;k<x;k++){
                        int kk = random.nextInt(10)+70;
                        send(kk,ce,arr);
                    }
                }else if(nn <=70 && nn > 40 ){
                    for(int k=0 ;k<x;k++){
                        int kk = random.nextInt(30)+40;
                        send(kk,ce,arr);
                    }
                }else{
                    for(int k=0 ;k<=x;k++){
                        int kk = random.nextInt(40);
                        send(kk,ce,arr);
                    }
                }



            }
    }



    private void send(int nn,String[] ce ,String[] arr ) {
        int aa =1;
        String date01 = null;//HH24时区，hh 是12时区
        //date01 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        long date02 = new Date().getTime();
        date01 = String.valueOf(date02);


        DecimalFormat mFormat = new DecimalFormat("000000000000");


        int r = random.nextInt(10);
        String log = null;




        /*for(int j=0;j<6;j++){

            if(ce[j].equals(arr[r])){
                r=random.nextInt(9);
                j=0;
            }

        }


        for(int i=1;i<6;i++){
            ce[i-1]=ce[i];
        }

        ce[5]=arr[r];




        aa+=1;*/
        log= threadName+ " " + date01 + " "+nn;//mFormat.format(aa);
        producer.send(new ProducerRecord<Integer, String>("kafka_my01",log));

        System.out.println(log);

        try {

            Thread.sleep(5000);
            // aa=aa+1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            TestDateCuo test1 = new TestDateCuo("5001");

            test1.setTopic(args[0]);
            test1.start();
            TestDateCuo test2 = new TestDateCuo("5002");
            test2.setTopic(args[0]);
            test2.start();
            TestDateCuo test3 = new TestDateCuo("5003");
            test3.setTopic(args[0]);
            test3.start();
            TestDateCuo test4 = new TestDateCuo("5004");
            test4.setTopic(args[0]);
            test4.start();
            TestDateCuo test5 = new TestDateCuo("5005");
            test5.setTopic(args[0]);
            test5.start();
            //ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

            //test1.getTopic();


    }

}
