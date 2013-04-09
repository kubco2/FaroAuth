package sk.shifty;

import java.io.IOException;
import java.math.BigDecimal;
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


    private Auth auth;
    private BigDecimal limit=new BigDecimal("20");
    private String uri=Constants.alephLimit;
    private String pageString;

    public Limit(Auth auth) {
        this.auth=auth;
    }

    public void load() throws IOException {
        Page page = new Page(uri);
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy");
        Date today = new Date();
        Date weekAgo = new Date(today.getTime() - 1000*60*60*24*7);
        page.withAuth(auth)
             .withPost("od="+sdf.format(weekAgo), "do",
                       "jednotky=GiB", "desetiny=2", "omez_cas", "nastav_jedn")
             .connect().load();
        page.disconnect();
        pageString=page.toString();
    }

    public BigDecimal getTransferred() throws IOException {
        return findLimit();
    }

    public BigDecimal getFree() throws IOException {
        return limit.subtract(getTransferred());
    }

    private BigDecimal findLimit() throws IOException {
        return findLimit("");
    }

    private BigDecimal findLimit(String type) throws IOException {
        load();
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
        Matcher m = p.matcher(pageString.toString());

        if(m.find()) {
            String upload = m.group(1).substring(0, 5);
            String download = m.group(2).substring(0, 5);
            String both = m.group(3).substring(0, 5);

            switch (type) {
                case "upload": return BigDecimal.valueOf(Double.parseDouble(upload)).setScale(2,BigDecimal.ROUND_HALF_UP);
                case "download": return BigDecimal.valueOf(Double.parseDouble(download)).setScale(2,BigDecimal.ROUND_HALF_UP);
                case "both":
                default: return  BigDecimal.valueOf(Double.parseDouble(both)).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        }
        return null;
    }
}
