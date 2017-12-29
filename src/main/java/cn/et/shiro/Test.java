package cn.et.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

@SuppressWarnings("deprecation")
public class Test {
	public static void main(String[] args) {
		//从配置文件中读取用户的权限信息  
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");  
        org.apache.shiro.mgt.SecurityManager securityManager = (org.apache.shiro.mgt.SecurityManager)factory.getInstance();  
        SecurityUtils.setSecurityManager(securityManager);  
        //获取登录用户信息  
        Subject currentUser = SecurityUtils.getSubject();
        //当前用户的会话
        Session session = currentUser.getSession();  
        session.setAttribute( "保存数据的键", "保存数据的值" );  
        /**
         * 用户包括两部分   
         *    principals, credentials  
         *     principals（本人）表示用户的标识信息 比如用户名 用户地址等  
         *     credentials（凭证）表示用户用于登录的凭证 比如密码 证书等  
         */  
        //isAuthenticated判断是否登录过
        if ( !currentUser.isAuthenticated() ) {  
            //令牌(用户名和密码) 其实就是credentials  
            UsernamePasswordToken token = new UsernamePasswordToken("jiaozi", "123456");  
            token.setRememberMe(true);  
            //开始登录操作  操作后 isAuthenticated就是true  
            try{
	            currentUser.login(token);  
	            System.out.println("登录成功");  
	            //判断是否拥有某个角色  
	            if(currentUser.hasRole("role1")){  
	                System.out.println("拥有角色role1");  
	            }
	            //判断是否拥有某种权限
	            if(currentUser.isPermitted("user:query:1")){  
	                System.out.println("拥有查询1号用户权限");  
	            }
            } catch ( UnknownAccountException uae ) {
			    System.out.println("账号不存在");
			} catch ( IncorrectCredentialsException ice ) {
				 System.out.println("密码错误");
			} catch ( LockedAccountException lae ) {
				 System.out.println("账号被锁定");
			} catch ( AuthenticationException ae ) {
				 System.out.println("验证异常");
			}
        }  
        currentUser.logout();  
	}
}
