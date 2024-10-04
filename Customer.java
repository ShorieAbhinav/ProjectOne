// Abhinav Shorie
// AXS240124

public class Customer {
    private String firstName;
    private String lastName;
    private String guestID;
    private float amountSpent;

    // Overloaded constructor
    public Customer(String firstName, String lastName, String guestID, float amountSpent){
        this.firstName = firstName;
        this.lastName = lastName;
        this.guestID = guestID;
        this.amountSpent = amountSpent;
    }

    // Accessors and Mutators
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGuestID() {
        return guestID;
    }

    public void setGuestID(String guestId) {
        this.guestID = guestId;
    }

    public float getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(float amountSpent) {
        this.amountSpent = amountSpent;
    }

}
