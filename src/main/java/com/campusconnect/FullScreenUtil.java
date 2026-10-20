package com.campusconnect;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

public class FullScreenUtil {

    public static void maximize(JFrame frame) {

        // Window resizable hona chahiye
        frame.setResizable(true);

        // Pehle normal state set karo
        frame.setExtendedState(JFrame.NORMAL);

        // Screen ka available size
        Dimension screenSize =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getMaximumWindowBounds()
                        .getSize();

        // Proper initial size set karo
        frame.setSize(
                screenSize.width,
                screenSize.height
        );

        // Center position
        frame.setLocationRelativeTo(null);

        // Finally maximize
        frame.setExtendedState(
                JFrame.MAXIMIZED_BOTH
        );
    }
}