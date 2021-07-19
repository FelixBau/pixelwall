package de.flxnet.pixelwall.panels;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelStore {

	public static int MAP_ID_COUNTER = 100000;
	
	public static int nextId() {
		int next = MAP_ID_COUNTER;
		MAP_ID_COUNTER++;
		return next;
	}
	
}
