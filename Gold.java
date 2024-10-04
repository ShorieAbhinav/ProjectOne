// Abhinav Shorie
// AXS240124

public class Gold extends Customer {
    private double discountPercentage;

    // Overloaded constructor
    public Gold(String firstName, String lastName, String guestID, float amountSpent, double discountPercentage){
        super(firstName, lastName, guestID, amountSpent);
        this.discountPercentage = discountPercentage;
    }

    // Accessors and Mutators
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public float  applyDiscount(float orderTotal){
        orderTotal = orderTotal - (float)(orderTotal * discountPercentage / 100);
        return orderTotal;
    }
    
}
