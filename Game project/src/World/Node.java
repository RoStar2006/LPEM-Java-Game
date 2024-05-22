/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Color;

/**
 *
 * @author Romeo
 */
public class Node extends Block {
    
    public Node(int x, int y) {
        super(x, y);
        initBlock("Node", 0, 1, 1, Color.BLACK);
    }
    
}
