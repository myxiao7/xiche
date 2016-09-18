package com.example;

/**
 * Created by win7 on 2016/9/18.
 */
public class MySon extends MyClass {
    @Override
    protected void printA() {
//        super.printA();
        System.out.print("aaaaaaaaaaaaa");
    }
    public static void main(String[] args){
        MySon mySon = new MySon();
        mySon.printA();
    }
}
