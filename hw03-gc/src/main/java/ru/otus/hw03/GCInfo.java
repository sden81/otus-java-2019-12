package ru.otus.hw03;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GCInfo {
    private static HashMap<String, ArrayList<Long>> storage;
    private static long beginTime;

    public static void init() {
        beginTime = System.currentTimeMillis();
        storage = new HashMap<>();
    }

    public static void addData(String gcName, long duration) {
        if (!storage.containsKey(gcName)) {
            long initCount = 0;
            long initAllDuration = 0;
            ArrayList<Long> dataArrayList = new ArrayList<>();
            dataArrayList.add(initCount);
            dataArrayList.add(initAllDuration);
            storage.put(gcName, dataArrayList);
        }

        ArrayList<Long> tmpArray = storage.get(gcName);
        long count = tmpArray.get(0);
        long allDuration = tmpArray.get(1);
        count++;
        allDuration += duration;
        tmpArray.set(0, count);
        tmpArray.set(1, allDuration);

        storage.put(gcName, tmpArray);
    }

    public static long getUptimeInSec() {
        return (System.currentTimeMillis() - beginTime) / 1000;
    }
    
    public static String getInfo(){
        String result = "";
        for (Map.Entry<String, ArrayList<Long>> entry: storage.entrySet()){
            result += "GCname: "+entry.getKey() + " | Count: "+entry.getValue().get(0)+" | Duration: "+entry.getValue().get(1) + " ms";
        }

        return result;
    }

}
