package sk.shifty;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 17/05/12
 * Time: 19:19
 */
public class Timeout {

    private long timeout;
    private long nextBreak=0;

    public Timeout(long timeoutMillis) {
        if(timeoutMillis == 0) {
            throw new IllegalArgumentException("must be greather than 0");
        }
        timeout=timeoutMillis;
    }

    public void prolong() {
        nextBreak=getTime()+timeout;
    }

    public boolean isValid() {
        boolean isValid = nextBreak>getTime();
        if(isValid) {
            return isValid;
        }
        prolong();
        return isValid;
    }

    public boolean exec() {
        return !isValid();
    }

    public long getTime() {
        return System.currentTimeMillis();
    }
}
