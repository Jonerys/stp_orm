import Entity.GoodsMainEntity;
import Entity.GoodswhEntity;
import Entity.WarehousesEntity;
import org.hibernate.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;
import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class DataAccess {
    public static SessionFactory sessionFactory = null;

    public static SessionFactory createSessionFactory() {
        StandardServiceRegistry registry = null;
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            MetadataSources sources = new MetadataSources(registry);
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }
        return sessionFactory;
    }
}



public class Main extends JFrame {

    public static void main(String[] args) {
        char ch = ' ';
        boolean exit = false;
        while (!exit) {
            mainMenu();
            try {
                int temp = System.in.read();
                ch = (char)temp;
                System.in.skip(System.in.available());
                Transaction transaction = null;
                switch (ch) {
                    case '0':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            Query query = session.createQuery("FROM GoodsMainEntity ORDER BY id asc");
                            List<GoodsMainEntity> goods = query.list();
                            System.out.println("goods_main\n\nid\tname");
                            for (GoodsMainEntity s : goods) {
                                System.out.println(s.toString());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Ошибка SELECT\nПодробнее: " + e.getMessage() + "\n\n");
                        }
                        break;
                    case '1':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите название: ");
                            Scanner sc = new Scanner(System.in);
                            String str = sc.nextLine();
                            GoodsMainEntity gme = new GoodsMainEntity();
                            gme.setName(str);
                            transaction = session.beginTransaction();
                            session.save(gme);
                            transaction.commit();
                            System.out.println("Строка успешно добавлена\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка INSERT\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '2':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД редактируемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            System.out.print("\nВведите новое название: ");
                            Scanner sc2 = new Scanner(System.in);
                            String str = sc2.nextLine();
                            transaction = session.beginTransaction();
                            Query query = session.createQuery("UPDATE GoodsMainEntity SET name = :name WHERE id = :o_id");
                            query.setParameter("o_id", id);
                            query.setParameter("name", str);
                            int result = query.executeUpdate();
                            transaction.commit();
                            System.out.println("Обновлено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка UPDATE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '3':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД удаляемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            transaction = session.beginTransaction();
                            Query query = session.createQuery("DELETE FROM GoodsMainEntity WHERE id = :o_id");
                            query.setParameter("o_id", id);
                            int result = query.executeUpdate();
                            transaction.commit();
                            System.out.println("Удалено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка DELETE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '4':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            Query query = session.createQuery("FROM GoodswhEntity ORDER BY id asc");
                            List<GoodswhEntity> gwh = query.list();
                            System.out.println("goodswh\n\nid\tg_name | w_name");
                            for (GoodswhEntity s : gwh) {
                                System.out.println(s.toString());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Ошибка SELECT\nПодробнее: " + e.getMessage() + "\n\n");
                        }
                        break;
                    case '5':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД товара (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int gId = sc.nextInt();
                            if (gId < 0) break;
                            Query query = session.createQuery("FROM GoodsMainEntity WHERE id = :g_id");
                            query.setParameter("g_id", gId);
                            List<GoodsMainEntity> goods = query.list();
                            System.out.print("\nВведите ИД склада (для выхода введите отрицательный ИД): ");
                            Scanner sc2 = new Scanner(System.in);
                            int wId = sc2.nextInt();
                            if (wId < 0) break;
                            Query query2 = session.createQuery("FROM WarehousesEntity WHERE id = :w_id");
                            query2.setParameter("w_id", wId);
                            List<WarehousesEntity> warehouses = query2.list();
                            GoodswhEntity gwh = new GoodswhEntity();
                            gwh.setGoodsMainByIdGd(goods.get(0));
                            gwh.setWarehousesByIdWh(warehouses.get(0));
                            transaction = session.beginTransaction();
                            session.save(gwh);
                            transaction.commit();
                            System.out.println("Строка успешно добавлена\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка INSERT\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '6':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД редактируемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            System.out.print("\nВведите новый ИД товара (если хотите оставить введите отрицательный ИД): ");
                            Scanner sc2 = new Scanner(System.in);
                            int gId = sc2.nextInt();
                            System.out.print("\nВведите новый ИД склада (если хотите оставить введите отрицательный ИД): ");
                            Scanner sc3 = new Scanner(System.in);
                            int wId = sc3.nextInt();
                            Query query = session.createQuery("FROM GoodsMainEntity WHERE id = :g_id");
                            query.setParameter("g_id", gId);
                            List<GoodsMainEntity> ng = query.list();
                            Query query2 = session.createQuery("FROM WarehousesEntity WHERE id = :w_id");
                            query2.setParameter("w_id", wId);
                            List<WarehousesEntity> nw = query2.list();
                            Query query3;
                            if (gId < 0 && wId < 0) {
                                System.out.println("Обновлено строк: 0\n\n");
                                break;
                            } else if (gId < 0) {
                                query3 = session.createQuery("UPDATE GoodswhEntity SET warehousesByIdWh = :nw WHERE id = :o_id");
                                query3.setParameter("nw", nw.get(0));
                            } else if (wId < 0) {
                                query3 = session.createQuery("UPDATE GoodswhEntity SET goodsMainByIdGd = :ng WHERE id = :o_id");
                                query3.setParameter("ng", ng.get(0));
                            } else {
                                query3 = session.createQuery( "UPDATE GoodswhEntity SET goodsMainByIdGd = :ng, warehousesByIdWh = :nw WHERE id = :o_id");
                                query3.setParameter("ng", ng.get(0));
                                query3.setParameter("nw", nw.get(0));
                            }
                            query3.setParameter("o_id", id);

                            transaction = session.beginTransaction();
                            int result = query3.executeUpdate();
                            transaction.commit();
                            System.out.println("Обновлено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка UPDATE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '7':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД удаляемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            transaction = session.beginTransaction();
                            Query query = session.createQuery("DELETE FROM GoodswhEntity WHERE id = :o_id");
                            query.setParameter("o_id", id);
                            int result = query.executeUpdate();
                            transaction.commit();
                            System.out.println("Удалено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка DELETE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case '8':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            Query query = session.createQuery("FROM WarehousesEntity ORDER BY id asc");
                            List<WarehousesEntity> warehouses = query.list();
                            System.out.println("warehouses\n\nid\tname");
                            for (WarehousesEntity s : warehouses) {
                                System.out.println(s.toString());
                            }
                            System.out.println();
                        } catch (Exception e) {
                            System.out.println("Ошибка SELECT\nПодробнее: " + e.getMessage());
                        }
                        break;
                    case '9':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите название: ");
                            Scanner sc = new Scanner(System.in);
                            String str = sc.nextLine();
                            WarehousesEntity whe = new WarehousesEntity();
                            whe.setName(str);
                            transaction = session.beginTransaction();
                            session.save(whe);
                            transaction.commit();
                            System.out.println("Строка успешно добавлена\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошбика INSERT\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case 'A':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД редактируемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            System.out.print("\nВведите новое название: ");
                            Scanner sc2 = new Scanner(System.in);
                            String str = sc2.nextLine();
                            transaction = session.beginTransaction();
                            Query query = session.createQuery("UPDATE WarehousesEntity SET name = :name WHERE id = :o_id");
                            query.setParameter("o_id", id);
                            query.setParameter("name", str);
                            int result = query.executeUpdate();
                            transaction.commit();
                            System.out.println("Обновлено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка UPDATE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case 'B':
                        try (Session session = DataAccess.createSessionFactory().openSession()) {
                            System.out.print("\nВведите ИД удаляемой записи (для выхода введите отрицательный ИД): ");
                            Scanner sc = new Scanner(System.in);
                            int id = sc.nextInt();
                            if (id < 0) break;
                            transaction = session.beginTransaction();
                            Query query = session.createQuery("DELETE FROM WarehousesEntity WHERE id = :o_id");
                            query.setParameter("o_id", id);
                            int result = query.executeUpdate();
                            transaction.commit();
                            System.out.println("Удалено строк: " + result + "\n\n");
                        } catch (Exception e) {
                            System.out.println("Ошибка DELETE\nПодробнее: " + e.getMessage() + "\n\n");
                            transaction.rollback();
                        }
                        break;
                    case 'E': exit = true; break;
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage() + "\n\n");
            }
        }
    }
    public static void mainMenu(){
        System.out.println("Приложение Create Read Update Delete on Hibernate");
        System.out.println("Варианты взаимодействия:");
        System.out.println("Таблица goods_main (список товаров):");
        System.out.println("0.\tВывод.");
        System.out.println("1.\tВставить.");
        System.out.println("2.\tОбновить.");
        System.out.println("3.\tУдалить.");
        System.out.println("Таблица goodswh (расположение товаров на складах):");
        System.out.println("4.\tВывод.");
        System.out.println("5.\tВставить.");
        System.out.println("6.\tОбновить.");
        System.out.println("7.\tУдалить.");
        System.out.println("Таблица warehouses (список складов):");
        System.out.println("8.\tВывод.");
        System.out.println("9.\tВставить.");
        System.out.println("A.\tОбновить.");
        System.out.println("B.\tУдалить.\n");
        System.out.println("E.\tВыход.");
        System.out.print("Введите номер пункта: ");
    }
}