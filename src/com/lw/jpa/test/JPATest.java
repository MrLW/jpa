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
		entityManager.flush(); //����update��sql��䣬��������commitʱ�ŵ��ã����Ǵ�ʱ���ݿ���Ȼû�иı�
	}
	
	//���������һ���������, ������Ķ����� OID. 
	//1. ���� EntityManager �������ж�Ӧ�Ķ���
	//2. JPA ��������������Ը��Ƶ���ѯ��EntityManager �����еĶ�����.
	//3. EntityManager �����еĶ���ִ�� UPDATE. 
	@Test
	public void testMerge4(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("ccc@qq.com");
		customer.setLastName("ccc");
		
		customer.setId(4);
		
		// ��3���ƣ������ֹ���ѯ��һ�£���3��ʹ��meger��ѯ
		// �����Ȳ�ѯ�£��ٵ��������޸�
		Customer customer2 = entityManager.find(Customer.class, 4); // ����һ�����Ʋ���
		
		entityManager.merge(customer); // �����൱��entityManager������id��ͬ�Ķ������
		
		System.out.println(customer == customer2); //false
	}
	
	// ���������һ���������, ������Ķ����� OID
	//1. ���� EntityManager ������û�иö���
	//2. �������ݿ���Ҳ�ж�Ӧ�ļ�¼ 				*******
	//3. JPA ���ѯ��Ӧ�ļ�¼, Ȼ�󷵻ظü�¼��һ���Ķ���, ��Ȼ���������������Ը��Ƶ���ѯ���Ķ�����.
	//4. �Բ�ѯ���Ķ���ִ�� update ����. 
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
	
	
	
	// ���������һ������Ķ��󣬼�����Ķ�����Id	   *******
	//1����EntityManager������û�иö���
	//2���������ݿ���Ҳ���ж�Ӧ��¼
	//3��jpa�ᴴ��һ���µĶ���Ȼ��ѵ�ǰ�����������Ը��Ƶ��´����Ķ�����
	//4�����´����Ķ���ִ��insert����
	@Test
	public void testMerge2(){
		Customer customer = new Customer() ;
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("dd@163.com");
		customer.setLastName("DD");
		
		customer.setId(100);// ע�⣺���id�������ŵ����ݿ���
		
		Customer customer2 = entityManager.merge(customer);
		System.out.println("customer:" + customer);
		System.out.println("customer2:" + customer2);
		
	}
	
	
	// ���������һ����ʱ���󣬻ᴴ��һ���µĶ��󣬰���ʱ��������Ը��Ƶ��µĶ����У�Ȼ����г־û�������
	// �����¶����֮ǰ�Ķ�����ͬһ������
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
	
	
	
	
	
	
	
	// �൱��session��delete����
	// ����jpa��removeֻ���Ƴ��־û����󣬶�session��delete�������Ƴ�����״̬����
	@Test
	public void testRemove(){
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	
	// �൱��session��save����
	// ע�⣺jpa��persist��������Ķ���������id�����򱨴�
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
	
	// �൱��session��load����
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 1) ;
//		System.out.println(customer.getClass().getName());
		System.out.println("-------------------");
		System.out.println(customer); // ֻ�����õ�customer��ʱ��Ż�����ݿ��в�ѯ
	}
	
	// �൱�� session��get����
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1); // ���ϲ�ѯ,��дsql���
		System.out.println("--------------");
		System.out.println(customer);
	}
}
