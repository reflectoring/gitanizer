package org.wickedsource.gitanizer.mirror.importing;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;
import org.wickedsource.gitanizer.mirror.domain.MirrorStatus;
import org.wickedsource.gitanizer.mirror.importing.logging.ImportLoggerFactory;

import java.time.LocalDateTime;

@Service
public class MirrorStatusService {

    private final MirrorRepository mirrorRepository;

    private ImportLoggerFactory importLoggerFactory;


    @Autowired
    public MirrorStatusService(MirrorRepository mirrorRepository, ImportLoggerFactory importLoggerFactory) {
        this.mirrorRepository = mirrorRepository;
        this.importLoggerFactory = importLoggerFactory;
    }

    public void progress(Long mirrorId, int percentage) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        mirror.setProgress(percentage);
        mirrorRepository.save(mirror);
    }

    public void error(Long mirrorId, String errorMessage) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        mirror.setStatus(MirrorStatus.ERROR);
        mirror.setSyncActive(false);
        mirrorRepository.save(mirror);
        logger(mirrorId).error(errorMessage);
    }

    public void importFinished(Long mirrorId) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        if (mirror.getStatus() != MirrorStatus.ERROR) {
            // only update status if there was no error during import
            mirror.setStatus(MirrorStatus.SYNCED);
            mirror.setLastImportFinished(LocalDateTime.now());
        }
        mirrorRepository.save(mirror);
    }

    public void error(Long mirrorId) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        mirror.setStatus(MirrorStatus.ERROR);
        mirror.setSyncActive(false);
        mirrorRepository.save(mirror);
    }

    public void importStarted(Long mirrorId) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        if (mirror.getLastImportFinished() == null) {
            mirror.setStatus(MirrorStatus.INITIAL_IMPORT);
        } else {
            // do not update mirror status
        }
        mirrorRepository.save(mirror);
    }

    private Logger logger(Long mirrorId) {
        return importLoggerFactory.getImportLoggerForMirror(mirrorId, "gitanizer");
    }

}
