package com.tw.joi.delivery.service.inventory;

import com.tw.joi.delivery.domain.Outlet;
import com.tw.joi.delivery.service.StoreNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Factory for retrieving the appropriate StoreInventoryHandler based on outlet type.
 */
@Component
@RequiredArgsConstructor
public class StoreInventoryHandlerFactory {

    private final List<StoreInventoryHandler> handlers;

    /**
     * Gets the appropriate handler for the given outlet.
     * 
     * @param outlet the outlet/store
     * @return the handler that supports this outlet type
     * @throws StoreNotFoundException if no handler supports this outlet type
     */
    public StoreInventoryHandler getHandler(Outlet outlet) {
        return handlers.stream()
            .filter(handler -> handler.supports(outlet))
            .findFirst()
            .orElseThrow(() -> new StoreNotFoundException(
                "No inventory handler found for store type: " + outlet.getClass().getSimpleName()));
    }
}
