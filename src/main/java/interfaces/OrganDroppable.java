package interfaces;

import items.OrganParts;

/**
 * Interface for entities that drop organ parts when defeated.
 */
public interface OrganDroppable {

    /**
     * Retrieves the organ part dropped by this entity.
     *
     * @return The dropped OrganParts object
     */
    OrganParts getDropItem();
}
