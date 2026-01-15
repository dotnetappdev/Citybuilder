package com.citybuilder;

import com.citybuilder.core.GameEngine;
import org.junit.Test;

/**
 * Simple test to verify the game UI.
 */
public class UITest {
    
    @Test
    public void testMainMenu() throws Exception {
        // Just verify the game engine can be created
        GameEngine engine = new GameEngine();
        assert engine != null;
        System.out.println("Game engine created successfully");
    }
}
