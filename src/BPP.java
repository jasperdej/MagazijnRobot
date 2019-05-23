import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BPP {

    private Order order;
    private ArrayList<Article> list;
    private ArrayList<Bin> bins;
    private double binCapacity = 1.5;
    private int binMaxItems = 10;

    public BPP(Order order) {
        Bin.setBinCount(0);
        this.order = order;
        list = order.getArticles();
        bins = new ArrayList<>();
        packItems();
    }

    private void packItems() {
        Collections.sort(list, new Comparator<Article>() {
            @Override
            public int compare(Article a, Article b) {
                return a.getSumWeight().compareTo(b.getSumWeight());
            }
        });
        Collections.reverse(list);

        for (Article l : list) {
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
                double tempCapacity = binCapacity; //set to all free
                int freeSpace = binMaxItems - b.getArticles().size(); //gets amount of free space
                if (freeSpace > 1) { //checks if free space is at least 2
                    double reservedWeight = 0; //sets reserved weight for extra stuff at 0
                    for (int i = 1; i < freeSpace; i++) { //adds to reserved weight for every free space he finds -1
                        int listIndex = list.size() - i - reservedLast; //finds the weight it has to substract
                        if(listIndex >= 0){ //makes sure the item it's gonna compare with isn't below 0
                            if (list.get(listIndex).getSumWeight() < l.getSumWeight()) { //checks if the item just found is still below the item in the list
                                if (reservedWeight + list.get(listIndex).getSumWeight() > 0) { //checks if the last item even fits in the bin
                                    reservedWeight += list.get(listIndex).getSumWeight(); //adds reserved item to reservedWeight
                                    reservedLast++; //adds 1 to reserved last index
                                }
                            }
                        }
                    }
                    tempCapacity -= reservedWeight;
                }
                if (b.getTotalWeight() + l.getSumWeight() <= tempCapacity && b.getArticles().size() < binMaxItems && placed == false) {
                    b.addItem(l);
                    b.addToTotalWeight(l.getSumWeight());
                    placed = true;
                }
            }
            if (placed == false) {
                bins.add(new Bin(order));
                bins.get(bins.size() - 1).addItem(l);
                bins.get(bins.size() - 1).addToTotalWeight(l.getSumWeight());
            }
        }
    }

    public ArrayList<Article> getArticleList() {
        ArrayList<Article> articles = new ArrayList<>();
        for (Bin b : bins) {
            for (Article a : b.getArticles()) {
                articles.add(a);
            }
        }
        return articles;
    }

    public ArrayList<Bin> getBinList() {
        return this.bins;
    }

    public void printPackedItems() {
        for (Bin b : bins) {
            System.out.println("Bin " + b.getBinNumber());
            b.printBin();
        }
    }

    public void printList() {
        for (Article l : list) {
            l.printWeight();
        }
    }
}