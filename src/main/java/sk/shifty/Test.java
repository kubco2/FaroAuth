package sk.shifty;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shifty
 * Date: 10/12/12
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class Test  {

    public static void main(String[] args) throws IOException {

        PingService ps = new PingService();
        System.out.println(ps.isReachable());

    }
}
