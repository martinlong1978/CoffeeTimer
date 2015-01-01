package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

    boolean disableEvents = false;


    public JGrindPanel(Settings settings, GrinderControl control, final MainScreen mainScreen)
    {

        run = new JButton("Run");
        setup = new JButton("Setup");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(run);
        buttonPanel.add(setup);

        singleFrame = new JShotPanel("Single", new ImageIcon(getClass().getResource("coffeesingle.png")), settings.getSingleShotProperty());
        doubleFrame = new JShotPanel("Double", new ImageIcon(getClass().getResource("coffeedouble.png")), settings.getDoubleShotProperty());

        setLayout(new BorderLayout());


        //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        singleFrame.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {
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
                if (!disableEvents)
                {
                    disableEvents = true;
                    if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
                    {
                        doubleFrame.setSelected(true);
                    }
                    else
                    {
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
                mainScreen.startGrind(doubleFrame.isSelected());
            }
        });

        setup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                mainScreen.setup();
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(singleFrame, BorderLayout.WEST);
        add(doubleFrame, BorderLayout.EAST);


    }


}
