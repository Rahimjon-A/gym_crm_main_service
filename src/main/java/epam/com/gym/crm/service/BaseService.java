package epam.com.gym.crm.service;

import java.util.List;

public interface BaseService<T, DTO> {
    T create(DTO dto);
    T findById(Long id);
    List<T> findAll();
}
