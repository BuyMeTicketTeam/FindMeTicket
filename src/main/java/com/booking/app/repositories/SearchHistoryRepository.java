package com.booking.app.repositories;

import com.booking.app.entities.user.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, UUID> {

}
