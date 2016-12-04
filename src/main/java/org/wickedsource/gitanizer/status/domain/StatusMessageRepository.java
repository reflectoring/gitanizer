package org.wickedsource.gitanizer.status.domain;

import org.springframework.data.repository.CrudRepository;

public interface StatusMessageRepository extends CrudRepository<StatusMessage, Long> {

    StatusMessage findTop1ByMirrorIdOrderByTimestampDesc(Long mirrorId);

}
