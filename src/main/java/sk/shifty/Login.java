package sk.shifty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login  {

    final static Logger log = LoggerFactory.getLogger(Login.class);

    private String checkAddress;
    private Auth auth;

    private String authPopupAddress;
    private String mac;
    private String token;
    private String gateway;
    private String timeout;

    public static long lastrun=0;

    public Login(String checkUrl, Auth auth) {
        checkAddress =checkUrl;
        this.auth=auth;
    }

    /**
     * Connect and load page, determine if it must authenticate, or renew
     */
    public void connect() throws AuthorizationException, IOException {
        Page page = new Page(checkAddress);
        page.connect().load();
        boolean faro = isFaroPage(page);
        log.debug("Faro page " + faro);
        if(faro) {
            log.info("must authenticate");
            try {
                lastrun=0;
                authenticate(page.findUrl("login"));
            } catch (IOException e) {
                throw new AuthorizationException("can't authenticate user",e);
            }
        }
    }

    /**
     * Renew FARO session
     */
    public void renew(){
        if(authPopupAddress == null) {
            log.debug("renew denied, because no authPopupAddress available");
            return;
        }
        if(timeout != null && lastrun+(Long.valueOf(timeout)*1000) > System.currentTimeMillis()) {
            log.debug("renew denied, because of timeout need: {} , now: {}",timeout,Long.valueOf(timeout)-(lastrun+(Long.valueOf(timeout)*1000)-System.currentTimeMillis())/1000);
            return;
        }
        lastrun=System.currentTimeMillis();
        log.debug("renew start with mac:"+mac+",token:"+token+",gateway:"+gateway+",timeout:"+timeout);
        try {
            Page authPopupPage = new Page(authPopupAddress).withAuth(auth);
            if(token!=null) {
                authPopupPage.withPost(
                        "mode=renew", "user=" + auth.getUsername(), "pass", "mac=" + mac,
                        "token=" + token, "gateway=" + gateway, "timeout=" + timeout
                );
            }
            authPopupPage.connect().load();
            getParamsForPost(authPopupPage.toString());
        } catch(Exception e) {
            log.error("cant reauthentizate",e);
        }
        log.debug("renew end with mac:"+mac+",token:"+token+",gateway:"+gateway+",timeout:"+timeout);
    }

    public void getParamsForPost(String source) {
        String pattern =
                "<form name=\"RenewalForm\" action=\"(.*)\" method=\"post\">\n" +
                "    <input type=\"hidden\" name=\"mode\" value=\"renew\">\n" +
                "    <input type=\"hidden\" name=\"user\" value=\""+auth.getUsername()+"\">\n" +
                "    <input type=\"hidden\" name=\"pass\" value=\"\">\n" +
                "    <input type=\"hidden\" name=\"mac\" value=\"(.*)\">\n" +
                "    <input type=\"hidden\" name=\"token\" value=\"(.*)\">\n" +
                "    <input type=\"hidden\" name=\"gateway\" value=\"(.*)\">\n" +
                "    <input type=\"hidden\" name=\"timeout\" value=\"(.*)\">\n" +
                "</form>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        if(m.find()) {
            authPopupAddress =m.group(1);
            mac=m.group(2);
            token=m.group(3);
            gateway=m.group(4);
            timeout=m.group(5);
        }

    }

    private void authenticate(String url) throws IOException {
        Page pageLogin = new Page(url).connect().load();
        url = pageLogin.findUrl("authlogin");
        url = Constants.alephHost +url;
        Page pageAuthen = new Page(url).withAuth(auth).connect().load();
        url = pageAuthen.findUrl("ticket");
        url = url.split("=",2)[1];
        authPopupAddress = pageAuthen.findUrl("authlogin");
        new Page(url).withAuth(auth).connect().load();
        log.info("logged in");

        renew();
    }

    /**
     * log out from FARO
     */
    public void logout() {
        if(gateway == null) {
            log.info("cant logout, because no gateway available");
            return;
        }
        log.debug("log out");
        try {
            Page logoutPage = new Page(gateway+"/logout");
            logoutPage.connect().load();
        } catch (IOException e) {
            log.error("exception thrown while logging out",e);
        }
        log.info("logged out");
    }

    private boolean isFaroPage(Page page) {
        return page.findOnPage("aleph.mendelu.cz");
    }
}
