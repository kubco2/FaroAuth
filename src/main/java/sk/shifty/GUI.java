package sk.shifty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 15/05/12
 * Time: 12:41
 */
public class GUI extends JFrame implements ControlableGUI {

    Preferences prefs =  Preferences.userNodeForPackage(Preferences.class);

    private FaroAuth faroAuth;
    private Auth auth;

    public GUI(FaroAuth fa,Auth a) {
        faroAuth=fa;
        auth=a;
        faroAuth.setGui(this);
        setTitle("FaroAuth");
        setLayout(new GridLayout(0,1));
        init();
        createSystemTray();
        pack();
        onStart();
    }

    public void onStart() {
        if(prefs.getBoolean("activateOnStart",false)) {
            start();
        }
    }

    public void start() {
        setActivateButton(true);
        faroAuth.start();
    }
    public void stop() {
        setActivateButton(false);
        faroAuth.stop();
    }

    public void setActivateButton(boolean start) {
        activate.setText(start?"Log out":"Log in");
    }

    JButton activate;
    JButton save;
    JTextField nameField;
    JTextField passField;
    JCheckBox activateOnStart;
    JLabel free;

    public void init() {

        activate = new JButton("Log in");
        activate.addActionListener(runAuth);
        activateOnStart = new JCheckBox("activation on start");
        activateOnStart.setSelected(prefs.getBoolean("activateOnStart",false));
        activateOnStart.addActionListener(activOnStart);
        nameField = new JTextField(auth.getUsername());
        nameField.setPreferredSize(new Dimension(100,25));
        passField = new JTextField("****");
        passField.setPreferredSize(new Dimension(100,25));
        save = new JButton("Save");
        save.addActionListener(saveCreds);
        free = new JLabel("..................................................");

        JPanel panelActivation = new JPanel(new FlowLayout());
        panelActivation.add(activate);
        panelActivation.add(activateOnStart);
        JPanel panelCreds = new JPanel(new FlowLayout());
        panelCreds.add(nameField);
        panelCreds.add(passField);
        panelCreds.add(save);
        JPanel panelInfo = new JPanel(new FlowLayout());
        panelInfo.add(free);

        try {
            final URI uri = new URI("https://github.com/kubco2/FaroAuth/issues");
            JButton link = new JButton();
            link.setText("V pripade problemov nevahajte napisat");
            link.setToolTipText(uri.toString());
            link.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    open(uri);
                }
            });
            JPanel panelWeb = new JPanel(new FlowLayout());
            panelWeb.add(link);
            add(panelWeb);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        add(panelActivation);
        add(panelCreds);
        add(panelInfo);
    }

    ActionListener activOnStart = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            prefs.putBoolean("activateOnStart",activateOnStart.isSelected());
        }
    };
    ActionListener runAuth = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(faroAuth.isAlive()) {
                stop();
            } else {
                start();
            }
        }
    };
    ActionListener saveCreds = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            auth.setCreditenials(nameField.getText(),passField.getText());
            passField.setText("****");
        }
    };

    public void createSystemTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage(this.getClass().getResource("/bulb.gif"), "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem display = new MenuItem("Display");
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
            }
        });
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        popup.add(display);
        popup.add(exit);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
    protected static Image createImage(URL path, String description) {
        return (new ImageIcon(path, description)).getImage();
    }

    private boolean online=false;
    @Override
    public void setStatus(boolean online) {
        this.online=online;
        updateBar();
    }
    private Double freeLimit;
    @Override
    public void setFreeLimit(Double free) {
        this.freeLimit=free;
        updateBar();
    }

    private void updateBar() {
        free.setText("status: "+(online?"online":"offline")+" - free data: "+freeLimit+" GB");
    }

    @Override
    public void showNotAuthentizedError() {
        JOptionPane.showMessageDialog(this,"can't authenticate user",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }
}
