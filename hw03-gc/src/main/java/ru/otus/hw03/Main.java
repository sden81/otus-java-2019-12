package ru.otus.hw03;


import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * -Xms512m
 * -Xmx512m
 * -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:HeapDumpPath=./logs/dump
 * -XX:+UseG1GC
 **/

public class Main {
    private static final int LOOPS_COUNT = 100000;

    public static void main(String[] args) throws InterruptedException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        switchOnMonitoring();
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName( "ru.otus:type=MemoryLeak" );

        GCInfo.init();
        SysInfo sysInfo = new SysInfo();
        MemoryLeak ml = new MemoryLeak(sysInfo);
        mbs.registerMBean( ml, name );

        for (int i = 0; i < LOOPS_COUNT; i++) {
            ml.addRandomStrings(700);
            ml.removeRandomStrings(300);
            System.out.println("Loop count: " + Integer.toString(i) + " | Uptime: " + GCInfo.getUptimeInSec() + " sec");
            if (i % 5 == 0) {
                System.out.println(SysInfo.getMemoryInfo());
            }
            Thread.sleep(100);
        }
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for ( GarbageCollectorMXBean gcbean : gcbeans ) {
            System.out.println( "GC name:" + gcbean.getName() );
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback ) -> {
                if ( notification.getType().equals( GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION ) ) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from( (CompositeData) notification.getUserData() );
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    GCInfo.addData(gcName, duration);
                    System.out.println(GCInfo.getInfo());
                }
            };
            emitter.addNotificationListener( listener, null, null );
        }
    }
}
