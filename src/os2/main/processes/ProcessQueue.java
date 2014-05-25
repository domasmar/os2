package os2.main.processes;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class ProcessQueue {

	class ProcessComparator implements Comparator<Process> {		
		public int compare(Process process1, Process process2) {
			return (process1.getPriority() < process2.getPriority()) ? 1 : -1; 
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
		Process p = this.queue.poll();
		this.queue.add(p);
		return p;
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
	}
	
	public void delete(int pid) {
		Iterator iterator = queue.iterator();
		while (iterator.hasNext()) {
			Process proc = (Process) iterator.next();
			if (proc.getPid() == pid) {
				iterator.remove();				
			}
		}
	}
	
}
