<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
    <center>
        <h1>User Management</h1>
        <h2><a href="users?action=users">List All Users</a></h2>
    </center>
    <div align="center">
        <form method="post" action="users">
            <input type="hidden" name="action" value="create" />
            <table border="1" cellpadding="5">
                <caption><h2>Add New User</h2></caption>
                <tr>
                    <th>Full Name:</th>
                    <td><input type="text" name="fullName" id="fullName" size="45" /></td>
                </tr>
                <tr>
                    <th>Email:</th>
                    <td><input type="email" name="email" id="email" size="45" /></td>
                </tr>
                <tr>
                    <th>Password:</th>
                    <td><input type="password" name="password" id="password" size="45" /></td>
                </tr>
                <tr>
                    <th>Role:</th>
                    <td><input type="text" name="role" id="role" size="20" value="user" /></td>
                </tr>
                <tr>
                    <th>Facebook ID:</th>
                    <td><input type="text" name="facebookId" id="facebookId" size="45" /></td>
                </tr>
                <tr>
                    <th>Google ID:</th>
                    <td><input type="text" name="googleId" id="googleId" size="45" /></td>
                </tr>
                <tr>
                    <th>Address:</th>
                    <td><input type="text" name="address" id="address" size="60" /></td>
                </tr>
                <tr>
                    <th>Active:</th>
                    <td><input type="checkbox" name="status" id="status" value="1" checked /></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Save" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>