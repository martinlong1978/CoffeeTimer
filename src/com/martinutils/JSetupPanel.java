package com.martinutils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Created by martin on 01/01/15.
 */
public class JSetupPanel extends JPanel implements CalibratorCallback, ActionListener, LabelListener
{
    private final JPanel controlPanel;
    private Calibrator calibrator;
    private final JButton button;
    private final JLabel outputLabel;
    private Settings settings;
    private GrinderControl control;
    private MainScreen mainScreen;
    private float output = 0;
    private long lockout = 0;

    public JSetupPanel(Settings settings, GrinderControl control, MainScreen mainScreen)
    {
        this.settings = settings;
        this.control = control;
        this.mainScreen = mainScreen;

        outputLabel = new JLabel("0.0");
        outputLabel.setFont(new Font(outputLabel.getFont().getName(), Font.PLAIN, 35));
        outputLabel.setBorder(new EmptyBorder(10, 25, 10, 25));

        controlPanel = new JPanel();

        setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        button = new JStyleButton("Start");
        button.setFont(new Font(getFont().getName(), Font.PLAIN, 40));

        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(250, 100));
        add(button, c);


        ShotProperty sp = new ShotProperty()
        {
            @Override
            public void increase(float amount)
            {
                setOutput(output + amount);
            }

            @Override
            public float getAmount()
            {
                return output;
            }
        };

        c.gridheight = 1;
        c.gridwidth = 1;

        c.gridx = 1;
        c.gridy = 0;
        controlPanel.add(new JShotAdjust("+", 1, sp, this, 60, 60), c);
        c.gridx = 1;
        c.gridy = 1;
        controlPanel.add(new JShotAdjust("-", -1, sp, this, 60, 60), c);
        c.gridx = 3;
        c.gridy = 0;
        controlPanel.add(new JShotAdjust("+", 0.1f, sp, this, 60, 60), c);
        c.gridx = 3;
        c.gridy = 1;
        controlPanel.add(new JShotAdjust("-", -0.1f, sp, this, 60, 60), c);

        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        c.gridwidth = 1;
        controlPanel.add(outputLabel, c);

        reset();

    }

    public void setOutput(float output)
    {
        this.output = output;
        outputLabel.setText(new DecimalFormat("#0.0").format(output));
        if (calibrator != null && calibrator.getState() == CalibratorState.INITIALDOSE)
        {
            button.setLabel(output < settings.getDoubleShot() ? "Top up" : "Done");
        }

    }

    private void reset()
    {
        setLockout(0);
        remove(controlPanel);
        setOutput(0);
        if (calibrator != null)
        {
            calibrator.dispose();
        }
        calibrator = new Calibrator(settings.getDoubleShot(), settings, control, this);
    }

    @Override
    public void updateOutput(int currentgrams, long time)
    {
        setOutput(currentgrams);
    }

    @Override
    public void updateState(CalibratorState state)
    {
        switch (state)
        {
            case START:
                button.setLabel("Start");
                break;
            case DOSING:
                add(controlPanel);
                controlPanel.updateUI();
                button.setLabel("Cancel");
                break;
            case INITIALDOSE:
                setLockout(5);
                button.setLabel("Done");
                break;
            case TOPUP:
                button.setLabel("Cancel");
                break;
            case DONE:
                reset();
                mainScreen.reset();
        }

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        switch (calibrator.getState())
        {
            case START:
                if (lockcheck())
                {
                    calibrator.initialDose();
                }
                break;
            case DOSING:
                reset();
                mainScreen.reset();
                break;
            case INITIALDOSE:
                if (output >= settings.getDoubleShot() || lockcheck())
                {
                    calibrator.enterActualOutput(output);
                }
                break;
            case TOPUP:
                reset();
                mainScreen.reset();
                break;
        }

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

    @Override
    public void updateLabel(String value)
    {
        // Do nothing
    }


    public void grinderActivated()
    {
        switch (calibrator.getState())
        {
            case START:
                if (lockcheck())
                {
                    calibrator.initialDose();
                }
                break;
            case INITIALDOSE:
                if (lockcheck())
                {
                    calibrator.enterActualOutput(output);
                }
                break;
        }
    }

    private void setLockout(int sec)
    {
        lockout = System.currentTimeMillis() + (1000 * sec);
    }
}
