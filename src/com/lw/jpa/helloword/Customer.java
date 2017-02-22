package com.lw.jpa.helloword;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Table(name="JPA_CUSTOMERS") // 不写默认为Customer
@Entity
public class Customer {

	private Integer id; 
	private String lastName; 
	private String email;
	private int age ;
	
	private Date createdTime; 
	private Date birth ;
	
	// 最好需要加在get方法里面
	@Column(name="ID") // 主键
	@GeneratedValue(strategy=GenerationType.AUTO) //生成主键的策略
//	@TableGenerator(name="ID_GENERATOR",
//					table="jpa_id_generators",
//					pkColumnName="PK_NAME",
//					pkColumnValue="CUSTOMER_ID",
//					valueColumnName="PK_VALUE",
//					allocationSize=100) // 每次添加100个
//	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="LAST_NAME",length=50) // 还可以对数据列的长度限制
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	// 默认添加了@Base注解,添加了一样
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Temporal(TemporalType.TIMESTAMP) // 限制时间类型的格式
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Temporal(TemporalType.DATE) // 限制时间类型的格式
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	//工具方法，不需要映射为数据表的一列
	//没有set方法，运行会报错
	@Transient
	public String getInfo(){
		return "lastName:" + lastName + ";email:" + email ;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + ", createdTime="
				+ createdTime + ", birth=" + birth + "]";
	}
	
}
