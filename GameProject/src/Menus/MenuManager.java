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
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int scroll = 0;
    
    private final int bMenuWidth = (bIconSize + bPadding) * bXItemCount + bPadding;
    private final int bMenuHeight = (bIconSize + bPadding) * bYItemCount + bPadding;
    private final int bMenuX = MainLogic.SCREEN_RES.x - bMenuWidth;
    private final int bMenuY = MainLogic.SCREEN_RES.y - bMenuHeight;
    private final int bMenuRows;
    private int blockMenuHeight = 0;
    
    private final int detailsButtonSize = bFontSize;
    private final int datailsSize = bFontSize;
    private final int detailsButtonX = bMenuX + bMenuWidth - bPadding - detailsButtonSize;
    private int detailsButtonY = 0;
    
    private final Color bgLightColor = new Color(255, 255, 255, 64);
    private final Color bgDarkColor = new Color(0, 0, 0, 96);
    private ArrayList<Block> alBuildingBlocks = new ArrayList<>();
    private Block selectedBlock;
    
    
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
        /*alBuildingBlocks.add(new ConveyorBelt(0, 0));
        alBuildingBlocks.add(new Splitter(0, 0));
        alBuildingBlocks.add(new Merger(0, 0));
        alBuildingBlocks.add(new RecipeBlock(0, 0));
        alBuildingBlocks.add(new NodeCollector(0, 0));
        alBuildingBlocks.add(new Wall(0, 0));
        alBuildingBlocks.add(new BigWall(0, 0));
        alBuildingBlocks.add(new Tower(0, 0));*/
        
        initBuildList();

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
        } else if(c.getMouseX() >= bMenuX && c.getMouseY() >= bMenuY) {
            int index = calulateBuildMenuIndex();
            
            if(index != -1) {
                drawBlockMenu(g, alBuildingBlocks.get(index));
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
        int xIndex = ((int)c.getMouseX() - bMenuX) / (bIconSize + bPadding);
        int yIndex = ((int)c.getMouseY() - bMenuY) / (bIconSize + bPadding) + scroll;
        int index = yIndex * bXItemCount + xIndex;
        if(index >= 0 && index < alBuildingBlocks.size()) {
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
        selectedBlock = alBuildingBlocks.get(index);
        buildManager.setSelectedBlock(selectedBlock);
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
                }
                
                int index = calulateBuildMenuIndex();
                if(index != -1) {
                    selectBlock(index);
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
        
        if(bMenuRows >= bYItemCount) {
            scroll += Math.signum(evt.getWheelRotation());
            scroll = Utils.clampInt(scroll, 0, bMenuRows - bYItemCount);
        }
        
        return true;
    }
    
    /**
     * This method automatically retrieves all blocks and adds them to the build menu.
     */
    @SuppressWarnings("unchecked")
    private void initBuildList() {
        ArrayList<Class> classes = null;
        
        try {
            classes = getClasses("Blocks");
        } catch(ClassNotFoundException | IOException ex) {
            Logger.getLogger(MenuManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(classes != null) {
                for(Class clazz : classes) {
                    Block b = null;
                    
                    if(Block.class.isAssignableFrom(clazz)) { // This line checks if the class is a block, so we can safely suppress the warnings
                        b = BuildManager.createBlock((Class<? extends Block>)clazz);
                    }
                    
                    if(b != null) {
                        alBuildingBlocks.add(b);
                    }
                }
            }
        }
    }
    
    /**
    * Scans all classes accessible from the context class loader which belong to the given package and subpackages. <br>
    * https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
    * @param packageName The base package
    * @return The classes
    * @throws ClassNotFoundException
    * @throws IOException
    */
    private static ArrayList<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        
        assert classLoader != null;
        
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs. <br>
     * Exceptions: Base, Nodes packages
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        
        if (!directory.exists() || directory.getName().contains("Base") || directory.getName().contains("Nodes")) {
            return classes;
        }
        
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        
        return classes;
    }
}
