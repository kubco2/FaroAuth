package sk.shifty;

import javax.swing.*;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 9:37 AM
 */
public class main {

    private static boolean enableGUI=true;
    private static String username;
    private static String password;

    private static Auth auth = new Auth();
    private static FaroAuth faroAuth;
    private static GUI gui;


    public static void main(String[] args) {
        boolean setup=false;
        for(String argument : args) {
            if(argument.startsWith("-u=")) {
                username=argument.substring(3);
                setup=true;
            }
            if(argument.startsWith("-p=")) {
                password=argument.substring(3);
                setup=true;
            }
            if(setup) {
                new Auth(username,password);
                System.exit(0);
            }
            if("-c".equals(argument)) {
                enableGUI=false;
            }
        }

        faroAuth = new FaroAuth("www.cesnet.cz",auth);
        if(enableGUI) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    gui = new GUI(faroAuth,auth);
                    gui.setVisible(true);
                }
            });
        } else {
            faroAuth.start();
        }

    }






}
