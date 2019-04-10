package chrgames.boardgame.models;

class Player {

    /**
     * Player's cash.
     */
    private int amount;

    /**
     * Player's income for every turn.
     */
    private int income;

    /**
     * Contains main information about amount and income of some player.
     * @param startAmount - start value of amount of money.
     * @param startIncome - start value of income.
     */
    Player(int startAmount, int startIncome) {
        this.amount = startAmount;
        this.income = startIncome;
    }

    void setIncome(int newIncome) {
        if (newIncome >= 0) {
            income = newIncome;
        }
    }

    void setAmount(int newAmount) {
        if (newAmount >= 0) {
            amount = newAmount;
        }
    }

    int getAmount() {
        return amount;
    }

    int getIncome() {
        return income;
    }


    /**
     * Calls when player makes move.<br>
     * This methods describes meaning of 'income' field. -
     * after each turn players rewarded with some money (income).
     */
    void round() {
        amount += income;
    }

    /**
     * Reward player for watching ad video with some money.
     * @param award - amount of rewarded money
     */
    void reward(int award) {
        amount += award;
    }
}
