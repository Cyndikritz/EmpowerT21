package Puzzle;

public class puzzelCheck {
    private String PuzzelName;
    private boolean twoByTwo;
    private boolean threeByThree;
    private boolean fourByFour;

    public puzzelCheck() {

    }

    public puzzelCheck(String puzzelName, boolean twoByTwo, boolean threeByThree, boolean fourByFour) {
        PuzzelName = puzzelName;
        this.twoByTwo = twoByTwo;
        this.threeByThree = threeByThree;
        this.fourByFour = fourByFour;
    }

    public String getPuzzelName() {
        return PuzzelName;
    }

    public void setPuzzelName(String puzzelName) {
        PuzzelName = puzzelName;
    }

    public boolean getTwoByTwo() {
        return twoByTwo;
    }

    public void setTwoByTwo(boolean twoByTwo) {
        this.twoByTwo = twoByTwo;
    }

    public boolean getThreeByThree() {
        return threeByThree;
    }

    public void setThreeByThree(boolean threeByThree) {
        this.threeByThree = threeByThree;
    }

    public boolean getFourByFour() {
        return fourByFour;
    }

    public void setFourByFour(boolean fourByFour) {
        this.fourByFour = fourByFour;
    }
}
