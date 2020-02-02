package ru.otus.hw03;

public class SysInfo {
    public static String getMemoryInfo() {
        String sysInfo = String.format(
                "Max/free: %d/%d Mb",
                byteToMb(Runtime.getRuntime().maxMemory()),
                byteToMb(Runtime.getRuntime().freeMemory())
        );

        return sysInfo;
    }

    protected static  long byteToMb(long inByte){
        return inByte / 1000000;
    }
}
