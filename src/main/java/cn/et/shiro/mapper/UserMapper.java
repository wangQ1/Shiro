package cn.et.shiro.mapper;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Select;

import cn.et.shiro.entity.Menu;
import cn.et.shiro.entity.UserInfo;

public interface UserMapper{
	//需要匹配数据库中的字段 所以要取别名
	@Select("select user_name userName, pass_word passWord from userinfo where user_name = #{0}")
	public UserInfo queryUserByName(String userName);
	
	@Select("select r.role_name from userinfo u "
			+ "inner join user_role_relation urr on u.user_id = urr.user_id "
			+ "inner join role r on urr.role_id = r.role_id "
			+ "where user_name = #{0}")
	public Set<String> querRoleByName(String userName);
	
	@Select("select p.perm_tag from userinfo u "
			+ "inner join user_role_relation urr on u.user_id = urr.user_id "
			+ "inner join role r on urr.role_id = r.role_id "
			+ "inner join role_perms_relation rpr on r.role_id = rpr.role_id "
			+ "inner join perms p on rpr.perm_id = p.perm_id "
			+ "where user_name = #{0}")
	public Set<String> queryPermsByName(String userName);
	
	@Select("select menu_url menuUrl, menu_name menuName, menu_filter menuFilter, is_menu isMenu "
			+ "from menu")
	public List<Menu> queryMenu();
	
	@Select("select menu_url menuUrl, menu_name menuName, menu_filter menuFilter, is_menu isMenu "
			+ "from menu where menu_url = #{0}")
	public List<Menu> queryMenuByUrl(String url);
	
	@Select("select menu_url menuUrl, menu_name menuName, menu_filter menuFilter, is_menu isMenu "
			+ "from userinfo u inner join user_menu_relation umr on u.user_id = umr.user_id "
			+ "inner join menu m on umr.menu_id = m.menu_id "
			+ "where u.user_name = #{0};")
	public List<Menu> queryMenuByUser(String userName);
}
