package os2.main;

import os2.main.processes.ProcessQueue;
import os2.main.resources.ResourceList;

public class Core {
	
	public static ResourceList resourceList = new ResourceList();
	public static ProcessQueue processQueue = new ProcessQueue();
	
	public static boolean running = false;
	
	public static void startOS() {
		Core.running = true;
		while (Core.running) {
			os2.main.processes.Process process = processQueue.get();
			process.nextStep();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
