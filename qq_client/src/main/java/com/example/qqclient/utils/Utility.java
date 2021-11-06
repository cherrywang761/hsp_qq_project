package com.example.qqclient.utils;

import java.util.Scanner;

/**
 * @ClassName Utility
 * @Description 工具类：处理各种情况的用户输入，并且能够按照程序员的需求，得到用户的控制台输入
 * @Author 86137
 * @Date 2021-11-02 0:13
 * @Version 1.0
 */
public class Utility {
    //静态属性
    private static Scanner scanner;

    static{
        scanner = new Scanner(System.in);
    }

    public Utility(){}

    public static char readMenuSelection(){
        while(true){
            String str = readKeyBoard(1,false);
            char c = str.charAt(0);
            if(c == '1' || c == '2' || c == '3' || c == '4' || c == '5'){
                return c;
            }
            System.out.println("选择错误，请重新输入：");
        }
    }

    public static char readChar(){
        String str = readKeyBoard(1, false);
        return str.charAt(0);
    }

    public static String readString(int limit){
        return readKeyBoard(limit, false);
    }

    public static String readString(int limit, String defaultValue){
        String str = readKeyBoard(limit, true);
        return str.equals("") ? defaultValue : str;
    }

    public static char readConfirmSelection(){
        while(true){
            String str = readKeyBoard(1,false).toUpperCase();
            char c = str.charAt(0);
            if(c == 'Y' || c == 'N'){
                return c;
            }
            System.out.println("选择错误，请重新输入：");
        }
    }

    public static String readKeyBoard(int limit, boolean blankReturn){
        String line = "";
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            if(line.length() == 0){
                if(blankReturn){
                    return line;
                }
            }else{
                if(line.length() >= 1 && line.length() <= limit){
                    break;
                }
                System.out.println("输入长度（不大于" + limit + "）错误，请重新输入：");
            }
        }
        return line;
    }
}
