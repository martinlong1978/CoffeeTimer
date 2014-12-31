package com.martinutils;

import java.io.*;

/**
 * Created by martin on 31/12/14.
 */
public class Settings implements ISettings
{
    private double rate;
    private double defaultRate;
    private int singleShot;
    private int doubleShot;
    private File settingsFile;

    public Settings(File settingsFile)
    {
        this(500, 500);
        this.settingsFile = settingsFile;
        load();
    }

    private void load()
    {
        if (settingsFile != null && settingsFile.exists())
        {
            try
            {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsFile));
                String line = bufferedReader.readLine();
                bufferedReader.close();
                String[] props = line.split(",");
                rate = Double.parseDouble(props[1]);
                defaultRate = Double.parseDouble(props[0]);
                singleShot = Integer.parseInt(props[2]);
                doubleShot = Integer.parseInt(props[3]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void save()
    {
        if (settingsFile == null)
        {
            return;
        }
        try
        {
            FileWriter fw = new FileWriter(settingsFile);
            fw.write(defaultRate + "," + rate + "," + singleShot + "," + doubleShot);
            fw.flush();
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Settings(double rate, double defaultRate)
    {
        this.rate = rate;
        this.defaultRate = defaultRate;
    }

    @Override
    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
        save();
    }

    public void resetRate()
    {
        rate = defaultRate;
        save();
    }

    public void setDefaultRate(double defaultRate)
    {
        this.defaultRate = defaultRate;
        save();
    }

    public double getDefaultRate()
    {
        return defaultRate;
    }
}
