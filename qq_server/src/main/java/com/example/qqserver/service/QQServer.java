package com.example.qqserver.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;
import com.example.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @ClassName QQServer
 * @Description qq的服务端，监听9999端口，等待客户端的连接并保持通信
 * @Author 86137
 * @Date 2021-11-02 23:21
 * @Version 1.0
 */
public class QQServer {

    //因为serverSocket会经常用到，所以写成一个属性
    private ServerSocket serverSocket = null;

    //创建一个集合用户存放用户，模仿数据库功能
    private static HashMap<String, User> validUsers = new HashMap<>();

    //在静态代码块初始化validUsers
    static{
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    //验证用户是否有效的方法
    private boolean checkUser(String userId, String pwd){
        User user = validUsers.get(userId);
        if(user == null){
            return false;
        }
        if(!user.getPasswd().equals(pwd)){
            return false;
        }
        return true;
    }

    public QQServer() {
        try {
            System.out.println("服务端在9999端口监听....");
            //启用推送新闻的线程
            new Thread(new SendNewsToAllService()).start();
            serverSocket = new ServerSocket(9999);
            //服务端要一直监听
            while (true) {
                //如果没有客户端连接，就会阻塞在这里
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //读取客户端发送的User对象
                User user = (User) ois.readObject();
                //创建一个Message对象，准备回复客户端
                Message message = new Message();
                //用于回复客户端的输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //验证信息
                if (checkUser(user.getUserId(), user.getPasswd())) {
                    //登录成功，组装Message
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCESS);
                    //...

                    //将message对象回复给客户端
                    oos.writeObject(message);
                    //创建一条线程和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread thread = new ServerConnectClientThread(socket, user.getUserId());
                    thread.start();

                    //线程放入集合统一管理
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), thread);
                } else {
                    //登录失败
                    System.out.println("用户：" + user.getUserId() + "验证失败！");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);

                    //关闭socket
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
