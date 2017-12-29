package cn.et.layout.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.et.layout.entity.Emp;
import cn.et.layout.entity.Result;
import cn.et.layout.entity.TreeNode;
import cn.et.layout.service.DeptService;
import cn.et.layout.utils.PageTools;
@Controller
public class DeptController {
	@Autowired
	DeptService ds;
	@ResponseBody
	@RequestMapping("/qd")
	//@RequestParam(required=false)加上这个可以解决500这个错误，这个代表不需要加?sname=xxx也可以运行
	public List<TreeNode> queryDept(Integer id){
		return ds.queryDept(id);
	}
	@ResponseBody
	@RequestMapping("/qe")
	public PageTools queryEmp(Integer id, String ename, Integer page,Integer rows){
		return ds.queryEmpByDept(id, ename, page, rows);
	}
	@ResponseBody
	@RequestMapping(value="/delEmp/{empid}",method=RequestMethod.DELETE)
	public Result deleteEmp(@PathVariable String empid){
		Result re = new Result();
		re.setCode(1);
		try {
			ds.deleteEmp(empid);
		} catch (Exception e) {
			re.setCode(0);
			re.setMessage("删除失败");
		}
		return re;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/updEmp/{id}",method=RequestMethod.PUT)
	public Result updateEmp(@PathVariable Integer id,Emp emp){
		emp.setId(id);
		Result re = new Result();
		re.setCode(1);
		try {
			ds.updateEmp(emp);
		} catch (Exception e) {
			re.setCode(0);
			re.setMessage("修改失败");
		}
		return re;
		
	}
	
	
	@ResponseBody
	@RequestMapping(value="/saveEmp",method=RequestMethod.POST)
	public Result saveSutdent(Emp emp){
		Result re = new Result();
		re.setCode(1);
		try {
			ds.saveEmp(emp);
		} catch (Exception e) {
			re.setCode(0);
			re.setMessage("添加失败");
		}
		return re;
	}
}
