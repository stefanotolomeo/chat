package com.company.chat.dao.manager;

import java.util.Map;

public interface IService<T> {

	void save(T item);

	T findById(String id);

	Map<String, T> findAll();

	void delete(String id);
}
