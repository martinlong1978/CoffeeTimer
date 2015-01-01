package com.martinutils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by martin on 31/12/14.
 */
public class MainScreen implements TimerCallback
{
    private final JGrindPanel grindPanel;
    private final JLabel progress;
    private final JSetupPanel setupPanel;
    private Settings settings;
    private GrinderControl control;
    private long lockout;
    private boolean settingsScreen = false;

    private JFrame frame;

    MainScreen(final Settings settings, GrinderControl control)
    {

        this.settings = settings;
        this.control = control;
        frame = new JFrame("CoffeeTimer");
        frame.setMaximumSize(new Dimension(320, 240));
        frame.setMinimumSize(new Dimension(320, 240));
        frame.setPreferredSize(new Dimension(320, 240));

        grindPanel = new JGrindPanel(settings, control, this);
        setupPanel = new JSetupPanel(settings, control, this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        frame.setUndecorated(true);

        progress = new JLabel("Ground 0g\n in 0s");
        progress.setHorizontalAlignment(JLabel.CENTER);
        progress.setFont(new Font(progress.getFont().getName(), Font.PLAIN, 30));

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(grindPanel);
    }

    public void startGrind(boolean doubleShot)
    {
        int amount = doubleShot ? settings.getDoubleShot() : settings.getSingleShot();
        frame.getContentPane().removeAll();
        frame.getContentPane().add(progress);
        Timer t = new Timer(amount, 0, settings, control);
        t.start(this);
    }

    public void show()
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args)
    {
        MainScreen screen = new MainScreen(new Settings(new File("coffee.settings")), new GrinderControl()
        {
            @Override
            public void start()
            {
                System.out.println("Starting grinder");
            }

            @Override
            public void stop()
            {
                System.out.println("Stopping grinder");
            }
        });
        screen.show();

        while (true)
        {
            try
            {
                System.in.read();
                screen.grinderActivated();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void updateOutput(final int currentgrams, final long time)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                progress.setText("Ground " + currentgrams + "g in " + (Math.floor(time / 100) / 10) + "s");
            }
        });
    }

    @Override
    public void complete(long totalTime)
    {
        reset();
        grindPanel.setLockout(5);
    }

    public void reset()
    {
        settingsScreen = false;
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(grindPanel);
                grindPanel.updateUI();
            }
        });
    }

    public void setup()
    {
        settingsScreen = true;
        frame.getContentPane().removeAll();
        frame.getContentPane().add(setupPanel);
        setupPanel.updateUI();
    }

    public void grinderActivated()
    {
        if (settingsScreen)
        {
            setupPanel.grinderActivated();
        }
        else
        {
            grindPanel.grinderActivated();
        }
    }
}
