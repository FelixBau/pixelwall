package de.flxnet.pixelwall.panels.tasks;

import java.util.List;

import org.bukkit.entity.Player;

import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.helpers.NMSHelper;
import de.flxnet.pixelwall.panels.Panel;
import de.flxnet.pixelwall.panels.PanelImageRender;
import lombok.Getter;
import lombok.Setter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelDestroyTask implements Runnable {

	@Getter @Setter
	private List<Player> players;
	
	@Getter @Setter
	private List<Panel> panels;
	
	@Getter @Setter
	private boolean force;
	
	public PanelDestroyTask(List<Player> players, List<Panel> panels, boolean force) {
		this.players = players;
		this.panels = panels;
		this.force = force;
	}
	
	@Override
	public void run() {
		int counter = 0;
		for(Player player : players) {
			for(Panel panel : panels) {
				if((!panel.isImageLoaded() || !panel.isRendered()) && !force) continue;
				for(PanelImageRender render : panel.getTileRenders()) {
					NMSHelper.sendPacket(player, render.getItemFrameDestroyData());
					counter++;
				}
			}
		}
		ConsoleHelper.consoleInfo("Sent " + counter + " panel destroy packets for " + players.size() + " players and " + panels.size() + " panels");
	}

}
