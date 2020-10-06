package handlers;

import static nva.commons.utils.attempt.Try.attempt;

import com.fasterxml.jackson.core.type.TypeReference;
import exceptions.EventException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import nva.commons.utils.JsonUtils;
import nva.commons.utils.attempt.Failure;
import nva.commons.utils.attempt.Try;


public class EventBridgeEvent<I> {

    private String account;
    private String region;
    private Map<String, Object> detail;
    private String detailType;
    private String source;
    private String id;
    private Instant time;
    private List<String> resources;

    public EventBridgeEvent() {
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

    public static <I> Builder newBuilder(EventBridgeEvent<I> copy) {
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Map<String, Object> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, Object> detail) {
        this.detail = detail;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        private Instant time;
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

        public Builder withTime(Instant time) {
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
