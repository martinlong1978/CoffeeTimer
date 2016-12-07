package com.martinutils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by martin on 01/01/15.
 */
public class JShotToggle extends JToggleButton implements LabelListener
{
    private String shot;

    public JShotToggle(String shot, Icon icon)
    {
        super(shot);
        this.shot = shot;
        this.setPreferredSize(new Dimension(200, 200));
        this.setFont(new Font(this.getFont().getName(), Font.PLAIN, 18));
        this.setBackground(Color.WHITE);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setIcon(icon);

    }

    @Override
    public void updateLabel(String value)
    {
        setText(shot + " " + value);
    }
}
