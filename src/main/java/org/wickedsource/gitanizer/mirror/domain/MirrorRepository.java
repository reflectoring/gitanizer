package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MirrorRepository extends CrudRepository<Mirror, Long> {

    int countByName(String name);

    @Query("select count(m) from Mirror m where name = :name and id <> :id")
    int countByNameExcludeId(@Param("name") String name, @Param("id") long id);
}
