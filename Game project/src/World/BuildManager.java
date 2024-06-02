/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Blocks.Block;
import Blocks.ConveyorBelt;
import Blocks.UpdatedBlock;
import Core.Camera;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Romeo
 */
public class BuildManager {
    private World world;
    private final Color areaNotFreeColor = new Color(255, 0 ,0 , 126);
    private final Color placeColor = new Color(245, 245, 245, 126);
    private final Color removeColor = Color.RED;
    private Block selectedBlock;
    private final Class[] paramTypesSub = { int.class, int.class }; // The costructor template for the block creation
    
    public BuildManager(World world) {
        this.world = world;
    }
    
    /*
        Drawing
    */
    
    public void draw(Camera c, Point2D.Double placeStartPoint, boolean rotatedBlock, Point removeStartPoint) {
        drawRemoveArea(c, removeStartPoint);
        drawSelectedBlocks(c, placeStartPoint, rotatedBlock);
    }
    
    private void drawRemoveArea(Camera c, Point removeStartPoint) {
        if(removeStartPoint != null) {
            int x = removeStartPoint.x;
            int y = removeStartPoint.y;
            int xSize = c.getMouseToWorldGridPosX() - removeStartPoint.x;
            int ySize = c.getMouseToWorldGridPosY() - removeStartPoint.y;
            int xSign = (int)Math.signum(xSize+0.1);
            int ySign = (int)Math.signum(ySize+0.1);
            
            if(xSign < 0) {
                x += xSize;
                xSize *= -1;
                xSign *= -1;
            }
            
            if(ySign < 0) {
                y += ySize;
                ySize *= -1;
                ySign *= -1;
            }
            
            c.drawRect(x, y, xSize+xSign, ySize+ySign, removeColor);
        }
    }
    
    private void drawSelectedBlocks(Camera c, Point2D.Double placeStartPoint, boolean rotatedBlock) {
        if(selectedBlock == null) {
            return;
        }
        
        UpdatedBlock updatedBlock = null;
        if(selectedBlock instanceof UpdatedBlock lUpdatedBlock) {
            updatedBlock = lUpdatedBlock;
        }
        
        if(placeStartPoint != null) { // Draw preview blocks for the placement
            double xSize = c.getMouseToWorldPosX() - placeStartPoint.x;
            double ySize = c.getMouseToWorldPosY() - placeStartPoint.y;
            
            if(Math.abs(xSize) >= Math.abs(ySize)) {
                int xSign = (int)Math.signum(xSize);
                
                if(!rotatedBlock && Math.abs(xSize) > 1 && updatedBlock != null && updatedBlock.hasDirection()) { // rotate blocks, if not rotated manually
                    updatedBlock.setDirection(new Point(xSign, 0));
                }
                
                for(int i = 0; i <= Math.abs(xSize); i += selectedBlock.getWidth()) { // draw block
                    drawSelectedBlock(c, selectedBlock, (int)Math.round(placeStartPoint.x+i*xSign - selectedBlock.getWidth()/2.0), (int)Math.round(placeStartPoint.y - selectedBlock.getHeight()/2.0), i/selectedBlock.getWidth()+1);
                }
            } else {
                int ySign = (int)Math.signum(ySize);
                
                if(!rotatedBlock && Math.abs(ySize) > 1  && updatedBlock != null && updatedBlock.hasDirection()) { // rotate blocks, if not rotated manually
                    updatedBlock.setDirection(new Point(0, ySign));
                }
                
                for(int i = 0; i <= Math.abs(ySize); i += selectedBlock.getHeight()) { // draw block
                    drawSelectedBlock(c, selectedBlock, (int)Math.round(placeStartPoint.x - selectedBlock.getWidth()/2.0), (int)Math.round(placeStartPoint.y+i*ySign - selectedBlock.getHeight()/2.0), i/selectedBlock.getHeight()+1);
                }
            }
        } else { // draw a single block under the mouse cursor
            drawSelectedBlock(c, selectedBlock, (int)Math.round(c.getMouseToWorldPosX() - selectedBlock.getWidth()/2.0), (int)Math.round(c.getMouseToWorldPosY() - selectedBlock.getHeight()/2.0), 1);
        }
        
        if(updatedBlock != null && updatedBlock.hasDirection()) { // draw the arrow when placing conveyor belts
            drawArrow(c, updatedBlock.getDirection(), updatedBlock.isExitArrowStyle());
        }
    }
    
