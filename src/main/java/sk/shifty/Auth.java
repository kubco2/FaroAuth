package sk.shifty;

import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 04/05/12
 * Time: 19:57
 */
public class Auth {

    Preferences prefs =  Preferences.userNodeForPackage(Preferences.class);

    private String username;

    public Auth(){
        username=prefs.get("username",null);
    }

    public Auth(String username, String password) {
        setCreditenials(username,password);
    }

    public String get() {
        if(username == null) {
            throw new IllegalArgumentException("username and password cannot be empty");
        }
        return "Basic " + prefs.get("upbss",null);
    }

    public String getUsername() {
        return username;
    }

    public void setCreditenials(String username, String password) {
        this.username=username;
        prefs.put("username",username);
        prefs.put("upbss",javax.xml.bind.DatatypeConverter.printBase64Binary((username+":"+password).getBytes()));
    }

}
