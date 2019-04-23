package magazijnrobot;

public class OrderLine {
    private int orderLineId;
    private Article article;
    private int quantity;


    public OrderLine (int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public Article getArticle (){
        return this.article;
    }

    public int getQuantity () {
        return this.quantity;
    }
}
