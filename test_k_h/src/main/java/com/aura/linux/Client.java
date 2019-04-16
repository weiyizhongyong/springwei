package com.aura.linux;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private  Socket socket;

  public Client(){
    try {
      socket = new Socket("127.0.0.1", 5333);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start(){
    final  Socket socket1=this.socket;
    //创建一条线程，监听获取服务端发送的消息
    new Thread(() -> {
      try {
        InputStream in = socket1.getInputStream();
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        //System.out.println("客户端说：" + br.readLine());
        //不断读取客户端数据
        while(true){
          System.out.println("服务端说：" + br.readLine());
        }
      }catch (Exception e){
      }
    }).start();

    try{//向服务端发送消息
      OutputStream out = socket.getOutputStream();
      //OutputStreamWriter osw = new OutputStreamWriter(out, true);
      OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
      PrintWriter pw = new PrintWriter(osw, true);
      pw.println("你好！服务器！");
      //创建Scanner读取用户输入内容
      Scanner scanner = new Scanner(System.in);
      while(true){
        //scan.nextLine();
        System.out.println("尊敬的用户，请您输入：");
        pw.println(scanner.nextLine());
      }
    }catch(Exception e){
      e.printStackTrace();
    }finally{
      if(socket != null){
        try{
          socket.close();
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    }

  }

  public static void main(String[] args) {
    Client client = new Client();
    client.start();
  }
}
