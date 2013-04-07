package sk.shifty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 9:45 AM
 */
public class FaroAuth implements Runnable {

    final static Logger log = LoggerFactory.getLogger(FaroAuth.class);

    private PingService host;
    private Login login;
    private Thread loginT;
    private SyncLimit syncLimit;

    private boolean running;
    private int timeout=10;
    private int syncTimeout=60;

    private CallBack callBack;
    private CallBack onlineSetter;

    public FaroAuth(String url, Auth auth) {
        this.host = new PingService(url);
        this.login = new Login(url,auth);
        this.syncLimit = new SyncLimit(auth);
        log.info("idle");
    }

    public void start() {
        log.info("starting");
        if(loginT == null || !loginT.isAlive()) {
            running=true;
            loginT = new Thread(this);
            loginT.start();
            syncLimit.start();
        } else {
            log.info("running");
        }

    }
    public void stop() {
        log.info("stopping");
        running=false;
        syncLimit.stop();
        loginT.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            if(host.isReachable()) {
                setOnline(true);
            } else {
                setOnline(false);
                try {
                    login.connect();
                } catch (AuthorizationException e) {
                    log.error("can't authenticate user",e);
                    executeCallBack("can't authenticate user");
                    break;
                } catch (IOException e) {
                    log.error("can't load site", e);
                    break;
                }
                setOnline(host.isReachable());
            }
            try {
                Thread.sleep(timeout * 1000);
            } catch (InterruptedException e) {
                log.debug("interrupting service.", e);
            }
        }
        stop();
    }



    public boolean isRunning() {
        return this.running;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void executeCallBack(String str) {
        if(callBack!=null) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("msg",str);
            callBack.setProperties(map);
            callBack.execute();
        }
    }

    public void setOnlineSetter(CallBack onlineSetter) {
        this.onlineSetter = onlineSetter;
    }

    public void setOnline(boolean online) {
        log.info("online: "+online);
        if(onlineSetter != null) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("limit",syncLimit.getLimit());
            map.put("online",online);
            onlineSetter.setProperties(map);
            onlineSetter.execute();
        }
    }

    class SyncLimit implements Runnable {

        final Logger log = LoggerFactory.getLogger(SyncLimit.class);

        private Thread syncT;
        private Auth auth;
        private Double limit;
        private boolean running;

        public SyncLimit(Auth auth) {
            this.auth=auth;
        }

        @Override
        public void run() {
            log.info("starting");

            Limit page = null;
            try {
                page = new Limit(auth);
            } catch (Exception e) {
                log.error("Limit cannot be created",e);
                stop();
                return;
            }
            DecimalFormat df1 = new DecimalFormat("#.##");
            DecimalFormat df2 = new DecimalFormat("#,##");
            while(running) {
                try{
                    try {
                        limit=Double.valueOf(df2.format(page.getFree()));
                    } catch (NumberFormatException e) {
                        try {
                            limit=Double.valueOf(df1.format(page.getFree()));
                        } catch (NumberFormatException nfe) {
                            log.error("stopping can't determine decimal points",nfe,e);
                            break;
                        }
                    }
                    log.info(limit + " free");
                } catch (IOException e) {
                    log.error("can't load site", e);
                }
                try{
                    Thread.sleep(syncTimeout*1000);
                } catch (InterruptedException e) {
                    log.debug("interrupting sync.", e);
                }
            }
            stop();
            log.info("stopping");
        }

        public void start() {
            if(syncT == null || !syncT.isAlive()) {
                running=true;
                syncT = new Thread(this);
                syncT.start();
            } else {
                log.info("running");
            }
        }
        public void stop() {
            running=false;
            syncT.interrupt();
        }
        public boolean isAlive() {
            return syncT.isAlive();
        }

        public Double getLimit() {
            return isAlive()?limit:null;
        }
    }
}
