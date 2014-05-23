package os2.main;

import os2.main.processes.ProcessQueue;
import os2.main.processes.imp.StartStop;
import os2.main.resources.Resource;
import os2.main.resources.ResourceList;
import os2.main.resources.ResourceType;
import os2.main.resources.descriptors.FromGUIDescriptor;

public class Core {

	public static ResourceList resourceList = new ResourceList();
	public static ProcessQueue processQueue = new ProcessQueue();

	public static boolean running = false;

	public static void init() {
		Core.processQueue.add(new StartStop());
	}

	public static void startOS() {

		Resource r = new Resource(ResourceType.PROGRAM_FROM_FLASH);
		FromGUIDescriptor descriptor = new FromGUIDescriptor();
		descriptor.setFileName("Fibo.ltu");
		r.setDescriptor(descriptor);
		Core.resourceList.addResource(r);
		
		r = new Resource(ResourceType.PROGRAM_FROM_FLASH);
		descriptor = new FromGUIDescriptor();
		descriptor.setFileName("Fibo_2.ltu");
		r.setDescriptor(descriptor);
		Core.resourceList.addResource(r);

		Core.running = true;
		while (Core.running) {
			os2.main.processes.Process process = processQueue.get();
			if (process != null) {
//				 System.out.println(process + " žingsnis: " +
//				 process.getStep() + " prioritetas: " +
//				 process.getPriority());
				process.nextStep();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
