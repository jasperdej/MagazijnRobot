import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BPP {

    private Order order;
    private ArrayList<Article> list;
    private ArrayList<Bin> bins;
    private double binCapacity = 1.5;
    private int binMaxItems = 10;

    //sets local variables.
    public BPP(Order order) {
        Bin.setBinCount(0);
        this.order = order;
        list = order.getArticles();
        bins = new ArrayList<>();
        packItems();
    }


    private void packItems() {
        //sorts list with articles based on weight, heaviest will be first
        Collections.sort(list, new Comparator<Article>() {
            @Override
            public int compare(Article a, Article b) {
                return a.getSumWeight().compareTo(b.getSumWeight());
            }
        });
        Collections.reverse(list);

        for (Article l : list) {
            //sorts list with bins based on weight, heaviest will be first
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
                //sets available weight to 100%
                double tempCapacity = binCapacity;
                //sets the amount of free spaces left in the bin
                int freeSpace = binMaxItems - b.getArticles().size();
                if (freeSpace > 1) {
                    //sets reserved weight for low weight items which can fill up the bin to maxItems
                    double reservedWeight = 0;
                    //for every free space the program finds, will add a low weight item to reservedWeight
                    //starting with the item with lowest weight, and then moving up
                    for (int i = 1; i < freeSpace; i++) {
                        //gets the index of the low weight item it has to fit
                        int listIndex = list.size() - i - reservedLast;
                        if(listIndex >= 0){
                            if (list.get(listIndex).getSumWeight() < l.getSumWeight()) {
                                if (reservedWeight + list.get(listIndex).getSumWeight() > 0) {
                                    reservedWeight += list.get(listIndex).getSumWeight();
                                    reservedLast++;
                                }
                            }
                        }
                    }
                    tempCapacity -= reservedWeight;
                }
                //adds the item to a bin
                if (b.getTotalWeight() + l.getSumWeight() <= tempCapacity && b.getArticles().size() < binMaxItems && placed == false) {
                    b.addItem(l);
                    b.addToTotalWeight(l.getSumWeight());
                    placed = true;
                }
            }
            //if no bin can hold the item a new bin will be created
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
