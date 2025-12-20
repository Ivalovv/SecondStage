package dao;

import model.User;

public interface UserDao {
    void create(User user);

    User read(int id);

    void update(User user);

    void delete(int id);
}
