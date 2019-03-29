package chrgames.boardgame.models;

public class Player {

    private int amount;

    private int income;

    Player(int startAmount, int startIncome) {
        this.amount = startAmount;
        this.income = startIncome;
    }

    public int getAmount() {
        return amount;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int newIncome) {
        if (newIncome >= 0) {
            income = newIncome;
        }
    }

    public void setAmount(int newAmount) {
        if (newAmount >= 0) {
            amount = newAmount;
        }
    }

    public void round() {
        amount += income;
    }

    public void reward(int award) {
        amount += award;
    }
}
