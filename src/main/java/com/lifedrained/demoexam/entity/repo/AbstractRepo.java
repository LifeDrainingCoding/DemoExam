package com.lifedrained.demoexam.entity.repo;

import com.lifedrained.demoexam.utils.EMUtils;
import com.lifedrained.demoexam.entity.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.List;

//Класс репозитория , который управляет сущностями
public  abstract class AbstractRepo<T extends AbstractEntity>{
    @PersistenceContext
    protected final EntityManager em;
    protected final Class<T> clazz;
    public AbstractRepo(Class<T> clazz) {
        this.clazz = clazz;
        em = EMUtils.getEntityManager();
    }
    //Получает все данные из таблицы используя язык JP SQL
    public List<T> findAll() {
        TypedQuery<T> query = em.createQuery("SELECT o FROM " + clazz.getSimpleName() + " o", clazz);
        return query.getResultList();
    }

    //Cохранение  сущности в таблицу используя Hibernate ORM
    public T save(T obj) {
        if (obj == null){
            throw new NullPointerException("Object MUSTN'T be null");
        }
        em.getTransaction().begin();
        if (obj.getId()== null || em.find(clazz, obj.getId()) == null  ) {
            em.persist(obj);  // INSERT
        } else {
            obj = em.merge(obj);  // UPDATE
        }
        em.getTransaction().commit();
        return obj;
    }


    //Поиск по ID сущности
    public T findById(Long id) {
        return em.find(clazz, id);
    }



    // Удаление сущности из таблицы
    public void delete(T obj) {
        em.getTransaction().begin();
        em.remove(em.contains(obj) ? obj : em.merge(obj));
        em.getTransaction().commit();
    }
}
