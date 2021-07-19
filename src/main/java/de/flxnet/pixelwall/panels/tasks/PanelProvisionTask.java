package de.flxnet.pixelwall.panels.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.flxnet.pixelwall.helpers.NMSHelper;
import de.flxnet.pixelwall.panels.Panel;
import de.flxnet.pixelwall.panels.PanelDirection;
import de.flxnet.pixelwall.panels.PanelImageRender;
import de.flxnet.pixelwall.panels.PanelRenderPackage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.World;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelProvisionTask implements Runnable {

	@Getter @Setter
	private List<Player> players;
	
	@Getter @Setter
	private Panel panel;
	
	public PanelProvisionTask(Panel panel, List<Player> players) {
		this.panel = panel;
		this.players = players;
	}
	
	@Override
	public void run() {
		if(!panel.isRendered()) {
			PanelPrepareTask prepareTask = new PanelPrepareTask(panel);
			prepareTask.run();
		}
		
		for(Player player : players) {
			CraftPlayer craftPlayer = (CraftPlayer) player;
			World world = craftPlayer.getHandle().getWorld();
			Location startLocation = panel.getLocation().toBukkitLocation();
			
			PanelDirection panelDirection = panel.getLocation().getDirection();
			
			int row = 0;
			int column = 0;
			
			for(PanelImageRender render : panel.getTileRenders()) {
				
				PanelRenderPackage renderPackage = null;
				
				if(column == panel.getColumns()) {
					column = 0;
					row++;
				}
				
				if(panelDirection == PanelDirection.NORTH) {
					Location placeLocation = startLocation.clone().subtract(column, 0, 0).add(0, row, 0);
					renderPackage = render.getRenderPackage(world, fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.SOUTH) {
					Location placeLocation = startLocation.clone().add(column, row, 0);
					renderPackage = render.getRenderPackage(world, fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.WEST) {
					Location placeLocation = startLocation.clone().add(0, 0, column).add(0, row, 0);
					renderPackage = render.getRenderPackage(world, fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.EAST) {
					Location placeLocation = startLocation.clone().subtract(0, 0, column).add(0, row, 0);
					renderPackage = render.getRenderPackage(world, fromLocation(placeLocation));
				}
				
				
				
				NMSHelper.sendPacket(player, renderPackage.getMapPacket());
				NMSHelper.sendPacket(player, renderPackage.getSpawnEntityPacket());
				NMSHelper.sendPacket(player, renderPackage.getEntityMetadataPacket());
				
				column++;
				
			}

		}
	}
	
	public BlockPosition fromLocation(Location location) {
		return new BlockPosition(location.getX(), location.getY() + 1, location.getZ());
	}

}
