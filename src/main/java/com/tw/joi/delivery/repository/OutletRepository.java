package com.tw.joi.delivery.repository;

import com.tw.joi.delivery.domain.Outlet;
import java.util.Optional;

/**
 * Repository interface for retrieving outlets/stores of any type.
 */
public interface OutletRepository {
    
    /**
     * Finds an outlet by its ID.
     * 
     * @param outletId the outlet/store ID
     * @return Optional containing the outlet if found
     */
    Optional<Outlet> findById(String outletId);
}
