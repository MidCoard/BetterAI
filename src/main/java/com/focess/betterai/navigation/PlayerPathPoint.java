package com.focess.betterai.navigation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;

public class PlayerPathPoint extends FocessPathPoint{

	private Player player;
	
	public PlayerPathPoint(Player player,int index, Location loc) {
		super(index, loc);
		this.player=player;
	}

	public String getPlayerName() {
		return player.getName().toLowerCase();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
