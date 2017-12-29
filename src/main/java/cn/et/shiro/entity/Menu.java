package cn.et.shiro.entity;

public class Menu {
	private int menuId;
	private String menuName;
	private String menuUrl;
	private String menuFilter;
	private int isMenu;
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuFilter() {
		return menuFilter;
	}
	public void setMenuFilter(String menuFilter) {
		this.menuFilter = menuFilter;
	}
	public int getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(int isMenu) {
		this.isMenu = isMenu;
	}
}
