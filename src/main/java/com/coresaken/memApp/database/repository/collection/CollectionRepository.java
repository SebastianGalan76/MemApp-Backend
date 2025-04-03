package com.coresaken.memApp.database.repository.collection;

import com.coresaken.memApp.database.model.collection.UserCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<UserCollection, Long> {
    Optional<UserCollection> findByUuid(UUID uuid);
}
