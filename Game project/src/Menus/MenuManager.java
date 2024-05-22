/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Core.Camera;
import Core.MainLogic;
import Utilities.Utils;
import World.*;
import World.UpdatedBlock;
import World.Wall;
import World.World;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
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

    public MenuManager(Camera camera) {
        this.c = camera;
        alBuildingBlocks.add(new Wall(0, 0));
        alBuildingBlocks.add(new BigWall(0, 0));
        
        bMenuRows = (int)Math.ceil(alBuildingBlocks.size() / (double)bXItemCount);
    }

    public void setWorld(World world) {
        this.world = world;
    }
    
    public void draw(Camera c, Graphics g) {
        if(inventoryMenu != null) {
            inventoryMenu.draw(c, g);
        }
        
        drawSelectedBlock(c);
        
        drawBuildMenu(g);
        
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
    
    public void drawBuildMenu(Graphics g) {
        g.setColor(bMenuBGColor);
        g.fillRect(bMenuX, bMenuY, bMenuWidth, bMenuHeight);
        
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
    
    public void drawSelectedBlock(Camera c) {
        if(selectedIndex == -1) {
            return;
        }
        Block b = alBuildingBlocks.get(selectedIndex);
        b.setPos((int)Math.round(c.getMouseToWorldPosX() - b.getWidth()/2.0), (int)Math.round(c.getMouseToWorldPosY() - b.getHeight()/2.0));
        b.draw(c);
        
        if(!world.isAreaFree(b) || !world.hasEnoughItems(b)) {
            c.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), areaNotFreeColor);
        }
    }
    
    public void mouseClickInput(int x, int y, boolean leftClick) {
        if(x >= bMenuX && y >= bMenuY) { // Click inside the build menu
            int xIndex = (x - bMenuX) / (bIconSize + bPadding);
            int yIndex = (y - bMenuY) / (bIconSize + bPadding) + scroll;
            int index = yIndex * bXItemCount + xIndex;
            if(index >= 0 && index < alBuildingBlocks.size()) {
                selectedBlock = alBuildingBlocks.get(index).getClass();
                selectedIndex = index;
            } else {
                selectedBlock = null;
                selectedIndex = -1;
            }
            return;
        }
        
        if(leftClick) {
            if(selectedBlock != null) { // Place block
                Class [] paramTypesSub = { int.class, int.class };
                Object [] paramValuesSub = {0, 0}; // Dummy value, are replaced when the block object is successfully created
                Block b = null;
                try {
                    b = selectedBlock.getDeclaredConstructor(paramTypesSub).newInstance(paramValuesSub);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(b != null) {
                        b.setPos((int)Math.round(c.getMouseToWorldPosX() - b.getWidth()/2.0), (int)Math.round(c.getMouseToWorldPosY() - b.getHeight()/2.0)); // Set the real block pos
                        world.placeBlock(b, false);
                    }
                }
            }
            if(selectedBlock == null && world.getBlock(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY()) instanceof UpdatedBlock updatedBlock) {
                if(!updatedBlock.getInventory().isEmpty()) {
                    inventoryMenu = new InventoryMenu(updatedBlock.getX()+updatedBlock.getWidth(), updatedBlock.getY(), updatedBlock.getInventory());
                } else {
                    inventoryMenu = null;
                }
            } else {
                inventoryMenu = null;
            }
        } else {
            if(selectedBlock != null) {
                selectedBlock = null;
                selectedIndex = -1;
            } else {
                world.removeBlock(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY(), true);
            }
        }
    }
    
    public boolean mouseWheelInput(MouseWheelEvent evt) {
        int x = evt.getX();
        int y = evt.getY();
        
        if(!(x >= bMenuX && y >= bMenuY)) { // Scroll outside of build menu
            return false;
        }
        
        if(bMenuRows >= bYItemCount) {
            scroll += Math.signum(evt.getWheelRotation());
            scroll = Utils.clampInt(scroll, 0, bMenuRows - bYItemCount);
        }
        
        return true;
    }
}
