/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Enemies;

import Core.Camera;
import Core.Utils;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class SpawnRegion {
    private int x, y;
    private final int size; // in blocks
    private final int drawAttempts = 5;
    private Color areaColor = new Color(36, 37, 38, 255/drawAttempts);

    public SpawnRegion(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return x;
    }
    
    public int getSpawnX() {
        return x + size/2;
    }
    
    public int getRandomSpawnX() {
        return Utils.nextInt(x, x+size);
    }

    public int getY() {
        return y;
    }
    
    public int getSpawnY() {
        return y + size/2;
    }
    
    public int getRandomSpawnY() {
        return Utils.nextInt(y, y+size);
    }

    public int getSize() {
        return size;
    }
    
    public void draw(Camera c) {
        for(int i = 0; i < drawAttempts; i++) {
            double offset = 2.0 * i/drawAttempts;
            double s = size - offset;
            c.fillRect(x+offset/2, y+offset/2, s, s, areaColor);
        }
    }
    
}
