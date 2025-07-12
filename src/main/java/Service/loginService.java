/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import DAO.UserDAO;
import DAO.loginDAO;
import Model.User;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author LENOVO Ideapad 3
 */
public class loginService {
    private loginDAO loginDAO;
    private UserDAO userDAO;

    public loginService() {
        loginDAO = new loginDAO();
        userDAO = new UserDAO();
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
    
    public User loginWithFacebook(String facebookId) {
        try {
            return userDAO.findByFacebookId(facebookId);
        } catch (SQLException ex) {
            Logger.getLogger(loginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public User loginWithGoogle(String googleId) {
        try {
            return userDAO.findByGoogleId(googleId);
        } catch (SQLException ex) {
            Logger.getLogger(loginService.class.getName())
                  .log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
