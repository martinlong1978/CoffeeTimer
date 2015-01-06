package com.martinutils;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by martin on 06/01/15.
 */
public class Backlight implements IBacklight
{
    private static Backlight instance;
    private long timeoutAt;

    private Backlight()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                synchronized (this)
                {
                    enable();
                    screenOff();
                    try
                    {
                        while (System.currentTimeMillis() < timeoutAt)
                        {
                            wait(timeoutAt - System.currentTimeMillis() + 100);
                        }
                        screenOff();
                        while (System.currentTimeMillis() > timeoutAt)
                        {
                            wait(20000);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void enable()
    {
        writeToFile("/sys/class/gpio/export", "252");
        writeToFile("/sys/class/gpio/gpio252/direction", "out");
    }

    private void writeToFile(String file, String content)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void screenOff()
    {
        writeToFile("/sys/class/gpio/gpio252/value", "0");
    }

    private void screenOn()
    {
        writeToFile("/sys/class/gpio/gpio252/value", "1");
    }

    public static IBacklight getBacklight()
    {
        if (instance == null)
        {
            instance = new Backlight();
        }
        return instance;
    }


    @Override
    public void resetTimeout()
    {
        synchronized (this)
        {
            screenOn();
            timeoutAt = System.currentTimeMillis() + (5 * 60 * 1000);
            notifyAll();
        }
    }

}
