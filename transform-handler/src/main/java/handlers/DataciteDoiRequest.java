package handlers;

import java.net.URI;
import nva.commons.utils.JsonUtils;

public class DataciteDoiRequest {

    private URI publicationId;
    private URI existingDoi;
    private String xml;
    private String type;

    public DataciteDoiRequest() {
    }

    private DataciteDoiRequest(Builder builder) {
        setPublicationId(builder.publicationId);
        setExistingDoi(builder.existingDoi);
        setXml(builder.xml);
        setType(builder.type);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(DataciteDoiRequest copy) {
        Builder builder = new Builder();
        builder.publicationId = copy.getPublicationId();
        builder.existingDoi = copy.getExistingDoi();
        builder.xml = copy.getXml();
        builder.type = copy.getType();
        return builder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public URI getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(URI publicationId) {
        this.publicationId = publicationId;
    }

    public URI getExistingDoi() {
        return existingDoi;
    }

    public void setExistingDoi(URI existingDoi) {
        this.existingDoi = existingDoi;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final class Builder {

        private URI publicationId;
        private URI existingDoi;
        private String xml;
        private String type;

        private Builder() {
        }

        public Builder withPublicationId(URI publicationId) {
            this.publicationId = publicationId;
            return this;
        }

        public Builder withExistingDoi(URI existingDoi) {
            this.existingDoi = existingDoi;
            return this;
        }

        public Builder withXml(String xml) {
            this.xml = xml;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public DataciteDoiRequest build() {
            return new DataciteDoiRequest(this);
        }
    }
}
