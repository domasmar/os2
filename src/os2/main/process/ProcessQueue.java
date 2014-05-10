package os2.main.process;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ProcessQueue {

	class ProcessComparator implements Comparator<Process> {		
		public int compare(Process process1, Process process2) {
			return (process1.getPriority() > process2.getPriority()) ? 1 : -1; 
		}		
	}
	
	private Comparator<Process> comparator = new ProcessComparator();
	private PriorityQueue<Process> queue = new PriorityQueue<Process>(comparator);
	
	public ProcessQueue() {
		
	}
	
	public void add(Process process) {
		this.queue.add(process);
	}
	
	public Process get() {
		return this.queue.remove();
	}
	
}
