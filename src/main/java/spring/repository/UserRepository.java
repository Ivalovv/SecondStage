package spring.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update User u
        set u.name = :name,
            u.email = :email,
            u.age = :age
        where u.id = :id
    """)
    int updateUser(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("age") Integer age
    );
}
