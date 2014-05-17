package os2.main.resources;

import java.util.ArrayList;
import java.util.Iterator;

public class ResourceList {

	private ArrayList<Resource> list = new ArrayList<Resource>();
	
	public ResourceList() {
		
	}
	
	public void addResource(Resource resource) {
		this.list.add(resource);
	}
	
	public Resource searchResource(String name) {
		Iterator<Resource> iterator = this.list.iterator();
		while (iterator.hasNext()) {
			Resource resource = iterator.next();
			if (resource.getName().equalsIgnoreCase(name)) {
				return resource;
			}
		}
		return null;
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	
}
