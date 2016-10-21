/**
 * Created by milcho on 10/12/16.
 */
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class receiveFriendsListener {

    private static  IntegerProperty receiveFriends = new SimpleIntegerProperty();
    public IntegerProperty receiveFriendsProperty(){
        return receiveFriends;
    }
    public int getReceiveFriends() {
        return receiveFriends.get();
    }
    @SuppressWarnings("static-access")
    public void setReceiveFriends(int change) {
        this.receiveFriends.set(change);
    }

}