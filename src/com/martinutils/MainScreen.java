package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * Created by martin on 31/12/14.
 */
public class MainScreen implements TimerCallback
{
    private final JShotToggle single;
    private final JShotToggle aDouble;
    private final JButton run;
    private final JPanel mainPanel;
    private final JPanel singleFrame;
    private final JButton singlePlus;
    private final JButton singleMinus;
    private final JButton doublePlus;
    private final JButton doubleMinus;
    private final JPanel doubleFrame;
    private final JLabel progress;
    private Settings settings;
    private GrinderControl control;
    boolean disableEvents = false;

    JFrame frame;

    MainScreen(final Settings settings, GrinderControl control)
    {

        this.settings = settings;
        this.control = control;
        frame = new JFrame("CoffeeTimer");
        single = new JShotToggle("Single");
        aDouble = new JShotToggle("Double");
        run = new JButton("Run");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Already there
        frame.setUndecorated(true);

        progress = new JLabel("Ground 0g\n in 0s");
        progress.setHorizontalAlignment(JLabel.CENTER);
        progress.setFont(new Font(progress.getFont().getName(), Font.PLAIN, 30));

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

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());


        //frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        frame.getContentPane().setLayout(new BorderLayout());

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
                startGrind(settings);
            }
        });

        mainPanel.add(run, BorderLayout.SOUTH);
        mainPanel.add(singleFrame, BorderLayout.WEST);
        mainPanel.add(doubleFrame, BorderLayout.EAST);
        frame.getContentPane().add(mainPanel);
    }

    private void startGrind(Settings settings)
    {
        int amount = aDouble.isSelected() ? settings.getDoubleShot() : settings.getSingleShot();
        frame.getContentPane().remove(mainPanel);
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
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(mainPanel);
                mainPanel.updateUI();
                frame.getContentPane().invalidate();
            }
        });

    }
}
