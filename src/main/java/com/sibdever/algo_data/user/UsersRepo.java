package com.sibdever.algo_data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepo extends JpaRepository<User, Integer> {

    @Query(value = "select user from users user where user.name = ?1")
    User findByUserName(String name);

    @Query(value = "select user from users user where user.ticket = ?1")
    User findByTicket(String ticket);

}