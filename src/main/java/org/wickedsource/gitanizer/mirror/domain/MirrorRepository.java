package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MirrorRepository extends CrudRepository<Mirror, Long> {

    int countByDisplayName(String name);

    @Query("select count(m) from Mirror m where displayName = :name and id <> :id")
    int countByDisplayNameExcludeId(@Param("name") String name, @Param("id") long id);

    Mirror findByGitRepositoryName(String gitRepositoryName);

    @Query("select m from Mirror m where lastImportFinished < :date and syncActive = true")
    List<Mirror> findByLastImportFinishedOlderThan(@Param("date") LocalDateTime date);
}
