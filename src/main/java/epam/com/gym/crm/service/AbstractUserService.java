package epam.com.gym.crm.service;

public interface AbstractUserService<T, DTO> extends BaseService<T, DTO> {
    T update(Long id, DTO dto);
    T findByUsername(String username);
}
