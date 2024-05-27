/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import World.WorldManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * This class contains the game and drawing camera. <br>
 * The game camera is updated by calling {@link #move()} and the drawing camera is updated by calling {@link #updateGraphics(Graphics)}. <br>
 * Additionally this class contains methods for drawing objects from the game world onto the screen by converting their positions and sizes. 
 * @author Romeo
 */
public class Camera {
    /*
        Camera settings
    */
    private final int xOffset, yOffset;
    private final double dirLerpValue = 4;
    private final double zoomLerpValue = 5;
    private final double minZoom = 0.5;
    private final double maxZoom = 2;
    private final double zoomStep = 0.25;
    private final int speed = 200; // px/s
    private final double renderDistanceX, renderDistanceY;
    private final double fontRatio = WorldManager.FONT_SIZE / (double)WorldManager.BLOCK_SIZE;
    
    /*
        Game Camera
    */
    private double x, y;
    private double xDir, yDir;
    private boolean[] arrDir = new boolean[] {false, false, false, false}; // up, down, left, right
    
    private double zoom = 1;
    private double newZoom = 1;
    
    private double mouseX, mouseY;
    
    /*
        Drawing camera
    */
    private double dX, dY;
    private double dZoom;
    private double blockPixelRatio = 0;
    private long drawID = 0;
    private long delayedDrawID = 0;
    
    private int cX, cY;
    private int cRDX, cRDY;
    
    private Graphics g;
    private int drawCount = 0;
    private int callCount = 0;
    private long start;
    private double drawDeltaTime = 0;

    /**
     * Initialize the camera with its offset from the top left corner to the middle of the screen in pixels.
     * @param xOffset
     * @param yOffset 
     */
    public Camera(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        
        renderDistanceX = xOffset / WorldManager.BLOCK_SIZE * 1.1;
        renderDistanceY = yOffset / WorldManager.BLOCK_SIZE * 1.1;
        
        start = System.nanoTime();
    }
    
    public void setPos(double x, double y) {
        this.x = x * WorldManager.BLOCK_SIZE;
        this.y = y * WorldManager.BLOCK_SIZE;
    }
    
    public void setZoom(double zoom) {
        this.zoom = zoom;
    }
    
    /*
        Movement
    */
    /**
     * Updates the camera movement direction based on the registered keystrokes. <br>
     * The camera movement and zoom are both smoothed out.
     */
    public void move() {
        double newXDir = 0;
        double newYDir = 0;
        
        if(arrDir[2] && !arrDir[3]) {
            newXDir = -1;
        } else if(!arrDir[2] && arrDir[3]) {
            newXDir = 1;
        }
        
        if(arrDir[0] && !arrDir[1]) {
            newYDir = -1;
        } else if(!arrDir[0] && arrDir[1]) {
            newYDir = 1;
        }
        
        if(newXDir != 0 && newYDir != 0) {
            newXDir /= Math.sqrt(2);
            newYDir /= Math.sqrt(2);
        }
        
        double deltaTime = MainLogic.getDeltaTime();
        xDir = Utils.lerpDouble(xDir, newXDir, dirLerpValue*deltaTime);
        yDir = Utils.lerpDouble(yDir, newYDir, dirLerpValue*deltaTime);
        
        x += xDir * speed * zoom * deltaTime;
        y += yDir * speed * zoom * deltaTime;
        
        zoom = Utils.lerpDouble(zoom, newZoom, zoomLerpValue*deltaTime);
        
        if(x < 0) {
            x = Utils.lerpDouble(x, 0, 3*deltaTime);
        } else if(x > WorldManager.BLOCK_SIZE * WorldManager.WORLD_SIZE_X) {
            x = Utils.lerpDouble(x, WorldManager.BLOCK_SIZE * WorldManager.WORLD_SIZE_X, 3*deltaTime);
        }
        
        if(y < 0) {
            y = Utils.lerpDouble(y, 0, 3*deltaTime);
        } else if(y > WorldManager.BLOCK_SIZE * WorldManager.WORLD_SIZE_Y) {
            y = Utils.lerpDouble(y, WorldManager.BLOCK_SIZE * WorldManager.WORLD_SIZE_Y, 3*deltaTime);
        }
    }
    
    /**
     * Change the boolean of the movement input.
     * <ol>
     * <li>W</li>
     * <li>S</li>
     * <li>A</li>
     * <li>D</li>
     * </ol>
     * @param i
     * @param value 
     */
    public void changeDir(int i, boolean value) {
        arrDir[i] = value;
    }
    
    /**
     * Changes the zoom based on the given direction.
     * @param scrollDir 
     */
    public void changeZoom(int scrollDir) {
        if(scrollDir < 0) {
            if(newZoom <= 1) {
                newZoom -= zoomStep/2;
            } else {
                newZoom -= zoomStep;
            }
        } else if(scrollDir > 0) {
            if(newZoom < 1) {
                newZoom += zoomStep/2;
            } else {
                newZoom += zoomStep;
            }
        }
        
        newZoom = Utils.clampDouble(newZoom, minZoom, maxZoom);
    }
    
    /**
     * 
     * @return The camera's x position in world space
     */
    public int getWorldX() {
        return (int)Math.ceil(x / WorldManager.BLOCK_SIZE);
    }
    
    /**
     * 
     * @return  The camera's y position in world space
     */
    public int getWorldY() {
        return (int)Math.ceil(y / WorldManager.BLOCK_SIZE);
    }

    /**
     * 
     * @return The camera's x render distance in one direction
     */
    public int getWorldRenderDistanceX() {
        return (int)Math.ceil(renderDistanceX * zoom);
    }
    
    /**
     * 
     * @return The camera's y render distance in one direction
     */
    public int getWorldRenderDistanceY() {
        return (int)Math.ceil(renderDistanceY * zoom);
    }

    /**
     * 
     * @return The current drawing id
     */
    public long getDrawID() {
        return drawID;
    }
    
    /**
     * 
     * @return The current delayed drawing id
     */
    public long getDelayedDrawID() {
        return delayedDrawID;
    }
    
    /*
        Drawing
    */
    
    public int worldToScreenPosX(double x) {
        return (int)Math.ceil(x * blockPixelRatio - dX);
    }
    
    public int worldToScreenPosY(double y) {
        return (int)Math.ceil(y * blockPixelRatio - dY);
    }
    
    private int scaleToZoom(double value) {
        return (int)(Math.ceil(value * blockPixelRatio));
    }
    
    public void updateMousePos(int x, int y) {
        mouseX = x;
        mouseY = y;
    }
    
    public double getMouseToWorldPosX() {
        return Utils.clampDouble(((this.x + (mouseX - xOffset) * zoom) / WorldManager.BLOCK_SIZE), 0, WorldManager.WORLD_SIZE_X-1);
    }
    
    public int getMouseToWorldGridPosX() {
        return Utils.clampInt((int)(Math.ceil(this.x + (mouseX - xOffset) * zoom) / WorldManager.BLOCK_SIZE), 0, WorldManager.WORLD_SIZE_X-1);
    }
    
    public double getMouseToWorldPosY() {
        return Utils.clampDouble(((this.y + (mouseY - yOffset) * zoom) / WorldManager.BLOCK_SIZE), 0, WorldManager.WORLD_SIZE_Y-1);
    }
    
    public int getMouseToWorldGridPosY() {
        return Utils.clampInt((int)(Math.ceil(this.y + (mouseY - yOffset) * zoom) / WorldManager.BLOCK_SIZE), 0, WorldManager.WORLD_SIZE_Y-1);
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }
    
    private boolean isOnScreen(double x, double y, double w, double h) {
        return x + w >= cX-cRDX && x <= cX+cRDX && 
               y + h >= cY-cRDY && y <= cY+cRDY;
    }

    public double getDrawDeltaTime() {
        return drawDeltaTime;
    }

    /**
     * Update the graphics attribute, reposition the drawing camera to the location of the game camera, save the current zoom and reset the draw count. <br>
     * This should be called from the draw loop and should result in significantly less tearing and artifacting.
     * @param g 
     */
    public void updateGraphics(Graphics g) {
        this.g = g;
        dZoom = zoom;
        dX = x / dZoom - xOffset;
        dY = y / dZoom - yOffset;
        blockPixelRatio = WorldManager.BLOCK_SIZE / dZoom;
        cX = getWorldX();
        cRDX = getWorldRenderDistanceX();
        cY = getWorldY();
        cRDY = getWorldRenderDistanceY();
        drawID = Utils.nextLong();
        delayedDrawID = Utils.nextLong();
                
        drawCount = 0;
        callCount = 0;
        
        drawDeltaTime = (System.nanoTime() - start) / Math.pow(10, 9) * MainLogic.GAME_SPEED;
        start = System.nanoTime();
    }
    
    /**
     * Sets the color of the graphics.
     * @param color 
     */
    public void setColor(Color color) {
        g.setColor(color);
        drawCount++;
    }
    
    /*
        Graphics draw wrappers
    */
    
    /**
     * Draw a line, positioned and scaled from world space into screen space.
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public void drawLine(double x1, double y1, double x2, double y2) {
        if(isOnScreen(x1, y1, x2 - x1, y2 - y1)) {
            g.drawLine(worldToScreenPosX(x1), worldToScreenPosY(y1), worldToScreenPosX(x2), worldToScreenPosY(y2));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a line, positioned and scaled from world space into screen space using a custom color.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param color 
     */
    public void drawLine(double x1, double y1, double x2, double y2, Color color) {
        if(isOnScreen(x1, y1, x2 - x1, y2 - y1)) {
            setColor(color);
            g.drawLine(worldToScreenPosX(x1), worldToScreenPosY(y1), worldToScreenPosX(x2), worldToScreenPosY(y2));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a line, positioned and scaled from world space into screen space using a custom color.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param thickness
     * @param color 
     */
    public void drawLine(double x1, double y1, double x2, double y2, int thickness, Color color) {
        if(isOnScreen(x1, y1, x2 - x1, y2 - y1)) {
            setColor(color);
            Graphics2D g2d = (Graphics2D)g;
            Stroke oldThickness = g2d.getStroke();
            g2d.setStroke(new BasicStroke((float)(thickness/dZoom)));
            g.drawLine(worldToScreenPosX(x1), worldToScreenPosY(y1), worldToScreenPosX(x2), worldToScreenPosY(y2));
            g2d.setStroke(oldThickness);
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a rectangle, positioned and scaled from world space into screen space.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void drawRect(double x, double y, double width, double height) {
        if(isOnScreen(x, y, width, height)) {
            g.drawRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a rectangle, positioned and scaled from world space into screen space using a custom color.
     * @param x
     * @param y
     * @param width
     * @param height 
     * @param color 
     */
    public void drawRect(double x, double y, double width, double height, Color color) {
        if(isOnScreen(x, y, width, height)) {
            setColor(color);
            g.drawRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw an oval, positioned and scaled from world space into screen space. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void drawOval(double x, double y, double width, double height) {
        if(isOnScreen(x, y, width, height)) {
            g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw an oval, positioned and scaled from world space into screen space using a custom color. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     * @param color
     */
    public void drawOval(double x, double y, double width, double height, Color color) {
        if(isOnScreen(x, y, width, height)) {
            setColor(color);
            g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a circle, positioned and scaled from world space into screen space. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size 
     */
    public void drawCircle(double x, double y, double size) {
        if(isOnScreen(x, y, size, size)) {
            int d = scaleToZoom(size);
            g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a circle, positioned and scaled from world space into screen space using a custom color. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size  
     * @param color
     */
    public void drawCircle(double x, double y, double size, Color color) {
        if(isOnScreen(x, y, size, size)) {
            setColor(color);
            int d = scaleToZoom(size);
            g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw a string, positioned and scaled from world space into screen space using a custom color. The string is drawn from the top left corner down to the bottom right.
     * @param str
     * @param x
     * @param y
     * @param color 
     * @return  The used font
     */
    public Font drawString(String str, double x, double y, Color color) {
        Font backupFont = g.getFont();
        Font usedFont = backupFont.deriveFont((float)(WorldManager.FONT_SIZE/dZoom));
        g.setFont(usedFont);
        setColor(color);
        g.drawString(str, worldToScreenPosX(x), worldToScreenPosY(y + WorldManager.FONT_SIZE / (double)WorldManager.BLOCK_SIZE));
        g.setFont(backupFont);
        drawCount++;
        callCount++;
        return usedFont;
    }
    
    /**
     * Draw a string, positioned from world space into screen space using a custom color. The string is drawn from the top left corner down to the bottom right.
     * @param str
     * @param x
     * @param y
     * @param color 
     * @return  The used font
     */
    public Font drawRawString(String str, double x, double y, Color color) {
        Font backupFont = g.getFont();
        Font usedFont = backupFont.deriveFont((float)WorldManager.FONT_SIZE);
        g.setFont(usedFont);
        setColor(color);
        g.drawString(str, worldToScreenPosX(x), (int)(worldToScreenPosY(y) + WorldManager.FONT_SIZE));
        g.setFont(backupFont);
        drawCount++;
        callCount++;
        return usedFont;
    }
    
    /**
     * Draw a string, positioned from world space into screen space using a custom color. The string is drawn from the top left corner down to the bottom right. <br>
     * Its y position is offset, so that it is the nth item in a drawn list.
     * @param str
     * @param x
     * @param y
     * @param number
     * @param color
     * @return The used font
     */
    public Font drawRawListString(String str, double x, double y, int number, Color color) {
        callCount++;
        return drawRawString(str, x, y + fontRatio * number * zoom, color);
    }
    
    /**
     * 
     * @return The scaled font that would be used for text
     */
    public Font getFont() {
        return g.getFont().deriveFont((float)(WorldManager.FONT_SIZE/dZoom));
    }
    
    /**
     * 
     * @return The unscaled "raw" font that would be used for text
     */
    public Font getRawFont() {
        return g.getFont().deriveFont((float)WorldManager.FONT_SIZE);
    }
    
    /*
        Graphics fill wrappers
    */
    
    /**
     * Fill a rectangle, positioned and scaled from world space into screen space.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void fillRect(double x, double y, double width, double height) {
        if(isOnScreen(x, y, width, height)) {
            g.fillRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Fill a rectangle, positioned and scaled from world space into screen space using a custom color.
     * @param x
     * @param y
     * @param width
     * @param height 
     * @param color
     */
    public void fillRect(double x, double y, double width, double height, Color color) {
        if(isOnScreen(x, y, width, height)) {
            setColor(color);
            g.fillRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Fill an oval, positioned and scaled from world space into screen space. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void fillOval(double x, double y, double width, double height) {
        if(isOnScreen(x, y, width, height)) {
            g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Fill an oval, positioned and scaled from world space into screen space using a custom color. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     * @param color 
     */
    public void fillOval(double x, double y, double width, double height, Color color) {
        if(isOnScreen(x, y, width, height)) {
            setColor(color);
            g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Fill a circle, positioned and scaled from world space into screen space. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size 
     */
    public void fillCircle(double x, double y, double size) {
        if(isOnScreen(x, y, size, size)) {
            int d = scaleToZoom(size);
            g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Fill a circle, positioned and scaled from world space into screen space using a custom color. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size 
     * @param color 
     */
    public void fillCircle(double x, double y, double size, Color color) {
        if(isOnScreen(x, y, size, size)) {
            setColor(color);
            int d = scaleToZoom(size);
            g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
            drawCount++;
        }
        callCount++;
    }
    
    /**
     * Draw some debug stats concerning the camera on-screen and the drawing and game camera pos
     */
    public void drawCamera() {
        int size = scaleToZoom(20) / WorldManager.BLOCK_SIZE;
        g.setColor(Color.GREEN);
        g.fillOval(xOffset-size/2, yOffset-size/2, size, size);
        g.setColor(Color.CYAN);
        g.fillOval(worldToScreenPosX(x / WorldManager.BLOCK_SIZE)-size/4, worldToScreenPosY(y / WorldManager.BLOCK_SIZE)-size/4, size/2, size/2);
        g.setColor(Color.BLACK);
        g.drawLine(xOffset, 0, xOffset, yOffset*2);
        g.drawLine(0, yOffset, xOffset*2, yOffset);
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(xOffset*2-100, 0, 100, g.getFontMetrics().getHeight()*8+1);
        
        g.setColor(Color.GREEN);
        g.drawString("Zoom: " + Utils.roundDouble(zoom, 3) + "x", xOffset*2-100, g.getFontMetrics().getHeight());
        /*g.drawString("X: " + Utils.roundDouble(x, 3), xOffset*2-100, g.getFontMetrics().getHeight()*2);
        g.drawString("Y: " + Utils.roundDouble(y, 3), xOffset*2-100, g.getFontMetrics().getHeight()*3);*/
        g.drawString("X: " + getWorldX(), xOffset*2-100, g.getFontMetrics().getHeight()*2);
        g.drawString("Y: " + getWorldY(), xOffset*2-100, g.getFontMetrics().getHeight()*3);
        g.drawString("D. Calls: " + drawCount, xOffset*2-100, g.getFontMetrics().getHeight()*4);
        g.drawString("M. Calls: " + callCount, xOffset*2-100, g.getFontMetrics().getHeight()*5);
        g.drawString("D. Time: " + Utils.roundDouble((System.nanoTime() - start)/1000000.0, 1) + "ms", xOffset*2-100, g.getFontMetrics().getHeight()*6);
        g.drawString("Mouse X: " + getMouseToWorldGridPosX(), xOffset*2-100, g.getFontMetrics().getHeight()*7);
        g.drawString("Mouse Y: " + getMouseToWorldGridPosY(), xOffset*2-100, g.getFontMetrics().getHeight()*8);
    }
}
