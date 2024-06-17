/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Blocks.Base.*;
import Core.Camera;
import Core.MainLogic;
import Core.Utils;
import Items.ItemType;
import World.BuildManager;
import World.World;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final int bPaddedIconSize = bIconSize + bPadding;
    private final int bXItemCount = 4;
    private final int bYItemCount = 5;
    private final int bFontSize = 16;
    private int scroll = 0;
    
    private final int bMenuWidth = bPaddedIconSize * (bXItemCount+1) + bPadding;
    private final int bMenuHeight = bPaddedIconSize * bYItemCount + bPadding;
    private final int bMenuX = MainLogic.SCREEN_RES.x - bMenuWidth;
    private final int bMenuY = MainLogic.SCREEN_RES.y - bMenuHeight;
    private final int bMenuCatX = MainLogic.SCREEN_RES.x - bPaddedIconSize;
    private int blockMenuHeight = 0;
    
    private final int detailsButtonSize = bFontSize;
    private final int datailsSize = bFontSize;
    private final int detailsButtonX = bMenuX + bMenuWidth - bPadding - detailsButtonSize;
    private int detailsButtonY = 0;
    
    private final Color bgLightColor = new Color(255, 255, 255, 64);
    private final Color bgDarkColor = new Color(0, 0, 0, 96);
    private HashMap<BlockType, ArrayList<Block>> hmBuildingBlocks = new HashMap<>();
    private Block selectedBlock;
    private BlockType curBlockType;
    
    /*
        Build and placing
    */
    private BuildManager buildManager;
    private Point2D.Double placeStartPoint; 
    private Point removeStartPoint;
    private boolean rotatedBlock;
    
    private DetailsMenu detailsMenu;
    
    public MenuManager(Camera camera) {
        this.c = camera;
        
        initBuildingBlocks();
    }
    
    public void init(BuildManager buildManager, World world) {
        this.buildManager = buildManager;
        this.world = world;
    }
    
    /**
     * This method uses the ClassGraph library to collect every available block
     * https://repo1.maven.org/maven2/io/github/classgraph/classgraph/
     */
    private void initBuildingBlocks() {
        boolean createdBlock = false;
        for(int i = 1; i < BlockType.values().length; i++) {
            if(i == 1) {
                curBlockType = BlockType.values()[i];
            }
            hmBuildingBlocks.put(BlockType.values()[i], new ArrayList<>());
        }
        
        try (ScanResult scanResult = new ClassGraph().acceptPackages("Blocks").enableClassInfo().scan()) {
            List<Class<Block>> alBlockClasses = scanResult.getSubclasses(Block.class.getName()).loadClasses(Block.class);
            
            for(Class<Block> bC : alBlockClasses) {
                if(!bC.getPackage().getName().contains("Base") && !bC.getPackage().getName().contains("Nodes")) {
                    Block block = BuildManager.createBlock(bC);
                    
                    if(block != null) {
                        createdBlock = true;
                        hmBuildingBlocks.get(block.getBlockType()).add(block);
                    }
                }
            }
        }
        
        if(!createdBlock) {
            System.err.println("ERROR: No building blocks were found!");
        }
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
        drawCategoryMenu(g);
        drawHoveredBlock(g);
        
        if(detailsMenu != null) {
            detailsMenu.draw(g);
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
            block.drawHovered(c);
        }
    }
    
    private void drawBuildMenu(Graphics g) {
        Font f = g.getFont();
        g.setFont(f.deriveFont((float)bFontSize));
        g.setColor(bgLightColor);
        g.fillRect(bMenuX, bMenuY, bMenuWidth, bMenuHeight);
        
        blockMenuHeight = 0;
        detailsButtonY = 0;
        
        if(selectedBlock != null) {
            drawBlockMenu(g, selectedBlock);
        } else if(c.getMouseX() >= bMenuX && c.getMouseX() < bMenuCatX && c.getMouseY() >= bMenuY) {
            int index = calulateBuildMenuIndex();
            
            if(index != -1) {
                drawBlockMenu(g, getAlBuildingBlocks().get(index));
            }
        }
        
        g.setFont(f);
        
        int x = 0;
        int y = scroll;
        
        for(Block block : getAlBuildingBlocks()) {
            if(y <= 0) {
                block.drawIcon(g, bMenuX + bPaddedIconSize * x + bPadding, bMenuY - bPaddedIconSize * y + bPadding, bIconSize);
            }
            
            x++;
            if(x % bXItemCount == 0) {
                x = 0;
                y--;
            }
        }
    }
    
    private void drawCategoryMenu(Graphics g) {
        for(int i = 1; i < BlockType.values().length; i++) {
            BlockType blockType = BlockType.values()[i];
            
            BlockTypeDrawer.drawBlockType(g, blockType, bMenuCatX, bMenuY+bPadding+bPaddedIconSize*(i-1), bIconSize);
        }
    }
    
    private void drawBlockMenu(Graphics g, Block b) {
        int count = 1 + b.getHmNeededItems().size();
        int detailsButtonTextOffset = 2;
        
        int strPosY = bMenuY-bFontSize*(count-1);
        int lX = bMenuX + bPadding;
        blockMenuHeight = bFontSize * count + bPadding;
        detailsButtonY = strPosY-bFontSize;
        
        g.setColor(bgLightColor);
        g.fillRect(bMenuX, bMenuY-blockMenuHeight, bMenuWidth, blockMenuHeight);
        
        b.drawIcon(g, lX, detailsButtonY, datailsSize);
        g.setColor(Color.WHITE);
        g.drawString(b.getName(), lX+bPadding+datailsSize, strPosY);
        
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(detailsButtonX, detailsButtonY, detailsButtonSize, detailsButtonSize);
        g.setColor(Color.BLACK);
        g.drawString("?", detailsButtonX+(detailsButtonSize-g.getFontMetrics().stringWidth("?"))/2, strPosY-detailsButtonTextOffset);
        
        strPosY += datailsSize;
        
        g.setColor(Color.WHITE);
        for(Map.Entry<ItemType, Integer> entry : b.getHmNeededItems().entrySet()) {
            g.drawString(entry.getKey() + " " + world.getCore().getInventory().getOrDefault(entry.getKey(), 0) + "/"+ entry.getValue(), lX, strPosY);
            strPosY += bFontSize;
        }
    }
    
    private void createInventoryMenu() {
        if(selectedBlock == null && world.getBlock(c.getMouseToWorldGridPosX(), c.getMouseToWorldGridPosY()) instanceof UpdatedBlock updatedBlock) {
            if(!updatedBlock.getInventory().isEmpty() || (updatedBlock instanceof RecipeBlock recipeBlock && !recipeBlock.getResultInventory().isEmpty())) {
                inventoryMenu = new InventoryMenu(updatedBlock.getX()+updatedBlock.getWidth(), updatedBlock.getY(), updatedBlock);
            } else {
                inventoryMenu = null;
            }
        } else {
            inventoryMenu = null;
        }
    }
    
    private int calulateBuildMenuIndex() {
        // Calculate on which item the user clicked
        int xIndex = ((int)c.getMouseX() - bMenuX) / bPaddedIconSize;
        int yIndex = ((int)c.getMouseY() - bMenuY) / bPaddedIconSize + scroll;
        int index = yIndex * bXItemCount + xIndex;
        if(index >= 0 && index < getAlBuildingBlocks().size()) {
            return index;
        } else {
            return -1;
        }
    }
    
    private int calculateCategoryIndex() {
        int index = ((int)c.getMouseY() - bMenuY) / bPaddedIconSize+1;
        
        if(index >= 1 && index < BlockType.values().length) {
            return index;
        } else {
            return -1;
        }
    }
    
    private boolean clickedOnInfo(int x, int y) {
        return x >= detailsButtonX && y >= detailsButtonY && x <= detailsButtonX+detailsButtonSize && y <= detailsButtonY+detailsButtonSize;
    }
    
    private void resetSelection() {
        selectedBlock = null;
        buildManager.setSelectedBlock(selectedBlock);
    }
    
    private void selectBlock(int index) {
        selectedBlock = getAlBuildingBlocks().get(index);
        buildManager.setSelectedBlock(selectedBlock);
    }
    
    private ArrayList<Block> getAlBuildingBlocks() {
        return hmBuildingBlocks.get(curBlockType);
    }
    
    public boolean canInteract() {
        return detailsMenu == null;
    }
    
    public void mouseClickInput(int x, int y, boolean leftClick, boolean released) {
        if(!canInteract()) {
            if(leftClick && released) {
                if(detailsMenu != null && detailsMenu.onCloseButton(x, y)) {
                    detailsMenu = null;
                }
            }
            
            return;
        }
        
        if(x >= bMenuX && y >= bMenuY-blockMenuHeight && removeStartPoint == null && placeStartPoint == null) { // Click inside the build menu
            if(released && leftClick) {
                if(y < bMenuY) { // Check if mouse is hovering over the selected block menu
                    if(selectedBlock != null && clickedOnInfo(x, y)) {
                        detailsMenu = new DetailsMenu(selectedBlock, bgDarkColor);
                    }
                    return;
                } else if(x >= bMenuCatX) { // Hovering over categories
                    int index = calculateCategoryIndex();
                    
                    if(index != -1) {
                        curBlockType = BlockType.values()[index];
                    }
                    
                    return;
                } else {
                    int index = calulateBuildMenuIndex();
                    if(index != -1) {
                        selectBlock(index);
                    }
                }
            } else if(!leftClick) {
                resetSelection();
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
        if(!canInteract()) {
            return true;
        }
        
        int x = evt.getX();
        int y = evt.getY();
        
        if(!(x >= bMenuX && y >= bMenuY-blockMenuHeight)) { // Scroll outside of build menu
            if(!evt.isControlDown() && selectedBlock instanceof UpdatedBlock updatedBlock && updatedBlock.hasDirection()) {
                updatedBlock.setDirection(buildManager.getNextDirection(updatedBlock.getDirection(), evt.getWheelRotation() == -1));
                rotatedBlock = true;
                return true;
            } else {
                return false;
            }
        }
        
        int bMenuRows = (int)Math.ceil(getAlBuildingBlocks().size() / (double)bXItemCount);
        
        if(bMenuRows >= bYItemCount) {
            scroll += Math.signum(evt.getWheelRotation());
            scroll = Utils.clampInt(scroll, 0, bMenuRows - bYItemCount);
        }
        
        return true;
    }
    
}
