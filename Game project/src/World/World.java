/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.Camera;
import Utilities.Utils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class World {
    /**
     * The resource nodes contained in the world, they are accessed with world coordiantes.
     */
    private HashMap<Point, Node> hmNodes;
    
    /**
     * First ArrayList represents the y-axis and the second nested ArrayList the x-Axis.
     * This contains every single player placed block
     */
    private ArrayList<ArrayList<Block>> alBlocks;
    
    /**
     * The blocks that need to be updated on a regular basis.
     */
    private ArrayList<UpdatedBlock> alUpdateBlocks;
    
    /**
     * The world size.
     */
    private final int xSize, ySize;

    public World(HashMap<Point, Node> hmNodes, int xSize, int ySize) {
        this.hmNodes = hmNodes;
        this.alBlocks = new ArrayList<>();
        this.alUpdateBlocks = new ArrayList<>();
        this.xSize = xSize;
        this.ySize = ySize;
        
        ArrayList<Block> tempAl = new ArrayList<>();
        for(int x = 0; x < xSize; x++) {
            tempAl.add(null);
        }
        
        for(int y = 0; y < ySize; y++) {
            alBlocks.add(new ArrayList<>(tempAl));
        }
    }
    
    /**
     * This places a block on its desired position, if that area is free.
     * @param block 
     */
    public void placeBlock(Block block) {
        if(isAreaFree(block)) {
            if(block instanceof UpdatedBlock updatedBlock) {
                alUpdateBlocks.add(updatedBlock);
            }
            
            alBlocks.get(block.getY()).set(block.getX(), block);
            for(int y = 0; y < block.getHeight(); y++) {
                for(int x = 0; x < block.getWidth(); x++) {
                    if(x != 0 || y != 0) {
                        alBlocks.get(block.getY() + y).set(block.getX() + x, block);
                    }
                }
            }
        }
    }
    
    /**
     * This removes a block positioned on the given position
     * @param x
     * @param y 
     */
    public void removeBlock(int x, int y) {
        Block block = getBlock(x, y);
        
        if(block != null) {
            for(int lY = 0; lY < block.getHeight(); lY++) {
                for(int lX = 0; lX < block.getWidth(); lX++) {
                    Block lBlock = alBlocks.get(block.getY() + lY).set(block.getX() + lX, null);
                    if(lBlock != null) {
                        lBlock.deleteBlock();
                    }
                }
            }
        }
    }
    
    private Block getBlock(int x, int y) {
        return alBlocks.get(y).get(x);
    }
    
    /**
     * This checks whether a block can be placed in its desired area.
     * @param block
     * @return 
     */
    public boolean isAreaFree(Block block) {
        if( block.getX() + block.getWidth() > xSize || block.getY() + block.getHeight() > ySize || 
            block.getX() < 0 || block.getY() < 0) {
            return false;
        }
        
        Point p = new Point();
        for(int y = 0; y < block.getHeight(); y++) {
            for(int x = 0; x < block.getWidth(); x++) {
                p.setLocation(block.getX() + x, block.getY() + y);
                if( hmNodes.containsKey(p) && !block.isPlacableOn(hmNodes.get(p)) || 
                    getBlock(p.x, p.y) != null && !block.isPlacableOn(getBlock(p.x, p.y))) {
                    return false;
                }
            }
        }
        
        return true;
    }

    public ArrayList<UpdatedBlock> getAlUpdateBlocks() {
        return alUpdateBlocks;
    }
    
    public void draw(Camera c) {
        int offset = Generator.CHUNK_SIZE / 2;
        int startChunkX = Utils.clampInt((c.getWorldX() - c.getWorldRenderDistanceX()) - offset, 0, xSize);
        int endChunkX = Utils.clampInt(startChunkX + (c.getWorldRenderDistanceX()*2) + offset, 0, xSize);
        int startChunkY = Utils.clampInt((c.getWorldY() - c.getWorldRenderDistanceY()) - offset, 0, ySize);
        int endChunkY = Utils.clampInt(startChunkY + (c.getWorldRenderDistanceY()*2) + offset, 0, ySize);
        
        for(int y = startChunkY; y < endChunkY; y++) {
            for(int x = startChunkX; x < endChunkX; x++) {
                Block b = getBlock(x, y);
                if(b != null) {
                    b.draw(c);
                    x += b.getWidth() - 1;
                }
            }
        }
        
        for(Node node : hmNodes.values()) {
            node.draw(c);
        }
    }
    
}
