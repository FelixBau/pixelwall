package de.flxnet.pixelwall;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import de.flxnet.pixelwall.commands.PanelCommand;
import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.helpers.PanelHelper;
import de.flxnet.pixelwall.listeners.PixelWallListener;
import de.flxnet.pixelwall.panels.PanelManager;
import lombok.Getter;

public class PixelWallPlugin extends JavaPlugin {

	@Getter
	private static PixelWallPlugin instance;
	
	@Getter
	private PanelManager panelManager;
	
	@Override
	public void onEnable() {
		instance = this;
		panelManager = new PanelManager();
		
		initializeCommands();
		initializeEventListeners();
		
		PanelHelper.initializePanels(Lists.newArrayList(Bukkit.getOnlinePlayers()));
		
		ConsoleHelper.consoleInfo("Plugin enabled!");
	}
	
	@Override
	public void onDisable() {
		ConsoleHelper.consoleInfo("Plugin disabled!");
	}
	
	private void initializeCommands() {
		PluginCommand panelCommand = getCommand("panel");
		PanelCommand panelCommandExecutor = new PanelCommand();
		panelCommand.setExecutor(panelCommandExecutor);
		panelCommand.setTabCompleter(panelCommandExecutor);
	}
	
	private void initializeEventListeners() {
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(new PixelWallListener(), this);
	}
	
}
