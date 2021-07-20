package de.flxnet.pixelwall.commands;

import java.net.URL;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import de.flxnet.pixelwall.PixelWallPlugin;
import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.helpers.PanelHelper;
import de.flxnet.pixelwall.panels.Panel;
import de.flxnet.pixelwall.panels.PanelLocation;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ConsoleHelper.prefix + "§cThis command is for players only.");
			return true;
		}
		
		Player player = (Player) sender;
		
		// /panel list
		if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
			
			List<Panel> panels = PixelWallPlugin.getInstance().getPanelManager().getPanels();
			
			if(panels.size() == 0) {
				ConsoleHelper.playerWarning(player, "Currently there are no panels");
				return true;
			}
			
			ConsoleHelper.playerWarning(player, "Currently saved panels:");
			for(Panel panel : panels) {
				ConsoleHelper.sendInteractivePanelInformation(player, panel);
			}
			
			return true;
		}
		
		// /panel update <name> <url>
		if(args.length == 2 && args[0].equalsIgnoreCase("update")) {
			Location location = player.getLocation();
			String name = args[1];
			
			Panel panel = PixelWallPlugin.getInstance().getPanelManager().getPanel(name);
			panel.update(null, PanelLocation.of(location, PanelHelper.getFacing(player.getFacing())));
			
			ConsoleHelper.playerSuccess(player, "Updated panel §b" + panel.getName() + " §7(" + panel.getLocation().toString() + ")");
			PanelHelper.destroyPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()), true);
			PanelHelper.buildPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()));
			
			return true;
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("update")) {
			
			Location location = player.getLocation();
			String name = args[1];
			String urlString = args[2];
			URL url = PanelHelper.getUrl(urlString);
			
			Panel panel = PixelWallPlugin.getInstance().getPanelManager().getPanel(name);
			panel.update(url, PanelLocation.of(location, PanelHelper.getFacing(player.getFacing())));
			
			ConsoleHelper.playerSuccess(player, "Updated panel §b" + panel.getName() + " §7(" + panel.getLocation().toString() + ")");
			PanelHelper.destroyPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()), true);
			PanelHelper.buildPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()));
			
			return true;
		}
		
		// /panel move <name> <amountX> <amountY> <amountZ>
		if(args.length == 4 && args[0].equalsIgnoreCase("move")) {
		
			
			
			return true;
		}
				
		// /panel add <name> <url>
		if(args.length == 3 && args[0].equalsIgnoreCase("add")) {
			
			String name = args[1];
			String urlString = args[2];
			URL url = PanelHelper.getUrl(urlString);
			Location location = player.getLocation();

			boolean exists = PixelWallPlugin.getInstance().getPanelManager().exists(name);
			if(exists) {
				ConsoleHelper.playerWarning(player, "A panel named '" + name + "' already exists.");
				return true;
			}
			
			Panel panel = new Panel(name, url, PanelLocation.of(location, PanelHelper.getFacing(player.getFacing())));
			
			boolean added = PixelWallPlugin.getInstance().getPanelManager().addPanel(panel);
			
			if(!added) {
				ConsoleHelper.playerError(player, "Could not add panel '" + name + "'");
				return true;
			}
			
			ConsoleHelper.playerSuccess(player, "Added new panel §b" + panel.getName() + " §7(" + panel.getLocation().toString() + ")");
			PanelHelper.buildPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()));
			
			return true;
		}
		
		// /panel delete <name>
		if(args.length == 2 && args[0].equalsIgnoreCase("delete")) {
			
			String name = args[1];
			
			Panel panel = PixelWallPlugin.getInstance().getPanelManager().getPanel(name);
			PanelHelper.destroyPanel(panel, Lists.newArrayList(Bukkit.getOnlinePlayers()), false);
			
			boolean deleted = PixelWallPlugin.getInstance().getPanelManager().deletePanel(name);
			if(!deleted) {
				ConsoleHelper.playerError(player, "Could not delete panel '" + name + "'");
				return true;
			}
			
			ConsoleHelper.playerSuccess(player, "Panel §b" + panel.getName() + " §ahas been deleted.");
			
			return true;
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1) return Lists.newArrayList("list", "add", "delete", "update");
		return null;
	}

}
