package com.lw.jpa.helloword;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Test {

	public static void main(String[] args) {
		//1������EntityManagerFactory
		String persistenceUnitName = "jpa-sample01";
		EntityManagerFactory entityManagerFactory = 
				Persistence.createEntityManagerFactory(persistenceUnitName ); 
		//2������EntityManager
		EntityManager manager = entityManagerFactory.createEntityManager();
		//3����������
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		//4�����г־û�����
		Customer customer = new Customer(); 
		customer.setAge(12);
		customer.setEmail("1819270694@qq.com");
		customer.setLastName("aa");
		customer.setCreatedTime(new Date());
		customer.setBirth(new Date());
		// ����
		manager.persist(customer);
		//5���ύ����
		transaction.commit();
		//6���ر�����
		manager.close();
		//7���ر�EntityManager
		entityManagerFactory.close();
	}
}
