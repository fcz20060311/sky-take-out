package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

//    后面进入的值需要根据EmployeeDTO对象的属性来进行传入
    @Select("insert into employee (id,name,username,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user  ) values (#{id},#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

//    这个的快捷键是alt+shift+enter
    Page<Employee> pagequery(EmployeePageQueryDTO employeePageQueryDTO);
}
