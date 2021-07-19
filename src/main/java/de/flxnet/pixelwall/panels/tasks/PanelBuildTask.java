package de.flxnet.pixelwall.panels.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.flxnet.pixelwall.helpers.ConsoleHelper;
import de.flxnet.pixelwall.helpers.NMSHelper;
import de.flxnet.pixelwall.helpers.PanelHelper;
import de.flxnet.pixelwall.panels.Panel;
import de.flxnet.pixelwall.panels.PanelDirection;
import de.flxnet.pixelwall.panels.PanelImageRender;
import de.flxnet.pixelwall.panels.PanelRenderPackage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.level.World;

public class PanelBuildTask implements Runnable {

	@Getter @Setter
	private Panel panel;
	
	@Getter @Setter
	private List<Player> players;
	
	private long startTime;
	private long endTime;
	
	public PanelBuildTask(Panel panel, List<Player> players) {
		this.panel = panel;
		this.players = players;
	}
	
	@Override
	public void run() {
		ConsoleHelper.console("§7Starting new PanelBuildTask §b(" + panel.getName() + ")");
		startTime = System.currentTimeMillis();

		boolean prepared = prepare();
		if(!prepared) {
			ConsoleHelper.console("§cFailed PanelBuildTask §b(" + panel.getName() + ") §4could not prepare image");
			return;
		}
		
		provide();
		
		endTime = System.currentTimeMillis();
		ConsoleHelper.console("§7Finished PanelBuildTask §b(" + panel.getName() + ")§7, took " + timeTaken() + "ms");
	}
	
	private long timeTaken() {
		return endTime - startTime;
	}
	
	private boolean prepare() {
		if(panel.isRendered()) {
			ConsoleHelper.console("§6Panel " + panel.getName() + " is already prepared, skipping.");
			return true;
		}
		
		boolean loaded = panel.loadImage();
		if(!loaded) return false;
		
		panel.calculateDimensions();
		panel.splitImage();
		
		boolean rendered = panel.renderImage();
		
		return loaded && rendered;
	}
	
	private void provide() {
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
					renderPackage = render.getRenderPackage(world, PanelHelper.fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.SOUTH) {
					Location placeLocation = startLocation.clone().add(column, row, 0);
					renderPackage = render.getRenderPackage(world, PanelHelper.fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.WEST) {
					Location placeLocation = startLocation.clone().add(0, 0, column).add(0, row, 0);
					renderPackage = render.getRenderPackage(world, PanelHelper.fromLocation(placeLocation));
				}
				
				if(panelDirection == PanelDirection.EAST) {
					Location placeLocation = startLocation.clone().subtract(0, 0, column).add(0, row, 0);
					renderPackage = render.getRenderPackage(world, PanelHelper.fromLocation(placeLocation));
				}
				
				NMSHelper.sendPacket(player, renderPackage.getMapPacket());
				NMSHelper.sendPacket(player, renderPackage.getSpawnEntityPacket());
				NMSHelper.sendPacket(player, renderPackage.getEntityMetadataPacket());
				
				column++;
				
			}

		}
	}
	
}
