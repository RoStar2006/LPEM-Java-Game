/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.Camera;
import Core.MainLogic;
import Enemies.EnemyManager;
import Enemies.SpawnRegion;
import Menus.MenuManager;
import Utilities.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class WorldManager {
    public final static int BLOCK_SIZE = 16;
    public final static int CHUNK_SIZE = 32;
    public final static int SPAWN_REGION_SIZE = 24;
    public final static int FONT_SIZE = 16;
    public final static int ENEMY_BUILD_DIST = 1;
    public static int WORLD_SIZE_X, WORLD_SIZE_Y;
    
    private World world;
    private EnemyManager enemyManager;
    private MenuManager menuManager;
    
    private double nextWave = 1; // seconds

    public WorldManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }
    
    public void update(double deltaTime) {
        if(world == null) {
            return;
        }
        
        nextWave -= deltaTime;
        
        world.updatePlannedBlocks(deltaTime);
        
        for(int i = world.getAlUpdateBlocks().size()-1; i >= 0; i--) {
            UpdatedBlock updatedBlock = world.getAlUpdateBlocks().get(i);
            
            if(updatedBlock == null || updatedBlock.isDeleted()) {
                world.getAlUpdateBlocks().remove(i);
                continue;
            }
            
            updatedBlock.update(deltaTime);
        }
        
        enemyManager.update(deltaTime, world);
        
        if(nextWave <= 0) {
            enemyManager.spawnEnemies(world.getCore());
            nextWave = 120;
        }
    }
    
    public void draw(Camera c, Graphics g) {
        if(world == null) {
            return;
        }
        
        world.draw(c);
        
        for(SpawnRegion spawnRegion : world.getAlSpawnRegions()) {
            spawnRegion.draw(c);
        }
        
        enemyManager.draw(c);
        menuManager.draw(c, g);
        
        drawWaveTimer(g);
    }
    
    private void drawWaveTimer(Graphics g) {
        String timeStr = (int)(nextWave/60) + ":";
        String tempSec = (int)(nextWave%60) + "";
        while(tempSec.length() < 2) {
            tempSec = "0" + tempSec;
        }
        
        timeStr += tempSec;
        g.setColor(Color.WHITE);
        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(24f));
        g.drawString(timeStr, (MainLogic.SCREEN_RES.x - g.getFontMetrics().stringWidth(timeStr))/2, g.getFontMetrics().getHeight());
        g.setFont(oldFont);
    }
    
    public Core getCore() {
        return world.getCore();
    }
    
    public void generateWorld(long seed, int xSize, int ySize) {
        Utils.initRandom(seed);
        WORLD_SIZE_X = xSize;
        WORLD_SIZE_Y = ySize;
        
        HashMap<Point, Node> hmNodes = new HashMap<>();
        enemyManager = new EnemyManager();
        world = new World(hmNodes, xSize, ySize, enemyManager);
        menuManager.setWorld(world);
        
        Core core = new Core(Utils.nextInt(xSize/5*2, xSize/5*3), Utils.nextInt(ySize/5*2, ySize/5*3));
        world.placeBlock(core, true);
        world.setCore(core);
        
        for(int y = 0; y < ySize; y++) {
            for(int x = 0; x < xSize; x++) {
                if(Utils.nextDouble() <= 0.01) {
                    Node node = new Node(x, y);
                    if(world.isAreaFree(node)) {
                        hmNodes.put(new Point(x, y), node);
                    }
                }
            }
        }
        
        boolean spawnRight = Utils.nextBoolean(); 
        boolean spawnBottom = Utils.nextBoolean(); 
        for(int i = 0; i < 2; i++) {
            int x = (spawnRight) ? xSize-SPAWN_REGION_SIZE : 0;
            int y = (spawnBottom) ? ySize-SPAWN_REGION_SIZE : 0;
            SpawnRegion spawnRegion = new SpawnRegion(x, y, SPAWN_REGION_SIZE);
            world.addSpawnRegion(spawnRegion);
            
            if(Utils.nextBoolean()) {   
                spawnRight = !spawnRight;
            } else {
                spawnBottom = !spawnBottom;
            }
        }
        enemyManager.setAlSpawnRegions(world.getAlSpawnRegions());
    }
}
