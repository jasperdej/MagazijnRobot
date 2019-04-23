package magazijnrobot;

import java.util.ArrayList;

public class Bin {

    private int binNumber;
    private Integer totalWeight;
    private ArrayList<Integer> items;

    public Bin(int binNumber) {
        this.binNumber = binNumber;
        totalWeight = 0;
        items = new ArrayList<>();
    }

    public int getBinNumber() {
        return binNumber;
    }

    public void printBin() {
        for (Integer i : items) {
            System.out.println("- " + i);
        }
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }

    public void addToTotalWeight(int weight) {
        totalWeight += weight;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void addItem(Integer weight) {
        items.add(weight);
    }
}
