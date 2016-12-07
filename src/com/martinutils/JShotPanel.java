package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;

/**
 * Created by martin on 01/01/15.
 */
public class JShotPanel extends javax.swing.JPanel
{
    private final JButton plusButton;
    private final JButton minusButton;
    private final JShotToggle toggleButton;

    public JShotPanel(String name, Icon icon, ShotProperty shotProperty)
    {
        toggleButton = new JShotToggle(name, icon);

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(160,160));
        plusButton = new JShotAdjust("+", 1, shotProperty, toggleButton, 100, 60);
        minusButton = new JShotAdjust("-", -1, shotProperty, toggleButton, 100, 60);
        add(toggleButton);
        add(minusButton);
        add(plusButton);

    }

    public void addItemListener(ItemListener listener)
    {
        toggleButton.addItemListener(listener);
    }

    public void setSelected(boolean selected)
    {
        toggleButton.setSelected(selected);
    }

    public boolean isSelected()
    {
        return toggleButton.isSelected();
    }
}
