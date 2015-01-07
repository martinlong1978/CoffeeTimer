package com.martinutils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by martin on 04/01/15.
 */
public class TimerScreen extends JPanel
{
    private final JLabel secondsLabel;
    private long seconds = 0;
    private long startTime;
    private MainScreen mainScreen;
    private Thread timerThread;

    public TimerScreen(final MainScreen mainScreen)
    {
        this.mainScreen = mainScreen;

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        final JButton cancel = new JStyleButton("Cancel");
        cancel.setPreferredSize(new Dimension(120, 40));

        secondsLabel = new JLabel("0");
        secondsLabel.setFont(new Font(secondsLabel.getFont().getName(), Font.BOLD, 60));
        secondsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        secondsLabel.setBackground(Color.WHITE);

        setLayout(new GridBagLayout());

        centerPanel.add(secondsLabel, BorderLayout.NORTH);
        centerPanel.add(cancel, BorderLayout.SOUTH);

        cancel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Backlight.getBacklight().resetTimeout();
                timerThread.interrupt();
            }
        });
        add(centerPanel);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Insets insets = getInsets();
        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.bottom + insets.top);
        int radius = Math.min(width, height)-20;
        int x = insets.left + ((width - radius) / 2);
        int y = insets.right + ((height - radius) / 2);

        double extent = 0.006d * seconds;

        Rectangle2D rect = new Rectangle2D.Double(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);
        g2d.setColor(Color.WHITE);
        g2d.fill(rect);

        Ellipse2D circle = new Ellipse2D.Double(x, y, radius, radius);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(circle);

        g2d.setColor(Color.BLUE);
        Arc2D arc = new Arc2D.Double(x, y, radius, radius, 90, Math.max(-150, -extent), Arc2D.PIE);
        g2d.fill(arc);

        extent = Math.max(0, extent - 150);

        g2d.setColor(Color.GREEN);
        arc = new Arc2D.Double(x, y, radius, radius, 300, Math.max(-30, -extent), Arc2D.PIE);
        g2d.fill(arc);

        extent = Math.max(0, extent - 30);

        g2d.setColor(Color.RED);
        arc = new Arc2D.Double(x, y, radius, radius, 270, -extent, Arc2D.PIE);
        g2d.fill(arc);

        circle = new Ellipse2D.Double(x + 15, y + 15, radius - 30, radius - 30);
        g2d.setColor(Color.WHITE);
        g2d.fill(circle);

        g2d.dispose();


    }

    public void start()
    {
        timerThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                seconds = 0;
                startTime = System.currentTimeMillis();
                try
                {
                    while (seconds < 60000)
                    {
                        long time = System.currentTimeMillis();
                        long waitTime = (startTime + seconds + 1000) - time;
                        Thread.sleep(waitTime);
                        time = System.currentTimeMillis();
                        seconds = time - startTime;
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                secondsLabel.setText(seconds/1000 + "");
                                repaint();
                            }
                        });
                    }

                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
                resetScreen();
                mainScreen.complete(0);
            }
        });
        timerThread.start();
    }

    private void resetScreen()
    {
        seconds = 0;
        secondsLabel.setText("0");
    }
}
