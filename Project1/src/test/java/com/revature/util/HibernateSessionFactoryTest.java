package com.revature.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HibernateSessionFactoryTest {
	private SessionFactory sut;

	@Before
	public void setUp() throws Exception {
		sut = HibernateSessionFactory.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		sut = null;
	}

	@Test
	public void ensureHibernateSessionFactoryIsSingleton() {
		SessionFactory c1 = HibernateSessionFactory.getInstance();
		assertSame(c1, sut);
	}

	@Test
	public void ensureThatAConnectionIsObtained(){
		Session sess = sut.openSession();
		assertNotNull(sess);
	}
}