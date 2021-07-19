package de.flxnet.pixelwall.panels;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.map.RenderData;
import org.bukkit.inventory.ItemStack;

import de.flxnet.pixelwall.helpers.Pair;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutMap;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.level.World;
import net.minecraft.world.level.saveddata.maps.WorldMap.b;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class PanelImageRender {

	@Getter
	private int id;
	
	@Getter
	private int entityId;
	
	@Getter @Setter
	private BufferedImage image;
	
	private RenderData renderData;
	
	private PanelRenderPackage renderPackage;
	
	@Getter @Setter
	private boolean itemFramesVisible;
	
	private PanelDirection tileDirection;
	
	public PanelImageRender(BufferedImage image, boolean itemFramesVisible, PanelDirection tileDirection) {
		this.id = PanelStore.nextId();
		this.image = image;
		this.itemFramesVisible = itemFramesVisible;
		this.tileDirection = tileDirection;
	}
	
	public PanelRenderPackage getRenderPackage(World world, BlockPosition blockPosition) {
		if(renderPackage != null) return renderPackage;
		Pair<PacketPlayOutSpawnEntity, PacketPlayOutEntityMetadata> entityPacketPair = getItemFrameSpawnData(world, blockPosition);
		renderPackage = new PanelRenderPackage(world, blockPosition, getMapData(), entityPacketPair.getFirst(), entityPacketPair.getSecond());
		return renderPackage;
	}
	
	public boolean finish() {
		renderData = this.render();
		if(renderData != null) return true;
		return false;
	}
	
	private PacketPlayOutMap getMapData() {
		net.minecraft.world.level.saveddata.maps.WorldMap.b mapDataB = new b(0, 0, 128, 128, renderData.buffer);
		return new PacketPlayOutMap(this.id, (byte) 0x0, false, Collections.emptyList(), mapDataB);
	}
	
	private Pair<PacketPlayOutSpawnEntity, PacketPlayOutEntityMetadata> getItemFrameSpawnData(World world, BlockPosition blockPosition) {
		EntityItemFrame entityItemFrame = new EntityItemFrame(world, blockPosition, EnumDirection.a);
		entityItemFrame.setInvisible(!itemFramesVisible);
		this.entityId = entityItemFrame.getId();
		
		ItemStack mapItem = new ItemStack(Material.FILLED_MAP, 1);
		net.minecraft.world.item.ItemStack craftItemStack = CraftItemStack.asNMSCopy(mapItem);
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInt("map", this.id);
		craftItemStack.setTag(compound);
		entityItemFrame.setItem(craftItemStack);
		
		PacketPlayOutSpawnEntity entityPacket = new PacketPlayOutSpawnEntity(entityItemFrame, tileDirection.getEnumDirectionId());
		PacketPlayOutEntityMetadata entityMetaPacket = new PacketPlayOutEntityMetadata(this.entityId, entityItemFrame.getDataWatcher(), false);
		
		return Pair.of(entityPacket, entityMetaPacket);
	}
	
	public PacketPlayOutEntityDestroy getItemFrameDestroyData() {
		return new PacketPlayOutEntityDestroy(this.entityId);
	}
	
	private RenderData render() {
        RenderData renderData = new RenderData();

        Arrays.fill(renderData.buffer, (byte) 0);
        renderData.cursors.clear();

        PanelMapCanvas canvas = new PanelMapCanvas();
        canvas.setBase(renderData.buffer);
        //canvas.drawText(10, 10, MinecraftFont.Font, "ID: " + this.id);
        //canvas.drawText(10, 20, MinecraftFont.Font, LocalDateTime.now().toString());
        canvas.drawImage(0, 0, this.image);
        
        byte[] buf = canvas.getBuffer();
        for (int i = 0; i < buf.length; ++i) {
            byte color = buf[i];
            if (color >= 0 || color <= -21) renderData.buffer[i] = color;
        }

        for (int i = 0; i < canvas.getCursors().size(); ++i) {
        	renderData.cursors.add(canvas.getCursors().getCursor(i));
        }
        
        return renderData;
	}
	
}
