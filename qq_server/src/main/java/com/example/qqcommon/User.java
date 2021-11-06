package com.example.qqcommon;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description TODO
 * @Author 86137
 * @Date 2021-11-01 23:42
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;      //表示用户Id
    private String passwd;      //表示用户密码
}
