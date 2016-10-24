package app.rbzeta.postaq.helper;

/**
 * Created by Robyn on 28/09/2016.
 */

public class ValidationHelper {

    public static boolean isValidEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
