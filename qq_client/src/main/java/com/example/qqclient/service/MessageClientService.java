package com.example.qqclient.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @ClassName MessageClientService
 * @Description 该类提供和消息相关的服务方法
 * @Author 86137
 * @Date 2021-11-05 23:17
 * @Version 1.0
 */
public class MessageClientService {

    /**
     * 私聊消息
     * @param content 内容
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendMessageToOne(String content, String senderId, String getterId){
        //构建Message
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setReceiver(getterId);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        System.out.println(senderId + " 对 " + getterId + " 说 " + content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 群发消息
     * @param content 内容
     * @param senderId 发送者
     */
    public void sendMessageToAll(String content, String senderId){
        //构建Message
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_TOALL_MES);
        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
