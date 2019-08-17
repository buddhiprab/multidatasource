package com.buddhi.multidatasource.repository;

import com.buddhi.multidatasource.model.ApiUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
}
