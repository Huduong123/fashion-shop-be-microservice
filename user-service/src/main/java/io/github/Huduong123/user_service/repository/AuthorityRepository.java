package io.github.Huduong123.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.user_service.entity.Authority;

@Repository
public interface  AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query("SELECT a FROM Authority a WHERE "
    + "(:username IS NULL OR a.user.username LIKE %:username%)"
    + "AND (:authority IS NULL OR a.authority LIKE %:authority%)")
    List<Authority> searchByUsernameAndAuthority(@Param("username") String username,
                                            @Param("authority") String authority);


    boolean existsByUserIdAndAuthority(Long userID, String authority);

    boolean existsByUserIdAndAuthorityAndIdNot(Long userID, String authority, Long id);
}
