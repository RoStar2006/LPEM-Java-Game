/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Core.Camera;
import Core.MainLogic;
import Core.Utils;
import Blocks.*;
import World.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class MenuManager {
    private World world;
    private Camera c;
    private InventoryMenu inventoryMenu;
    
    /*
        Build menu
    */
    private final int bIconSize = 32;
    private final int bPadding = 3;
    private final int bXItemCount = 4;
    private final int bYItemCount = 5;
    private final int bFontSize = 16;
    private final int bMenuWidth = (bIconSize + bPadding) * bXItemCount + bPadding;
    private final int bMenuHeight = (bIconSize + bPadding) * bYItemCount + bPadding;
    private final int bMenuX = MainLogic.SCREEN_RES.x - bMenuWidth;
    private final int bMenuY = MainLogic.SCREEN_RES.y - bMenuHeight;
    private final int bMenuRows;
    private final Color bMenuBGColor = new Color(255, 255, 255, 64);
    private ArrayList<Block> alBuildingBlocks = new ArrayList<>();
    private int scroll = 0;
    
    /*
        Block placement
    */
    private int selectedIndex = -1;
    private Class<? extends Block> selectedBlock;
    private Color areaNotFreeColor = new Color(255, 0 ,0 , 126);
    private Point2D.Double placeStartPoint = null;
    private Color placeColor = new Color(245, 245, 245, 126);
    private boolean rotatedBlock = false;
    private Point removeStartPoint = null;
    private Color removeColor = Color.RED;

    public MenuManager(Camera camera) {
        this.c = camera;
        alBuildingBlocks.add(new Wall(0, 0));
        alBuildingBlocks.add(new BigWall(0, 0));
        alBuildingBlocks.add(new NodeCollector(0, 0));
        alBuildingBlocks.add(new ConveyorBelt(0, 0));
        
        bMenuRows = (int)Math.ceil(alBuildingBlocks.size() / (double)bXItemCount);
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public void draw(Graphics g) {
        if(inventoryMenu != null) {
            inventoryMenu.draw(c, g);
        }
        
        drawSelectedBlocks();
        drawRemoveArea();
        
        drawBuildMenu(g);
        drawHoveredBlock(g);
    }
    
    private void drawRemoveArea() {
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
    
    private void drawHoveredBlock(Graphics g) {
        Block block = world.getBlock(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY());
        if(block == null) {
            block = world.getNode(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY());
        }
        
        if(block != null) {
            g.setColor(Color.WHITE);
            Font oldFont = g.getFont();
            g.setFont(oldFont.deriveFont(24f));
            g.drawString(block.getName(), (MainLogic.SCREEN_RES.x - g.getFontMetrics().stringWidth(block.getName()))/2, MainLogic.SCREEN_RES.y-3);
            g.setFont(oldFont);
        }
    }
    
    private void drawBuildMenu(Graphics g) {
        Font f = g.getFont();
        g.setFont(f.deriveFont((float)bFontSize));
        g.setColor(bMenuBGColor);
        g.fillRect(bMenuX, bMenuY-bFontSize, bMenuWidth, bMenuHeight+bFontSize);
        
        if(selectedIndex != -1) {
            g.setColor(Color.WHITE);
            g.drawString(alBuildingBlocks.get(selectedIndex).getName(), bMenuX, bMenuY);
        } else {
            int index = calulateBuildMenuIndex();
            
            if(index != -1) {
                g.setColor(Color.WHITE);
                g.drawString(alBuildingBlocks.get(index).getName(), bMenuX, bMenuY);
            }
        }
        
        g.setFont(f);
        
        int x = 0;
        int y = scroll;
        
        for(Block block : alBuildingBlocks) {
            if(y <= 0) 
                block.drawIcon(g, bMenuX + (bIconSize + bPadding) * x + bPadding, bMenuY - (bIconSize + bPadding) * y + bPadding, bIconSize);
            
            x++;
            if(x % bXItemCount == 0) {
                x = 0;
                y--;
            }
        }
    }
    
    private void drawSelectedBlocks() {
        if(selectedIndex == -1) {
            return;
        }
        
        Block b = alBuildingBlocks.get(selectedIndex);
        ConveyorBelt conveyorBelt = null;
        if(b instanceof ConveyorBelt lConveyorBelt) {
            conveyorBelt = lConveyorBelt;
        }
        
        if(placeStartPoint != null) {
            double xSize = c.getMouseToWorldPosX() - placeStartPoint.x;
            double ySize = c.getMouseToWorldPosY() - placeStartPoint.y;
            
            if(Math.abs(xSize) >= Math.abs(ySize)) {
                int xSign = (int)Math.signum(xSize);
                
                if(!rotatedBlock && Math.abs(xSize) > 1 && conveyorBelt != null) { // rotate blocks, if not rotated manually
                    conveyorBelt.setDirection(new Point(xSign, 0));
                }
                
                for(int i = 0; i <= Math.abs(xSize); i += b.getWidth()) { // draw block
                    drawSelectedBlock(b, (int)Math.round(placeStartPoint.x+i*xSign - b.getWidth()/2.0), (int)Math.round(placeStartPoint.y - b.getHeight()/2.0), i/b.getWidth()+1);
                }
            } else {
                int ySign = (int)Math.signum(ySize);
                
                if(!rotatedBlock && Math.abs(ySize) > 1  && conveyorBelt != null) { // rotate blocks, if not rotated manually
                    conveyorBelt.setDirection(new Point(0, ySign));
                }
                
                for(int i = 0; i <= Math.abs(ySize); i += b.getHeight()) { // draw block
                    drawSelectedBlock(b, (int)Math.round(placeStartPoint.x - b.getWidth()/2.0), (int)Math.round(placeStartPoint.y+i*ySign - b.getHeight()/2.0), i/b.getHeight()+1);
                }
            }
        } else { // draw a single block under the mouse cursor
            drawSelectedBlock(b, (int)Math.round(c.getMouseToWorldPosX() - b.getWidth()/2.0), (int)Math.round(c.getMouseToWorldPosY() - b.getHeight()/2.0), 1);
        }
        
        if(conveyorBelt != null) { // draw the arrow when placing conveyor belts
            double arrowSize = 0.75;
            int arrowThickness = 3;
            int arrowAngle = 30;
            Color arrowColor = Color.YELLOW;
            double xSize = Math.signum(conveyorBelt.getDirection().x)*arrowSize;
            double ySize = Math.signum(conveyorBelt.getDirection().y)*arrowSize;
            double cX = b.getX()+(b.getWidth()+xSize)/2.0;
            double cY = b.getY()+(b.getHeight()+ySize)/2.0;
            c.drawLine(cX, cY, cX+xSize, cY+ySize, arrowThickness, arrowColor);
            
            double xOffset = Math.signum(conveyorBelt.getDirection().x+0.1)*arrowSize*Math.sin(Math.toRadians(arrowAngle)); // +0.1 to make the number either positive or negative, never 0
            c.drawLine(cX+xOffset, cY+ySize/2, cX, cY+ySize, arrowThickness, arrowColor);
            c.drawLine(cX-xOffset, cY+ySize/2, cX, cY+ySize, arrowThickness, arrowColor);
            
            double yOffset = Math.signum(conveyorBelt.getDirection().y+0.1)*arrowSize*Math.sin(Math.toRadians(arrowAngle));
            c.drawLine(cX+xSize/2, cY+yOffset, cX+xSize, cY, arrowThickness, arrowColor);
            c.drawLine(cX+xSize/2, cY-yOffset, cX+xSize, cY, arrowThickness, arrowColor);
        }
    }
    
    private void drawSelectedBlock(Block b, int x, int y, int count) {
        b.setPos(x, y);
        b.draw(c, true);

        if(!world.isAreaFree(b) && !world.isReplacable(b) || !world.hasEnoughItems(b, count)) {
            c.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), areaNotFreeColor);
        } else {
            c.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), placeColor);
        }
    }
    
    private void placeBlocks() {
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
        placeStartPoint = null;
        rotatedBlock = false;
    }
    
    private void placeBlock(double x, double y) {
        Class [] paramTypesSub = { int.class, int.class };
        Object [] paramValuesSub = {0, 0}; // Dummy value, are replaced when the block object is successfully created
        Block b = null;
        try {
            b = selectedBlock.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.err.println("ERROR while placing block!");
        } finally {
            if(b != null) {
                b.setPos((int)Math.round(x - b.getWidth()/2.0), (int)Math.round(y - b.getHeight()/2.0)); // Set the real block pos
                if(b instanceof ConveyorBelt conveyorBelt) {
                    conveyorBelt.setDirection(((ConveyorBelt)alBuildingBlocks.get(selectedIndex)).getDirection());
                }
                world.placeBlock(b, false);
            }
        }
    }
    
    private void removeBlocks() {
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
        removeStartPoint = null;
    }
    
    private void createInventoryMenu() {
        if(selectedBlock == null && world.getBlock(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY()) instanceof UpdatedBlock updatedBlock) {
            if(!updatedBlock.getInventory().isEmpty()) {
                inventoryMenu = new InventoryMenu(updatedBlock.getX()+updatedBlock.getWidth(), updatedBlock.getY(), updatedBlock.getInventory());
            } else {
                inventoryMenu = null;
            }
        } else {
            inventoryMenu = null;
        }
    }
    
    private int calulateBuildMenuIndex() {
        // Calculate on which item the user clicked
            int xIndex = ((int)c.getMouseX() - bMenuX) / (bIconSize + bPadding);
            int yIndex = ((int)c.getMouseY() - bMenuY) / (bIconSize + bPadding) + scroll;
            int index = yIndex * bXItemCount + xIndex;
            if(index >= 0 && index < alBuildingBlocks.size()) {
                return index;
            } else {
                return -1;
            }
    }
    
    public void mouseClickInput(int x, int y, boolean leftClick, boolean released) {
        if(x >= bMenuX && y >= bMenuY-bFontSize && removeStartPoint == null && placeStartPoint == null) { // Click inside the build menu
            if(released) {
                if(y < bMenuY) { // Check if mouse is hovering over the selected block title
                    selectedBlock = null;
                    selectedIndex = -1;
                    return;
                }
                int index = calulateBuildMenuIndex();
                if(index != -1) {
                    selectedBlock = alBuildingBlocks.get(index).getClass();
                    selectedIndex = index;
                } else {
                    selectedBlock = null;
                    selectedIndex = -1;
                }
            }
            
            return;
        }
        
        if(leftClick) {
            if(selectedBlock != null) {
                inventoryMenu = null;
                if(!released) {
                    if(removeStartPoint != null) {
                        removeStartPoint = null;
                    } else {
                        placeStartPoint = new Point2D.Double(c.getMouseToWorldPosX(), c.getMouseToWorldPosY());
                        rotatedBlock = false;
                    }
                } else {
                    placeBlocks();
                }
            } else {
                createInventoryMenu();
                removeStartPoint = null;
            }
        } else {
            if(selectedBlock != null) {
                selectedBlock = null;
                selectedIndex = -1;
                placeStartPoint = null;
            } else {
                if(!released) {
                    if(placeStartPoint != null) {
                        placeStartPoint = null;
                    } else {
                        removeStartPoint = new Point(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY());
                    }
                } else {
                    removeBlocks();
                }
            }
        }
    }
    
    public boolean mouseWheelInput(MouseWheelEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        
        if(!(x >= bMenuX && y >= bMenuY-bFontSize)) { // Scroll outside of build menu
            if(selectedBlock != null && !evt.isControlDown()) {
                if(alBuildingBlocks.get(selectedIndex) instanceof ConveyorBelt conveyorBelt) {
                    conveyorBelt.setDirection(getNextDirection(conveyorBelt.getDirection(), evt.getWheelRotation() == -1));
                    rotatedBlock = true;
                }
                return true;
            } else {
                return false;
            }
        }
        
        if(bMenuRows >= bYItemCount) {
            scroll += Math.signum(evt.getWheelRotation());
            scroll = Utils.clampInt(scroll, 0, bMenuRows - bYItemCount);
        }
        
        return true;
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
