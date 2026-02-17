package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.User;

public class UserStorage<T extends User> extends GenericInMemoryStorage<T> {
    public boolean existsByUsername(String username) {
        if (username == null) return false;
        return storage.values()
                .stream()
                .anyMatch(entity ->
                        entity.getUsername() != null && entity.getUsername().equalsIgnoreCase(username)
                );
    }
}
