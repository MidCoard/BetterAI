package com.focess.betterai.navigation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;

import com.focess.betterai.utils.WorldUtil;
import com.focess.betterai.utils.ZombieUtil;
import com.focess.betterai.zombie.AIZombie;
import com.focess.pathfinder.core.navigation.NavigationManager;
import com.focess.pathfinder.core.navigation.focess.FocessNavigation;
import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;
import com.focess.pathfinder.navigation.BasicPath;
import com.focess.pathfinder.navigation.PathPoint;
import com.toolapi.utils.TestUtil;

import pers.blockdurability.data.DurabilityManager;

/**
 * 僵尸意志
 */
public class ZombieNavigation extends FocessNavigation{

	private AIZombie aiZombie;
	private ZombiePath zombiePath;
	
	public ZombieNavigation(AIZombie aiZombie) {
		this.aiZombie=aiZombie;
		zombiePath=new ZombiePath(this);
		NavigationManager.INSTANCE.registerFocessNavigation(this);
	}
	
	
	@Override
	public ZombiePath getCurrentPath() {
		return this.zombiePath;
	}
	
	@Override
	public boolean isReachPathPoint(FocessPathPoint pathPoint) {
		return (WorldUtil.dis(aiZombie.getBukkitEntity().getLocation(), pathPoint.getLocation())<1);
	}

	@Override
	public void gotoPathPoint(FocessPathPoint pathPoint) {
		if(pathPoint==null)return;
		Location loc=getBlockLocation();
		Location pLoc=pathPoint.getLocation();
		Zombie zombie=aiZombie.getBukkitEntity();
		if(Math.abs(loc.getY()-pLoc.getY())<1) {
			zombie.getPathfinder().moveTo(pathPoint.getLocation());
		}else {//y坐标不同
			//System.out.println("else");
			if(loc.getY()>pLoc.getY()) {//向下走
				Block unBlock=WorldUtil.getBlockUnderEntity(aiZombie.getBukkitEntity());
				int dur=DurabilityManager.INSTANCE.getBlockDurability(unBlock);
				dur++;
				if(dur==10){
					unBlock.setType(Material.AIR);
					DurabilityManager.INSTANCE.setBlockDurability(unBlock, -1);
				}else
				DurabilityManager.INSTANCE.setBlockDurability(unBlock, dur);
			}else if(loc.getY()<pLoc.getY()) {//向上走
				zombie.setVelocity(new Vector(0,1,0));
			}
			//DurabilityManager.INSTANCE.setBlockDurability(block, dur);
		}
	}

	public AIZombie getAIZombie() {
		return aiZombie;
	}

	public void setAIZombie(AIZombie aiZombie) {
		this.aiZombie = aiZombie;
	}

	//注：此处pathPoint需toBlockLocation
	@Override
	public boolean canReachPathPointInOneStep(FocessPathPoint pathPoint) {
		Location eLoc=getBlockLocation();
		if(WorldUtil.dis(eLoc, pathPoint.getLocation())<=1.0) {
			return true;
		}
		return false;
	}
	
	private Location getLocation() {
		return this.aiZombie.getBukkitEntity().getLocation();
	}
	
	private Location getBlockLocation() {
		return getLocation().toBlockLocation();
	}

	@Override
	public void recalculatePath() {
		BasicPath path=this.getCurrentPath();
		List<FocessPathPoint> points=path.getPathPoints();
		points.clear();
		Player player=ZombieUtil.findPlayer(aiZombie);
		if(player==null)return;
		Location pLoc=player.getLocation();
		points.add(new FocessPathPoint(1,pLoc));//编号为1
	}


	@Override
	public void timer() {
		FocessPathPoint point=this.getCurrentPath().getNowPathPoint();
		if(point!=null) {
			TestUtil.test("id:"+aiZombie.getEntityID()+",GO:"+point.getLocation());
		}else {
			TestUtil.test("id:"+aiZombie.getEntityID()+",GO:null");
		}
		this.gotoPathPoint(point);
	}

}
