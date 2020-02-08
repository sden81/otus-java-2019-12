package ru.otus.hw03;

import java.util.ArrayList;

public class MemoryLeak implements MemoryLeakMBean {
    protected ArrayList<String> arrayList;
    protected SysInfo sysInfo;

    public MemoryLeak(SysInfo sysInfo) {
        arrayList = new ArrayList<>();
        this.sysInfo = sysInfo;
    }

    public void addRandomStrings(int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            String rndString = RandomString.generateRandomString(600);
            arrayList.add(rndString.toString());
        }
    }

    public void removeRandomStrings(int itemCount){
        for (int i = 0; i < itemCount; i++) {
            arrayList.remove(0);
        }
    }

}
