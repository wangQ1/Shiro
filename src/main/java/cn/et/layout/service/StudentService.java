package cn.et.layout.service;

import cn.et.layout.entity.Student;
import cn.et.layout.utils.PageTools;

public interface StudentService {
	public PageTools queryStudent(String sname,Integer page,Integer rows);
	public void deleteStudent(String sid);
	public void updateStudent(Student stu);
	public void saveStudent(Student student);
}