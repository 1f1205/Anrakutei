package com.Ichif1205.anrakutei.shot;

import com.Ichif1205.anrakutei.Player;

public class ShotFactory {

	public static Shot create(Player player) {
		if (player.mItemB) {
			return new ItemBShot(player.getPlayerPosX(), player.getPlayerPosY());
		} else {
			return new Shot(player.getPlayerPosX(), player.getPlayerPosY());
		}
	}
}
