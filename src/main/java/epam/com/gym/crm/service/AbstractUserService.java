package epam.com.gym.crm.service;

public interface AbstractUserService<T> extends BaseService<T> {
    T update(String username, T entity);
    T findByUsername(String username);
}
