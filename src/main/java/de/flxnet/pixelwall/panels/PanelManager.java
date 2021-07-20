package de.flxnet.pixelwall.panels;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import de.flxnet.pixelwall.PixelWallPlugin;
import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.helpers.JsonHelper;
import de.flxnet.pixelwall.helpers.PersistenceHelper;
import lombok.Getter;
import lombok.Setter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelManager {

	@Getter
	private List<Panel> panels;
	
	@Getter @Setter
	private boolean log = false;
	
	public PanelManager() {
		panels = Lists.newArrayList();
		if(!load()) ConsoleHelper.consoleError("Could not load panels from file!");
		
		Bukkit.getScheduler().runTaskTimer(PixelWallPlugin.getInstance(), task -> {
			boolean saved = save();
			if(!saved) ConsoleHelper.console("§cPanels could not be saved in scheduled task!");
			if(log) ConsoleHelper.console("§aPanels have been saved.");
		}, 20 * 1, 20 * 30);
	}
	
	public boolean exists(String name) {
		return getPanel(name) != null;
	}
	
	public Panel getPanel(String name) {
		return panels.stream().filter(d -> d.getName().equals(name)).findAny().get();
	}
	
	public boolean addPanel(Panel panel) {
		return panels.add(panel) && save();
	}
	
	public boolean deletePanel(String name) {
		return panels.remove(getPanel(name));
	}
	
	public boolean save() {
		String json = JsonHelper.getGson().toJson(panels);
		try {
			Files.write(json, PersistenceHelper.getAndCreateDataFile("panels.json"), Charsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean load() {
		try {
			Panel[] savedPanels = JsonHelper.getGson().fromJson(new FileReader(PersistenceHelper.getAndCreateDataFile("panels.json")), Panel[].class);
			if(savedPanels != null) panels = Lists.newArrayList(savedPanels); return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
}
