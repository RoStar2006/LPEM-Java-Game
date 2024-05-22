/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

/**
 *
 * @author Romeo
 */
public class PlannedBlock {
    private Block block;
    private double curCooldown;
    private final double maxCooldown = 1;
    private int state;

    public PlannedBlock(Block block, int state) {
        this.block = block;
        if(state == 0) {
            this.curCooldown = maxCooldown;
            block.build();
        } else {
            this.curCooldown = 0;
            block.destruct();
        }
        this.state = state;
    }
    
    public boolean update(double deltaTime) {
        if(state == 0) {
            curCooldown -= deltaTime;
            return curCooldown <= 0;
        } else {
            curCooldown += deltaTime;
            return curCooldown >= maxCooldown; 
        }
    }

    public Block getBlock() {
        return block;
    }

    public int getState() {
        return state;
    }
    
    public void regenerateBlock(double deltaTime) {
        block.regenerate(block.getMaxHealth() * deltaTime);
    }
}
