/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Blocks.Base.Block;

/**
 *
 * @author Romeo
 */
public class PlannedBlock {
    private Block block;
    private int state;

    public PlannedBlock(Block block, int state) {
        this.block = block;
        
        if(state == 0) {
            block.build();
        } else {
            block.destruct();
        }
        this.state = state;
    }
    
    public boolean update(double deltaTime) {
        if(state == 0) { // construction
            return block.updateBuildProgress(0, deltaTime);
        } else { // destruction
            return block.updateBuildProgress(1, deltaTime);
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getState() {
        return state;
    }
    
    public void regenerateBlock(double deltaTime) {
        block.getStatsManager().hiddenRegen(deltaTime / block.getBuildTime());
    }
}
