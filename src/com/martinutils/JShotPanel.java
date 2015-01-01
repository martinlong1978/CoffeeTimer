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

        setLayout(new BorderLayout());
        plusButton = new JShotAdjust("+", 1, shotProperty, toggleButton);
        minusButton = new JShotAdjust("-", -1, shotProperty, toggleButton);
        add(plusButton, BorderLayout.EAST);
        add(minusButton, BorderLayout.WEST);
        add(toggleButton, BorderLayout.NORTH);

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
