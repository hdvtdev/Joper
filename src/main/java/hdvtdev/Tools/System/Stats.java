package hdvtdev.Tools.System;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class Stats {

    private static Instant startTime;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public static String getMemoryUsage() {
        MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = mxBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = mxBean.getNonHeapMemoryUsage();
        return ("RAM " + (heapMemoryUsage.getUsed() + nonHeapMemoryUsage.getUsed()) / (1024 * 1024) + "mb");
    }

    public static String getCpuUsage() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"ProcessCpuLoad"});

            if (list.isEmpty()) {
                return "CPU 0.0%";
            }

            Attribute att = (Attribute) list.getFirst();
            Double value = (Double) att.getValue();

            if (value == -1.0) {
                return "CPU 0.00%";
            }

            return "CPU " + df.format(value * 100) + "%";

        } catch (Exception e) {
            System.err.println("Error while retrieving CPU usage: " + e.getMessage());
            return "CPU 0.00%";
        }
    }

    public static String getUptime() {
        Duration uptime = Duration.between(startTime, Instant.now());

        long hours = uptime.toHours();
        long minutes = uptime.toMinutes() % 60;
        long seconds = uptime.getSeconds() % 60;

        return String.format("Uptime %02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void startTimeNow() {
        startTime = Instant.now();
    }

    public static void statsLogger() {
        //пофиксить баг с GUI
        System.out.print("\r[DisBot] [SysInfo] " + getMemoryUsage() + "  " + getCpuUsage() + "  " + getUptime());
    }
}