package com.aura.linux;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {

  private ServerSocket serverSocket;
  private final Map socketMap=new HashMap();
  private final Map clientSizeMap=new HashMap();


  public Server(){
    try{
      serverSocket = new ServerSocket(5333);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void start(){


    try{
      Socket socket = null;

      //创建一条线程，向客户端发送消息
      new Thread(() -> {
        Scanner scanner = new Scanner(System.in);
        while(true){
          String demo=scanner.nextLine();
          String[] demoArray=demo.split("/:");
          String socketIp=demoArray[0];
          String info=demoArray[1];

          System.out.println("服务端输入的ip："+socketIp);
          System.out.println("服务端发送的消息:"+info);
          Socket socket12 =(Socket)socketMap.get(socketIp);
          try{
            OutputStream out = socket12.getOutputStream();
            //OutputStreamWriter osw = new OutputStreamWriter(out, true);
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            PrintWriter pw = new PrintWriter(osw, true);
            pw.println("你好！客户端！");
            //创建Scanner读取用户输入内容

            //scan.nextLine();
            pw.println(info);

          }catch(Exception e){
            e.printStackTrace();
          }
        }

      }).start();

      //serverSocket.accept() 方法会产生阻塞，直到某个Socket连接，返回请求连接的Socket
      while ((socket = serverSocket.accept())!=null){//每当有新的客户端连接到服务器，便创建一条线程，获取连接到服务端的客户端，并将之socket保存在Map集合中
        final  Socket socket1 = socket;
        socketMap.put(socket1.getInetAddress().getHostAddress(),socket1);
        System.out.println("===========================================");
        System.out.println("有新的连接，新连接的客户端Ip："+socket1.getInetAddress().getHostAddress());
        System.out.println("当前连接的客户端总数："+socketMap.size()) ;
        Integer clientsize=socketMap.size();
        clientSizeMap.put(socket1.getInetAddress().getHostAddress(),clientsize);

        new Thread(() -> {
          System.out.println("客户端"+clientsize+"已连接！");
          System.out.println("===========================================");
          try {
            Integer number =(Integer) clientSizeMap.get(socket1.getInetAddress().getHostAddress());
            InputStream in = socket1.getInputStream();
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            //不断读取客户端数据
            while(true){
              String content = br.readLine();
              System.out.println("客户端"+number+"：" + content);
              if (content.startsWith("sh ")) {
                executeShell(content.substring(3));
              }
            }
          }catch (Exception e){
          }

        }).start();
      }


    }catch(Exception e){
      e.printStackTrace();
    }
  }


  public static int executeShell(String shellCommand) throws IOException {
    int success = 0;
    StringBuffer stringBuffer = new StringBuffer();
    BufferedReader bufferedReader = null;
//格式化日期时间，记录日志时使用
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");

    try {
      stringBuffer.append(dateFormat.format(new Date())).append("准备执行Shell命令 ").append(shellCommand).append(" \r\n");

      Process pid = null;
      String[] cmd = {"/bin/sh", shellCommand};
      //执行Shell命令
      pid = Runtime.getRuntime().exec(cmd);
      if (pid != null) {
        stringBuffer.append("进程号：").append(pid.toString()).append("\r\n");
        //bufferedReader用于读取Shell的输出内容
        bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()), 1024);
        pid.waitFor();
      } else {
        stringBuffer.append("没有pid\r\n");
      }
      stringBuffer.append(dateFormat.format(new Date())).append("Shell命令执行完毕\r\n执行结果为：\r\n");
      String line = null;
      //读取Shell的输出内容，并添加到stringBuffer中
      while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line).append("\r\n");
      }
    } catch (Exception ioe) {
      stringBuffer.append("执行Shell命令时发生异常：\r\n").append(ioe.getMessage()).append("\r\n");
    } finally {
      if (bufferedReader != null) {
        OutputStreamWriter outputStreamWriter = null;
        try {
          bufferedReader.close();
          System.out.println(stringBuffer.toString());
//          //将Shell的执行情况输出到日志文件中
//          OutputStream outputStream = new FileOutputStream(executeShellLogFile);
//          outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
//          outputStreamWriter.write(stringBuffer.toString());
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          outputStreamWriter.close();
        }
      }
      success = 1;
    }
    return success;
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.start();
  }
}

