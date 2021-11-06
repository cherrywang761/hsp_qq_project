package com.example.qqclient.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;
import com.example.qqcommon.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @ClassName UserClientService
 * @Description 该类完成用户登录验证和用户注册等功能
 * @Author 86137
 * @Date 2021-11-02 22:44
 * @Version 1.0
 */
public class UserClientService {

    //因为我们可能在其他地方要使用User信息，因此做成成员属性就可以使用get方法获取信息
    private User u = new User();

    //因为socket在其他地方也可能使用，因此也做成属性
    private Socket socket;

    //根据userId和密码到服务器验证是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean flag = false;
        //创建User对象
        u.setUserId(userId);
        u.setPasswd(pwd);

        try {
            //连接到服务器，发送User对象进行验证
            socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 9999);

            //得到ObjectOutputStream对象（对象流传递信息）
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //写入User对象，并发送
            oos.writeObject(u);


            //读取从服务端回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCESS)) {
                //登录成功，创建一个和服务器端保持通信的线程 -> 创建一个类（ClientConnectServerThread)
                ClientConnectServerThread thread = new ClientConnectServerThread(socket);
                //启动客户端的线程
                thread.start();

                //为了后续客户端的拓展，我们将线程放入集合中管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId, thread);
                flag = true;
            } else {
                //如果登录失败，我们就不能启动和服务器通信的线程，但是socket创建了，所以我们要关闭它
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    //向服务器端请求在线用户列表
    public void onlineFriendList() {
        //发送一个Message，类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());

        //发送给服务端
        //应该得到当前线程的Socket 对应的对象流oos
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(
//                            ManageClientConnectServerThread.
//                                    getClientConnectServerThread(u.getUserId())
//                                    .getSocket()
//                                    .getOutputStream()
                            socket.getOutputStream()
                    );
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //编写方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        //一定要指明客户端
        message.setSender(u.getUserId());

        try {
            //发送message
//            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId() + " 退出了系统");
            //结束进程
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
