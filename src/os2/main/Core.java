package os2.main;

import os2.main.processes.ProcessQueue;
import os2.main.processes.imp.StartStop;
import os2.main.resources.Resource;
import os2.main.resources.ResourceList;

public class Core {
	
	public static ResourceList resourceList = new ResourceList();
	public static ProcessQueue processQueue = new ProcessQueue();
	
	public static boolean running = false;
	
	public static void init() {
		Core.processQueue.add(new StartStop());
	}
	
	public static void startOS() {
		
		Resource r = new Resource("FROM_GUI");
		r.setInformation("Fibo.ltu");
		Core.resourceList.addResource(r);
		
		Core.running = true;
		while (Core.running) {
			os2.main.processes.Process process = processQueue.get();
			//System.out.println(process);
			if (process != null) {
				process.nextStep();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
