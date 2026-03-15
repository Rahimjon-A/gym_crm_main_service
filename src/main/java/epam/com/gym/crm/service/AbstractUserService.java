package epam.com.gym.crm.service;

public interface AbstractUserService<T, DTO> extends BaseService<T, DTO> {
    T update(String username, DTO dto);
    T findByUsername(String username);
}
