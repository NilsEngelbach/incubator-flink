package org.apache.flink.runtime.monitoring;

public class SystemStatistics implements SystemStatisticsMBean{
	
	@Override
    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    @Override
    public String getOSName() {
        return System.getProperty("os.name");
    }

    @Override
    public String getOSVersion() {
        return System.getProperty("os.version");
    }

    @Override
    public long getFreeMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.freeMemory() / (1024 * 1024);
    }

    @Override
    public long getMaxMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.maxMemory() / (1024 * 1024);
    }

    @Override
    public long getTotalMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() / (1024 * 1024);
    }
    
}
