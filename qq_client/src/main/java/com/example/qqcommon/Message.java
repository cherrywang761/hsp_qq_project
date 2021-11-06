package com.example.qqcommon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Message
 * @Description 表示客户端和服务端通信时的消息对象
 * @Author 86137
 * @Date 2021-11-01 23:45
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    //发送方
    private String sender;

    //接收方
    private String receiver;

    //内容
    private String content;

    //发送时间
    private String sendTime;

    //消息类型
    private String mesType;


    //进行拓展， 和文件相关的成员
    private byte[] fileBytes;
    private int fileLen = 0;

    //将文件传输到哪里
    private String dest;

    //文件源路径
    private String src;
}
