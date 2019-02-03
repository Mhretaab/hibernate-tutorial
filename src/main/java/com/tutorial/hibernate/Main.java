package com.tutorial.hibernate;

import com.tutorial.hibernate.models.Bee;
import com.tutorial.hibernate.models.Honey;
import com.tutorial.hibernate.utils.EntityManagerFactoryWithJava;
import com.tutorial.hibernate.utils.HibernateSessionFactoryWithJava;
import com.tutorial.hibernate.utils.HibernateSessionFactoryWithXml;
import com.tutorial.hibernate.utils.EntityManagerFactoryWithXml;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        /* clean tables */
        //clean();
        /* simple create example */
        //createHoney();
        /* relation example */
        //createRelation();
        /* delete example */
        //delete();
        /* update example */
        //update();
        /* query example */
        //query();

        //entity manager based queriess

        //create honey with entitymanager
        //createHoneyWithEntityManager();
        //createBeeWithEntityManager();
        //entityManagerQuery();

        //createHoneyWithCurrentSession();
        //createHoneyBeeRelation();
        //getVsLoadDifference();
        //useHqlQuery();
        //useNativeSqlQuery();
        //useHibernateCriteria();
        useNamedQueries();
    }

    private static void useNamedQueries() {
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        Query query = session.getNamedQuery("HQL_GET_ALL_HONEY");
        List<Honey> honeyList = query.list();
        for (Honey h: honeyList){
            System.out.println("Honey name: " + h.getName() + ", " + h.getBees());
        }

        query = session.getNamedQuery("@SQL_GET_ALL_BEES");

        List<Object[]> rows = query.list();
        System.out.println("list of bees");
        System.out.println("----------------------");
        for (Object[] row : rows) {
            System.out.println(row[0] + "   " + row[1]);
        }

        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void useHibernateCriteria() {
        Session sess = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = sess.beginTransaction();

        Criteria criteria = sess.createCriteria(Honey.class);
        List<Honey> honeyList = criteria.list();

        for (Honey h : honeyList) {
            System.out.println(h.getName() + " " + h.getBees());
        }

        criteria.add(Restrictions.eq("id", new Integer(300)));
        Honey tigHoney = (Honey) criteria.uniqueResult();
        System.out.println("unique result with restrictions");
        System.out.println(tigHoney.getName() + " " + tigHoney.getBees());

        System.out.println("order limit and offset");
        List<Honey> hns = sess.createCriteria(Honey.class)
                .addOrder(Order.desc("name"))
                .setFirstResult(0)
                .setMaxResults(2).list();

        int count = 0;
        for(Honey h: hns){
            System.out.println(count + ": " + h.getName() + " " + h.getBees());
            count++;
        }

        tx.commit();
        sess.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void useNativeSqlQuery() {
        Session sess = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = sess.beginTransaction();

        SQLQuery query = sess.createSQLQuery("SELECT h.name AS honey_name, b.name AS bee_name FROM honey AS h JOIN bee AS b ON h.id = b.honey_id");

        List<Object[]> rows = query.list();
        System.out.println("Honey Name  Bee Name");
        System.out.println("----------------------");
        for (Object[] row : rows) {
            System.out.println(row[0] + "   " + row[1]);
        }

        tx.commit();
        sess.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void useHqlQuery() {
        Session sessOne = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction txOne = sessOne.beginTransaction();
        Query query = sessOne.createQuery("SELECT h FROM Honey AS h");
        List<Honey> honeyList = query.list();
        System.out.println("printing list of honey");
        for (Honey h : honeyList) {
            System.out.println("Honey name: " + h.getName());
            System.out.println("List of Bees" + h.getBees());
        }

        query = sessOne.createQuery("SELECT h FROM Honey AS h WHERE h.id = :id");
        query.setInteger("id", 250);
        Honey h = (Honey) query.uniqueResult();
        System.out.println("printing unique honey");
        System.out.println("Honey name: " + h.getName());
        System.out.println("List of Bees: " + h.getBees());

        System.out.println("printing with offset and limit");
        query = sessOne.createQuery("FROM Honey");
        query.setFirstResult(1);
        query.setFetchSize(1);
        List<Honey> honeys = query.list();
        for (Honey o : honeys) {
            System.out.println("Honey id: " + o.getId());
            System.out.println("Honey name: " + o.getName());
            System.out.println("List of Bees" + o.getBees());
        }
        System.out.println("printing honey update");
        query = sessOne.createQuery("UPDATE Honey AS h SET h.name = :name WHERE h.id = :id");
        query.setParameter("name", "Tig Honey");
        query.setLong("id", 300);
        int result = query.executeUpdate();
        System.out.println("Honey Update Status=" + result);

        System.out.println("printing bee deleting");
        query = sessOne.createQuery("DELETE Bee AS b WHERE b.id=:id");
        query.setInteger("id", 201);
        result = query.executeUpdate();
        System.out.println("Bee Delete Status=" + result);

        txOne.commit();
        sessOne.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void getVsLoadDifference() {
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        Honey h = (Honey) session.get(Honey.class, new Integer(250));
        System.out.println(h.getName());
        //Set<Bee> bees = h.getBees();
        for (Bee b : h.getBees()) {
            System.out.println("bee name: " + b.getName());
        }

        Honey a = (Honey) session.load(Honey.class, new Integer(250));//returns proxy  object for lazy initialization
        for (Bee b : a.getBees()) {
            System.out.println("bee name: " + b.getName());
        }

        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void createHoneyBeeRelation() {
        Session session = HibernateSessionFactoryWithXml.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Honey honey = new Honey();
        Bee b = new Bee();
        Bee a = new Bee();

        honey.setName("Amhara Honey");
        honey.setTaste("Wow");

        b.setName("Amhara Jungle bee");
        a.setName("Amhara Forest bee");

        honey.getBees().add(b);
        honey.getBees().add(a);

        b.setHoney(honey);
        a.setHoney(honey);

        //session.save(b);
        //session.save(a);
        session.save(honey);
        tx.commit();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void createHoneyWithCurrentSession() {
        Honey honey = new Honey();
        honey.setName("Tigray Honey");
        honey.setTaste("Yummy");
        Session session = HibernateSessionFactoryWithXml.getInstance().getCurrentSession();
        Transaction tx = session.getTransaction();
        tx.begin();
        session.save(honey);
        tx.commit();

        logger.info(MessageFormat.format("{0} of honey object created", honey.getId()));
        HibernateSessionFactoryWithXml.getInstance().close();//close session factory otherwise program will not end
    }

    private static void createHoneyWithEntityManager() {
        EntityManager entityManager = EntityManagerFactoryWithXml.getEntityManager();
        //(Session)entityManager.getDelegate()
        //Session session = entityManager.unwrap(org.hibernate.Session.class)
        entityManager.getTransaction().begin();
        Honey honey = new Honey();
        honey.setName("wild honey");
        honey.setTaste("Just awesome");
        entityManager.persist(honey);
        entityManager.getTransaction().commit();
        entityManager.close();
        EntityManagerFactoryWithXml.close();
    }

    private static void createBeeWithEntityManager() {
        Honey honey = new Honey();
        honey.setName("Another Forest honey");
        honey.setTaste("So amazing taste");
        Bee bee = new Bee();
        bee.setName("forest bee");
        Bee b = new Bee();
        b.setName("another bee");
        bee.setHoney(honey);
        b.setHoney(honey);
        honey.getBees().add(bee);
        honey.getBees().add(b);

        EntityManager entityManager = EntityManagerFactoryWithXml.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(honey);
        //entityManager.persist(bee);
        entityManager.getTransaction().commit();
        entityManager.close();
        EntityManagerFactoryWithXml.close();
    }

    private static void entityManagerQuery() {
        EntityManager entityManager = EntityManagerFactoryWithXml.getEntityManager();
        entityManager.getTransaction().begin();
        Honey honey = entityManager.find(Honey.class, 50);
        Set<Bee> bees = honey.getBees();
        for (Bee b : bees) {
            System.out.println("bee name: " + b.getName());
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        EntityManagerFactoryWithXml.close();

        //failed to lazily initialize - could not initialize proxy - no Session because session is closed and object is detached
        /*for (Bee b:bees) {
            System.out.println(b.getName());
        }*/
    }

    private static void clean() {
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Bee").executeUpdate();
        session.createQuery("delete from Honey").executeUpdate();
        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static Honey createHoney() {
        Honey forestHoney = new Honey();
        forestHoney.setName("forest honey");
        forestHoney.setTaste("very sweet"); //Honey is in transient state
        Session session = HibernateSessionFactoryWithJava.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        session.save(forestHoney);//Honey is in persistence state since session is not closed
        //session.save does not create an insert statement immediately but just selects the next sequence value
        tx.commit();//sends the SQL insert statement to the database
        session.close();
        HibernateSessionFactoryWithJava.getInstance().close();
        //Honey is in detached state - any changes here will not affect honey in database
        return forestHoney;
    }

    private static void createRelation() {
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();

        Transaction tx = session.beginTransaction();
        Honey honey = new Honey();
        honey.setName("country honey");
        honey.setTaste("Delicious");
        session.save(honey);
        Bee bee = new Bee("Sebastian");
        session.save(bee);
        /* create the relation on both sides */
        bee.setHoney(honey);
        honey.getBees().add(bee);
        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void delete() {
        Honey honey = createHoney();
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(honey);
        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void update() {
        Honey honey = createHoney();
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        honey.setName("Modern style");
        session.update(honey);
        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }

    private static void query() {
        Session session = HibernateSessionFactoryWithXml.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        List honeys = session.createQuery("select h from Honey as h").list();
        for (Iterator iter = honeys.iterator(); iter.hasNext(); ) {
            Honey element = (Honey) iter.next();
            Hibernate.initialize(element.getBees());
            logger.debug(element.toString());
        }
        tx.commit();
        session.close();
        HibernateSessionFactoryWithXml.getInstance().close();
    }
}
