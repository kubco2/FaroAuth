package sk.shifty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 04/05/12
 * Time: 18:57
 */
public class Page {

    final static Logger log = LoggerFactory.getLogger(Page.class);

    private String page;
    private URL url;
    private HttpURLConnection conn;
    private Auth basicAuth=null;

    private boolean key=false;
    private String[] postParams=null;

    public Page(String url) throws MalformedURLException {
        log.debug("creating page with url "+url);
        if(!url.startsWith("http")) {
            url="http://"+url;
        }
        this.url = new URL(url);
    }

    public Page withAuth(Auth auth) {
        basicAuth = auth;
        return this;
    }

    public Page withPost(String ... params) throws UnsupportedEncodingException {
        postParams=params;
        return this;
    }

    public Page connect() throws IOException {

        conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty ("User-agent", "Shifty-Authenticator");
        if(basicAuth != null) {
            conn.setRequestProperty("Authorization",basicAuth.get());
        }
        if(postParams != null) {
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            sendOutput(encodeParams(postParams));
        } else {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html; charset=utf-8");
        }
        return this;
    }

    public Page load() throws IOException {
        page="";
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String thisLine;
        while ((thisLine = br.readLine()) != null) {
            page+=thisLine+"\n";
        }
        return this;
    }

    public void disconnect() {
        conn.disconnect();
    }

    public String findUrl(String keyword) {
        Pattern p = Pattern.compile("[&?/%\\-=.:a-zA-z0-9]*"+keyword+"[&?/%=a-zA-z0-9]*");
        Matcher m = p.matcher(page);
        key=m.find();
        if(key) {
            String a = m.group();
            return a;
        }
        return null;
    }

    public boolean findOnPage(String key) {
        Pattern p = Pattern.compile("\\b"+key+"\\b");
        Matcher m = p.matcher(page);
        return m.find();
    }



    public String encodeParams(String[] params) throws UnsupportedEncodingException {
        String enc="UTF-8";
        String result="";
        for(String param : params) {
            if(param.contains("=")) {
                String[] sequence = param.split("=");
                result += URLEncoder.encode(sequence[0], enc) + "=" + URLEncoder.encode(sequence[1], enc);
            } else {
                result += URLEncoder.encode(param, enc);
            }
            result +="&";
        }
        return result.substring(0,result.length()-1);
    }

    public void sendOutput(String encodedParams) throws IOException {
        conn.setRequestProperty("Content-Length", ""+ encodedParams.length());
        DataOutputStream printout = new DataOutputStream(conn.getOutputStream());
        printout.writeBytes(encodedParams);
        printout.flush();
        printout.close();
    }

    public String toString() {
        return page;
    }
}
