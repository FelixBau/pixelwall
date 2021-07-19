package de.flxnet.pixelwall.helpers;

import java.io.File;
import java.io.IOException;

import de.flxnet.pixelwall.PixelWallPlugin;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PersistenceHelper {
	
	public static File getAndCreateDataFile(String fileName) {
		File pluginDataFolder = PixelWallPlugin.getInstance().getDataFolder();
		if(!pluginDataFolder.exists()) pluginDataFolder.mkdirs();
		File dataFile = new File(pluginDataFolder, fileName);
		try {
			if(!dataFile.exists()) dataFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataFile;
	}
	
}
