<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
    <center>
        <h1>User Management</h1>
        <h2><a href="users?action=create">Add New User</a></h2>
    </center>
    <div align="center">
        <table border="1" cellpadding="5">
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Full Name</th>
                <th>Role</th>
                <th>Facebook ID</th>
                <th>Google ID</th>
                <th>Address</th>
                <th>Active</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
            <c:forEach var="customer" items="${listUser}">
                <tr>
                    <td><c:out value="${customer.id}"/></td>
                    <td><c:out value="${customer.email}"/></td>
                    <td><c:out value="${customer.fullName}"/></td>
                    <td><c:out value="${customer.role}"/></td>
                    <td><c:out value="${customer.facebookId}"/></td>
                    <td><c:out value="${customer.googleId}"/></td>
                    <td><c:out value="${customer.address}"/></td>
                    <td><c:out value="${customer.status}"/></td>
                    <td><c:out value="${customer.createdAt}"/></td>
                    <td>
                        <a href="users?action=edit&id=${customer.id}">Edit</a>
                        <a href="users?action=delete&id=${customer.id}" onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</body>
</html>
