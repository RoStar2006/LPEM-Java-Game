/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Core.Camera;
import Core.MainLogic;
import Core.Utils;
import Blocks.*;
import World.BuildManager;
import World.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
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
    private int scroll = 0;
    private final Color bMenuBGColor = new Color(255, 255, 255, 64);
    private ArrayList<Block> alBuildingBlocks = new ArrayList<>();
    private Block selectedBlock;
    
    /*
        Build and placing
    */
    private BuildManager buildManager;
    private Point2D.Double placeStartPoint; 
    private Point removeStartPoint;
    private boolean rotatedBlock;
    
    public MenuManager(Camera camera) {
        this.c = camera;
        alBuildingBlocks.add(new Wall(0, 0));
        alBuildingBlocks.add(new BigWall(0, 0));
        alBuildingBlocks.add(new NodeCollector(0, 0));
        alBuildingBlocks.add(new ConveyorBelt(0, 0));
        alBuildingBlocks.add(new Splitter(0, 0));
        alBuildingBlocks.add(new Tower(0, 0));
        
        bMenuRows = (int)Math.ceil(alBuildingBlocks.size() / (double)bXItemCount);
    }
    
    public void init(BuildManager buildManager, World world) {
        this.buildManager = buildManager;
        this.world = world;
    }
    
    public void draw(Graphics g) {
        if(world == null || buildManager == null) {
            return;
        }
        
        if(inventoryMenu != null) {
            inventoryMenu.draw(c, g);
        }
        
        buildManager.draw(c, placeStartPoint, rotatedBlock, removeStartPoint);
        
        drawBuildMenu(g);
        drawHoveredBlock(g);
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
            block.drawHovered(c);
        }
    }
    
    private void drawBuildMenu(Graphics g) {
        Font f = g.getFont();
        g.setFont(f.deriveFont((float)bFontSize));
        g.setColor(bMenuBGColor);
        g.fillRect(bMenuX, bMenuY-bFontSize, bMenuWidth, bMenuHeight+bFontSize);
        
        if(selectedBlock != null) {
            g.setColor(Color.WHITE);
            g.drawString(selectedBlock.getName(), bMenuX, bMenuY);
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
    
    private void resetSelection() {
        selectedBlock = null;
        buildManager.setSelectedBlock(selectedBlock);
    }
    
    private void selectBlock(int index) {
        selectedBlock = alBuildingBlocks.get(index);
        buildManager.setSelectedBlock(selectedBlock);
    }
    
    public void mouseClickInput(int x, int y, boolean leftClick, boolean released) {
        if(x >= bMenuX && y >= bMenuY-bFontSize && removeStartPoint == null && placeStartPoint == null) { // Click inside the build menu
            if(released) {
                if(y < bMenuY) { // Check if mouse is hovering over the selected block title
                    resetSelection();
                    return;
                }
                int index = calulateBuildMenuIndex();
                if(index != -1) {
                    selectBlock(index);
                } else {
                    resetSelection();
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
                    buildManager.placeBlocks(c, placeStartPoint, rotatedBlock);
                    placeStartPoint = null;
                    rotatedBlock = false;
                }
            } else {
                createInventoryMenu();
                removeStartPoint = null;
            }
        } else {
            inventoryMenu = null;
            if(selectedBlock != null) {
                resetSelection();
                placeStartPoint = null;
            } else {
                if(!released) {
                    if(placeStartPoint != null) {
                        placeStartPoint = null;
                    } else {
                        removeStartPoint = new Point(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY());
                    }
                } else {
                    buildManager.removeBlocks(c, removeStartPoint);
                    removeStartPoint = null;
                }
            }
        }
    }
    
    public boolean mouseWheelInput(MouseWheelEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        
        if(!(x >= bMenuX && y >= bMenuY-bFontSize)) { // Scroll outside of build menu
            if(!evt.isControlDown() && selectedBlock instanceof UpdatedBlock updatedBlock && updatedBlock.hasDirection()) {
                updatedBlock.setDirection(buildManager.getNextDirection(updatedBlock.getDirection(), evt.getWheelRotation() == -1));
                rotatedBlock = true;
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
}
