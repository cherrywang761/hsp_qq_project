package com.example.qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @ClassName ManageServerConnectClientThread
 * @Description 管理服务端与客户端保持通信的线程
 * @Author 86137
 * @Date 2021-11-02 23:50
 * @Version 1.0
 */
public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> map = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getMap() {
        return map;
    }

    //添加线程对象到map
    public static void addServerConnectClientThread(String userId, ServerConnectClientThread thread){
        map.put(userId, thread);
    }

    //根据userId查询线程对象
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return map.get(userId);
    }

    //删除userId对应的线程
    public static ServerConnectClientThread removeServerConnectClientThread(String userId){
        return map.remove(userId);
    }

    //这里编写方法，可以返回在线用户列表
    public static String getOnlineUser(){
        //遍历hashmap的key
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while(iterator.hasNext()){
            String next = iterator.next();
            sb.append(next).append(" ");
        }
        return sb.toString();
    }
}
