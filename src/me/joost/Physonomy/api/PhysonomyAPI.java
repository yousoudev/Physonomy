package me.joost.Physonomy.api;

import me.joost.Physonomy.Physonomy;

public class PhysonomyAPI {

	private Physonomy instance;
	
	public PhysonomyAPI() {
		instance = Physonomy.getPlugin(Physonomy.class);
	}
}
