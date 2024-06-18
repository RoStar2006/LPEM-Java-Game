/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Blocks.Base;

import Core.Camera;
import Core.StatsManager;
import Items.ItemType;
import World.WorldManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public class Block {
    protected int x, y;
    protected int width, height;
    private double buildTime;
    private double buildProgress;
    private double buildAnimPerc = 1.0;
    protected Color color1;
    protected Color color2;
    protected StatsManager statsManager;
    private String name = "Block";
    private BlockType blockType = BlockType.None;
    private ArrayList<Class<? extends Node>> alAllowedNodes = new ArrayList<>();
    private HashMap<ItemType, Integer> hmNeededItems = new HashMap<>();
    
    private long drawID;
    private boolean ignoreCheck = false;
    private boolean deleted = false;
    private final double healthBarHeight = 5.0 / WorldManager.BLOCK_SIZE;
    
    private boolean isBuilding = false;
    private final static Color BUILD_COLOR = new Color(64, 224, 208, 128);
    private boolean isDestructing = false;
    private final static Color DESTRUCT_COLOR = new Color(224, 64, 80, 128);
    
    private Polygon polygon = new Polygon();
    private Point polygonCenter = new Point();
    
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 1;
        this.height = 1;
        this.color1 = Color.MAGENTA;
        this.color2 = Color.WHITE;
        statsManager = new StatsManager(0);
        statsManager.setBlock(this);
    }
    
    /*
        Draw methods
    */
    
    public final void draw(Camera c) {
        if(checkIfDrawn(c)) {
            return;
        }
        
        drawID = c.getDrawID();
        if(c.isOnScreen(x, y, width, height)) {
            drawIcon(c.getGraphics(), c.worldToScreenPosX(x), c.worldToScreenPosY(y), c.scaleToZoom(width), c.scaleToZoom(height));
            c.increaseCallCount(6); // Calls 6 methods
            c.increaseDrawCount(4); // Draws 4 times
        }
        c.drawRect(x, y, width, height, Color.BLACK);
        
        if(statsManager.getMaxHealth() > 0 && isActive() && statsManager.showDamage()) {
            c.fillRect(x, y-healthBarHeight, width, healthBarHeight, Color.GRAY);
            c.fillRect(x, y-healthBarHeight, width * (statsManager.getHealth()/(double)statsManager.getMaxHealth()), healthBarHeight, Color.RED);
        }
        
        if(isDestructing) {
            drawDestructOverlay(c);
        } else if(isBuilding) {
            drawBuildingOverlay(c);
        }
    }
    
    public final void draw(Camera c, boolean ignoreCheck) {
        this.ignoreCheck = ignoreCheck;
        draw(c);
    }
    
    public final void drawBuildingOverlay(Camera c) {
        c.fillRect(x + width/2.0*(1 - buildAnimPerc), y + height/2.0*(1 - buildAnimPerc), width*buildAnimPerc, height*buildAnimPerc, BUILD_COLOR);
    }
    
    public final void drawDestructOverlay(Camera c) {
        c.fillRect(x + width/2.0*(1 - buildAnimPerc), y + height/2.0*(1 - buildAnimPerc), width*buildAnimPerc, height*buildAnimPerc, DESTRUCT_COLOR);
        c.drawRect(x, y, width, height, Color.RED);
    }
    
    public final void drawIcon(Graphics g, int x, int y, int size) {
        drawIcon(g, x, y, size, size);
    }
    
    protected void drawIcon(Graphics g, int x, int y, int width, int height) {
        int hWidth = width/2;
        int hHeight = height/2;
        
        g.setColor(color1);
        g.fillRect(x, y, hWidth, hHeight);
        g.fillRect(x+hWidth, y+hHeight, hWidth, hHeight);
        g.setColor(color2);
        g.fillRect(x+hWidth, y, hWidth, hHeight);
        g.fillRect(x, y+hHeight, hWidth, hHeight);
    }
    
    public void drawHovered(Camera c) {}
    
    /**
     * This need to be called if a block is being deleted 
     */
    public void deleteBlock() {
        deleted = true;
    }
    
    protected void initBlock(String name, double buildTime, double maxHealth, int width, int height, Color color) {
        initBlock(name, buildTime, maxHealth, width, height, color, color);
    }
    
    protected void initBlock(String name, double buildTime, double maxHealth, int width, int height, Color color1, Color color2) {
        this.name = name;
        statsManager.init(maxHealth);
        statsManager.setBlock(this);
        this.buildTime = buildTime;
        this.height = height;
        this.width = width;
        this.color1 = color1;
        this.color2 = color2;
    }
    
    /**
     * Add a node to the allowed list, which forces this block to be placed on this node.
     * @param pClass 
     */
    protected void addAllowedNode(Class<? extends Node> pClass) {
        alAllowedNodes.add(pClass);
    }
    
    protected void addNeededItem(ItemType itemType, int quantity) {
        if(quantity > 0) {
            hmNeededItems.put(itemType, quantity);
        }
    }
    
    public final boolean updateBuildProgress(int state, double deltaTime) {
        if(state == 0) {
            buildAnimPerc -= deltaTime / buildTime;
            buildProgress = Math.min(buildProgress+deltaTime, buildTime);
            return buildProgress >= buildTime;
        } else {
            buildAnimPerc += deltaTime / buildTime;
            buildProgress = Math.max(buildProgress-deltaTime, 0);
            return buildProgress <= 0;
        }
    }
    
    /**
     * Returns a triangle shaped polygon, the point facing outwards, if the block is set to have an outwards facing direction ({@link #isExitArrowStyle()}), else its point faces inwards.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param faceOutwards
     * @param angle
     * @return 
     */
    protected final Polygon getTriangle(int x, int y, int width, int height, boolean faceOutwards, double angle) {
        polygon.reset();
        
        if(faceOutwards) {
            polygon.addPoint(x+width/2, y+height); // top angle
            polygon.addPoint(x+width, y+height/2); // bottom-right angle
            polygon.addPoint(x, y+height/2); // bottom-left angle
        } else {
            polygon.addPoint(x, y+height); // bottom-left angle
            polygon.addPoint(x+width, y+height); // bottom-right angle
            polygon.addPoint(x+width/2, y+height/2); // top angle
        }
        polygonCenter.setLocation(x+width/2, y+height/2);
        rotatePolygon(polygon, angle, polygonCenter, null);
        
        return polygon;
    }
    
    /**
     * Rotates any given polygon <br>Code from: http://www.java2s.com/example/java-utility-method/polygon-rotate/rotatepolygon-polygon-pg-double-rotangle-point-centroid-polygon-original-ee480.html
     * @param pg
     * @param rotAngle
     * The angle needed is in radians
     * @param centroid
     * @param original 
     */
    public final static void rotatePolygon(Polygon pg, double rotAngle, Point centroid, Polygon original) {
        double x, y;
        
        for (int i = 0; i < pg.npoints; i++) {
            if (original != null) {
                x = original.xpoints[i] - centroid.x;
                y = original.ypoints[i] - centroid.y;
            } else {
                x = pg.xpoints[i] - centroid.x;
                y = pg.ypoints[i] - centroid.y;
            }
            pg.xpoints[i] = centroid.x + (int) Math.round(((x * Math.cos(rotAngle)) - (y * Math.sin(rotAngle))));
            pg.ypoints[i] = centroid.y + (int) Math.round(((x * Math.sin(rotAngle)) + (y * Math.cos(rotAngle))));
        }
    }
    
    /*
        Getters & Setters
    */
    
    /**
     * This method should only be used by the menu manager, to either draw a preview block or place a block!
     * @param x
     * @param y 
     */
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setBlockType(BlockType blockType) {
        this.blockType = blockType;
    }
    
    /**
     * 
     * @param block
     * @return Returns a boolean indicating if this block can be placed on another block.
     */
    public boolean isPlacableOn(Block block) {
        for(Class<? extends Node> c : alAllowedNodes) {
            if(c.isAssignableFrom(block.getClass())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isNodesOnly() {
        return !alAllowedNodes.isEmpty();
    }
    
    /**
     * This checks if a block was already drawn in that frame.
     * @param c
     * @return 
     */
    protected boolean checkIfDrawn(Camera c) {
        if(ignoreCheck) {
            ignoreCheck = false;
            return false;
        }
        return c.getDrawID() == drawID;
    }
    
    public void completeBuilding() {
        this.isBuilding = false;
    }
    
    public void build() {
        this.isBuilding = true;
    }

    public void destruct() {
        this.isDestructing = true;
    }

    public boolean isDestructing() {
        return isDestructing;
    }

    public boolean isBuilding() {
        return isBuilding;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public boolean isActive() {
        return !deleted && !isDestructing && !isBuilding;
    }
    
    public int getX() {
        return x;
    }
    
    public double getCenterX() {
        return x + width/2.0;
    }

    public int getY() {
        return y;
    }
    
    public double getCenterY() {
        return y + height/2.0;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor1() {
        return color1;
    }

    public String getName() {
        return name;
    }

    public double getBuildTime() {
        return buildTime;
    }

    public double getBuildProgress() {
        return buildProgress;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public HashMap<ItemType, Integer> getHmNeededItems() {
        return hmNeededItems;
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
