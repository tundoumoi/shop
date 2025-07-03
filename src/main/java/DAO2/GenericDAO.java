/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO2;

import java.util.List;

/**
 * Interface chung định nghĩa các phương thức CRUD cho mọi entity.
 * @param <T>  Kiểu entity
 * @param <K>  Kiểu khóa chính (primary key)
 */
public interface GenericDAO<T, K> {
    /** Lưu mới entity vào DB
     * @param entity */
    void create(T entity);

    /** Tìm entity theo khóa chính
     * @param id
     * @return  */
    T findById(K id);

    /** Lấy tất cả entity
     * @return  */
    List<T> findAll();

    /** Cập nhật entity
     * @param entity
     * @return  */
    T update(T entity);

    /** Xóa entity khỏi DB
     * @param entity */
    void delete(T entity);
}