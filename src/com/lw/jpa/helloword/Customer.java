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

@Table(name="JPA_CUSTOMERS") // ��дĬ��ΪCustomer
@Entity
public class Customer {

	private Integer id; 
	private String lastName; 
	private String email;
	private int age ;
	
	private Date createdTime; 
	private Date birth ;
	
	// �����Ҫ����get��������
	@Column(name="ID") // ����
	@GeneratedValue(strategy=GenerationType.AUTO) //���������Ĳ���
//	@TableGenerator(name="ID_GENERATOR",
//					table="jpa_id_generators",
//					pkColumnName="PK_NAME",
//					pkColumnValue="CUSTOMER_ID",
//					valueColumnName="PK_VALUE",
//					allocationSize=100) // ÿ�����100��
//	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="LAST_NAME",length=50) // �����Զ������еĳ�������
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	// Ĭ�������@Baseע��,�����һ��
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
	@Temporal(TemporalType.TIMESTAMP) // ����ʱ�����͵ĸ�ʽ
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@Temporal(TemporalType.DATE) // ����ʱ�����͵ĸ�ʽ
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	//���߷���������Ҫӳ��Ϊ���ݱ��һ��
	//û��set���������лᱨ��
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
