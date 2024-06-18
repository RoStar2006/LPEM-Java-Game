/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Blocks.Base.Core;
import Blocks.Base.Block;
import Blocks.Base.BlockType;
import Blocks.Base.Node;
import Blocks.Base.NodeCollector;
import Blocks.Base.RecipeBlock;
import Blocks.Base.UpdatedBlock;
import Core.Camera;
import Enemies.Base.EnemyManager;
import Enemies.Base.SpawnRegion;
import Items.ItemType;
import Core.Utils;
import Projectiles.Base.ProjectileManager;
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
    
    /**
     * The blocks whose {@link UpdatedBlock#delayedDraw(Core.Camera, World.World)} should be called
     */
    private ArrayList<UpdatedBlock> alDelayedDraws = new ArrayList<>();
    
    private ArrayList<PlannedBlock> alPlannedBlocks;
    
    /**
     * The world size.
     */
    private final int xSize, ySize;
    
    private ArrayList<SpawnRegion> alSpawnRegions;
    private EnemyManager enemyManager;
    private ProjectileManager projectileManager;
    private Core core;

    public World(int xSize, int ySize, HashMap<Point, Node> hmNodes, ArrayList<SpawnRegion> alSpawnRegions, EnemyManager enemyManager, ProjectileManager projectileManager) {
        this.hmNodes = hmNodes;
        this.alBlocks = new ArrayList<>();
        this.alUpdateBlocks = new ArrayList<>();
        this.alSpawnRegions = new ArrayList<>();
        this.alPlannedBlocks = new ArrayList<>();
        this.alSpawnRegions = alSpawnRegions;
        this.xSize = xSize;
        this.ySize = ySize;
        this.enemyManager = enemyManager;
        this.projectileManager = projectileManager;
        
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
        int maxIndex = Math.min(alPlannedBlocks.size(), WorldManager.SIMUL_BD_COUNT);
        
        for(int i = 0; i < maxIndex; i++) {
            PlannedBlock plannedBlock = alPlannedBlocks.get(i);

            if(updatePlannedBlock(plannedBlock, deltaTime)) {
                alPlannedBlocks.remove(i);
                i--;
                maxIndex--;
            }
        }
    }
    
    private boolean updatePlannedBlock(PlannedBlock plannedBlock, double deltaTime) {
        boolean result = false;
        
        if(plannedBlock.getBlock().getStatsManager().isDead()) {
            result = true;
        } else if(plannedBlock.getState() == 0) { // construction
            if(plannedBlock.getBlock().isDestructing()) {
                result = true;
            } else {
                plannedBlock.regenerateBlock(deltaTime);
                if(plannedBlock.update(deltaTime)) {
                    plannedBlock.getBlock().completeBuilding();
                    result = true;
                }
            }
        } else { // destruction
            if(plannedBlock.update(deltaTime)) {
                Block block = plannedBlock.getBlock();

                if(block != null) {
                    refundBlock(block);
                }

                result = true;
            }
        }
        
        return result;
    }
    
    /**
     * This method updates all the blocks in the {@link #alUpdateBlocks} list
     * @param deltaTime
     */
    public void updateBlocks(double deltaTime) {
        for(int i = alUpdateBlocks.size()-1; i >= 0; i--) {
            UpdatedBlock updatedBlock = alUpdateBlocks.get(i);
            
            if(updatedBlock == null || updatedBlock.isDeleted()) {
                alUpdateBlocks.remove(i);
            } else if(updatedBlock.isActive()) {
                updatedBlock.update(deltaTime, this);
            }
        }
    }
    
    /**
     * This places a block on its desired position, if that area is free.
     * @param block 
     * @param forcePlace 
     */
    public void placeBlock(Block block, boolean forcePlace) {
        boolean areaFree = isAreaFree(block);
        if(areaFree || isReplacable(block)) { // If area is free or replacable
            if(block instanceof UpdatedBlock updatedBlock) {
                alUpdateBlocks.add(updatedBlock);
            }
            boolean canPlace = true;
            
            if(!forcePlace) {
                canPlace = hasEnoughItems(block);
                
                if(canPlace) {
                    if(!areaFree) { // Area not free, so refund it, to replace it
                        refundBlock(block);
                    }
                    
                    for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) {
                        core.removeItem(entry.getKey(), entry.getValue());
                    }

                    alPlannedBlocks.add(new PlannedBlock(block, 0));
                    block.getStatsManager().setToOneHP();
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

                if(block instanceof NodeCollector nodeCollector) {
                    nodeCollector.setNode(getNode(block.getX(), block.getY()));
                }
            }
        }
    }
    
    private void refundBlock(Block block) {
        for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) { // refund the old items
            core.addItem(entry.getKey(), entry.getValue(), null);
        }
        
        if(block instanceof UpdatedBlock updatedBlock) { // add the inventory to the core
            for(Map.Entry<ItemType, Integer> entry : updatedBlock.getInventory().entrySet()) { // refund the old items
                core.addItem(entry.getKey(), entry.getValue(), null);
            }
            
            if(updatedBlock instanceof RecipeBlock recipeBlock) {
                for(Map.Entry<ItemType, Integer> entry : recipeBlock.getResultInventory().entrySet()) { // refund the old items
                    core.addItem(entry.getKey(), entry.getValue(), null);
                }
            }
        }
        
        removeBlock(block.getX(), block.getY(), false);
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
        
        for(int y = 0; y < block.getHeight(); y++) {
            for(int x = 0; x < block.getWidth(); x++) {
                p.setLocation(block.getX() + x, block.getY() + y);
                Node node = hmNodes.getOrDefault(p, null);
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
    
    public boolean isReplacable(Block block) {
        if(block.getBlockType() == BlockType.None) { // Block type "None" are not replacable
            return false;
        }
        
        Block lB = getBlock(block.getX(), block.getY());
        if(lB == null || lB.getBlockType() != block.getBlockType()) { // Block type not matching
            return false;
        }
        
        if(lB.getWidth() != block.getWidth() || lB.getHeight() != block.getHeight() || 
           lB.getX() != block.getX() || lB.getY() != block.getY()) { // Postition and size have to match exactly
            return false;
        }
        
        if(block instanceof UpdatedBlock ub && ub.hasDirection() && lB instanceof UpdatedBlock lUB && lUB.hasDirection()) { // Blocks with directions have to point in different directions
            if(!block.getClass().equals(lB.getClass())) { // Different block class
                return true;
            } else { // Check in which direction they are pointing
                return !ub.getDirection().equals(lUB.getDirection());
            }
        }
        
        return !block.getClass().equals(lB.getClass()); // Block has to be a different class
    }
    
    /**
     * Check if the {@link #core} has enough items to build a given structure once
     * @param block
     * @return 
     */
    public boolean hasEnoughItems(Block block) {
        return hasEnoughItems(block, 1);
    }
    
    /**
     * Check if the {@link #core} has enough items to build a given structure n times
     * @param block
     * @param blockCount
     * @return 
     */
    public boolean hasEnoughItems(Block block, int blockCount) {
        for(Map.Entry<ItemType, Integer> entry : block.getHmNeededItems().entrySet()) {
            if(!core.containsItems(entry.getKey(), entry.getValue()*blockCount)) {
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

    public void setCore(Core core) {
        this.core = core;
    }

    public Block getBlock(int x, int y) {
        if( x < 0 || x >= WorldManager.WORLD_SIZE_X ||
            y < 0 || y >= WorldManager.WORLD_SIZE_Y) {
            return null;
        }
        
        return alBlocks.get(y).get(x);
    }
    
    public Node getNode(int x, int y) {
        return hmNodes.getOrDefault(new Point(x, y), null);
    }

    public Core getCore() {
        return core;
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ProjectileManager getProjectileManager() {
        return projectileManager;
    }
    
    public void draw(Camera c) {
        for(Node node : hmNodes.values()) { // Draw the nodes
            node.draw(c);
        }
        
        alDelayedDraws.clear();
        
        // Draw all the player placed blocks, but only those in render distance
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
                    if(b instanceof UpdatedBlock updatedBlock) {
                        alDelayedDraws.add(updatedBlock);
                    }
                }
            }
        }
        
        // Execute all the delayed draw of the blocks in the render distance
        for(UpdatedBlock updatedBlock : alDelayedDraws) {
            if(updatedBlock != null) {
                updatedBlock.delayedDraw(c, this);
            }
        }
        
        // Draw the spawn region
        for(SpawnRegion spawnRegion : alSpawnRegions) {
            spawnRegion.draw(c);
        }
    }
    
}
