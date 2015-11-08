package al.arra.api.flickr.crypt;

import java.util.Random;

/**
 * Created by Gezim on 11/7/2015.
 */
public class TimestampService {
    private Timer timer;

    /**
     * Default constructor.
     */
    public TimestampService()
    {
        timer = new Timer();
    }

    /**
     * {@inheritDoc}
     */
    public String getNonce()
    {
        Long ts = getTs();
        return String.valueOf(ts + timer.getRandomInteger());
    }

    /**
     * {@inheritDoc}
     */
    public String getTimestampInSeconds()
    {
        return String.valueOf(getTs());
    }

    private Long getTs()
    {
        return timer.getMilis() / 1000;
    }

    void setTimer(Timer timer)
    {
        this.timer = timer;
    }

    /**
     * Inner class that uses {@link System} for generating the timestamps.
     *
     * @author Pablo Fernandez
     */
    static class Timer
    {
        private final Random rand = new Random();
        Long getMilis()
        {
            return System.currentTimeMillis();
        }

        Integer getRandomInteger()
        {
            return rand.nextInt();
        }
    }
}
