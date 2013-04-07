package sk.shifty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: shifty
 * Date: 16/05/12
 * Time: 22:04
 */
public class Limit {

    private Page page;
    private Auth auth;
    private double limit=20d;
    private double reserve=0.1d; //limit free reserve

    public Limit(Auth auth) throws IOException {
        page = new Page("https://is.mendelu.cz/auth/wifi/statistika_vpm.pl");
        this.auth=auth;
    }

    public void load() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy");
        Date today = new Date();
        Date weekAgo = new Date(today.getTime() - 1000*60*60*24*7);

        page.withAuth(auth)
             .withPost("od="+sdf.format(weekAgo), "do",
                       "jednotky=GiB", "desetiny=2", "omez_cas", "nastav_jedn")
             .connect().load();
        page.disconnect();
    }

    public double[] getTransferred() throws IOException {
        load();
        return findLimit();
    }

    public double getFree() throws IOException {
        return limit-getTransferred()[2]-reserve;
    }

    private double[] findLimit() {
        String pattern =
                "<tr class=\"zahlavi\">" +
                "<td class=\"odsazena\" align=\"left\"><small>Total</small></td>" +
                "<td class=\"odsazena\" align=\"left\"><small></small></td>" +
                "<td class=\"odsazena\" align=\"left\"><small></small></td>" +
                "<td class=\"odsazena\" align=\"right\" nowrap=\"nowrap\"><small>(.*)</small></td>" +
                "<td class=\"odsazena\" align=\"right\" nowrap=\"nowrap\"><small>(.*)</small></td>" +
                "<td class=\"odsazena\" align=\"right\" nowrap=\"nowrap\"><small>(.*)</small></td>" +
                "<td class=\"odsazena\" align=\"left\"><small></small></td>" +
                "<td class=\"odsazena\" align=\"left\"><small></small></td>" +
                "<td class=\"odsazena\" align=\"left\"><small></small></td>" +
                "</tr>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(page.toString());

        if(m.find()) {
            return new double[]{Double.parseDouble(m.group(1).substring(0,5)),Double.parseDouble(m.group(2).substring(0,5)),Double.parseDouble(m.group(3).substring(0,5))};
        }
        return null;
    }
}
