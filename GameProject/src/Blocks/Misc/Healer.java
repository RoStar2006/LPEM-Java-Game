/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Misc;

import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Blocks.Base.UpdatedBlock;
import Core.Camera;
import Items.ItemType;
import World.World;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author RupRo053
 */
public class Healer extends UpdatedBlock {
    private int range = 5;
    private final double maxCooldown = 3;
    private double cooldown = 0;
    ArrayList<Block> alHealedBlock = new ArrayList<>();
    
    public Healer(int x, int y) {
        super(x, y);
        setBlockType(BlockType.Misc);
        initBlock("Healer", 1, 125, 1, 1, Color.GREEN, Color.YELLOW);
        setRequiredEnergy(0.5);
        addNeededItem(ItemType.CircuitBoard, 25);
        addNeededItem(ItemType.IronSheet, 15);
        addNeededItem(ItemType.Screws, 10);
    }
    
    @Override
    public void update(double deltaTime, World world) {
        if(cooldown > 0) {
            cooldown -= deltaTime;
        } else {
            cooldown = maxCooldown;
            
            alHealedBlock.clear();
            for(int lX = x-range; lX < x+range+1; lX++) {
                for(int lY = y-range; lY < y+range+1; lY++) {
                    Block block = world.getBlock(lX, lY);
                    if(block != null && !alHealedBlock.contains(block)) {
                        alHealedBlock.add(block);
                        block.getStatsManager().regenerateFlat(5);
                        block.getStatsManager().regeneratePerc(0.1/100.0);
                    }
                }
            }
        }
    }

    @Override
    public void drawHovered(Camera c) {
        c.drawRect(x-range, y-range, range*2+1, range*2+1, Color.WHITE);
    }
    
    protected void setRange(int range) {
        this.range = range;
    }
    
}
