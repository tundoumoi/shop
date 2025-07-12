/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Util.DatabaseConnection;
import Model.product;
import Model.productVariant;
import Model.productImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class productDAO {

    private static final Logger LOGGER = Logger.getLogger(productDAO.class.getName());
    //delete
    private static final String DELETE_PRODUCT_SQL = "UPDATE products SET status = 0 WHERE id = ?";
    //update
    final String UPDATE_PRODUCT_SQL = "UPDATE products SET name = ?, description = ?, price = ?, category = ?, status = ? WHERE id = ?";
    final String UPDATE_VARIANT_SQL = "UPDATE product_variants SET size = ?, quantity = ? WHERE id = ?";
    final String UPDATE_IMAGE_SQL = "UPDATE product_images SET image_url = ?, is_primary = ? WHERE id = ?";
    //insert
    final String INSERT_PRODUCT_SQL = "INSERT INTO products (name, description, price, category, status, created_at) VALUES (?, ?, ?, ?, ?, GETDATE())";
    final String INSERT_VARIANT_SQL = "INSERT INTO product_variants (product_id, size, quantity) VALUES (?, ?, ?)";
    final String INSERT_IMAGE_SQL = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
    //select for admin
    final String productQuery = "SELECT * FROM products ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    final String variantQuery = "SELECT * FROM product_variants WHERE product_id = ?";
    final String imageQuery = "SELECT * FROM product_images WHERE product_id = ?";
    
    final String SELECT_PRODUCT_SQL_BY_ID = "SELECT * FROM products WHERE id = ?";
    final String SELECT_VARIANTS_SQL_BY_ID = "SELECT * FROM product_variants WHERE product_id = ?";
    final String SELECT_IMAGES_SQL_BY_ID = "SELECT * FROM product_images WHERE product_id = ?";
    //select for user
    final String SELECT_PRODUCTS_SQL = "SELECT * FROM products WHERE status = 1 ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    final String SELECT_VARIANTS_SQL = "SELECT * FROM product_variants WHERE product_id = ?";
    final String SELECT_IMAGES_SQL = "SELECT * FROM product_images WHERE product_id = ?";

    
    public List<product> getAllProducts(int offset, int limit) {
        List<product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement productStmt = conn.prepareStatement(productQuery)) {

            productStmt.setInt(1, offset);
            productStmt.setInt(2, limit);

            try (ResultSet rs = productStmt.executeQuery()) {
                while (rs.next()) {
                    product product = new product();
                    int productId = rs.getInt("id");

                    product.setId(productId);
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getBoolean("status"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));

                    // Lấy biến thể
                    List<productVariant> variants = new ArrayList<>();
                    try (PreparedStatement variantStmt = conn.prepareStatement(variantQuery)) {
                        variantStmt.setInt(1, productId);
                        try (ResultSet vrs = variantStmt.executeQuery()) {
                            while (vrs.next()) {
                                productVariant variant = new productVariant();
                                variant.setId(vrs.getInt("id"));
                                variant.setProductId(productId);
                                variant.setSize(vrs.getString("size"));
                                variant.setQuantity(vrs.getInt("quantity"));
                                variants.add(variant);
                            }
                        }
                    }

                    // Lấy hình ảnh
                    List<productImage> images = new ArrayList<>();
                    try (PreparedStatement imageStmt = conn.prepareStatement(imageQuery)) {
                        imageStmt.setInt(1, productId);
                        try (ResultSet irs = imageStmt.executeQuery()) {
                            while (irs.next()) {
                                productImage image = new productImage();
                                image.setId(irs.getInt("id"));
                                image.setProductId(productId);
                                image.setImageUrl(irs.getString("image_url"));
                                image.setIsPrimary(irs.getBoolean("is_primary"));
                                images.add(image);
                            }
                        }
                    }

                    product.setVariants(variants);
                    product.setImages(images);

                    products.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    
    public List<product> selectAllActiveProducts(int offset, int limit) {
        List<product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_PRODUCTS_SQL)) {

            ps.setInt(1, offset);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    product product = new product();
                    int productId = rs.getInt("id");

                    product.setId(productId);
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getBoolean("status"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));

                    // Lấy biến thể
                    List<productVariant> variants = new ArrayList<>();
                    try (PreparedStatement varStmt = conn.prepareStatement(SELECT_VARIANTS_SQL)) {
                        varStmt.setInt(1, productId);
                        try (ResultSet vrs = varStmt.executeQuery()) {
                            while (vrs.next()) {
                                productVariant variant = new productVariant();
                                variant.setId(vrs.getInt("id"));
                                variant.setProductId(productId);
                                variant.setSize(vrs.getString("size"));
                                variant.setQuantity(vrs.getInt("quantity"));
                                variants.add(variant);
                            }
                        }
                    }

                    // Lấy hình ảnh
                    List<productImage> images = new ArrayList<>();
                    try (PreparedStatement imgStmt = conn.prepareStatement(SELECT_IMAGES_SQL)) {
                        imgStmt.setInt(1, productId);
                        try (ResultSet irs = imgStmt.executeQuery()) {
                            while (irs.next()) {
                                productImage image = new productImage();
                                image.setId(irs.getInt("id"));
                                image.setProductId(productId);
                                image.setImageUrl(irs.getString("image_url"));
                                image.setIsPrimary(irs.getBoolean("is_primary"));
                                images.add(image);
                            }
                        }
                    }

                    product.setVariants(variants);
                    product.setImages(images);

                    products.add(product);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách sản phẩm (status = 1) có phân trang", e);
        }

        return products;
    }

    public product selectProductById(int id) {

        product product = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_PRODUCT_SQL_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setCategory(rs.getString("category"));
                    product.setStatus(rs.getBoolean("status"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));

                    // Biến thể
                    List<productVariant> variants = new ArrayList<>();
                    try (PreparedStatement variantStmt = conn.prepareStatement(SELECT_VARIANTS_SQL_BY_ID)) {
                        variantStmt.setInt(1, id);
                        try (ResultSet vrs = variantStmt.executeQuery()) {
                            while (vrs.next()) {
                                productVariant variant = new productVariant();
                                variant.setId(vrs.getInt("id"));
                                variant.setProductId(id);
                                variant.setSize(vrs.getString("size"));
                                variant.setQuantity(vrs.getInt("quantity"));
                                variants.add(variant);
                            }
                        }
                    }

                    // Hình ảnh
                    List<productImage> images = new ArrayList<>();
                    try (PreparedStatement imageStmt = conn.prepareStatement(SELECT_IMAGES_SQL_BY_ID)) {
                        imageStmt.setInt(1, id);
                        try (ResultSet irs = imageStmt.executeQuery()) {
                            while (irs.next()) {
                                productImage image = new productImage();
                                image.setId(irs.getInt("id"));
                                image.setProductId(id);
                                image.setImageUrl(irs.getString("image_url"));
                                image.setIsPrimary(irs.getBoolean("is_primary"));
                                images.add(image);
                            }
                        }
                    }

                    product.setVariants(variants);
                    product.setImages(images);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy sản phẩm theo ID: " + id, e);
        }

        return product;
    }

    
    public boolean deleteProduct(int id) throws SQLException {
        boolean rowDeleted = false;
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(DELETE_PRODUCT_SQL)) {
                    ps.setInt(1, id);
                    int rowsDeletedCount = ps.executeUpdate();
                    rowDeleted = rowsDeletedCount > 0;
                    if (rowDeleted) {
                        LOGGER.log(Level.INFO, "Product deleted (status=0) successfully with ID: {0}", id);
                    } else {
                        LOGGER.log(Level.INFO, "No Product found with ID: {0}", id);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting Product (soft delete)", e);
            throw e;
        }
        return rowDeleted;
    }
    
    public boolean updateProduct(product product) throws SQLException {

        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // transaction start

            // Update product
            try (PreparedStatement ps = conn.prepareStatement(UPDATE_PRODUCT_SQL)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setString(4, product.getCategory());
                ps.setBoolean(5, product.isStatus());
                ps.setInt(6, product.getId());
                ps.executeUpdate();
            }

            // Update or Insert variants
            for (productVariant variant : product.getVariants()) {
                if (variant.getId() > 0) {
                    try (PreparedStatement ps = conn.prepareStatement(UPDATE_VARIANT_SQL)) {
                        ps.setString(1, variant.getSize());
                        ps.setInt(2, variant.getQuantity());
                        ps.setInt(3, variant.getId());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(INSERT_VARIANT_SQL)) {
                        ps.setInt(1, product.getId());
                        ps.setString(2, variant.getSize());
                        ps.setInt(3, variant.getQuantity());
                        ps.executeUpdate();
                    }
                }
            }

            // Update or Insert images
            for (productImage image : product.getImages()) {
                if (image.getId() > 0) {
                    try (PreparedStatement ps = conn.prepareStatement(UPDATE_IMAGE_SQL)) {
                        ps.setString(1, image.getImageUrl());
                        ps.setBoolean(2, image.isIsPrimary());
                        ps.setInt(3, image.getId());
                        ps.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps = conn.prepareStatement(INSERT_IMAGE_SQL)) {
                        ps.setInt(1, product.getId());
                        ps.setString(2, image.getImageUrl());
                        ps.setBoolean(3, image.isIsPrimary());
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit();
            success = true;
            LOGGER.log(Level.INFO, "Cập nhật sản phẩm (kèm biến thể + ảnh) thành công: ID = ", product.getId());

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật sản phẩm", e);
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        return success;
    }


    public boolean insertProduct(product product) throws SQLException {

        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // bắt đầu transaction

            // Insert sản phẩm
            int generatedId = -1;
            try (PreparedStatement ps = conn.prepareStatement(INSERT_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.setString(4, product.getCategory());
                ps.setBoolean(5, product.isStatus());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                        product.setId(generatedId); // cập nhật lại ID cho object Product
                    } else {
                        throw new SQLException("Không lấy được ID vừa insert từ bảng products.");
                    }
                }
            }

            // Insert variants
            try (PreparedStatement ps = conn.prepareStatement(INSERT_VARIANT_SQL)) {
                for (productVariant variant : product.getVariants()) {
                    ps.setInt(1, generatedId);
                    ps.setString(2, variant.getSize());
                    ps.setInt(3, variant.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // Insert images
            try (PreparedStatement ps = conn.prepareStatement(INSERT_IMAGE_SQL)) {
                for (productImage image : product.getImages()) {
                    ps.setInt(1, generatedId);
                    ps.setString(2, image.getImageUrl());
                    ps.setBoolean(3, image.isIsPrimary());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            success = true;
            LOGGER.log(Level.INFO, "Inserted new product with ID: {0}", generatedId);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.log(Level.SEVERE, "Rollback failed", rollbackEx);
                }
            }
            LOGGER.log(Level.SEVERE, "Error inserting product", e);
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }

        return success;
    }
    
    public int countAllActiveProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public static void main(String[] args) {
        productDAO dao = new productDAO();
        product p = dao.selectProductById(8);

        if (p != null) {
            System.out.println("Tên sản phẩm: " + p.getName());
            for (productVariant v : p.getVariants()) {
                System.out.println(" - Size: " + v.getSize() + ", SL: " + v.getQuantity());
            }
            for (productImage img : p.getImages()) {
                System.out.println(" - Ảnh: " + img.getImageUrl() + ", Chính: " + img.isIsPrimary());
            }
        } else {
            System.out.println("Không tìm thấy sản phẩm.");
        }
    }
    public productVariant getVariantById(int id) {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM product_variants WHERE id = ?")) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            productVariant v = new productVariant();
            v.setId(rs.getInt("id"));
            v.setProductId(rs.getInt("product_id"));
            v.setSize(rs.getString("size"));
            v.setQuantity(rs.getInt("quantity"));
            return v;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
public void updateVariantQuantity(int variantId, int newQuantity) {
    String sql = "UPDATE product_variants SET quantity = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, newQuantity);
        ps.setInt(2, variantId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
public List<productVariant> getVariantsByProductId(int productId) {
    List<productVariant> variants = new ArrayList<>();
    String sql = "SELECT * FROM product_variants WHERE product_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            productVariant variant = new productVariant();
            variant.setId(rs.getInt("id"));
            variant.setProductId(rs.getInt("product_id"));
            variant.setSize(rs.getString("size"));
            variant.setQuantity(rs.getInt("quantity"));
            variants.add(variant);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return variants;
}
public List<product> searchProductsByName(String keyword, int offset, int limit) {
    List<product> list = new ArrayList<>();
    String sql = "SELECT * FROM products WHERE status = 1 AND name LIKE ? ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ps.setInt(2, offset);
        ps.setInt(3, limit);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            product p = new product();
            int productId = rs.getInt("id");
            p.setId(productId);
            p.setName(rs.getString("name"));
            p.setDescription(rs.getString("description"));
            p.setPrice(rs.getBigDecimal("price"));
            p.setCategory(rs.getString("category"));
            p.setStatus(rs.getBoolean("status"));
            p.setCreatedAt(rs.getTimestamp("created_at"));

            // Hình ảnh
            List<productImage> images = new ArrayList<>();
            try (PreparedStatement imgStmt = conn.prepareStatement("SELECT * FROM product_images WHERE product_id = ?")) {
                imgStmt.setInt(1, productId);
                ResultSet imgRs = imgStmt.executeQuery();
                while (imgRs.next()) {
                    productImage img = new productImage();
                    img.setId(imgRs.getInt("id"));
                    img.setProductId(productId);
                    img.setImageUrl(imgRs.getString("image_url"));
                    img.setIsPrimary(imgRs.getBoolean("is_primary"));
                    images.add(img);
                }
            }
            p.setImages(images);

            // Biến thể
            List<productVariant> variants = new ArrayList<>();
            try (PreparedStatement varStmt = conn.prepareStatement("SELECT * FROM product_variants WHERE product_id = ?")) {
                varStmt.setInt(1, productId);
                ResultSet varRs = varStmt.executeQuery();
                while (varRs.next()) {
                    productVariant v = new productVariant();
                    v.setId(varRs.getInt("id"));
                    v.setProductId(productId);
                    v.setSize(varRs.getString("size"));
                    v.setQuantity(varRs.getInt("quantity"));
                    variants.add(v);
                }
            }
            p.setVariants(variants);

            list.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
public int countSearchResults(String keyword) {
    String sql = "SELECT COUNT(*) FROM products WHERE status = 1 AND name LIKE ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}


}
