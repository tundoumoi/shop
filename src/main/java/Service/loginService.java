/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import DAO.loginDAO;
import Model.User;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class loginService {
    private loginDAO loginDAO;

    public loginService() {
        loginDAO = new loginDAO();
    }

    /**
     * Kiểm tra đăng nhập
     * @param email email người dùng nhập vào
     * @param password mật khẩu người dùng nhập vào
     * @return true nếu đăng nhập thành công, false nếu thất bại
     */
    public boolean login(String email, String password) {
        return loginDAO.checkLogin(email, password);
    }

    /**
     * Lấy thông tin user sau khi đăng nhập thành công (tùy chọn)
     * @param email
     * @return User object nếu tìm thấy, null nếu không tìm thấy
     */
    public User getUserInfo(String email) {
        return loginDAO.getUserByEmail(email);
    }
}
