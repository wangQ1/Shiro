package cn.et.shiro.conf;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.et.shiro.entity.UserInfo;
import cn.et.shiro.mapper.UserMapper;
@Component//元注解   把普通pojo实例化到spring容器中
public class MyRealm extends AuthorizingRealm{
	@Autowired
	UserMapper um;
	/**
	 * 认证   执行SecurityUtils.getSubject().login()方法(即:登录验证)的时候调用该方法，并把登陆输入的用户名和密码传过来和数据库中的用户名和密码对比 是否相等
	 * 返回值 null表示认证失败  非null认证通过
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken tonken) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken)tonken;
		UserInfo userByName = um.queryUserByName(tonken.getPrincipal().toString());
		if(userByName != null && new String(upt.getPassword()).equals(userByName.getPassWord())){
			System.out.println(upt.getPassword());
			System.out.println(userByName.getUserName());
			SimpleAccount sa = new SimpleAccount(upt.getUsername(), upt.getPassword(), "myRealm");
			return sa;
		}
		return null;
	}
	/**
	 * 调用SecurityUtils.getSubject().isPermitted()方法检查权限时会调用，并将用户信息传过来，利用参数获取当前用户的权限数据
	 * 将当前用户在数据库的角色和权限 加载到 AuthorizationInfo 并返回
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		System.out.println("获取权限信息");
		String userName = pc.getPrimaryPrincipal().toString();
		Set<String> roleList = um.querRoleByName(userName);//获取角色集合
		Set<String> permsList = um.queryPermsByName(userName);//获取权限集合
		//角色和权限的集合
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();  
        authorizationInfo.setRoles(roleList);
        authorizationInfo.setStringPermissions(permsList);  
        return authorizationInfo;
	}
}
