package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.data.repository.CrudRepository;

public interface MirrorRepository extends CrudRepository<Mirror, Long> {

    int countByName(String name);
}
