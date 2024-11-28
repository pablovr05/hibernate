package com.project;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class Manager {

    private static SessionFactory factory; 
    
    public static void createSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            
            // Add the mapping resources instead of annotated classes
            configuration.addResource("Ciutat.hbm.xml");
            configuration.addResource("Ciutada.hbm.xml");

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
                
            factory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close () {
        factory.close();
    }
  
    public static Ciutat addCiutat(String nom, String pais, int codiPostal) {
        Session session = factory.openSession();
        Transaction tx = null;
        Ciutat result = null;
        try {
            tx = session.beginTransaction();
            result = new Ciutat(nom, pais, codiPostal); // 'pais' es más claro que 'ciutat'
            session.persist(result);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace(); 
            result = null;
        } finally {
            session.close(); 
        }
        return result;
    }    

    public static Ciutada addCiutada(String nom, String cognom, int edat) {
        Session session = null;
        Transaction tx = null;
        Ciutada result = null;
    
        try {
            // Crear una nueva sesión de Hibernate
            session = factory.openSession();
            System.out.println("Session created: " + session);
    
            // Iniciar una transacción
            tx = session.beginTransaction();
            System.out.println("Transaction started: " + tx);
    
            // Crear un nuevo objeto Ciutada
            result = new Ciutada(nom, cognom, edat);
            System.out.println("Created Ciutada: " + result);
    
            // Verificar el objeto Ciutada antes de persistirlo
            System.out.println("Before persisting, Ciutada details: ");
            System.out.println("Ciutada Name: " + result.getNom());
            System.out.println("Ciutada Surname: " + result.getCognom());
            System.out.println("Ciutada Age: " + result.getEdat());
    
            // Intentar persistir el objeto Ciutada en la base de datos
            session.persist(result);
            System.out.println("Persisted Ciutada: " + result);
    
            // Confirmar la transacción
            tx.commit();
            System.out.println("Transaction committed");
    
        } catch (HibernateException e) {
            // Si ocurre un error, revertir la transacción
            if (tx != null) {
                tx.rollback();
                System.out.println("Transaction rolled back");
            }
            e.printStackTrace();
            result = null;
        } finally {
            // Asegurarse de cerrar la sesión
            if (session != null && session.isOpen()) {
                session.close();
                System.out.println("Session closed");
            }
        }
    
        return result;
    }
    

    public static void updateCiutada(long ciutatId, String nom, String cognom, int edat){
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Ciutada ciutada = (Ciutada) session.get(Ciutada.class, ciutatId);

            ciutada.setNom(nom);
            ciutada.setCognom(cognom);
            ciutada.setEdat(edat);
            
            session.merge(ciutada);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }

    public static void updateCiutat(long ciutatId, String nom, String pais, int codiPostal, Set<Ciutada> ciutadans) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) {
                throw new RuntimeException("Cart not found with id: " + ciutatId);
            }
            
            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setCodiPostal(codiPostal);
            
            session.merge(ciutat);
            tx.commit();
            
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static <T> void delete(Class<? extends T> clazz, Serializable id) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            T obj = clazz.cast(session.get(clazz, id));
            if (obj != null) {  // Only try to remove if the object exists
                session.remove(obj);
                tx.commit();
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static <T> Collection<?> listCollection(Class<? extends T> clazz) {
        return listCollection(clazz, "");
    }

    public static <T> Collection<?> listCollection(Class<? extends T> clazz, String where){
        Session session = factory.openSession();
        Transaction tx = null;
        Collection<?> result = null;
        try {
            tx = session.beginTransaction();
            if (where.length() == 0) {
                result = session.createQuery("FROM " + clazz.getName(), clazz).list(); // Added class parameter
            } else {
                result = session.createQuery("FROM " + clazz.getName() + " WHERE " + where, clazz).list();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return result;
    }

    public static <T> String collectionToString(Class<? extends T> clazz, Collection<?> collection){
        String txt = "";
        for(Object obj: collection) {
            T cObj = clazz.cast(obj);
            txt += "\n" + cObj.toString();
        }
        if (txt.length() > 0 && txt.substring(0, 1).compareTo("\n") == 0) {
            txt = txt.substring(1);
        }
        return txt;
    }

    public static void queryUpdate(String queryString) {
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(queryString, Void.class); // Updated to NativeQuery
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
    }

    public static List<Object[]> queryTable(String queryString) {
        Session session = factory.openSession();
        Transaction tx = null;
        List<Object[]> result = null;
        try {
            tx = session.beginTransaction();
            NativeQuery<Object[]> query = session.createNativeQuery(queryString, Object[].class); // Updated to NativeQuery
            result = query.getResultList(); // Changed from list() to getResultList()
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        } finally {
            session.close(); 
        }
        return result;
    }

    public static String tableToString(List<Object[]> rows) {
        String txt = "";
        for (Object[] row : rows) {
            for (Object cell : row) {
                txt += cell.toString() + ", ";
            }
            if (txt.length() >= 2 && txt.substring(txt.length() - 2).compareTo(", ") == 0) {
                txt = txt.substring(0, txt.length() - 2);
            }
            txt += "\n";
        }
        if (txt.length() >= 2) {
            txt = txt.substring(0, txt.length() - 1);
        }
        return txt;
    }

    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        Ciutat ciutat;
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            ciutat = session.get(Ciutat.class, ciutatId);
            // Eagerly fetch the items collection
            ciutat.getCiutadans().size();
            tx.commit();
        }
        return ciutat;
    }
}