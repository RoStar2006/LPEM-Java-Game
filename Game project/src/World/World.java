/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.Camera;
import Enemies.EnemyManager;
import Enemies.SpawnRegion;
import Items.ItemType;
import Utilities.Utils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    
    private ArrayList<PlannedBlock> alPlannedBlocks;
    
    /**
     * The world size.
     */
    private final int xSize, ySize;
    
    private ArrayList<SpawnRegion> alSpawnRegions;
    private EnemyManager enemyManager;
    private Core core;

    public World(HashMap<Point, Node> hmNodes, int xSize, int ySize, EnemyManager enemyManager) {
        this.hmNodes = hmNodes;
        this.alBlocks = new ArrayList<>();
        this.alUpdateBlocks = new ArrayList<>();
        this.alSpawnRegions = new ArrayList<>();
        this.alPlannedBlocks = new ArrayList<>();
        this.xSize = xSize;
        this.ySize = ySize;
        this.enemyManager = enemyManager;
        
        ArrayList<Block> tempAl = new ArrayList<>();
        for(int x = 0; x < xSize; x++) {
            tempAl.add(null);
        }
        
        for(int y = 0; y < ySize; y++) {
            alBlocks.add(new ArrayList<>(tempAl));
        }
    }
    
    /**
     * This method handles all planned block constructions and destructions
     * @param deltaTime 
     */
    public void updatePlannedBlocks(double deltaTime) {
        for(int i = alPlannedBlocks.size()-1; i >= 0; i--) {
            PlannedBlock plannedBlock = alPlannedBlocks.get(i);
            
            if(plannedBlock.getState() == 0) { // construction
                if(plannedBlock.getBlock().isDestructing()) {
                    alPlannedBlocks.remove(i);
                } else {
                    plannedBlock.regenerateBlock(deltaTime);
                    if(plannedBlock.update(deltaTime)) {
                        plannedBlock.getBlock().completeBuilding();
                        alPlannedBlocks.remove(i);
                    }
                }
            } else { // destruction
                if(plannedBlock.update(deltaTime)) {
                    Block block = plannedBlock.getBlock();
                    
                    if(block != null) {
                        for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) {
                            core.addItem(entry.getKey(), entry.getValue());
                        }
                        
                        for(int lY = 0; lY < block.getHeight(); lY++) {
                            for(int lX = 0; lX < block.getWidth(); lX++) {
                                Block lBlock = alBlocks.get(block.getY() + lY).set(block.getX() + lX, null);
                                if(lBlock != null) {
                                    lBlock.deleteBlock();
                                }
                            }
                        }
                    }
                    
                    alPlannedBlocks.remove(i);
                }
            }
            
        }
    }
    
    /**
     * This places a block on its desired position, if that area is free.
     * @param block 
     * @param forcePlace 
     */
    public void placeBlock(Block block, boolean forcePlace) {
        if(isAreaFree(block)) {
            if(block instanceof UpdatedBlock updatedBlock) {
                alUpdateBlocks.add(updatedBlock);
            }
            boolean canPlace = true;
            
            if(!forcePlace) {
                canPlace = hasEnoughItems(block);
                
                if(canPlace) {
                    for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) {
                        core.removeItem(entry.getKey(), entry.getValue());
                    }
                    
                    alPlannedBlocks.add(new PlannedBlock(block, 0));
                    block.damage(block.getMaxHealth()-1, this);
                }
            }
            
            if(canPlace) {
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
    }
    
    /**
     * This removes a block positioned on the given position
     * @param x
     * @param y 
     * @param playerRemove 
     * @return  
     */
    public boolean removeBlock(int x, int y, boolean playerRemove) {
        Block block = getBlock(x, y);
        
        if(block != null) {
            if(block.equals(core)) {
                if(!playerRemove) {
                    System.out.println("Lost");
                }
                return false;
            }
            
            if(playerRemove) {
                if(!block.isDestructing()) {
                    alPlannedBlocks.add(new PlannedBlock(block, 1));
                }
            } else {
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
        return true;
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
        Node node = null;
        for(int y = 0; y < block.getHeight(); y++) {
            for(int x = 0; x < block.getWidth(); x++) {
                p.setLocation(block.getX() + x, block.getY() + y);
                node = hmNodes.getOrDefault(p, null);
                if( getBlock(p.x, p.y) != null ||
                    inSpawnRegion(block) ||
                    (block.isNodesOnly() && (node == null || !block.isPlacableOn(node))) ||
                    !block.isNodesOnly() && node != null ||
                    enemyManager.isEnemyOnPos(block.getX() + x, block.getY() + y)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean hasEnoughItems(Block block) {
        for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) {
            if(!core.containsItems(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean inSpawnRegion(Block block) {
        for(SpawnRegion spawnRegion : alSpawnRegions) {
            if( spawnRegion.getX() + spawnRegion.getSize() > block.getX() && 
                spawnRegion.getY() + spawnRegion.getSize() > block.getY() &&
                spawnRegion.getX() < block.getX() + block.getWidth() &&
                spawnRegion.getY() < block.getY() + block.getHeight()) {
                return true;
            }
        }
        
        return false;
    }
    
    public void addSpawnRegion(SpawnRegion spawnRegion) {
        alSpawnRegions.add(spawnRegion);
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public Block getBlock(int x, int y) {
        return alBlocks.get(y).get(x);
    }
    
    public Node getNode(int x, int y) {
        return hmNodes.getOrDefault(new Point(x, y), null);
    }

    public Core getCore() {
        return core;
    }
    
    public ArrayList<UpdatedBlock> getAlUpdateBlocks() {
        return alUpdateBlocks;
    }

    public ArrayList<SpawnRegion> getAlSpawnRegions() {
        return alSpawnRegions;
    }
    
    public void draw(Camera c) {
        for(Node node : hmNodes.values()) {
            node.draw(c);
        }
                
        int offset = WorldManager.CHUNK_SIZE / 2;
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
    }
    
}
