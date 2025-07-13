/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.productDAO;
import Model.product;
import Model.productImage;
import Model.productVariant;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for business logic on Product operations, delegates to ProductDAO.
 */
public class ProductService {
    private productDAO dao;

    public ProductService() {
        this.dao = new productDAO();
    }

    /**
     * Thêm mới sản phẩm kèm variants và images
     */
    public void addProduct(product product) throws SQLException {
        // Validate basic fields
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (product.getPrice() == null || product.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Product price must be non-negative");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required");
        }

        // Validate variants
        List<productVariant> variants = product.getVariants();
        if (variants != null) {
            for (productVariant v : variants) {
                if (v.getSize() == null || v.getSize().trim().isEmpty()) {
                    throw new IllegalArgumentException("Variant size is required");
                }
                if (v.getQuantity() < 0) {
                    throw new IllegalArgumentException("Variant quantity must be non-negative");
                }
            }
        }

        // Validate images
        List<productImage> images = product.getImages();
        if (images != null) {
            long primaryCount = images.stream().filter(productImage::isIsPrimary).count();
            if (primaryCount != 1) {
                throw new IllegalArgumentException("Exactly one primary image is required");
            }
            for (productImage img : images) {
                if (img.getImageUrl() == null || img.getImageUrl().trim().isEmpty()) {
                    throw new IllegalArgumentException("Image URL is required");
                }
            }
        }

        // Delegate to DAO
        boolean success = dao.insertProduct(product);
        if (!success) {
            throw new SQLException("Failed to insert product");
        }
    }
// Lấy danh sách sản phẩm theo category có phân trang
public List<product> getProductsByCategory(String category, int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    return new productDAO().getProductsByCategory(category, offset, pageSize);
}

// Tính tổng số trang dựa trên số lượng sản phẩm trong category
public int getTotalPagesByCategory(String category, int pageSize) {
    int totalProducts = new productDAO().countProductsByCategory(category);
    return (int) Math.ceil((double) totalProducts / pageSize);
}

    /**
     * Cập nhật sản phẩm kèm variants và images
     */
    public void updateProduct(product product) throws SQLException {
        if (product.getId() <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        // Same validation as add
        addProduct(product); // reuse validation

        // Delegate to DAO update
        boolean success = dao.updateProduct(product);
        if (!success) {
            throw new SQLException("Failed to update product with ID " + product.getId());
        }
    }

    /**
     * Xóa mềm sản phẩm
     */
    public void deleteProduct(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        dao.deleteProduct(id);
    }

    /**
     * Lấy chi tiết sản phẩm
     */
    public product getProductById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }
        return dao.selectProductById(id);
    }

    /**
     * Lấy danh sách phân trang
     */
    public List<product> getProductsByPage(int page, int pageSize) throws SQLException {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be >= 1");
        }
        int offset = (page - 1) * pageSize;
        return dao.getAllProducts(offset, pageSize);
    }

    /**
     * Tính tổng số trang
     */
    public int getTotalPages(int pageSize) throws SQLException {
        if (pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be >= 1");
        }
        int total = dao.countAllActiveProducts();
        return (int) Math.ceil((double) total / pageSize);
    }
 public productVariant getVariantById(int id) {
    productDAO dao = new productDAO();
    return dao.getVariantById(id);
}
 public void updateVariantQuantity(int variantId, int newQuantity) {
    dao.updateVariantQuantity(variantId, newQuantity);
}
 public List<productVariant> getVariantsByProductId(int productId) {
    productDAO dao = new productDAO();
    return dao.getVariantsByProductId(productId);
}

public List<product> getProductsByCategoryAndDescription(String category, String description, int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    return dao.getProductsByCategoryAndDescription(category, description, offset, pageSize);
}

public int getTotalPagesByCategoryAndDescription(String category, String description, int pageSize) {
    int total = dao.countProductsByCategoryAndDescription(category, description);
    return (int) Math.ceil((double) total / pageSize);
}

}

