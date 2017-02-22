package com.lw.jpa.test;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lw.jpa.helloword.Customer;

public class JPATest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction transaction;
	
	@Before
	public void init(){
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-sample01");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}
	
	@After
	public void destroy(){
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	@Test
	public void testFlush(){
		Customer customer = entityManager.find(Customer.class, 2) ;
		System.out.println(customer);
		customer.setLastName("AAA");
		entityManager.flush(); //调用update的sql语句，否则是在commit时才调用，但是此时数据库依然没有改变
	}
	
	//若传入的是一个游离对象, 即传入的对象有 OID. 
	//1. 若在 EntityManager 缓存中有对应的对象
	//2. JPA 会把游离对象的属性复制到查询到EntityManager 缓存中的对象中.
	//3. EntityManager 缓存中的对象执行 UPDATE. 
	@Test
	public void testMerge4(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ccc@qq.com");
		customer.setLastName("ccc");
		
		customer.setId(4);
		
		// 和3类似，这里手工查询了一下，而3是使用meger查询
		// 这里先查询下，再到缓存中修改
		Customer customer2 = entityManager.find(Customer.class, 4); // 会有一个复制操作
		
		entityManager.merge(customer); // 这里相当于entityManager和两个id相同的对象关联
		
		System.out.println(customer == customer2); //false
	}
	
	// 若传入的是一个游离对象, 即传入的对象有 OID
	//1. 若在 EntityManager 缓存中没有该对象
	//2. 若在数据库中也有对应的记录 				*******
	//3. JPA 会查询对应的记录, 然后返回该记录对一个的对象, 再然后会把游离对象的属性复制到查询到的对象中.
	//4. 对查询到的对象执行 update 操作. 
	@Test
	public void testMerge3(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ee@163.com");
		customer.setLastName("EE");
		
		customer.setId(4);
		Customer customer2 = entityManager.merge(customer);
		
		System.out.println(customer == customer2);
	}
	
	
	
	// 若传入的是一个游离的对象，即传入的对象有Id	   *******
	//1、在EntityManager缓存中没有该对象
	//2、若在数据库中也咩有对应记录
	//3、jpa会创建一个新的对象，然后把当前游离对象的属性复制到新创建的对象中
	//4、对新创建的对象执行insert操作
	@Test
	public void testMerge2(){
		Customer customer = new Customer() ;
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		
		customer.setId(100);// 注意：这个id并不会存放到数据库中
		
		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer:" + customer);
		System.out.println("customer2:" + customer2);
		
	}
	
	
	// 若传入的是一个临时对象，会创建一个新的对象，把临时对象的属性复制到新的对象中，然后进行持久化操作，
	// 所以新对象和之前的对象不是同一个对象。
	@Test
	public void testMerge1(){
		Customer customer = new Customer();
		customer.setAge(33);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("cc@qq.com");
		customer.setLastName("cc");
		
		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer:" + customer.getId());
		System.out.println("customer2:" + customer2.getId());
	}
	
	
	
	
	
	
	
	// 相当于session的delete方法
	// 但是jpa的remove只能移除持久化对象，而session的delete还可以移除游离状态对象
	@Test
	public void testRemove(){
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	
	// 相当于session的save方法
	// 注意：jpa的persist方法保存的对象不能设置id，否则报错
	@Test
	public void testPersistence(){
		Customer customer = new Customer() ;
		customer.setAge(11);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("bb@qq.com");
		customer.setLastName("bb");
		entityManager.persist(customer);
		System.out.println(customer.getId());
	}
	
	// 相当于session的load方法
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 1) ;
//		System.out.println(customer.getClass().getName());
		System.out.println("-------------------");
		System.out.println(customer); // 只有在用到customer的时候才会从数据库中查询
	}
	
	// 相当于 session的get方法
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1); // 马上查询,书写sql语句
		System.out.println("--------------");
		System.out.println(customer);
	}
}
