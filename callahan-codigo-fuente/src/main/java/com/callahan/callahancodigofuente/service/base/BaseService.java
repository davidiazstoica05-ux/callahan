package com.callahan.callahancodigofuente.service.base;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;


public abstract class BaseService<T, ID, R extends JpaRepository<T, ID>> implements BaseServiceI<T, ID> {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected R repo;

    @Override
    public List<T> findAll() {
        // TODO Auto-generated method stub
        return repo.findAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        // TODO Auto-generated method stub
        return repo.findById(id);
    }

    @Override
    public T save(T t) {
        // TODO Auto-generated method stub
        return repo.save(t);
    }

    @Override
    public T edit(T t) {
        // TODO Auto-generated method stub
        return repo.save(t);
    }

    @Override
    public void delete(T t) {

        repo.delete(t);

    }

    @Override
    public void deleteById(ID id) {

        repo.deleteById(id);

    }

}

