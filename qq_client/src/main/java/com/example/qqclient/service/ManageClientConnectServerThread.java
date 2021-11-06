package com.example.qqclient.service;

import java.util.HashMap;

/**
 * @ClassName ManageClientConnectServerThread
 * @Description 管理客户端连接到服务器端的线程的集合类
 * @Author 86137
 * @Date 2021-11-02 23:08
 * @Version 1.0
 */
public class ManageClientConnectServerThread {
    //我们把多个线程放入一个HashMap集合中，key就是用户Id， value就是线程
    private static HashMap<String, ClientConnectServerThread> map = new HashMap<>();


    //将某个线程加入集合中
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread thread){
        map.put(userId, thread);
    }

    //将某个线程移除集合
    public static ClientConnectServerThread removeClientConnectServerThread(String userId){
        return map.remove(userId);
    }

    //通过userId，可以得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return map.get(userId);
    }
}
