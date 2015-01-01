package com.martinutils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by martin on 01/01/15.
 */
public class JShotAdjust extends JButton
{

    public JShotAdjust(String label, final float amount, final ShotProperty singleShotProperty, final LabelListener labelListener)
    {
        this(label, amount, singleShotProperty, labelListener, 75);
    }

    public JShotAdjust(String label, final float amount, final ShotProperty singleShotProperty, final LabelListener labelListener, int size)
    {
        super(label);
        labelListener.updateLabel(singleShotProperty.getAmount() + "g");
        this.setPreferredSize(new Dimension(size, size));
        this.setFont(new Font(this.getFont().getName(), Font.BOLD, 30));
        this.setBackground(Color.WHITE);
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {

                singleShotProperty.increase(amount);
                labelListener.updateLabel(singleShotProperty.getAmount() + "g");
            }
        });
    }
}
