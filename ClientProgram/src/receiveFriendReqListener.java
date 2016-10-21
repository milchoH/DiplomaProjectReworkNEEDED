/**
 * Created by milcho on 10/12/16.
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class receiveFriendReqListener {

    private static  IntegerProperty recFrRqu = new SimpleIntegerProperty();
    //returns the stringProperty object
    public IntegerProperty recFrRquProperty(){
        return recFrRqu;
    }
    //Return the firstname value
    public int getRecFrRqu() {
        return recFrRqu.get();
    }
    //sets the firstname value
    @SuppressWarnings("static-access")
    public void setRecFrRqu(int change) {
        this.recFrRqu.set(change);
    }

}