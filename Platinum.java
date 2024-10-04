// Abhinav Shorie
// AXS240124

public class Platinum extends Customer{
    private int bonusBucks;

    public Platinum(String firstName, String lastName, String guestID, float amountSpent, int bonusBucks){
        super(firstName, lastName, guestID, amountSpent);
        this.bonusBucks = bonusBucks;
    }

    // Accessors and Mutators
    public int getBonusBucks() {
        return bonusBucks;
    }

    public void setBonusBucks(int bonusBucks) {
        this.bonusBucks = bonusBucks;
    }

    public float applyBonusBucks(float orderTotal){
        if(bonusBucks >= orderTotal){
            bonusBucks -= Math.ceil(orderTotal);
            return 0;
        }
        else {
            orderTotal -= bonusBucks;
            bonusBucks = 0;
            return orderTotal;
        }
    }

    public void calculateBonusBucks(float newSpent){
        if (super.getAmountSpent() > 200){
            int bucksEarned = (int)(newSpent / 5);
            bonusBucks += bucksEarned;
        }
    }
}
