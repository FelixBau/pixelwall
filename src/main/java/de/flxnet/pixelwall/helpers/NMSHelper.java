package de.flxnet.pixelwall.helpers;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.network.protocol.Packet;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class NMSHelper {

	/**
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, Packet<?> packet) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		craftPlayer.getHandle().b.sendPacket(packet);
	}
	
}
