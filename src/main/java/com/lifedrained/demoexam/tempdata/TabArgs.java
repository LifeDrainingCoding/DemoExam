package com.lifedrained.demoexam.tempdata;

import com.lifedrained.demoexam.entity.AbstractEntity;

//Стандартный POJO класс для хранения ссылки на класс  и имя сущности
//Необходим для инициализации вкладок
public class TabArgs{
    private Class<?> entityClazz;
    private String entityName;
    public <T> TabArgs(Class<T> entityClazz , String entityName){
        this.entityClazz = entityClazz;
        this.entityName = entityName;
    }

    public <T extends  AbstractEntity> Class<T> getEntityClazz() {
        return (Class<T>) entityClazz;
    }

    public void setEntityClazz(Class<?> entityClazz) {
        this.entityClazz = entityClazz;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