    private void drawArrow(Camera c, Point direction, boolean exitStyle) {
        double arrowSize = 0.75;
        int arrowThickness = 3;
        int arrowAngle = 30;
        Color arrowColor = Color.YELLOW;
        double xSize = Math.signum(direction.x)*arrowSize;
        double ySize = Math.signum(direction.y)*arrowSize;
        double cX;
        double cY;
        if(exitStyle) {
            cX = selectedBlock.getX()+(selectedBlock.getWidth()+xSize*2)/2.0;
            cY = selectedBlock.getY()+(selectedBlock.getHeight()+ySize*2)/2.0;
        } else {
            cX = selectedBlock.getX()+(selectedBlock.getWidth()-xSize*3)/2.0;
            cY = selectedBlock.getY()+(selectedBlock.getHeight()-ySize*3)/2.0;
        }
        c.drawLine(cX, cY, cX+xSize, cY+ySize, arrowThickness, arrowColor);

        double xOffset = Math.signum(direction.x+0.1)*arrowSize*Math.sin(Math.toRadians(arrowAngle)); // +0.1 to make the number either positive or negative, never 0
        c.drawLine(cX+xOffset, cY+ySize/2, cX, cY+ySize, arrowThickness, arrowColor);
        c.drawLine(cX-xOffset, cY+ySize/2, cX, cY+ySize, arrowThickness, arrowColor);

        double yOffset = Math.signum(direction.y+0.1)*arrowSize*Math.sin(Math.toRadians(arrowAngle));
        c.drawLine(cX+xSize/2, cY+yOffset, cX+xSize, cY, arrowThickness, arrowColor);
        c.drawLine(cX+xSize/2, cY-yOffset, cX+xSize, cY, arrowThickness, arrowColor);
    }
    
    private void drawSelectedBlock(Camera c, Block b, int x, int y, int count) {
        b.setPos(x, y);
        b.draw(c, true);
        b.drawHovered(c);

        if(!world.isAreaFree(b) && !world.isReplacable(b) || !world.hasEnoughItems(b, count)) { // Can not build
            c.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), areaNotFreeColor);
        } else { // Can build
            c.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), placeColor);
        }
    }
    
    /*
        Build and remove logic
    */
    
    public void placeBlocks(Camera c, Point2D.Double placeStartPoint, boolean rotatedBlock) {
        if(selectedBlock != null && placeStartPoint != null) { // Place block
            double xSize = c.getMouseToWorldPosX() - placeStartPoint.x;
            double ySize = c.getMouseToWorldPosY() - placeStartPoint.y;
            
            if(Math.abs(xSize) >= Math.abs(ySize)) {
                int sign = (int)Math.signum(xSize);
                for(int i = 0; i <= Math.abs(xSize); i++) {
                    placeBlock(placeStartPoint.x+i*sign, placeStartPoint.y);
                }
            } else {
                int sign = (int)Math.signum(ySize);
                for(int i = 0; i <= Math.abs(ySize); i++) {
                    placeBlock(placeStartPoint.x, placeStartPoint.y+i*sign);
                }
            }
        }
    }
    
    private void placeBlock(double x, double y) {
        Object [] paramValuesSub = {0, 0}; // Dummy value, are replaced when the block object is successfully created
        Block b = null;
        try {
            b = selectedBlock.getClass().getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("ERROR while placing block!");
        } finally {
            if(b != null) {
                b.setPos((int)Math.round(x - b.getWidth()/2.0), (int)Math.round(y - b.getHeight()/2.0)); // Set the real block pos
                if(b instanceof UpdatedBlock updatedBlock && updatedBlock.hasDirection()) {
                    updatedBlock.setDirection(((UpdatedBlock)selectedBlock).getDirection());
                }
                world.placeBlock(b, false);
            }
        }
    }
    
    public void removeBlocks(Camera c, Point removeStartPoint) {
        if(removeStartPoint != null) {
            int xSize = c.getMouseToWorldGridPosX() - removeStartPoint.x;
            int ySize = c.getMouseToWorldGridPosY() - removeStartPoint.y;
            int xSign = (int)Math.signum(xSize);
            int ySign = (int)Math.signum(ySize);
            
            for(int x = 0; x <= Math.abs(xSize); x++) {
                for(int y = 0; y <= Math.abs(ySize); y++) {
                    world.removeBlock(removeStartPoint.x+x*xSign, removeStartPoint.y+y*ySign, true);
                }
            }
        }
    }
    
    /*
        Getters and setters
    */
    
    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
    }
    
    public Point getNextDirection(Point currentDirection, boolean turnLeft) {
        Point nextDirection = new Point();

        if (turnLeft) {
            if (currentDirection.x == 1 && currentDirection.y == 0) {
                nextDirection.setLocation(0, -1);
            } else if (currentDirection.x == -1 && currentDirection.y == 0) {
                nextDirection.setLocation(0, 1);
            } else if (currentDirection.x == 0 && currentDirection.y == 1) {
                nextDirection.setLocation(1, 0);
            } else if (currentDirection.x == 0 && currentDirection.y == -1) {
                nextDirection.setLocation(-1, 0);
            }
        } else {
            if (currentDirection.x == 1 && currentDirection.y == 0) {
                nextDirection.setLocation(0, 1);
            } else if (currentDirection.x == -1 && currentDirection.y == 0) {
                nextDirection.setLocation(0, -1);
            } else if (currentDirection.x == 0 && currentDirection.y == 1) {
                nextDirection.setLocation(-1, 0);
            } else if (currentDirection.x == 0 && currentDirection.y == -1) {
                nextDirection.setLocation(1, 0);
            }
        }
        return nextDirection;
    }
}
