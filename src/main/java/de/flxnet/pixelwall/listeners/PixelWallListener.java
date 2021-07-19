package de.flxnet.pixelwall.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.google.common.collect.Lists;

import de.flxnet.pixelwall.helpers.PanelHelper;

public class PixelWallListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PanelHelper.initializePanels(Lists.newArrayList(player));		
	}
	
	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		
	}
	
}
