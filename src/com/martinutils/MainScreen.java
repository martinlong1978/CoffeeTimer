package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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

    private JFrame frame;

    MainScreen(final Settings settings, GrinderControl control)
    {

        this.settings = settings;
        this.control = control;
        frame = new JFrame("CoffeeTimer");

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
        new MainScreen(new Settings(new File("coffee.settings")), new GrinderControl()
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
        }).show();

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

    }

    public void reset()
    {
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
        frame.getContentPane().removeAll();
        frame.getContentPane().add(setupPanel);
        setupPanel.updateUI();
    }
}
