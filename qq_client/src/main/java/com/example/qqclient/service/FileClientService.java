package com.example.qqclient.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @ClassName FileClientService
 * @Description 该类完成客户端的文件传输
 * @Author 86137
 * @Date 2021-11-06 17:44
 * @Version 1.0
 */
public class FileClientService {

    /**
     *
     * @param src 文件源目录
     * @param dest 将文件传输到对方的哪个目录
     * @param senderId 发送用户Id
     * @param receiverId 接收用户Id
     */
    public void sendFileToOne(String src, String dest, String senderId, String receiverId){
        //初始化message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setSrc(src);
        message.setDest(dest);

        //读取src文件
        FileInputStream inputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];

        try {
            inputStream = new FileInputStream(src);
            //将文件流数据读入到fileBytes中存储
            inputStream.read(fileBytes);

            //将文件字节数组设置到message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println("\n" + senderId +" 给 "+ receiverId + " 发送文件 " + src +
                " 到对方目录 " + dest);

        //发送
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread
                    .getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
