package agentai.recommend.model;

import java.util.List;

public class RecommendationInput {
    private int userId;
    private List<ViewedProduct> viewedProducts;
    private List<OrderedProduct> orderedProducts;
    private List<Product> products;
    // Không ném exception nữa
    
    public RecommendationInput() {
    }

    // (Tuỳ chọn) Constructor tiện lợi với full args
    public RecommendationInput(int userId,
                               List<ViewedProduct> viewedProducts,
                               List<OrderedProduct> orderedProducts,
                               List<Product> products) {
        this.userId = userId;
        this.viewedProducts = viewedProducts;
        this.orderedProducts = orderedProducts;
        this.products = products;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public List<ViewedProduct> getViewedProducts() { return viewedProducts; }
    public void setViewedProducts(List<ViewedProduct> viewedProducts) { this.viewedProducts = viewedProducts; }
    public List<OrderedProduct> getOrderedProducts() { return orderedProducts; }
    public void setOrderedProducts(List<OrderedProduct> orderedProducts) { this.orderedProducts = orderedProducts; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> Products) { this.products = Products; }
    
}




