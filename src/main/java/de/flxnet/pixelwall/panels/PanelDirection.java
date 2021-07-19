package de.flxnet.pixelwall.panels;

import lombok.Getter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public enum PanelDirection {

	NORTH(3),
	SOUTH(2),
	WEST(5),
	EAST(4),
	UP(1),
	DOWN(0);
	
	@Getter
	private int enumDirectionId;
	
	private PanelDirection(int enumDirectionId) {
		this.enumDirectionId = enumDirectionId;
	}
	
}
