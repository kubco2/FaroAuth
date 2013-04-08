package sk.shifty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 9:45 AM
 */
public class FaroAuth implements Runnable {

    final static Logger log = LoggerFactory.getLogger(FaroAuth.class);

    private ControlableGUI gui;

    private PingService host;
    private Login login;
    private Thread loginT;
    private SyncLimit syncLimit;

    private boolean running;
    private int timeout=10;     // seconds
    private int syncTimeout=60; // seconds
    private int renewTimeout=1; // number of cycles of main process

    public FaroAuth(String url, Auth auth) {
        this.host = new PingService(url);
        this.login = new Login(url,auth);
        this.syncLimit = new SyncLimit(auth);
        log.info("idle");
    }

    public void start() {
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
        running=false;
        loginT.interrupt();
        syncLimit.stop();
        login.logout();
        if(gui!=null) {
            gui.setActivateButton(false);
        }
    }

    @Override
    public void run() {
        log.info("starting");
        int cycle=0;
        while (running) {
            cycle++;
            if(host.isReachable()) {
                setOnline(true);
                if(cycle % renewTimeout == 0) {
                    //login.renew();
                }
            } else {
                setOnline(false);
                try {
                    login.connect();
                } catch (AuthorizationException e) {
                    log.error("can't authenticate user",e);
                    if(gui!=null) {
                        syncLimit.stop();
                        gui.showNotAuthentizedError();
                    }
                    break;
                } catch (IOException e) {
                    log.error("can't load site", e);
                }
                setOnline(host.isReachable());
            }
            try {
                Thread.sleep(timeout * 1000);
            } catch (InterruptedException e) {
                log.debug("interrupting FaroAuth.");
            }
        }
        log.info("stopping");
        stop();
    }

    public boolean isAlive() {
        return loginT != null && loginT.isAlive();
    }

    public void setOnline(boolean online) {
        log.info("online: "+online);
        if(gui != null) {
            gui.setStatus(online);
        }
    }
    public void setFreeLimit() {
        log.info(syncLimit.getLimit() + " free");
        if(gui != null) {
            gui.setFreeLimit(syncLimit.getLimit());
        }
    }

    public void setGui(ControlableGUI gui) {
        if(gui == null) {
            throw new IllegalArgumentException("gui cannot be null");
        }
        this.gui = gui;
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
                } catch (IOException e) {
                    log.error("can't load site", e);
                }
                setFreeLimit();
                try{
                    Thread.sleep(syncTimeout*1000);
                } catch (InterruptedException e) {
                    log.debug("interrupting sync.");
                }
            }
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
