package cn.et.shiro.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.et.shiro.entity.Menu;
import cn.et.shiro.mapper.UserMapper;

@Component // 元注解 把普通pojo实例化到spring容器中
public class MyFilter extends AuthorizationFilter {
	static Map<String, String> urls = new HashMap<String, String>();
	@SuppressWarnings("unused")
	@Autowired
	private ShiroFilterFactoryBean sffb;

	/**
	 * 匹配指定过滤器规则的url
	 * 
	 * @param regex
	 * @param url
	 * @return
	 */
	public static boolean matchUrl(String regex, String url) {
		regex = regex.replaceAll("/+", "/");
		if (regex.equals(url)) {
			return true;
		}
		regex = regex.replaceAll("\\.", "\\\\.");
		// /login.html  /l*.html
		regex = regex.replaceAll("\\*", ".*");
		// /**/login.html /a/b/login.html
		if (regex.indexOf("/.*.*/") >= 0) {
			regex = regex.replaceAll("/\\.\\*\\.\\*/", "((/.*/)+|/)");
		}
		System.out.println(regex + "----" + url);
		return Pattern.matches(regex, url);
	}

	@Autowired
	UserMapper um;

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(matchUrl("/**/*.html", "/t/g/login.html"));
	}

	/**
	 * isAccessAllowed用于判断当前url的请求是否能验证通过 如果验证失败
	 * 调用父类的onAccessDenied决定跳转到登录失败页还是授权失败页面
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		// 获取用户当前访问的资源路径 (/a.jsp | /a.png | ...)
		String contextPath = req.getContextPath();
		String url = req.getRequestURI();
		url = url.split(contextPath)[1];
		// List<Menu> menu = um.queryMenuByUrl(url);获取url需要的权限信息
		List<Menu> menu = um.queryMenu(); // 获取菜单信息
		// 当数据库中没有配置当前url的权限信息时 直接打回请求
		if (menu.size() == 0) {
			return false;
		}
		String urlAuth = null;
		for (Menu m : menu) {
			// 判断菜单中是否有与请求中的url匹配的url
			if (matchUrl(m.getMenuUrl().trim(), url)) {
				// 如果有， 获取当前url的过滤器
				urlAuth = m.getMenuFilter();
			}
		}
		if(urlAuth == null){
			return true;
		}
		// 如果配置的过滤器是anon 直接放过
		if (urlAuth.startsWith("anon")) {
			return true;
		}
		// 如果配置的是authc 判断当前用户是否认证通过
		Subject subject = getSubject(request, response);
		if (urlAuth.startsWith("authc")) {
			return subject.isAuthenticated();
		}
		// 授权认证 也需要判断是否登录 没有登录就返回登录页面 登录了继续下面的验证
		boolean ifAuthc = subject.isAuthenticated();
		if (!ifAuthc)
			return ifAuthc;
		// 如果是定义的roles过滤器 获取所有的roles 一般是roles[a,b]
		if (urlAuth.startsWith("roles")) {
			String[] rolesArray = urlAuth.split("roles\\[")[1].split("\\]")[0].split(",");
			if (rolesArray == null || rolesArray.length == 0) {
				return true;
			}
			Set<String> roles = CollectionUtils.asSet(rolesArray);
			// 当前用户的roles与url的roles比较
			return subject.hasAllRoles(roles);
		}
		// 如果定义的时perms 获取所有的perms 一般是perms[user:query:*,user:update:1]
		if (urlAuth.startsWith("perms")) {
			String[] perms = urlAuth.split("perms\\[")[1].split("\\]")[0].split(",");
			boolean isPermitted = true;
			if (perms != null && perms.length > 0) {
				if (perms.length == 1) {
					// 如果没有这个权限
					if (!subject.isPermitted(perms[0])) {
						isPermitted = false;
					}
				} else {
					// 如果没有所有权限
					if (!subject.isPermittedAll(perms)) {
						isPermitted = false;
					}
				}
			}
			return isPermitted;
		}
		return false;
	}
}
