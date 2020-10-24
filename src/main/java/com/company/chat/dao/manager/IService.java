package com.company.chat.dao.manager;

import com.company.chat.dao.exceptions.FailedCRUDException;

import java.util.Map;

public interface IService<T> {

	String save(T item) throws FailedCRUDException;

	T findById(String id);

	Map<String, T> findAll();

	T delete(String id) throws FailedCRUDException;
}
