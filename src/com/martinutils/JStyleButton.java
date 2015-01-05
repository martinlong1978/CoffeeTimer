package com.martinutils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by martin on 04/01/15.
 */
public class JStyleButton extends JButton
{
    public JStyleButton(String run)
    {
        super(run);
        setPreferredSize(new Dimension(100, 40));
        setFont(new Font(getFont().getName(), Font.PLAIN, 20));
        setBackground(new Color(50,50,120));
        setForeground(Color.WHITE);

    }
}
