package magazijnrobot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ModifiedBestFit {

    private ArrayList<Integer> list;
    private ArrayList<Bin> bins;
    private int binCapacity;
    private int binMaxItems;

    public ModifiedBestFit(int binCapacity, int binMaxItems) {
        list = new ArrayList<>();
        bins = new ArrayList<>();
        this.binCapacity = binCapacity;
        this.binMaxItems = binMaxItems;
    }

    public void addItem(int weight) {
        list.add(weight);
    }

    public void packItems() {
        Collections.sort(list, Collections.reverseOrder());
        for (int l : list) {
            Collections.sort(bins, new Comparator<Bin>() {
                @Override
                public int compare(Bin a, Bin b) {
                    return a.getTotalWeight().compareTo(b.getTotalWeight());
                }
            });
            Collections.reverse(bins);
            boolean placed = false;
            int reservedLast = 0;
            for (Bin b : bins) {
                int tempCapacity = binCapacity; //set to all free
                int freeSpace = binMaxItems - b.getItems().size(); //gets amount of free space
                if (freeSpace > 1) { //checks if free space is at least 2
                    int reservedWeight = 0; //sets reserved weight for extra stuff at 0
                    for (int i = 1; i < freeSpace; i++) { //adds to reserved weight for every free space he finds -1
                        int listIndex = list.size() - i - reservedLast; //finds the weight it has to substract
                        if (list.get(listIndex) < l) { //checks if the item just found is still below the item in the list
                            if (reservedWeight + list.get(listIndex) > 0) { //checks if the last item even fits in the bin
                                reservedWeight += list.get(listIndex); //adds reserved item to reservedWeight
                                reservedLast++; //adds 1 to reserved last index
                            }
                        }
                    }
                    tempCapacity -= reservedWeight;
                }
                if (b.getTotalWeight() + l <= tempCapacity && b.getItems().size() < binMaxItems && placed == false) {
                    b.addItem(l);
                    b.addToTotalWeight(l);
                    placed = true;
                }
            }
//
            if (placed == false) {
                bins.add(new Bin(bins.size() + 1));
                bins.get(bins.size() - 1).addItem(l);
                bins.get(bins.size() - 1).addToTotalWeight(l);
            }
        }
    }

    public void printPackedItems() {
        for (Bin b : bins) {
            System.out.println("Bin " + b.getBinNumber());
            b.printBin();
        }
    }
}
