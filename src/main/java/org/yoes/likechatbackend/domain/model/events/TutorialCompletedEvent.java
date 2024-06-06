package org.yoes.likechatbackend.domain.model.events;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public final class TutorialCompletedEvent extends ApplicationEvent {

    private final Long enrollmentId;

    private final Long tutorialId;

    public TutorialCompletedEvent(Object source, Long enrollmentId, Long tutorialId) {
        super(source);
        this.enrollmentId = enrollmentId;
        this.tutorialId = tutorialId;
    }
}
