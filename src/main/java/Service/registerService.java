/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.registerDAO;
import Model.customer;
import java.sql.SQLException;

/**
 * Service xử lý đăng ký tài khoản từ nhiều nguồn: email, Google, Facebook.
 */
public class registerService {
    private registerDAO dao = new registerDAO();

    /**
     * Đăng ký bằng email/password.
     * @param input Đối tượng customer chứa fullName, email, password, address
     * @return customer đã được lưu vào DB (có id tự sinh)
     * @throws SQLException
     * @throws DuplicateAuthException nếu email đã tồn tại
     */
    public customer registerEmail(customer input) throws SQLException, DuplicateAuthException {
        System.out.println("[registerEmail] Bắt đầu đăng ký bằng email: " + input.getEmail());

        // 1. Kiểm tra email đã tồn tại?
        customer exist = dao.findByEmail(input.getEmail());
        if (exist != null) {
            System.out.println("[registerEmail] Email đã tồn tại: " + input.getEmail());
            throw new DuplicateAuthException("Email " + input.getEmail() + " đã được sử dụng.");
        }

        // 2. Chưa có → thực hiện insert
        dao.insert(input);
        System.out.println("[registerEmail] Insert thành công: " + input.getEmail());

        return input;
    }

    /**
     * Đăng ký hoặc liên kết tài khoản Google.
     */
    public customer registerGoogle(String googleId, String email, String fullName) throws SQLException, DuplicateAuthException {
        System.out.println("[registerGoogle] Đăng ký/đăng nhập với Google ID: " + googleId);
        System.out.println("[registerGoogle] Email: " + email + " | FullName: " + fullName);

        // 1. Nếu đã có Google ID trong hệ thống
        customer c = dao.findByGoogleId(googleId);
        if (c != null) {
            System.out.println("[registerGoogle] Đã tồn tại googleId: " + googleId);
            throw new DuplicateAuthException("Google account already linked.");
        }

        // 2. Nếu đã có email nhưng chưa có Google ID → cập nhật Google ID
        c = dao.findByEmail(email);
        if (c != null) {
            System.out.println("[registerGoogle] Tồn tại email: " + email);
            if (c.getGoogleId() == null || c.getGoogleId().isEmpty()) {
                dao.updateGoogleId(googleId, email);
                c.setGoogleId(googleId);
                System.out.println("[registerGoogle] Đã liên kết Google ID với tài khoản email.");
                return c;
            } else {
                System.out.println("[registerGoogle] Email đã liên kết Google ID khác.");
                throw new DuplicateAuthException("Email already linked with another Google ID.");
            }
        }

        // 3. Chưa tồn tại → đăng ký mới
        c = new customer();
        c.setGoogleId(googleId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword(""); // có thể dùng UUID ngẫu nhiên nếu cần
        dao.insert(c);
        System.out.println("[registerGoogle] Đã tạo tài khoản mới cho Google ID.");

        return c;
    }

    /**
     * Đăng ký hoặc liên kết tài khoản Facebook.
     */
    public customer registerFacebook(String facebookId, String email, String fullName) throws SQLException {
        System.out.println("[registerFacebook] Đăng ký/đăng nhập với Facebook ID: " + facebookId);
        System.out.println("[registerFacebook] Email: " + email + " | FullName: " + fullName);

        customer c = dao.findByFacebookId(facebookId);
        if (c != null) {
            System.out.println("[registerFacebook] Đã tồn tại facebookId.");
            return c;
        }

        c = dao.findByEmail(email);
        if (c != null) {
            System.out.println("[registerFacebook] Tồn tại email: " + email);
            if (c.getFacebookId() == null || c.getFacebookId().isEmpty()) {
                dao.updateFacebookId(facebookId, email);
                c.setFacebookId(facebookId);
                System.out.println("[registerFacebook] Đã liên kết Facebook ID với tài khoản email.");
            }
            return c;
        }

        // Chưa có tài khoản nào → tạo mới
        c = new customer();
        c.setFacebookId(facebookId);
        c.setEmail(email);
        c.setFullName(fullName);
        c.setPassword("");  // không cần mật khẩu
        dao.insert(c);
        System.out.println("[registerFacebook] Đã tạo tài khoản mới cho Facebook ID.");

        return c;
    }

    public customer findByEmail(String email) throws SQLException {
        return dao.findByEmail(email);
    }

    /**
     * Ngoại lệ báo trùng thông tin khi đăng ký.
     */
    public class DuplicateAuthException extends Exception {
        public DuplicateAuthException(String message) {
            super(message);
        }
    }
}


//    public void createNewUser(customer user) {
//        // Insert user và sinh user_code
//        int generatedId = dao.insertUser(user);
//        String code = String.format("%04d", generatedId);
//        dao.updateUserCode(generatedId, code);
//        user.setId(generatedId);
//        user.setId(code);
//    }
//
//    public void updateSocialId(customer user) {
//        dao.updateSocialId(user.getId(), user.getFacebookId(), user.getGoogleId());
//    }
