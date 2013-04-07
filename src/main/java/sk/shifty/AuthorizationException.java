package sk.shifty;

/**
 * User: shifty
 * Date: 4/6/13
 * Time: 1:05 PM
 */
public class AuthorizationException extends Exception {

    public AuthorizationException(){
        super();
    }
    public AuthorizationException(String str){
        super(str);
    }
    public AuthorizationException(Throwable thr){
        super(thr);
    }
    public AuthorizationException(String str,Throwable thr){
        super(str,thr);
    }
}
