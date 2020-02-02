package ru.otus.hw03;

import static org.junit.Assert.*;

public class GCInfoTest {

    @org.junit.Test
    public void showInfo() {
        GCInfo.init();
        GCInfo.addData("G1 Young Generation",20);
        GCInfo.addData("G1 Young Generation",10);
        GCInfo.addData("G1 Young Generation",5);
        String info = GCInfo.getInfo();
        assertTrue(info.equals("GCname: G1 Young Generation | Count: 3 | Duration: 35 ms"));
    }
}