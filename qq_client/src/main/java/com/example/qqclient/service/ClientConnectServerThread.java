package com.example.qqclient.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @ClassName ClientConnectServerThread
 * @Description TODO
 * @Author 86137
 * @Date 2021-11-02 22:57
 * @Version 1.0
 */
public class ClientConnectServerThread extends Thread {

    //该线程必须要持有一个Socket
    private Socket socket;

    //构造器可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    //为了更方便获取socket，写一个get方法
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器保持通信，所以我们使用while循环
        while (true) {
            System.out.println("客户端线程，等待读取从服务端发送过来的数据");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象过来，线程会阻塞在这里
                Message message = (Message) ois.readObject();

                //如果读取到的是服务端返回的在线用户列表
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线列表信息，并显示
                    String[] onlineUsers
                            = message.getContent().split(" ");
                    System.out.println("\n==========在线用户列表=========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户： " + onlineUsers[i]);
                    }
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //把服务端转发过来的消息展示下
                    System.out.println("\n" + message.getSender() + "对你说： " + message.getContent());
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //把服务端转发过来的消息展示下
                    System.out.println("\n" + message.getSender() + "对全体成员说： " + message.getContent());
                }
                //把服务端转发过来的文件进行下载
                else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n" + message.getSender() + " 给 " + message.getReceiver() +
                            " 发文件 " + message.getSrc() + " 到我的电脑目录 " + message.getDest());

                    //取出message的文件字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功..");
                }
                else{
                    System.out.println("是其他类型的message，暂时不处理...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
