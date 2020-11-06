package com.even.lc.service;

import com.even.lc.dao.AdminRoleDAO;
import com.even.lc.dao.UserDao;
import com.even.lc.dto.UserDTO;
import com.even.lc.entity.AdminRole;
import com.even.lc.entity.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminUserRoleService adminUserRoleService;

    /**
     * 判断用户是否存在ByName
     * @param username
     * @return
     */
    public boolean isExit(String username){
        User user=userDao.findByUsername(username);
        return null!=user;

    }

    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    public User findByUserName(String username) {
       return userDao.findByUsername(username);
    }

    /**
     * 通过username，password获取用户
     * @param username
     * @param password
     * @return
     */
    public User get(String username,String password){
        return userDao.getByUsernameAndPassword(username,password);
    }


    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public int register(User user){

        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        // 转义
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        password = HtmlUtils.htmlEscape(password);
        user.setPassword(password);

        user.setEnabled(true);

        if (username.equals("")||password.equals("")){
            return 0;
        }

        boolean exist = isExit(username);
        if (exist){
            return 2;
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times=2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        user.setSalt(salt);
        // 加盐密码
        user.setPassword(encodedPassword);
        userDao.save(user);
        return 1;

    }

    public List<UserDTO> list() {
        List<User> users = userDao.findAll();
        List<UserDTO> userDTOS = users
                .stream().map(user -> (UserDTO) new UserDTO().convertForm(user)).collect(Collectors.toList());
        userDTOS.forEach(u -> {
            List<AdminRole> roles = adminRoleService.listRolesByUser(u.getUsername());
            u.setRoles(roles);
        });
        return userDTOS;
    }

    public void updateUserStatus(User user){
        User userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setEnabled(user.isEnabled());
        userDao.save(userInDB);
    }

    /**
     * password:123
     * @param user
     * @return
     */
    public User resetPassword(User user){
        User userInDB = userDao.findByUsername(user.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5","123",salt,times).toString();
        userInDB.setPassword(encodedPassword);
        return userInDB;
    }

    public void editUser(User user){
        User userInDB = userDao.findByUsername(user.getUsername());
        userInDB.setName(user.getName());
        userInDB.setEmail(user.getEmail());
        userInDB.setPhone(user.getPhone());
        userDao.save(userInDB);
        adminUserRoleService.saveRoleChange(userInDB.getId(), user.getRoles());
    }
    public void deleteById(int id){
        userDao.deleteById(id);
    }
}
