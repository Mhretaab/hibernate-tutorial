package com.tutorial.hibernate.utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateSessionFactoryWithXml {
    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryWithXml(){

    }

    public static SessionFactory getInstance(){
        if(sessionFactory != null){
            return sessionFactory;
        }

        final Configuration configuration=new Configuration().configure(); // configures settings from hibernate.cfg.xml

        StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();

        // If you miss the below line then it will complaing about a missing dialect setting
        serviceRegistryBuilder.applySettings(configuration.getProperties());

        ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }
}
