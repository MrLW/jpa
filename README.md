# jpa
JPA �� hibernate ��һ�����󣨾���JDBC��JDBC�����Ĺ�ϵ��

JPA �����Ͼ���һ��  ORM �淶������ORM ��� ���� ��Ϊ JPA ��δ�ṩ ORM ʵ�֣���ֻ���ƶ���һЩ�淶���ṩ��һЩ��̵� API �ӿڣ�������ʵ������ ORM �����ṩʵ��


## ��������

**1��persistence.xml�������ݿ��������**

```

	<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" 
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
		<persistence-unit name="jpa-sample01" transaction-type="RESOURCE_LOCAL">
		
			<!-- ����ʲô��Ʒ��Ϊ	jpa��ʵ�� 
				���ֻ��һ��jpa��ʵ�֣������ʡ��
			-->
			<provider>org.hibernate.ejb.HibernatePersistence</provider>
			<!-- ��ӳ־û��� -->
			<class>com.lw.jpa.helloword.Customer</class>
			<properties>
				<property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
				<property name="javax.persistence.jdbc.url" value="jdbc:mysql:///jpa"/>
				<property name="javax.persistence.jdbc.user" value="root"/>
				<property name="javax.persistence.jdbc.password" value="root"/>
				<!-- ����jpaʵ�ֲ�Ʒ�Ļ������� -->
				<property name="hibernate.format_sql" value="true"/>
				<property name="hibernate.show_sql" value="true"/>
				<property name="hibernate.hbm2ddl.auto" value="update"/>
			</properties>
			</persistence-unit>
	</persistence>
```

**2������ʵ����,��ʹ��ע�����õ����ݿ��еı�**
```
	
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
	
```

**3��ʹ��EntityManager������ݵ�curd**
```

	@Before
	public void init(){
		//jpa-sample01:��persistence.xml��persistence-unit��name����
		entityManagerFactory = 			Persistence.createEntityManagerFactory("jpa-sample01");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}
	// �൱��session��save����
	// ע�⣺jpa��persist��������Ķ���������id�����򱨴�,Ҳ����˵ֻ�ܱ����������,���ܱ���־û�����
	// ����hibernate���Ա�����id��,���Ա���־û�����
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
	
	
	// �൱��session��delete����
	// ����jpa��removeֻ���Ƴ��־û����󣬶�session��delete�������Ƴ�����״̬����
	@Test
	public void testRemove(){
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	
	// �൱��session��load����
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 1) ;
		//System.out.println(customer.getClass().getName());
		System.out.println("-------------------");
		System.out.println(customer); // ֻ�����õ�customer��ʱ��Ż�����ݿ��в�ѯ
	}
	
	// �൱�� session��get����
	@Test
	public void testFind() {
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println("--------------");
		System.out.println(customer);
	}
	
	@After
	public void destroy(){
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	
	
```


```

	//Merge����,����session��saveOrUpdate����
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

```

## ����ע��

- @Entity:��ӵ�һ��ʵ����,������ʵ���Ӧ�����ݿ��е�һ�ű�,û��û�����@Tableע��,��Ĭ�ϱ������Ǹ�ʵ�������
- @Table������ָ����ʵ���Ӧ�����ݿ��ı���
- @Column�����õ����Ե�get������,ָ���������.
- @GeneratedValue:ָ�����������ɲ���
- @Base��Ĭ�ϵ�ע��
- @Transient��ϣ�������ĳ��������Ҫӳ�����ݱ��һ����
- @Temporal��ָ�����ڵĶ�Ӧ���ݿ�ĸ�ʽ

**ʹ��Table��������**
```
	
	//@TableGenerator:����һ������������
	@TableGenerator(name="ID_GENERATOR",                                     
					table="jpa_id_generators", // ���ɲ��Գ־û��ı���                             
					pkColumnName="PK_NAME", // ָ���־û�����      �������ɲ��Զ�Ӧ��������              					  ��pkColumnValueȷ��һ��             
					pkColumnValue="CUSTOMER_ID",  //  pkColumnValue ���Ե�ֵ��ʾ�ڳ־û����в�������Ӧ������           ��pkColumnNameȷ��һ��
					valueColumnName="PK_VALUE", ��ʾ�ڳ־û����У���������ǰ�����ɵ�ֵ������ֵ��������ÿ�δ����ۼ�	    ȷ����
					allocationSize=100) // ÿ�����100��           
	// strategy=GenerationType.TABLE:�����������ɲ���ʹ��Table
	// generator:ָ��Table������             
	@GeneratedValue(strategy=GenerationType.TABLE,generator="ID_GENERATOR")  
```

