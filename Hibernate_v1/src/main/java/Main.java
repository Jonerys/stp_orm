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

import javax.persistence.criteria.*;
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaQuery<GoodsMainEntity> criteriaSelect = criteriaBuilder.createQuery(GoodsMainEntity.class);
                            Root<GoodsMainEntity> root = criteriaSelect.from(GoodsMainEntity.class);
                            criteriaSelect.orderBy(criteriaBuilder.asc(root.get("id")));
                            criteriaSelect.select(root);
                            Query query = session.createQuery(criteriaSelect);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaUpdate<GoodsMainEntity> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GoodsMainEntity.class);
                            Root<GoodsMainEntity> root = criteriaUpdate.from(GoodsMainEntity.class);
                            criteriaUpdate.set("name", str);
                            criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), id));
                            transaction = session.beginTransaction();
                            Query query = session.createQuery(criteriaUpdate);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaDelete<GoodsMainEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(GoodsMainEntity.class);
                            Root<GoodsMainEntity> root = criteriaDelete.from(GoodsMainEntity.class);
                            criteriaDelete.where(criteriaBuilder.equal(root.get("id"), id));
                            transaction = session.beginTransaction();
                            Query query = session.createQuery(criteriaDelete);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaQuery<GoodswhEntity> criteriaSelect = criteriaBuilder.createQuery(GoodswhEntity.class);
                            Root<GoodswhEntity> root = criteriaSelect.from(GoodswhEntity.class);
                            criteriaSelect.orderBy(criteriaBuilder.asc(root.get("id")));
                            criteriaSelect.select(root);
                            Query query = session.createQuery(criteriaSelect);
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

                            System.out.print("\nВведите ИД склада (для выхода введите отрицательный ИД): ");
                            Scanner sc2 = new Scanner(System.in);
                            int wId = sc2.nextInt();
                            if (wId < 0) break;
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaQuery<GoodsMainEntity> criteriaSelect1 = criteriaBuilder.createQuery(GoodsMainEntity.class);
                            Root<GoodsMainEntity> root1 = criteriaSelect1.from(GoodsMainEntity.class);
                            criteriaSelect1.where(criteriaBuilder.equal(root1.get("id"), gId));
                            criteriaSelect1.select(root1);
                            Query query1 = session.createQuery(criteriaSelect1);
                            List<GoodsMainEntity> goods = query1.list();
                            CriteriaQuery<WarehousesEntity> criteriaSelect2 = criteriaBuilder.createQuery(WarehousesEntity.class);
                            Root<WarehousesEntity> root2 = criteriaSelect2.from(WarehousesEntity.class);
                            criteriaSelect2.where(criteriaBuilder.equal(root2.get("id"), wId));
                            criteriaSelect2.select(root2);
                            Query query2 = session.createQuery(criteriaSelect2);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaQuery<GoodsMainEntity> criteriaSelect1 = criteriaBuilder.createQuery(GoodsMainEntity.class);
                            Root<GoodsMainEntity> root1 = criteriaSelect1.from(GoodsMainEntity.class);
                            criteriaSelect1.where(criteriaBuilder.equal(root1.get("id"), gId));
                            criteriaSelect1.select(root1);
                            Query query1 = session.createQuery(criteriaSelect1);
                            List<GoodsMainEntity> ng = query1.list();
                            CriteriaQuery<WarehousesEntity> criteriaSelect2 = criteriaBuilder.createQuery(WarehousesEntity.class);
                            Root<WarehousesEntity> root2 = criteriaSelect2.from(WarehousesEntity.class);
                            criteriaSelect2.where(criteriaBuilder.equal(root2.get("id"), wId));
                            criteriaSelect2.select(root2);
                            Query query2 = session.createQuery(criteriaSelect2);
                            List<WarehousesEntity> nw = query2.list();
                            Query query3;
                            if (gId < 0 && wId < 0) {
                                System.out.println("Обновлено строк: 0\n\n");
                                break;
                            } else if (gId < 0) {
                                CriteriaUpdate<GoodswhEntity> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GoodswhEntity.class);
                                Root<GoodswhEntity> root = criteriaUpdate.from(GoodswhEntity.class);
                                criteriaUpdate.set("warehousesByIdWh", nw.get(0));
                                criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), id));
                                query3 = session.createQuery(criteriaUpdate);
                            } else if (wId < 0) {
                                CriteriaUpdate<GoodswhEntity> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GoodswhEntity.class);
                                Root<GoodswhEntity> root = criteriaUpdate.from(GoodswhEntity.class);
                                criteriaUpdate.set("goodsMainByIdGd", ng.get(0));
                                criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), id));
                                query3 = session.createQuery(criteriaUpdate);
                            } else {
                                CriteriaUpdate<GoodswhEntity> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GoodswhEntity.class);
                                Root<GoodswhEntity> root = criteriaUpdate.from(GoodswhEntity.class);
                                criteriaUpdate.set("goodsMainByIdGd", ng.get(0));
                                criteriaUpdate.set("warehousesByIdWh", nw.get(0));
                                criteriaUpdate.where(criteriaBuilder.equal(root.get("id"), id));
                                query3 = session.createQuery(criteriaUpdate);
                            }

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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaDelete<GoodswhEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(GoodswhEntity.class);
                            Root<GoodswhEntity> root = criteriaDelete.from(GoodswhEntity.class);
                            criteriaDelete.where(criteriaBuilder.equal(root.get("id"), id));
                            transaction = session.beginTransaction();
                            Query query = session.createQuery(criteriaDelete);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaQuery<WarehousesEntity> criteriaSelect = criteriaBuilder.createQuery(WarehousesEntity.class);
                            Root<WarehousesEntity> root = criteriaSelect.from(WarehousesEntity.class);
                            criteriaSelect.orderBy(criteriaBuilder.asc(root.get("id")));
                            criteriaSelect.select(root);
                            Query query = session.createQuery(criteriaSelect);
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
                            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                            CriteriaDelete<WarehousesEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(WarehousesEntity.class);
                            Root<WarehousesEntity> root = criteriaDelete.from(WarehousesEntity.class);
                            criteriaDelete.where(criteriaBuilder.equal(root.get("id"), id));
                            transaction = session.beginTransaction();
                            Query query = session.createQuery(criteriaDelete);
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