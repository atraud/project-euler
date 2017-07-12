package org.traud.util.misc;

/**
 * Created by traud on 8/10/2016.
 */
public class MemoryTracker {
	private long lastFree = -1;
	private long total;
	private long totalCount;

	public void check() {
		Runtime rt = Runtime.getRuntime();
		long free = rt.freeMemory();

		if (lastFree != -1 && lastFree >= free) {
			long increase = lastFree - free;
			total += increase;
			totalCount++;
		}
		lastFree = free;
	}

	public long getMemoryAllocationEstimate() {
		return total;
	}

	public long getMemoryAllocationEstimateAverage() {
		return totalCount > 0 ? total / totalCount : total;
	}

	public long getTotalCount() {
		return totalCount;
	}
}
