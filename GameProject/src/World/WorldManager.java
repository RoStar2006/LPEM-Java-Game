/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Blocks.Nodes.CopperNode;
import Blocks.Base.Core;
import Blocks.Nodes.IronNode;
import Blocks.Base.Node;
import Blocks.Base.UpdatedBlock;
import Core.Camera;
import Core.MainLogic;
import Enemies.EnemyManager;
import Enemies.SpawnRegion;
import Menus.MenuManager;
import Core.Utils;
import Projectiles.Base.ProjectileManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
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
    private ProjectileManager projectileManager;
    private MenuManager menuManager;
    private BuildManager buildManager;
    
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
        world.updateBlocks(deltaTime);
        
        projectileManager.update(deltaTime, world);
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
        
        projectileManager.draw(c);
        enemyManager.draw(c);
        menuManager.draw(g);
        
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
        ArrayList<SpawnRegion> alSpawnRegions = new ArrayList<>();
        enemyManager = new EnemyManager();
        projectileManager = new ProjectileManager();
        
        world = new World(xSize, ySize, hmNodes, alSpawnRegions, enemyManager, projectileManager);
        buildManager = new BuildManager(world);
        menuManager.init(buildManager, world);
        
        Core core = new Core(Utils.nextInt(xSize/5*2, xSize/5*3), Utils.nextInt(ySize/5*2, ySize/5*3));
        world.placeBlock(core, true);
        world.setCore(core);
        
        for(int y = 0; y < ySize; y++) {
            for(int x = 0; x < xSize; x++) {
                if(Utils.nextDouble() <= 0.005) {
                    Node node;
                    
                    if(Utils.nextBoolean()) {
                        node = new IronNode(x, y);
                    } else {
                        node = new CopperNode(x, y);
                    }
                    
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
            alSpawnRegions.add(spawnRegion);
            
            if(Utils.nextBoolean()) {   
                spawnRight = !spawnRight;
            } else {
                spawnBottom = !spawnBottom;
            }
        }
        
        enemyManager.setAlSpawnRegions(alSpawnRegions);
    }
}
