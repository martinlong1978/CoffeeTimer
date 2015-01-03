package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by martin on 02/01/15.
 */
public class TestDisplay implements Display
{

    private final JFrame frame;

    public TestDisplay()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        frame.setUndecorated(true);
    }

    public Container getContainer()
    {
        return frame.getContentPane();
    }

    @Override
    public void show()
    {
        frame.pack();
        frame.setVisible(true);
    }


    public void mouseClick(final int x, final int y)
    {
        System.out.println("Doing mouse events " + x + " " + y);
        try
        {

            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    frame.dispatchEvent(new MouseEvent(frame,
                            MouseEvent.MOUSE_MOVED,
                            System.currentTimeMillis(),
                            0,
                            x, y,
                            0,
                            false));
                }
            });
            Thread.sleep(10);
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    frame.dispatchEvent(new MouseEvent(frame,
                            MouseEvent.MOUSE_PRESSED,
                            System.currentTimeMillis(),
                            0,
                            x, y,
                            1,
                            false, MouseEvent.BUTTON1));
                }
            });
            Thread.sleep(10);
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    frame.dispatchEvent(new MouseEvent(frame,
                            MouseEvent.MOUSE_RELEASED,
                            System.currentTimeMillis(),
                            0,
                            x, y,
                            1,
                            false, MouseEvent.BUTTON1));
                }
            });
            Thread.sleep(5);
            javax.swing.SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    frame.dispatchEvent(new MouseEvent(frame,
                            MouseEvent.MOUSE_CLICKED,
                            System.currentTimeMillis(),
                            0,
                            x, y,
                            1,
                            false, MouseEvent.BUTTON1));
                }
            });
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
