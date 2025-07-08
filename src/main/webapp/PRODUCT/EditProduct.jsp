<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Product</title>
    <script>
        function addVariantRow(size = '', quantity = '') {
            const table = document.getElementById('variantTable');
            const row = table.insertRow();
            row.innerHTML = `
                <td><input type="text" name="variantSize" value="${size}" required /></td>
                <td><input type="number" name="variantQuantity" value="${quantity}" required /></td>
                <td><button type="button" onclick="removeRow(this)">Remove</button></td>`;
        }
        function addImageRow(imageUrl = '', isPrimary = false) {
            const table = document.getElementById('imageTable');
            const row = table.insertRow();
            row.innerHTML = `
                <td><input type="text" name="imageUrl" value="${imageUrl}" required /></td>
                <td><input type="checkbox" name="isPrimary" ${isPrimary ? 'checked' : ''} /></td>
                <td><button type="button" onclick="removeRow(this)">Remove</button></td>`;
        }
        function removeRow(btn) {
            const row = btn.closest('tr');
            row.remove();
        }
    </script>
</head>
<body>
    <center>
        <h1>Product Management</h1>
        <h2>Edit Product</h2>
        <div align="center">
            <form method="post" action="products?id=${product.id}">
                <input type="hidden" name="action" value="update" />
                <table border="1" cellpadding="5">
                    <tr><th>Name:</th><td><input type="text" name="name" value="${product.name}" size="100" required /></td></tr>
                    <tr><th>Description:</th><td><textarea name="description" rows="4" cols="100">${product.description}</textarea></td></tr>
                    <tr><th>Price:</th><td><input type="number" name="price" value="${product.price}" step="0.01" required /></td></tr>
                    <tr><th>Category:</th><td><input type="text" name="category" value="${product.category}" required /></td></tr>
                    <tr><th>Status:</th><td><select name="status"><option value="1" ${product.status ? 'selected' : ''}>Active</option><option value="0" ${!product.status ? 'selected' : ''}>Inactive</option></select></td></tr>
                </table>
                <h3>Variants</h3>
                <table id="variantTable" border="1" cellpadding="5">
                  <tr><th>Size</th><th>Quantity</th><th>Action</th></tr>
                  <!-- render sẵn variants từ server -->
                  <c:forEach var="v" items="${product.variants}">
                    <tr>
                      <td><input type="text" name="variantSize" value="${v.size}" required/></td>
                      <td><input type="number" name="variantQuantity" value="${v.quantity}" required/></td>
                      <td><button type="button" onclick="removeRow(this)">Remove</button></td>
                    </tr>
                  </c:forEach>
                  <!-- nếu chưa có dòng nào, render 1 dòng trống -->
                  <c:if test="${empty product.variants}">
                    <tr>
                      <td><input type="text" name="variantSize" required/></td>
                      <td><input type="number" name="variantQuantity" required/></td>
                      <td><button type="button" onclick="removeRow(this)">Remove</button></td>
                    </tr>
                  </c:if>
                </table>
                <button type="button" onclick="addVariantRow()">Add Variant</button>

                <h3>Images</h3>
                <table id="imageTable" border="1" cellpadding="5">
                  <tr><th>Image URL</th><th>Primary</th><th>Action</th></tr>
                  <!-- render sẵn images từ server -->
                  <c:forEach var="img" items="${product.images}">
                    <tr>
                      <td><input type="text" name="imageUrl" value="${img.imageUrl}" required/></td>
                      <td><input type="checkbox" name="isPrimary" ${img.isPrimary ? 'checked' : ''}/></td>
                      <td><button type="button" onclick="removeRow(this)">Remove</button></td>
                    </tr>
                  </c:forEach>
                  <c:if test="${empty product.images}">
                    <tr>
                      <td><input type="text" name="imageUrl" required/></td>
                      <td><input type="checkbox" name="isPrimary"/></td>
                      <td><button type="button" onclick="removeRow(this)">Remove</button></td>
                    </tr>
                  </c:if>
                </table>
                <button type="button" onclick="addImageRow()">Add Image</button>

                <br/><br/>
                <input type="submit" value="Update"/>
            </form>
        </center>
    </body>
</html>