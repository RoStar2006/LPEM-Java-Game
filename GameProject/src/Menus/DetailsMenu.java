/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Menus;

import Blocks.Base.*;
import Core.MainLogic;
import Items.Inventory;
import Items.ItemDrawer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author Romeo
 */
public class DetailsMenu {
    private Block block;
    private Color bgColor;
    private final Color boxColor = new Color(16, 16, 16);
    private final Color textColor = Color.WHITE;
    private final Color attributeColor = Color.LIGHT_GRAY;
    private final Color titleColor = Color.YELLOW;
    private final float fontSize = 16;
    private final int closeButtonWidth = 150;
    private final int closeButtonHeight = 50;
    private final int closeButtonX = (MainLogic.SCREEN_RES.x - closeButtonWidth)/2;
    private final int closeButtonY = MainLogic.SCREEN_RES.y - closeButtonHeight*2;
    private final ArrayList<String> alDetailsString = new ArrayList<>();
    private final ArrayList<Node> alNodes;
    
    public DetailsMenu(Block block, ArrayList<Node> alNodes, Color bgColor) {
        this.bgColor = bgColor;
        this.block = block;
        this.alNodes = alNodes;
        initDetailsList(block);
    }
    
    private void initDetailsList(Block block) {
        alDetailsString.add("<title><center><blockIcon>" + block.getName());
        alDetailsString.add("<offset>1");
        alDetailsString.add("<title>General");
        alDetailsString.add(addNumber("Health:", block.getStatsManager().getMaxHealth()));
        alDetailsString.add("Size:" + block.getWidth() + "x" + block.getHeight());
        alDetailsString.add(addNumber("Build Time:", block.getBuildTime()) + " seconds");
        
        String[] costsArr = Inventory.formatList(block.getHmNeededItems());
        if(costsArr.length > 0) {
            alDetailsString.add("Build Cost:");
            for(String str : costsArr) {
                alDetailsString.add("<tab>" + str);
            }
        } else {
            alDetailsString.add("Build Cost:none");
        }
        
        if(block instanceof UpdatedBlock updatedBlock) {
            if(updatedBlock.hasInventory()) {
                alDetailsString.add("<title>Items");
                alDetailsString.add("Item Capacity:" + updatedBlock.getMaxStackSize() + " items");
                if(block.getBlockType() == BlockType.ItemLogistic) {
                    String speedStr = "Speed:";
                    if(updatedBlock instanceof ConveyorBelt conveyorBelt) {
                        speedStr = addNumber(speedStr, conveyorBelt.getSpeed()) + " items/second";
                    } else {
                        speedStr += "instant";
                    }
                    alDetailsString.add(speedStr);
                }
            }
            
            if(updatedBlock instanceof Turret tower) {
                alDetailsString.add("<title>Function");
                alDetailsString.add(addNumber("Range:", tower.getRange()) + " blocks");
                alDetailsString.add(addNumber("Fire Rate:", tower.getStatsManager().getAttackRate()) + "/second");
                alDetailsString.add(addNumber("Spread:", tower.getSpread()) + " degrees");
                alDetailsString.add(addNumber("Projectile Count:", tower.getProjectileCount()));
                ArrayList<String> alProjectileDetails = tower.formattedProjectileDetails();
                for(String str : alProjectileDetails) {
                    if(str.contains("<itemIcon>")) {
                        alDetailsString.add("<offset>0.5");
                        alDetailsString.add("<box>3");
                    }
                    alDetailsString.add("<tab>" + str);
                }
                if(!alProjectileDetails.isEmpty()) {
                    alDetailsString.add("<offset>0.5");
                }
            }
            
            if(updatedBlock instanceof RecipeBlock recipeBlock) {
                alDetailsString.add("<title>Input/Output");
                ArrayList<String> alRecipeDetails = recipeBlock.formattedRecipeDetails();
                for(String str : alRecipeDetails) {
                    if(str.contains("<itemIcon>")) {
                        alDetailsString.add("<box>3");
                    }
                    if(!str.contains("<offset>") && !str.contains("<box>") ) {
                        str = "<tab>" + str;
                    }
                    alDetailsString.add(str);
                }
            } else if(updatedBlock instanceof NodeCollector nodeCollector) {
                alDetailsString.add("<title>Input/Output");
                alDetailsString.add(addNumber("Mining Speed:", nodeCollector.getMiningSpeed()) + " items/second");
                alDetailsString.add("Minable nodes:");
                for(Node node : alNodes) {
                    if(block.isPlacableOn(node)) {
                        alDetailsString.add("<tab>" + node.getName());
                    }
                }
            }
        } 
    }
    
