package com.citybuilder;

import com.citybuilder.core.GameEngine;
import com.citybuilder.ui.MainMenuFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Utility to take screenshots of the game UI.
 */
public class ScreenshotUtil {
    public static void main(String[] args) throws Exception {
        // Create the main menu
        GameEngine engine = new GameEngine();
        engine.start();
        
        // Wait for UI to render
        Thread.sleep(2000);
        
        // Get all frames
        Frame[] frames = Frame.getFrames();
        if (frames.length > 0) {
            Frame frame = frames[0];
            
            // Capture screenshot
            BufferedImage screenshot = new BufferedImage(
                frame.getWidth(),
                frame.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );
            
            frame.paint(screenshot.getGraphics());
            
            // Save screenshot
            File output = new File("main-menu-screenshot.png");
            ImageIO.write(screenshot, "png", output);
            System.out.println("Screenshot saved to: " + output.getAbsolutePath());
        }
        
        System.exit(0);
    }
}
