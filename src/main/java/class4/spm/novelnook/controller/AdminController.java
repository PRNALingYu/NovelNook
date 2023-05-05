package class4.spm.novelnook.controller;

import class4.spm.novelnook.mapper.AdminMapper;
import class4.spm.novelnook.pojo.Staff;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.*;
import tool.R;

import java.io.IOError;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.Collection;
import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminMapper adminMapper;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }


    @GetMapping("/staff")
    public R<List<Staff>> getStaff(){//展示列表界面
        List<Staff> list = adminMapper.ShowStaff();
        return R.success(list);

    }
//    @GetMapping("/staff/{username}")
//    public List<Staff> getStaffByUsername(@PathVariable("username") String username){//列表界面查找
//        System.out.println(username);
//        List<Staff> list=adminMapper.getStaffByUserName(username);
//        if (list.isEmpty()) {
//            list.add(new Staff("can't", "find", "this", "User", " ", " "," "));
//        }
//        return list;
//    }

    @GetMapping("/staff/{username}")
//    @PathVariable是用来接收请求路径中的参数值
    public R<List<Staff>>  getStaffByUsername(@PathVariable("username") String username){//列表界面查找
        System.out.println(username);
        List<Staff> list=adminMapper.getStaffByUserName(username);
//        R<Staff> r = (R<Staff>) list;
        if (list.isEmpty()) {
            return R.error("No such person");
        }
        return R.success(list);
//        r.success(list);
//        return r.success(list);
    }

    @DeleteMapping("/staff/{username}")
    public R<List<Staff>> deleteStaffByUsername(@PathVariable("username") String username){//列表界面删除
        System.out.println(username);
        List<Staff> l  = adminMapper.getStaffByUserName(username);
//        System.out.println(list);
        if (l == null) {
            return R.error("Delete fail");
        }
        else {
            adminMapper.deleteStaffByUserName(username);
            return R.success();
        }
    }
    
    /**
     * update function
     * @param staff param got from json data in RequestBody
     * @return
     */
    @PutMapping("/staff")
    public R update(@RequestBody Staff staff) {
        //flag is used to judge whether operation is success
        int flag = adminMapper.updateByUserName(staff);

        if(flag > 0) {
            return R.success(null);
        }

        return R.error("update fail");

    }

    //测试用的登录界面在/resources/static/admin/log.html,访问url：localhost:8080/admin/login
//    @RequestMapping("/login")
////    @RequestParam通过界面输入获取参数值
//    //登录界面：输入正确的用户名和密码后跳转至其他界面，输入错误后返回登录界面
//    public void login(HttpServletResponse response,@RequestParam("staffname") String staffname,@RequestParam("password") String password) throws IOException {
//        if("admin".equals(staffname) && "123".equals(password)) {//测试用
//            response.sendRedirect("/admin/staff");
//        }
//        else {
//            response.sendRedirect("/admin/log.html");
//        }
//    }


    @PostMapping("/login")
    public R<Staff> login(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password) {
        // 验证用户名和密码是否匹配
        Staff staff = (Staff) adminMapper.getStaffByUserName(username);
        List list = adminMapper.getStaffByUserName(username);
        String Password = staff.getPassword();
        if (list != null && Password.equals(password)) {
            // 登录成功，设置cookie等
            //response.addCookie(new Cookie("username", staff.getUsername()));
            return R.success(staff);
        } else {
            // 登录失败，返回错误信息和状态码
            return R.error("用户名或密码错误");
        }
    }



//头像设置：String在本地数据库存储，使用时可以尝试response.sendRedirect()方法
    //前端的话，貌似是<img src="/users/1/avatar">（？没了解过。。）
    @GetMapping("/staff/{username}/avatar")
    public R show_avatars(@PathVariable("username") String username){//用户列表展示界面显示用户头像，通过将用户头像储存在文件系统中实现
        Staff staff =(Staff) adminMapper.getStaffByUserName(username);
        List list = adminMapper.getStaffByUserName(username);
        if(list.isEmpty()){
            return R.error("User not found");
        }
        String avatar = staff.getAvatar();
        if (avatar== null){
            return R.error("Avatar not found");
        }
        else{
            return R.success(avatar);
        }
    }


}