    public final static String addNumber(String str, double number) {
        if(number % 1 <= 0.1) {
            str += (int)number;
        } else {
            str += number;
        }
        
        return str;
    }
    
    public void draw(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, MainLogic.SCREEN_RES.x, MainLogic.SCREEN_RES.y);
        
        drawDetailsString(g);
        drawCloseButton(g);
    }
    
    private void drawDetailsString(Graphics g) {
        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(fontSize));
        
        int lY = MainLogic.SCREEN_RES.y/4;
        int lX = MainLogic.SCREEN_RES.x/2-closeButtonWidth;
        String tabSpaces = "    ";
        int whiteSpaceWidth = g.getFontMetrics().stringWidth(" ");
        int boxHeight = -1;
        
        for(String str : alDetailsString) {
            int offset = 0;
            Color color = textColor;
            
            if(str.contains("<offset>")) {
                str = str.replaceAll("<offset>", "");
                lY += fontSize * Double.parseDouble(str);
                continue;
            }
            
            if(str.contains("<box>")) {
                str = str.replaceAll("<box>", "");
                boxHeight = Integer.parseInt(str);
                continue;
            }
            
            if(str.contains("<title>")) {
                str = str.replaceAll("<title>", "");
                color = titleColor;
            } else {
                str = tabSpaces + str;
            }
            
            str = str.replaceAll("<tab>", tabSpaces);
            
            if(str.contains("<center>")) {
                str = str.replaceAll("<center>", "");
                String tempStr = str.substring(str.lastIndexOf(">"), str.length());
                offset += closeButtonWidth-g.getFontMetrics().stringWidth(tempStr)/2;
            }
            
            if(boxHeight > 0) {
                int whiteSpaceCount = str.split("\\s").length-2;
                int whiteSpaceOffset = whiteSpaceCount*whiteSpaceWidth;
                g.setColor(boxColor);
                g.fillRect(lX+offset+whiteSpaceOffset, lY-(int)fontSize, closeButtonWidth*2-whiteSpaceOffset, (int)fontSize*boxHeight);
                
                boxHeight = -1;
            }
            
            if(str.contains("<itemIcon>")) {
                str = str.replaceAll("<itemIcon>", "");
                int size = (int)fontSize;
                int whiteSpaceCount = str.split("\\s").length-2;
                ItemDrawer.drawItem(g, Inventory.getItemType(str), lX+offset+size/2+(whiteSpaceCount*whiteSpaceWidth), lY-size/2, size);
                offset += size*1.1;
            } else if(str.contains("<blockIcon>")) {
                str = str.replaceAll("<blockIcon>", "");
                int size = (int)fontSize;
                int whiteSpaceCount = str.split("\\s").length-2;
                block.drawIcon(g, lX+offset+(whiteSpaceCount*whiteSpaceWidth), lY-size, size);
                offset += size*1.1;
            }
            
            if(str.contains(":")) {
                String[] splitStr = str.split(":", 2);
                splitStr[0] += ": ";
                g.setColor(attributeColor);
                g.drawString(splitStr[0], lX+offset, lY);
                offset += g.getFontMetrics().stringWidth(splitStr[0]);
                
                g.setColor(textColor);
                g.drawString(splitStr[1], lX+offset, lY);
            } else if(str.contains("~")) {
                String[] splitStr = str.split("~", 2);
                g.setColor(textColor);
                g.drawString(splitStr[0], lX+offset, lY);
                offset += g.getFontMetrics().stringWidth(splitStr[0]);
                
                splitStr[1] = "~ " + splitStr[1];
                g.setColor(attributeColor);
                g.drawString(splitStr[1], lX+offset, lY);
            } else {
                g.setColor(color);
                g.drawString(str, lX+offset, lY);
            }
            
            lY += fontSize;
        }
        
        g.setFont(oldFont);
    }
    
    private void drawCloseButton(Graphics g) {
        g.setColor(boxColor);
        g.fillRect(closeButtonX, closeButtonY, closeButtonWidth, closeButtonHeight);
        g.setColor(attributeColor);
        Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(32f));
        g.drawString("Close", closeButtonX+(closeButtonWidth-g.getFontMetrics().stringWidth("Close"))/2, closeButtonY+(closeButtonHeight+g.getFont().getSize()/3*2)/2);
        g.setFont(oldFont);
    }
    
    public boolean onCloseButton(int x, int y) {
        return x >= closeButtonX && y >= closeButtonY && x <= closeButtonX+closeButtonWidth && y <= closeButtonY+closeButtonHeight;
    }
}
