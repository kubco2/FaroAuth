package sk.shifty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 9:16 AM
 */
public class PingService {

    final static Logger log = LoggerFactory.getLogger(PingService.class);

    private int LINUX=0;
    private int WINDOWS=1;
    private int MAC=2;
    private int OS;

    private String url="8.8.8.8";
    private int deadline =1; //seconds

    public PingService() {
        String os = System.getProperty("os.name");
        if(os.startsWith("Linux")) {
            OS=0;
        } else if(os.startsWith("Windows")) {
            OS=1;
        } else if(os.startsWith("Mac")) {
            OS=0;
        } else {
            throw new IllegalStateException("OS is not recognized");
        }
    }
    public PingService(int deadline) {
        this();
        setDeadline(deadline);
    }

    public PingService(String url) {
        this();
        setUrl(url);
    }

    public PingService(int deadline, String url) {
        this();
        setDeadline(deadline);
        setUrl(url);
    }

    public boolean isReachable() {
        if(OS==LINUX) {
            try {
                return pingLinux();
            } catch (Exception e) {
                return false;
            }
        }
        if(OS==WINDOWS) {
            try {
                return pingWindows();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private boolean pingLinux() throws Exception {
        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -s 1 -w "+ deadline +" "+url);
        int returnVal = p1.waitFor();
        log.debug("ping return {}",returnVal);
        return (returnVal==0);
    }

    private boolean pingWindows() throws Exception {
        Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 -l 1 -w "+(deadline *1000)+" "+url);
        int returnVal = p1.waitFor();
        log.debug("ping return {}",returnVal);
        return (returnVal==0);
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        if(deadline <= 0) {
            throw new IllegalArgumentException("deadline must be more than 0");
        }
        this.deadline = deadline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if(url == null || "".equals(url)) {
            throw new IllegalArgumentException("url must be URL, not empty");
        }
        this.url = url;
    }
}
