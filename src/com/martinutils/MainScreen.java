package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by martin on 31/12/14.
 */
public class MainScreen implements TimerCallback
{

    // 5 and 6 are in use by PiTFT. Doh! Use 2 and 3
    private final JGrindPanel grindPanel;
    private final JLabel progress;
    private final JSetupPanel setupPanel;
    private final TimerScreen timerPanel;
    private Settings settings;
    private GrinderControl control;
    private boolean settingsScreen = false;

    private Container parentContainer;

    MainScreen(final Settings settings, GrinderControl control, Container parent)
    {

        this.settings = settings;
        this.control = control;
        parentContainer = parent;

        grindPanel = new JGrindPanel(settings, control, this);
        setupPanel = new JSetupPanel(settings, control, this);
        timerPanel = new TimerScreen(this);


        progress = new JLabel("Ground 0g\n in 0s");
        progress.setHorizontalAlignment(JLabel.CENTER);
        progress.setFont(new Font(progress.getFont().getName(), Font.PLAIN, 30));

        parentContainer.setMaximumSize(new Dimension(320, 240));
        parentContainer.setMinimumSize(new Dimension(320, 240));
        parentContainer.setPreferredSize(new Dimension(320, 240));
        parentContainer.setLayout(new BorderLayout());
        parentContainer.add(grindPanel);
    }

    public void startGrind(boolean doubleShot)
    {
        int amount = doubleShot ? settings.getDoubleShot() : settings.getSingleShot();
        parentContainer.removeAll();
        parentContainer.add(progress);
        Timer t = new Timer(amount, 0, settings, control);
        t.start(this);
    }

    public void startTimer()
    {
        parentContainer.removeAll();
        parentContainer.add(timerPanel);
        timerPanel.updateUI();
        timerPanel.start();
    }

    public static void main(String[] args)
    {

        final Display display = new File("/dev/fb1").exists() ? new FrameBufferDisplay() : new TestDisplay();
        final GrinderControl grinderControl = new File("/dev/fb1").exists() ? new RaspPiGrinderControl() : new DummyGrinderControl();

        final MainScreen screen = new MainScreen(new Settings(new File("/opt/coffee/coffee.settings")), grinderControl, display.getContainer());
        display.show();
        grinderControl.addGrindButtonListener(new GrindButtonListener()
        {
            @Override
            public void buttonPressed()
            {
                screen.grinderActivated();
            }
        });


/*
        try
        {
            Thread.sleep(2000);
            display.mouseClick(30, 120);
            Thread.sleep(1000);
            display.mouseClick(30, 120);
            Thread.sleep(1000);
            display.mouseClick(250, 120);
            Thread.sleep(1000);
            display.mouseClick(30, 120);
            Thread.sleep(1000);
            display.mouseClick(250, 120);
            Thread.sleep(1000);
            display.mouseClick(145, 220);
            Thread.sleep(1000);
            display.mouseClick(150, 10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

*/
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
                parentContainer.removeAll();
                parentContainer.add(grindPanel);
                grindPanel.updateUI();
            }
        });
    }

    public void setup()
    {
        settingsScreen = true;
        parentContainer.removeAll();
        parentContainer.add(setupPanel);
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
