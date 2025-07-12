/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentai.recommend.model;

/**
 *
 * @author LENOVO Ideapad 3
 */
public class Recommendation {
    private int userId;
    private int productId;
    private float score;
    private String algorithm;

    public Recommendation() {
    }

    public Recommendation(int userId, int productId, float score, String algorithm) {
        this.userId = userId;
        this.productId = productId;
        this.score = score;
        this.algorithm = algorithm;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public float getScore() { return score; }
    public void setScore(float score) { this.score = score; }
    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
}