package com.example.qqserver.service;

import com.example.qqcommon.Message;
import com.example.qqcommon.MessageType;
import com.example.utils.Utility;

import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @ClassName SendNewsToAllService
 * @Description TODO
 * @Author 86137
 * @Date 2021-11-06 20:11
 * @Version 1.0
 */
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {

        //为了可以推送多次，使用while循环
        while (true) {

            System.out.println("请输入服务器要推送的新闻/消息[输入exit表示退出推送服务]");
            String news = Utility.readString(100);
            if("exit".equals(news)){
                break;
            }
            //构建message
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMesType(MessageType.MESSAGE_TOALL_MES);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说： " + news);

            //遍历出所有socket
            HashMap<String, ServerConnectClientThread> map =
                    ManageServerConnectClientThread.getMap();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                try {
                    String onLineUserId = iterator.next();
                    ServerConnectClientThread thread = map.get(onLineUserId);
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
