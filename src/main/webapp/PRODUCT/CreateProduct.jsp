<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add New Product</title>
    <script>
        // Đếm số image để đặt giá trị cho radio
        let imageCounter = 0;

        function addVariantRow(size = '', quantity = '') {
            const table = document.getElementById('variantTable');
            const row = table.insertRow();
            row.innerHTML = `
                <td><input type="text" name="variantSize" value="${size}" required/></td>
                <td><input type="number" name="variantQuantity" value="${quantity}" required/></td>
                <td><button type="button" onclick="removeRow(this)">Remove</button></td>
            `;
        }

        function addImageRow(url = '', isPrimary = false) {
            const table = document.getElementById('imageTable');
            // Header row không tính, nên cols = table.rows.length - 1
            const idx = table.rows.length - 1;
            const row = table.insertRow();
            row.innerHTML = `
                <td><input type="text" name="imageUrl" value="${url}" required/></td>
                <td>
                  <input type="radio"
                         name="primaryImage"
                         value="${idx}"
                         ${isPrimary ? 'checked' : ''}
                         required/>
                </td>
                <td><button type="button" onclick="removeImageRow(this)">Remove</button></td>
            `;
        }

        function removeImageRow(btn) {
            // Xoá hàng
            const row = btn.closest('tr');
            row.remove();
            // Cập nhật lại value radio cho khớp thứ tự
            const radios = document.querySelectorAll('#imageTable input[type="radio"]');
            radios.forEach((r, i) => r.value = i);
        }

        window.onload = function() {
            // Khởi tạo sẵn 1 row variant và 1 row image
            addVariantRow();
            addImageRow();
        };
    </script>
</head>
<body>
    <center>
        <h1>Product Management</h1>
        <h2>Add New Product</h2>
        <form method="post" action="products">
            <input type="hidden" name="action" value="create"/>

            <table border="1" cellpadding="5">
                <tr>
                    <th>Name:</th>
                    <td><input type="text" name="name" size="100" required/></td>
                </tr>
                <tr>
                    <th>Description:</th>
                    <td><textarea name="description" rows="4" cols="100"></textarea></td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td><input type="number" name="price" step="0.01" required/></td>
                </tr>
                <tr>
                    <th>Category:</th>
                    <td><input type="text" name="category" required/></td>
                </tr>
                <tr>
                    <th>Status:</th>
                    <td>
                        <select name="status">
                            <option value="1">Active</option>
                            <option value="0">Inactive</option>
                        </select>
                    </td>
                </tr>
            </table>

            <h3>Variants</h3>
            <table id="variantTable" border="1" cellpadding="5">
                <tr><th>Size</th><th>Quantity</th><th>Action</th></tr>
            </table>
            <button type="button" onclick="addVariantRow()">Add Variant</button>

            <h3>Images</h3>
            <p><small>Chọn 1 radio để đánh dấu ảnh chính</small></p>
            <table id="imageTable" border="1" cellpadding="5">
                <tr><th>Image URL</th><th>Primary</th><th>Action</th></tr>
            </table>
            <button type="button" onclick="addImageRow()">Add Image</button>

            <br/><br/>
            <input type="submit" value="Save"/>
        </form>
    </center>
</body>
</html>
