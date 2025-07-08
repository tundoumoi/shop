<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List Products (Admin)</title>
</head>
<body>
    <center>
        <h1>Product Management</h1>
        <h2>Product List</h2>
        <div align="center">
            <a href="products?action=create">Add New Product</a>
            <br/><br/>
            <table border="1" cellpadding="5" cellspacing="0">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Price</th>
                        <th>Category</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productList}">
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td>${product.description}</td>
                            <td>${product.price}</td>
                            <td>${product.category}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.status}">Active</c:when>
                                    <c:otherwise>Inactive</c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${product.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            <td>
                                <a href="products?action=edit&id=${product.id}">Edit</a> |
                                <a href="products?action=delete&id=${product.id}" onclick="return confirm('Are you sure you want to delete this product?');">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <!-- Pagination Controls -->
            <br/>
            <c:if test="${noOfPages > 1}">
                <div align="center">
                    <ul style="list-style:none; padding:0;">
                        <c:if test="${currentPage > 1}">
                            <li style="display:inline; margin:0 5px;">
                                <a href="products?action=list&page=${currentPage-1}">« Prev</a>
                            </li>
                        </c:if>
                        <c:forEach var="i" begin="1" end="${noOfPages}">
                            <li style="display:inline; margin:0 5px;">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <strong>${i}</strong>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="products?action=list&page=${i}">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                        </c:forEach>
                        <c:if test="${currentPage < noOfPages}">
                            <li style="display:inline; margin:0 5px;">
                                <a href="products?action=list&page=${currentPage+1}">Next »</a>
                            </li>
                        </c:if>
                    </ul>
                </div>
            </c:if>
        </div>
    </center>
</body>
</html>
