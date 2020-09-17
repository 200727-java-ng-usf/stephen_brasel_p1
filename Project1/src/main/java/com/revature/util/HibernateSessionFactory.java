package com.revature.util;

import com.revature.models.AppUser;
import com.revature.models.Reimbursement;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateSessionFactory {
	private static SessionFactory sessionFactory;
	private static Properties props = new Properties();

	static{

		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream propsInput = loader.getResourceAsStream("application.properties");
			if (propsInput == null) {
				props.setProperty("url", System.getProperty("url"));
				props.setProperty("username", System.getProperty("username"));
				props.setProperty("password", System.getProperty("password"));
			} else {
				props.load(propsInput);
			}
//			props.load(new FileReader("./src/main/resources/application.properties"));
//			if(props.isEmpty()){
//				props.load(new FileReader("./application.properties"));
//			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to load application properties.");
//			e.printStackTrace();
		}
	}

	private HibernateSessionFactory(){
	}

	public static SessionFactory getInstance(){
		if(sessionFactory != null){
			return sessionFactory;
		}

		try{

			Configuration config = new Configuration()
					.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
					.setProperty("hibernate.connection.url", "jdbc:" +
							"postgresql://" +
							props.getProperty("url") +
							":5432" +
							"/postgres")
					.setProperty("hibernate.connection.username", props.getProperty("username"))
					.setProperty("hibernate.connection.password", props.getProperty("password"))
//					.setProperty("hibernate.connection.pool_size", "1")
					.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect")
					.setProperty("hibernate.current_session_context_class", "thread")
//					.setProperty("hibernate.hbm2ddl.auto", "create-drop")
				.addAnnotatedClass(AppUser.class)
				.addAnnotatedClass(Reimbursement.class)
				;

			config.setImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE);

			System.out.println("Configuration: " + config);

			sessionFactory = config.buildSessionFactory();

			System.out.println("SessionFactory: " + sessionFactory);

			return sessionFactory;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		    e.printStackTrace();
		    return null;
		}
	}

//	@Override
//	protected Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException();
//	}
}
