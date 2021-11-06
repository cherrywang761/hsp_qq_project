package com.example.qqclient.view;

import com.example.qqclient.service.FileClientService;
import com.example.qqclient.service.MessageClientService;
import com.example.qqclient.service.UserClientService;
import com.example.qqclient.utils.Utility;

/**
 * @ClassName QQView
 * @Description 客户端的菜单页面
 * @Author 86137
 * @Date 2021-11-02 0:11
 * @Version 1.0
 */
public class QQView {

    //控制是否显示菜单
    private boolean loop = true;

    //接收用户的键盘输入
    private String key = "";

    //该对象是用于登录服务器/注册用户
    private UserClientService userClientService = new UserClientService();

    //该对象是用于消息私聊/群聊
    private MessageClientService messageClientService = new MessageClientService();

    //该对象是用于文件发送
    private FileClientService fileClientService = new FileClientService();

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("=============欢迎登录网络通信系统==============");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择：");
            key = Utility.readString(1);

            //根据用户的输入来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户号：");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密  码：");
                    String password = Utility.readString(50);

                    //todo 这里需要构建一个User对象，发送给服务端进行校验
                    if (userClientService.checkUser(userId, password)) {
                        System.out.println("=============欢迎(用户" + userId + ")==============");
                        //进入二级菜单
                        while (loop) {
                            System.out.println("\n=============网络通信系统二级菜单(用户" + userId + ")==============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    System.out.print("显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("想对全体成员说的话：");
                                    String toAllContent = Utility.readString(100);
                                    messageClientService.sendMessageToAll(toAllContent,userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户号（在线）：");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content = Utility.readString(100);
                                    //编写方法，将消息发送给服务端
                                    messageClientService.sendMessageToOne(content, userId, getterId);
                                    break;
                                case "4":
                                    System.out.println("想发送文件的地址(d:\\图片1.jpg)：");
                                    String src = Utility.readString(200);

                                    System.out.println("想发送文件给谁：");
                                    String receiverId = Utility.readString(50);

                                    System.out.println("对方接收文件的有效地址(d:\\code_temp\\图片1.jpg)：");
                                    String dest = Utility.readString(200);

                                    fileClientService.sendFileToOne(src, dest, userId, receiverId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("传值异常");
                                    break;
                            }
                        }
                    } else {
                        //登录服务器失败
                        System.out.println("==========登录失败==========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
                default:
                    System.out.println("传值异常");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new QQView().mainMenu();
    }
}
