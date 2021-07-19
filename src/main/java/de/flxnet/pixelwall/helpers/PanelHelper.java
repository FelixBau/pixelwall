package de.flxnet.pixelwall.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.flxnet.pixelwall.PixelWallPlugin;
import de.flxnet.pixelwall.panels.Panel;
import de.flxnet.pixelwall.panels.PanelDirection;
import de.flxnet.pixelwall.panels.tasks.PanelDestroyTask;
import de.flxnet.pixelwall.panels.tasks.PanelPrepareTask;
import de.flxnet.pixelwall.panels.tasks.PanelProvisionTask;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelHelper {
	
	public static void initializePanels(List<Player> players) {
		List<Panel> panels = PixelWallPlugin.getInstance().getPanelManager().getPanels();
		panels.forEach(tile -> {
			providePanel(tile, players);
		});
	}
	
	public static void preparePanel(Panel panel) {
		submitAsyncTask(new PanelPrepareTask(panel));
	}
	
	public static void providePanel(Panel panel, List<Player> players) {
		submitAsyncTask(new PanelProvisionTask(panel, players));
	}
	
	public static void destroyPanel(Panel panel, List<Player> players, boolean force) {
		submitAsyncTask(new PanelDestroyTask(Lists.newArrayList(players), Lists.newArrayList(panel), force));
	}
	
	public static void destroyAllPanels(List<Player> players, boolean force) {
		submitAsyncTask(new PanelDestroyTask(players, PixelWallPlugin.getInstance().getPanelManager().getPanels(), force));
	}
	
	public static PanelDirection getFacing(BlockFace blockFace) {
		return PanelDirection.valueOf(blockFace.toString());
	}
	
	public static URL getUrl(String urlString) {
		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void submitAsyncTask(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(PixelWallPlugin.getInstance(), runnable);
	}

}
