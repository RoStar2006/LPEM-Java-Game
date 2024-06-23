/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Blocks.Base.Core;
import Blocks.Base.Node;
import Core.Camera;
import Core.MainLogic;
import Enemies.Base.EnemyManager;
import Enemies.Base.SpawnRegion;
import Menus.MenuManager;
import Core.Utils;
import Projectiles.Base.ProjectileManager;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class WorldManager {
    public final static int BLOCK_SIZE = 16;
    public final static int CHUNK_SIZE = 32;
    public final static int ORE_CHUNK_SIZE = CHUNK_SIZE*2;
    public final static int SPAWN_REGION_SIZE = 24;
    public final static int FONT_SIZE = 16;
    public final static int ENEMY_BUILD_DIST = 1;
    public static int WORLD_SIZE_X, WORLD_SIZE_Y;
    /**
     * How many block can be build/destructed simultaneously
     */
    public final static int SIMUL_BD_COUNT = 3;
    
    private World world;
    private EnemyManager enemyManager;
    private ProjectileManager projectileManager;
    private MenuManager menuManager;
    private BuildManager buildManager;
    
    private final static Class[] paramTypesSub = { int.class, int.class }; // The costructor template for the node creation
    private ArrayList<Class<? extends Node>> alNodeClasses = new ArrayList<>();
    
    private double nextWave = 180; // seconds
    private double waveDelay = 150; // seconds

    public WorldManager(MenuManager menuManager) {
        this.menuManager = menuManager;
        
        initNodes();
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
            nextWave = waveDelay;
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
        String waveStr = "Next Wave: " + enemyManager.getCurWave();
        g.drawString(waveStr, (MainLogic.SCREEN_RES.x - g.getFontMetrics().stringWidth(waveStr))/2, (int)(g.getFontMetrics().getHeight()*1.75));
        g.setFont(oldFont);
    }
    
    public Core getCore() {
        return world.getCore();
    }
    
    public void generateWorld(MainLogic mainLogic, long seed, int xSize, int ySize) {
        Utils.initRandom(seed);
        WORLD_SIZE_X = xSize;
        WORLD_SIZE_Y = ySize;
        
        HashMap<Point, Node> hmNodes = new HashMap<>();
        ArrayList<SpawnRegion> alSpawnRegions = new ArrayList<>();
        enemyManager = new EnemyManager();
        projectileManager = new ProjectileManager();
        
        world = new World(xSize, ySize, hmNodes, alSpawnRegions, mainLogic, enemyManager, projectileManager);
        buildManager = new BuildManager(world);
        menuManager.init(buildManager, world);
        
        Core core = new Core(Utils.nextInt(xSize/5*2, xSize/5*3), Utils.nextInt(ySize/5*2, ySize/5*3));
        world.placeBlock(core, true);
        world.setCore(core);
        
        generateOres(world, hmNodes);
        
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
    
    private void generateOres(World world, HashMap<Point, Node> hmNodes) {
        for(Class<? extends Node> nC : alNodeClasses) {
            Node tempNode = createNode(nC, 0, 0);
            
            if(tempNode == null) {
                continue;
            }
            
            for(int chY = 0; chY < WORLD_SIZE_Y/ORE_CHUNK_SIZE; chY++) {
                for(int chX = 0; chX < WORLD_SIZE_X/ORE_CHUNK_SIZE; chX++) { // Loop through ore chunks
                    int i = 0;
                    double maxCount = tempNode.getSpawnsInChunk();
                    
                    if(maxCount % 1 != 0) { // roll a dice if the spawn count isn't a whole number
                        if(Utils.nextDouble() >= (maxCount % 1)) {
                            maxCount = Math.ceil(maxCount);
                        } else {
                            maxCount = (int)maxCount;
                        }
                    }
                    
                    while(i < maxCount) { // spawn until the max spawn count is reached
                        int x = Utils.nextInt(ORE_CHUNK_SIZE*chX, ORE_CHUNK_SIZE*(chX+1));
                        int y = Utils.nextInt(ORE_CHUNK_SIZE*chY, ORE_CHUNK_SIZE*(chY+1));
                        
                        Node node = createNode(nC, x, y);
                        if(world.isAreaFree(node)) {
                            hmNodes.put(new Point(x, y), node);
                            i++;
                        }
                    }
                }
            }
        }
    }
    
    private void initNodes() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("Blocks.Nodes").enableClassInfo().scan()) {
            alNodeClasses = new ArrayList<>(scanResult.getSubclasses(Node.class.getName()).loadClasses(Node.class));
        }
        
        if(alNodeClasses.isEmpty()) {
            System.err.println("ERROR: No nodes were found!");
        }
    }
    
    private static Node createNode(Class<? extends Node> nodeClass, int x, int y) {
        Node node = null;
        
        try {
            Object[] paramValuesSub = {x, y};
            
            node = nodeClass.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("ERROR while creating node!");
        }
        
        return node;
    }
}
