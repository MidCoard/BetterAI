package com.focess.betterai.navigation;


import com.focess.pathfinder.navigation.path.FocessPath;

public class FocessZombiePath extends FocessPath {

    @Override
    public boolean isIdle() {
        return this.getPathPoints().size() == 0 || this.isFinished();
    }

}
