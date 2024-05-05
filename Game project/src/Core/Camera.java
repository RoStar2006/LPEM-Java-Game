/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

import Utilities.Utils;
import java.awt.Color;
import java.awt.Graphics;

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
    
    /*
        Game Camera
    */
    private double x, y;
    private double xDir, yDir;
    private boolean[] arrDir = new boolean[] {false, false, false, false}; // up, down, left, right
    
    private double zoom = 1;
    private double newZoom = 1;
    
    /*
        Drawing camera
    */
    private double dX, dY;
    private double dZoom;
    
    private Graphics g;
    private int drawCount = 0;
    private long start;

    /**
     * Initialize the camera with its offset from the top left corner to the middle of the screen in pixels.
     * @param xOffset
     * @param yOffset 
     */
    public Camera(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
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
            newZoom += zoomStep;
        }
        
        newZoom = Utils.clampDouble(newZoom, minZoom, maxZoom);
    }
    
    /*
        Drawing
    */
    
    private double getDrawingX() {
        return dX/dZoom - xOffset;
    }
    
    private double getDrawingY() {
        return dY/dZoom - yOffset;
    }
    
    private int worldToScreenPosX(double x) {
        return (int)Math.ceil(x / dZoom - getDrawingX());
    }
    
    private int worldToScreenPosY(double y) {
        return (int)Math.ceil(y / dZoom - getDrawingY());
    }
    
    private int scaleToZoom(double value) {
        return (int)Math.ceil(value / dZoom);
    }

    /**
     * Update the graphics attribute, reposition the drawing camera to the location of the game camera, save the current zoom and reset the draw count. <br>
     * This should be called from the draw loop and should result in significantly less tearing and artifacting.
     * @param g 
     */
    public void updateGraphics(Graphics g) {
        this.g = g;
        dX = x;
        dY = y;
        dZoom = zoom;
        drawCount = 0;
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
        g.drawLine(worldToScreenPosX(x1), worldToScreenPosY(y1), worldToScreenPosX(x2), worldToScreenPosY(y2));
        drawCount++;
    }
    
    /**
     * Draw a rectangle, positioned and scaled from world space into screen space.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void drawRect(double x, double y, double width, double height) {
        g.drawRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
        drawCount++;
    }
    
    /**
     * Draw an oval, positioned and scaled from world space into screen space. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void drawOval(double x, double y, double width, double height) {
        g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
        drawCount++;
    }
    
    /**
     * Draw a circle, positioned and scaled from world space into screen space. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size 
     */
    public void drawCircle(double x, double y, double size) {
        int d = scaleToZoom(size);
        g.drawOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
        drawCount++;
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
        g.fillRect(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
        drawCount++;
    }
    
    /**
     * Fill an oval, positioned and scaled from world space into screen space. The oval is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void fillOval(double x, double y, double width, double height) {
        g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), scaleToZoom(width), scaleToZoom(height));
        drawCount++;
    }
    
    /**
     * Fill a circle, positioned and scaled from world space into screen space. The circle is drawn from the top left corner down to the bottom right.
     * @param x
     * @param y
     * @param size 
     */
    public void fillCircle(double x, double y, double size) {
        int d = scaleToZoom(size);
        g.fillOval(worldToScreenPosX(x), worldToScreenPosY(y), d, d);
        drawCount++;
    }
    
    /**
     * Draw some debug stats concerning the camera on-screen and the drawing and game camera pos
     */
    public void drawCamera() {
        int size = scaleToZoom(20);
        g.setColor(Color.GREEN);
        g.fillOval(worldToScreenPosX(dX)-size/2, worldToScreenPosY(dY)-size/2, size, size);
        g.setColor(Color.CYAN);
        g.fillOval(worldToScreenPosX(x)-size/4, worldToScreenPosY(y)-size/4, size/2, size/2);
        g.setColor(Color.BLACK);
        g.drawLine(xOffset, 0, xOffset, yOffset*2);
        g.drawLine(0, yOffset, xOffset*2, yOffset);
        g.setColor(new Color(0, 0, 0, 128));
        g.fillRect(xOffset*2-100, 0, 100, g.getFontMetrics().getHeight()*5+1);
        
        g.setColor(Color.GREEN);
        g.drawString("Zoom: " + Utils.roundDouble(zoom, 3) + "x", xOffset*2-100, g.getFontMetrics().getHeight());
        g.drawString("X: " + Utils.roundDouble(x, 3), xOffset*2-100, g.getFontMetrics().getHeight()*2);
        g.drawString("Y: " + Utils.roundDouble(y, 3), xOffset*2-100, g.getFontMetrics().getHeight()*3);
        g.drawString("D. Calls: " + drawCount, xOffset*2-100, g.getFontMetrics().getHeight()*4);
        g.drawString("D. Time: " + Utils.roundDouble((System.nanoTime() - start)/1000000.0, 1) + "ms", xOffset*2-100, g.getFontMetrics().getHeight()*5);
    }
}
