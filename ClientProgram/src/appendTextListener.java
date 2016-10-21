/**
 * Created by milcho on 10/12/16.
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class appendTextListener {
    /*
     * This should receive and display messages
     */
    private static  IntegerProperty appendText = new SimpleIntegerProperty(0);
    //returns the stringProperty object
    public IntegerProperty appendTextProperty(){
        return appendText;
    }
    //Return the firstname value
    public int getAppendText() {
        return appendText.get();
    }
    //sets the firstname value
    @SuppressWarnings("static-access")
    public void setAppendText(int change) {
        this.appendText.set(change);
    }

}

