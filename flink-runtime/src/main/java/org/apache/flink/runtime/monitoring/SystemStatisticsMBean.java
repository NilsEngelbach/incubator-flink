package org.apache.flink.runtime.monitoring;

public interface SystemStatisticsMBean {
	public String getJavaVersion();

	public String getOSName();

	public String getOSVersion();

	public long getFreeMemory();

	public long getMaxMemory();

	public long getTotalMemory();

}
