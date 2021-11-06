package com.example.qqserver.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @ClassName ServerConnectClientThread
 * @Description 服务端用于和客户端保持连接的线程
 * @Author 86137
 * @Date 2021-11-02 23:42
 * @Version 1.0
 */
public class ServerConnectClientThread extends Thread {
    //必须持有socket
    private Socket socket;

    //连接到服务端的userId
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("服务端和客户端保持通信" + userId + "读取数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();

                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端需要在线用户列表的请求
                    System.out.println(message.getSender() + " 要在线用户列表");
                    String onlineUser = ManageServerConnectClientThread.getOnlineUser();

                    //要返回给客户端，构建一个Message对象
                    Message retMes = new Message();
                    retMes.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    retMes.setContent(onlineUser);
                    retMes.setReceiver(message.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(retMes);
                }
                //私聊消息处理
                else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){
                    //转发给接收方的socket
                    String getterId = message.getReceiver();
                    ServerConnectClientThread thread = ManageServerConnectClientThread.getServerConnectClientThread(getterId);
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                    //如果客户不在线，可以保存在数据库，就可以实现离线留言
                }
                //群发消息处理
                else if(message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //需要遍历 管理线程的集合，得到所有的线程的socket， 然后把message转发即可
                    HashMap<String, ServerConnectClientThread> map = ManageServerConnectClientThread.getMap();
                    Iterator<String> iterator = map.keySet().iterator();
                    while(iterator.hasNext()){
                        //取出在线用户id
                        String onlineUserId = iterator.next();

                        //排除群发消息的本用户
                        if(!onlineUserId.equals(message.getSender())){
                            //开始转发
                            ObjectOutputStream oos = new ObjectOutputStream(map.get(onlineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }
                //转发文件
                else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    ServerConnectClientThread thread = ManageServerConnectClientThread.getServerConnectClientThread(message.getReceiver());

                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    //转发
                    oos.writeObject(message);
                }

                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    //客户端要退出了
                    System.out.println(message.getSender() + "要退出系统了");
                    //将客户端对应的线程从我们的集合中删除
                    ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    //退出线程
                    break;
                }
                else{
                    System.out.println("其他请求暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
