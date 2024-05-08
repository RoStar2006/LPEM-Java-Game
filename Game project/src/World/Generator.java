/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Utilities.Utils;
import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;

/**
 *
 * @author Romeo
 */
public final class Generator {
    public final static int BLOCK_SIZE = 32;
    public final static int CHUNK_SIZE = 32;
    public static int WORLD_SIZE_X, WORLD_SIZE_Y;
    
    public static World generateWorld(long seed, int xSize, int ySize) {
        Utils.initRandom(seed);
        HashMap<Point, Node> hmNodes = new HashMap<>();
        WORLD_SIZE_X = xSize;
        WORLD_SIZE_Y = ySize;
        
        for(int y = 0; y < ySize; y++) {
            for(int x = 0; x < xSize; x++) {
                if(Utils.nextDouble() <= 0.01) {
                    Node node = new Node(x, y);
                    hmNodes.put(new Point(x, y), node);
                }
            }
        }
        World w = new World(hmNodes, xSize, ySize);
        
        /*
            Test code
        */
        Color color = new Color(255, 255, 255);
        for(int y = 0; y < ySize; y += 1) {
            double yPerc = ((double)y/ySize);
            for(int x = 0; x < xSize; x += 1) {
                double xPerc = ((double)x/xSize);
                double zPerc = 1 - xPerc;
                // Color color2 = new Color((int)(color.getRed() * xPerc), (int)(color.getGreen() * yPerc), (int)(color.getBlue() * zPerc));
                Color color2 = new Color(Utils.nextInt(0, 256), Utils.nextInt(0, 256), Utils.nextInt(0, 256));
                w.placeBlock(new UpdatedBlock(x, y, 2, 2, color2));
            }
        }
        
        for(int y = 0; y < ySize; y += 2) {
            for(int x = 0; x < xSize; x += 2) {
                if(Utils.nextBoolean()) {
                    w.removeBlock(x, y);
                }
            }
        }
        
        return w;
        // return new World(hmNodes, xSize, ySize);
    }
}
