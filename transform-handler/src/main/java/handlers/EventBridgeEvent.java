package handlers;

import static nva.commons.utils.attempt.Try.attempt;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import exceptions.EventException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import nva.commons.utils.JsonUtils;
import nva.commons.utils.attempt.Failure;
import nva.commons.utils.attempt.Try;
import org.joda.time.DateTime;

/**
 * { "version": "0", "id": "04672dec-d61e-d8cb-de6b-955b0a837d91", "detail-type": "Scheduled Event", "source":
 * "aws.events", "account": "884807050265", "time": "2020-10-05T11:07:57Z", "region": "eu-west-1", "resources": [
 * "arn:aws:events:eu-west-1:884807050265:rule/testRule" ], "detail": {} }
 */
public class EventBridgeEvent<I> extends ScheduledEvent {

    public EventBridgeEvent(){
        super();
    }
    private EventBridgeEvent(Builder builder) {
        super();
        setAccount(builder.account);
        setRegion(builder.region);
        setDetail(builder.detail);
        setDetailType(builder.detailType);
        setSource(builder.source);
        setId(builder.id);
        setTime(builder.time);
        setResources(builder.resources);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(EventBridgeEvent copy) {
        Builder builder = new Builder();
        builder.account = copy.getAccount();
        builder.region = copy.getRegion();
        builder.detail = copy.getDetail();
        builder.detailType = copy.getDetailType();
        builder.source = copy.getSource();
        builder.id = copy.getId();
        builder.time = copy.getTime();
        builder.resources = copy.getResources();
        return builder;
    }

    public I getDetailsObject() throws EventException {
        TypeReference<I> typeReference = new TypeReference<>() {};
        return Optional.ofNullable(getDetail())
            .map(attempt(detail -> JsonUtils.objectMapper.convertValue(getDetail(), typeReference)))
            .orElse(Try.of(null))
            .orElseThrow(this::handleParsingException);
    }

    private EventException handleParsingException(Failure<I> failure) {
        return new EventException(failure.getException());
    }

    public static final class Builder {

        private String account;
        private String region;
        private Map<String, Object> detail;
        private String detailType;
        private String source;
        private String id;
        private DateTime time;
        private List<String> resources;

        private Builder() {
        }

        public Builder withAccount(String account) {
            this.account = account;
            return this;
        }

        public Builder withRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder withDetail(Map<String, Object> detail) {
            this.detail = detail;
            return this;
        }

        public <I> Builder withDetail(I object) {
            TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {};
            this.detail = JsonUtils.objectMapper.convertValue(object, typeReference);
            return this;
        }

        public Builder withDetailType(String detailType) {
            this.detailType = detailType;
            return this;
        }

        public Builder withSource(String source) {
            this.source = source;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTime(DateTime time) {
            this.time = time;
            return this;
        }

        public Builder withResources(List<String> resources) {
            this.resources = resources;
            return this;
        }

        public EventBridgeEvent build() {
            return new EventBridgeEvent(this);
        }
    }
}
