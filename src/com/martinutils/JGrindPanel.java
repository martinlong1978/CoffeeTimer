package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by martin on 01/01/15.
 */
public class JGrindPanel extends JPanel
{

    private final JButton run;
    private final JShotPanel singleFrame;
    private final JShotPanel doubleFrame;
    private final JButton setup;
    private final JPanel buttonPanel;
    private final JButton timer;

    boolean disableEvents = false;
    private long lockout;
    private MainScreen mainScreen;


    public JGrindPanel(Settings settings, GrinderControl control, final MainScreen mainScreen)
    {
        this.mainScreen = mainScreen;

        run = new JStyleButton("Run");
        setup = new JStyleButton("Setup");
        timer = new JStyleButton("Timer");


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(run);
        buttonPanel.add(setup);
        buttonPanel.add(timer);

        singleFrame = new JShotPanel("Single", new ImageIcon(getClass().getResource("coffeesingle.png")), settings.getSingleShotProperty());
        doubleFrame = new JShotPanel("Double", new ImageIcon(getClass().getResource("coffeedouble.png")), settings.getDoubleShotProperty());

        setLayout(new BorderLayout());

        singleFrame.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {
                Backlight.getBacklight().resetTimeout();
                if (!disableEvents)
                {
                    disableEvents = true;
                    if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
                    {
                        singleFrame.setSelected(true);
                    }
                    else
                    {
                        doubleFrame.setSelected(false);
                        System.out.println("Selected single");
                    }
                    disableEvents = false;
                }
            }
        });

        doubleFrame.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {
                Backlight.getBacklight().resetTimeout();
                if (!disableEvents)
                {
                    disableEvents = true;
                    if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
                    {
                        doubleFrame.setSelected(true);
                    }
                    else
                    {
                        System.out.println("Selected double");
                        singleFrame.setSelected(false);
                    }
                    disableEvents = false;
                }
            }
        });

        doubleFrame.setSelected(true);

        run.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                Backlight.getBacklight().resetTimeout();
                grinderActivated();
            }
        });

        setup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                Backlight.getBacklight().resetTimeout();
                mainScreen.setup();
            }
        });

        timer.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Backlight.getBacklight().resetTimeout();
                mainScreen.startTimer();
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(singleFrame, BorderLayout.WEST);
        add(doubleFrame, BorderLayout.EAST);


    }

    public void grinderActivated()
    {
        if (lockcheck())
        {
            mainScreen.startGrind(doubleFrame.isSelected());
        }
    }

    public void setLockout(int sec)
    {
        lockout = System.currentTimeMillis() + (1000 * sec);
    }

    private boolean lockcheck()
    {
        if (System.currentTimeMillis() > lockout)
        {
            // Immediately lock out for 1 min
            setLockout(60);
            return true;
        }
        return false;
    }
}
