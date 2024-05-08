/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import Core.MainLogic;
import Utilities.Utils;
import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class UpdatedBlock extends Block {
    
    public UpdatedBlock(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }
    
    /*
        Test code
    */
    private double waitTime = 1;
    private double curTime = 0;
    
    /**
     * This is called every {@link MainLogic#update()} tick and is the core component of an {@link UpdatedBlock}
     * @param deltaTime 
     */
    public void update(double deltaTime) {
        curTime += deltaTime;
        
        if(curTime >= waitTime) {
            waitTime = Utils.nextDouble(0.2, 0.6);
            curTime = 0;
            color = new Color(Utils.nextInt(0, 256), Utils.nextInt(0, 256), Utils.nextInt(0, 256));
        }
    }
    
}
