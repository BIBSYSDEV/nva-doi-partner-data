package no.unit.nva.transformer;

import java.io.StringWriter;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import no.unit.nva.transformer.dto.CreatorDto;
import no.unit.nva.transformer.dto.DynamoRecordDto;
import org.datacite.schema.kernel_4.Resource;
import org.datacite.schema.kernel_4.Resource.Publisher;
import org.datacite.schema.kernel_4.Resource.Titles;
import org.datacite.schema.kernel_4.Resource.Titles.Title;

public class Transformer {

    private final static JAXBContext jaxbContext = getJaxbContext();
    private final Resource resource;
    private final DynamoRecordDto dynamoRecordDto;
    private final Marshaller marshaller;

    /**
     * Transforms a DynamoDB record to a Datacite Record.
     *
     * @param dynamoRecordDto A DynamoDB record.
     * @throws JAXBException If the XML initialisation fails.
     */
    public Transformer(DynamoRecordDto dynamoRecordDto) throws JAXBException {
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        this.dynamoRecordDto = dynamoRecordDto;
        this.resource = new Resource();
        fromDynamoRecord();
    }

    /**
     * Produces an XML string representation of the Datacite record.
     *
     * @return String XML.
     * @throws JAXBException If the record cannot be produced.
     */
    public String asXml() throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(resource, stringWriter);
        return stringWriter.toString();
    }

    private static JAXBContext getJaxbContext() {
        try {
            return JAXBContext.newInstance(Resource.class);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private void fromDynamoRecord() {
        setResourceIdentifier();
        setResourceCreators();
        setResourceTitle();
        setPublisher();
        setPublicationYear();
        setResourceType();
    }

    private void setResourceType() {
        resource.setResourceType(dynamoRecordDto.getResourceType().toResourceType());
    }

    private void setPublicationYear() {
        resource.setPublicationYear(dynamoRecordDto.getPublicationYear());
    }

    private void setPublisher() {
        Publisher publisher = new Publisher();
        publisher.setValue(dynamoRecordDto.getPublisher().getValue());
        resource.setPublisher(publisher);
    }

    private void setResourceTitle() {
        Title title = new Title();
        title.setValue(dynamoRecordDto.getTitle().getValue());
        Titles titles = new Titles();
        titles.getTitle().add(title);
        resource.setTitles(titles);
    }

    private void setResourceIdentifier() {
        resource.setIdentifier(dynamoRecordDto.getIdentifier().asIdentifier());
    }

    private void setResourceCreators() {
        var creators = new Resource.Creators();
        dynamoRecordDto.getCreator().stream()
            .map(CreatorDto::toCreator)
            .collect(Collectors.toList())
            .forEach(creator -> creators.getCreator().add(creator));
        resource.setCreators(creators);
    }
}
