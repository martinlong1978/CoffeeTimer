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
public class JMainPanel extends JPanel
{
    private Settings settings;
    private GrinderControl control;
    private MainScreen mainScreen;

    private final JShotToggle single;
    private final JShotToggle aDouble;
    private final JButton run;
    private final JPanel singleFrame;
    private final JButton singlePlus;
    private final JButton singleMinus;
    private final JButton doublePlus;
    private final JButton doubleMinus;
    private final JPanel doubleFrame;
    private final JButton setup;
    private final JPanel buttonPanel;

    boolean disableEvents = false;


    public JMainPanel(Settings settings, GrinderControl control, final MainScreen mainScreen)
    {
        this.settings = settings;
        this.control = control;
        this.mainScreen = mainScreen;

        single = new JShotToggle("Single",new ImageIcon(getClass().getResource("coffeesingle.png")));
        aDouble = new JShotToggle("Double", new ImageIcon(getClass().getResource("coffeedouble.png")));
        run = new JButton("Run");
        setup = new JButton("Setup");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(run);
        buttonPanel.add(setup);

        singleFrame = new JPanel();
        doubleFrame = new JPanel();

        singlePlus = new JShotAdjust("+", 1, settings.getSingleShotProperty(), single);
        singleMinus = new JShotAdjust("-", -1, settings.getSingleShotProperty(), single);
        doublePlus = new JShotAdjust("+", 1, settings.getDoubleShotProperty(), aDouble);
        doubleMinus = new JShotAdjust("-", -1, settings.getDoubleShotProperty(), aDouble);

        singleFrame.setLayout(new BorderLayout());
        doubleFrame.setLayout(new BorderLayout());

        singleFrame.add(singlePlus, BorderLayout.EAST);
        singleFrame.add(singleMinus, BorderLayout.WEST);
        singleFrame.add(single, BorderLayout.NORTH);

        doubleFrame.add(doublePlus, BorderLayout.EAST);
        doubleFrame.add(doubleMinus, BorderLayout.WEST);
        doubleFrame.add(aDouble, BorderLayout.NORTH);

        setLayout(new BorderLayout());


        //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        single.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {
                if (!disableEvents)
                {
                    disableEvents = true;
                    if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
                    {
                        single.setSelected(true);
                    }
                    else
                    {
                        aDouble.setSelected(false);
                        aDouble.setBackground(Color.WHITE);
                        single.setBackground(Color.BLUE);
                    }
                    disableEvents = false;
                }
            }
        });

        aDouble.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent itemEvent)
            {
                if (!disableEvents)
                {
                    disableEvents = true;
                    if (itemEvent.getStateChange() == ItemEvent.DESELECTED)
                    {
                        aDouble.setSelected(true);
                    }
                    else
                    {
                        single.setSelected(false);
                        single.setBackground(Color.WHITE);
                        aDouble.setBackground(Color.BLUE);
                    }
                    disableEvents = false;
                }
            }
        });

        aDouble.setSelected(true);

        run.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                mainScreen.startGrind(aDouble.isSelected());
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
        add(singleFrame, BorderLayout.WEST);
        add(doubleFrame, BorderLayout.EAST);


    }


}
