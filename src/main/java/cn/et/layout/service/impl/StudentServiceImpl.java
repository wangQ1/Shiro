package cn.et.layout.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.et.layout.entity.Student;
import cn.et.layout.entity.StudentExample;
import cn.et.layout.mapper.StudentMapper;
import cn.et.layout.service.StudentService;
import cn.et.layout.utils.PageTools;
@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	StudentMapper sm;
	public PageTools queryStudent(String sname,Integer page,Integer rows){
		if(sname == null){
			sname = "";
		}
		StudentExample se = new StudentExample();
		se.createCriteria().andSnameLike("%" + sname + "%");
		int total = queryStudentCount(se);//查询总数
		PageTools pt = new PageTools(page, total, rows);
		RowBounds rb = new RowBounds(pt.getStartIndex()-1, rows);
		List<Student> studentList = sm.selectByExampleWithRowbounds(se, rb);
		pt.setRows(studentList);
		return pt;
	}
	
	public int queryStudentCount(StudentExample se){
		int total = (int)sm.countByExample(se);
		return total;
	}
	
	public void deleteStudent(String sid){
		String array[] = sid.split(",");
		for (int i = 0; i < array.length; i++) {
			sm.deleteByPrimaryKey(Integer.parseInt(array[i]));
		}
	}
	
	public void updateStudent(Student stu){
		sm.updateByPrimaryKey(stu);
	}
	
	public void saveStudent(Student student){
		sm.insertSelective(student);
	}
	
}

