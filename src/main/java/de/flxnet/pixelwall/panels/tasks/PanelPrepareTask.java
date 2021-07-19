package de.flxnet.pixelwall.panels.tasks;

import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.panels.Panel;
import lombok.Getter;
import lombok.Setter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelPrepareTask implements Runnable {
	
	@Getter @Setter
	private Panel panel;
	
	private long startTime;
	
	private long endTime;
	
	public PanelPrepareTask(Panel panel) {
		this.panel = panel;
	}
	
	@Override
	public void run() {
		ConsoleHelper.console("§7Starting new PanelPrepareTask §b(" + panel.getName() + ")");
		startTime = System.currentTimeMillis();
		
		boolean loaded = panel.loadImage();
		System.out.println("loaded=" + loaded);
		
		if(!loaded) {
			ConsoleHelper.console("§cFailed PanelPrepareTask §b(" + panel.getName() + ") §4could not load image");
			return;
		}
		
		panel.calculateDimensions();
		panel.splitImage();
		panel.renderImage();
		
		endTime = System.currentTimeMillis();
		ConsoleHelper.console("§7Finished PanelPrepareTask §b(" + panel.getName() + ")§7, took " + timeTaken() + "ms");
	}
	
	private long timeTaken() {
		return endTime - startTime;
	}
	
	/*
	@Override
	public void run() {
		ConsoleHelper.console("§6TilePrepareTask §b(" + tile.getName() + ")");
		
		ConsoleHelper.console("§7[STEP1] §2Loading image: §estarting");
		tile.loadImage();
		ConsoleHelper.console("§7[STEP1] §2Loading image: §afinished");
		
		ConsoleHelper.console("§7[STEP2] §2Calculating dimensions: §estarting");
		tile.calculateDimensions();
		ConsoleHelper.console("§7[STEP2] §2Calculating dimensions: §afinished");
		
		ConsoleHelper.console("§7[STEP3] §2Split image: §estarting");
		tile.splitImage();
		ConsoleHelper.console("§7[STEP3] §2Split image: §afinished");
		
		ConsoleHelper.console("§7[STEP4] §2Render image: §estarting");
		tile.renderImage();
		ConsoleHelper.console("§7[STEP4] §2Render image: §afinished");
		
		ConsoleHelper.console("§6TilePrepareTask: cols=" + tile.getColumns() + ", rows=" + tile.getRows() + ", loaded=" + tile.isImageLoaded() + ", rendered=" + tile.isRendered());
	}
	*/

}
