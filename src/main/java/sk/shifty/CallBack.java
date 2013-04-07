package sk.shifty;

import java.util.Map;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 1:01 PM
 */
public interface CallBack {
    public void setProperties(Map<String,Object> map);
    public void execute();
}
