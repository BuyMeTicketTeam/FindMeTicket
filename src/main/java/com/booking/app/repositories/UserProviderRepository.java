package com.booking.app.repositories;

import com.booking.app.entity.Role;
import com.booking.app.entity.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProviderRepository extends JpaRepository<UserProvider, Integer> {
}
