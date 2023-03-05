package map.project.repository;

import map.project.domain.Entity;

public interface Repository<ID, E extends Entity<ID>> {
    Iterable<E> findAll();
    E findOne(ID id);
    E save(E e);
}
