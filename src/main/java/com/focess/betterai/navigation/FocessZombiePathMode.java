package com.focess.betterai.navigation;

import com.focess.betterai.goal.ZombieBlockGoal;
import com.focess.betterai.goal.ZombieBlockPlaceGoal;
import com.focess.betterai.goal.ZombieInteractBlockGoal;
import com.focess.pathfinder.goal.GoalItem;
import com.focess.pathfinder.goal.NamedGoalItem;
import com.focess.pathfinder.navigation.FocessNavigation;
import com.focess.pathfinder.navigation.path.FocessPath;
import com.focess.pathfinder.navigation.path.mode.PathCallBackMode;
import org.bukkit.Material;

public class FocessZombiePathMode extends PathCallBackMode {


    private static final int COUNT = 200;
    private final ZombieBlockPlaceGoal zombieBlockPlaceGoal;
    private final ZombieBlockGoal zombieInteractBlockGoal;
    private int currentCount = 0;

    public FocessZombiePathMode(FocessNavigation navigation, FocessPath path, double speed) {
        super(navigation, path, speed);
        zombieBlockPlaceGoal = (ZombieBlockPlaceGoal) this.getNavigation().getFocessEntity().getGoalSelector().getGoal(new NamedGoalItem("ZombieBlockPlace")).get(0).getGoal();
        zombieInteractBlockGoal = (ZombieBlockGoal) this.getNavigation().getFocessEntity().getGoalSelector().getGoal(new NamedGoalItem("ZombieBlock")).get(0).getGoal();
    }

    @Override
    public boolean callBack() {
        if (currentCount == COUNT) {
            ((FocessZombieNavigation) this.getNavigation()).markShouldRecalculatePath();
            return false;
        }
        if (!this.isNeedCallBack)
            return true;
        boolean flag = checkPosition();
        if (flag)
            currentCount = 0;
        else currentCount++;
        return flag;
    }

    private boolean checkPosition() {
        return !targetLocation.clone().subtract(0, 1, 0).getBlock().getType().isAir() && targetLocation.getBlock().getType().isAir() && targetLocation.clone().add(0, 1, 0).getBlock().getType().isAir();
    }

    @Override
    public void move() {
        if (!this.isNeedCallBack) {
            targetLocation = this.getPath().getCurrentPathPoint().getLocation();
            this.apply();
        }
        if (this.checkPosition()) {
            this.isNeedCallBack = true;
            if (!super.callBack())
                this.getNavigation().moveTo(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ(), this.getSpeed());
            else
            if (!this.getPath().isFinished() && !this.getPath().isIdle()){
                    targetLocation = this.getPath().getCurrentPathPoint().getLocation();
                    this.apply();
                }
        }
    }

    private void apply() {
        if (targetLocation.clone().subtract(0, 1, 0).getBlock().getType().isAir())
            zombieBlockPlaceGoal.addRequest(targetLocation.clone().subtract(0, 1, 0));
        if (!targetLocation.getBlock().getType().isAir())
            zombieInteractBlockGoal.addRequest(targetLocation.clone());
        if (!targetLocation.clone().add(0, 1, 0).getBlock().getType().isAir())
            zombieInteractBlockGoal.addRequest(targetLocation.clone().add(0, 1, 0));
    }
}
