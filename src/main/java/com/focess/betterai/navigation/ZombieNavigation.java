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

import com.focess.betterai.BetterAI;
import com.focess.betterai.utils.WorldUtil;
import com.focess.betterai.utils.ZombieUtil;
import com.focess.betterai.zombie.AIZombie;
import com.focess.pathfinder.core.navigation.NavigationManager;
import com.focess.pathfinder.core.navigation.focess.FocessNavigation;
import com.focess.pathfinder.core.navigation.focess.FocessPathPoint;
import com.focess.pathfinder.navigation.BasicPath;
import com.focess.pathfinder.navigation.PathPoint;
import com.toolapi.handler.Handler;
import com.toolapi.handler.HandlerManager;
import com.toolapi.utils.BlockUtil;
import com.toolapi.utils.MathUtil;
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
		BetterAI.registers.add(this);
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
		if(Math.abs(loc.getY()-pLoc.getY())<1) {//y坐标相同
			//System.out.println("go1");
			Location newLoc = null;
			Location blockLoc;
			int ran=MathUtil.getRandomNumber(1, 2);
			if(loc.getBlockX()==pLoc.getBlockX()&&loc.getBlockZ()!=pLoc.getBlockZ()) {
				ran=1;
			}else if(loc.getBlockZ()==pLoc.getBlockZ()&&loc.getBlockX()!=pLoc.getBlockX()) {
				ran=2;
			}
			if(ran==1) {
				newLoc=new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),
						loc.getBlockZ()<pLoc.getBlockZ()?loc.getBlockZ()+1:loc.getBlockZ()-1);
			}else {
				newLoc=new Location(loc.getWorld(),
						loc.getBlockX()<pLoc.getBlockX()?loc.getBlockX()+1:loc.getBlockX()-1
								,loc.getBlockY(),loc.getBlockZ());
			}
			
			blockLoc=newLoc.clone();
			blockLoc.setY(blockLoc.getY()-1);
			if(blockLoc.getBlock().getType()==Material.AIR)
				blockLoc.getBlock().setType(Material.STONE);
			
			if(BlockUtil.getBlockByLocationWithDeltaY(blockLoc, 1).getType()!=Material.AIR) {//要走的位置上面有障碍物
				Block tmpBlock=BlockUtil.getBlockByLocationWithDeltaY(blockLoc, 1);
				ZombieUtil.attackBlock(tmpBlock);
			}else if(BlockUtil.getBlockByLocationWithDeltaY(blockLoc, 2).getType()!=Material.AIR) {//要走的位置上面有障碍物
				Block tmpBlock=BlockUtil.getBlockByLocationWithDeltaY(blockLoc, 2);
				ZombieUtil.attackBlock(tmpBlock);
			}else {
				zombie.getPathfinder().moveTo(newLoc);
			}
		}else {//y坐标不同
			//System.out.println("else");
			if(loc.getY()>pLoc.getY()) {//向下走
				//System.out.println("向下走");
				Block unBlock=WorldUtil.getBlockUnderEntity(aiZombie.getBukkitEntity());
				ZombieUtil.attackBlock(unBlock);
				if(unBlock.getType()==Material.AIR)
					this.aiZombie.getBukkitEntity().teleport(unBlock.getLocation());
			}else if(loc.getY()+0.8<pLoc.getY()) {//向上走
				//System.out.println("向上走");
				if(BlockUtil.getBlockByLocationWithDeltaY(loc, 2).getType()!=Material.AIR) {//有障碍物
					Block tmpBlock=BlockUtil.getBlockByLocationWithDeltaY(loc, 2);
					ZombieUtil.attackBlock(tmpBlock);
				}else {
					zombie.setVelocity(new Vector(0,0.2,0));
					HandlerManager.INSTANCE.addHanlder(new Handler(){
						@Override
						public void handle() throws Exception {
							//System.out.println("STONE");
							loc.getBlock().setType(Material.STONE);
						}
					});
				}
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
		ZombiePath path=this.getCurrentPath();
		List<FocessPathPoint> points=path.getPathPoints();
		points.clear();
		Player player=ZombieUtil.findPlayer(aiZombie);
		if(player==null)return;
		Location pLoc=player.getLocation();
		points.add(new PlayerPathPoint(player,1,pLoc));//编号为1
	}


	@Override
	public void timer() {
		if(!this.aiZombie.isAlive()) {
			BetterAI.registers.remove(this);
			NavigationManager.INSTANCE.unregisterFocessNavigation(this);
			return;
		}
		FocessPathPoint point=this.getCurrentPath().getNowPathPoint();
		if(point==null){//无对象
			//TODO 一段时间后移除
		}
		this.gotoPathPoint(point);
	}


	@Override
	public boolean equals(Object ano) {
		if(ano==null||!(ano instanceof ZombieNavigation))return false;
		ZombieNavigation zb=(ZombieNavigation)ano;
		if(zb.getAIZombie().equals(this.getAIZombie()))return true;
		return false;
	}

}
