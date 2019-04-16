package com.aura.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class TestValue01 extends Thread{
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
    public TestValue01(String name){
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
        int nn =80;
        String [] ce ={"5001","5002","5003","5004","5005","5006"};
        String [] arr = {"5001","5002","5003","5004","5005","5006"
                ,"5017","5018","5019","5010"} ;

            while (true){
                send(nn);
                int r = random.nextInt(10)+1;
                nn=nn+r;





            }
    }



    private void send(int nn ) {
        int aa =1;
        String date01 = null;//HH24时区，hh 是12时区
        //date01 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        long date02 = new Date().getTime();
        date01 = String.valueOf(date02);


        DecimalFormat mFormat = new DecimalFormat("000000000000");


        int r = random.nextInt(10);
        String log = null;





        log= threadName+ " " + date01 + " "+nn;//mFormat.format(aa);
        producer.send(new ProducerRecord<Integer, String>("kafka_my01",log));

        System.out.println(log);

        try {

            Thread.sleep(4000);
            // aa=aa+1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            TestValue01 test1 = new TestValue01("5001");

            test1.setTopic("kafka_my01");
            test1.start();
            TestValue01 test2 = new TestValue01("5002");
            test2.setTopic(args[0]);
            test2.start();
            TestValue01 test3 = new TestValue01("5003");
            test3.setTopic(args[0]);
            test3.start();
            TestValue01 test4 = new TestValue01("5004");
            test4.setTopic(args[0]);
            test4.start();
            TestValue01 test5 = new TestValue01("5005");
            test5.setTopic(args[0]);
            test5.start();
            //ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

            //test1.getTopic();


    }

}
