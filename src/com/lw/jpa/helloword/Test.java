package com.lw.jpa.helloword;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Test {

	public static void main(String[] args) {
		//1、创建EntityManagerFactory
		String persistenceUnitName = "jpa-sample01";
		EntityManagerFactory entityManagerFactory = 
				Persistence.createEntityManagerFactory(persistenceUnitName ); 
		//2、创建EntityManager
		EntityManager manager = entityManagerFactory.createEntityManager();
		//3、开启事务
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		//4、进行持久化操作
		Customer customer = new Customer(); 
		customer.setAge(12);
		customer.setEmail("1819270694@qq.com");
		customer.setLastName("aa");
		customer.setCreatedTime(new Date());
		customer.setBirth(new Date());
		// 保存
		manager.persist(customer);
		//5、提交事务
		transaction.commit();
		//6、关闭事务
		manager.close();
		//7、关闭EntityManager
		entityManagerFactory.close();
	}
}
