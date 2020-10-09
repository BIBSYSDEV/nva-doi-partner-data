package no.unit.nva.transformer;

import no.unit.nva.transformer.dto.CreatorDto;
import no.unit.nva.transformer.dto.DynamoRecordDto;
import org.datacite.schema.kernel_4.Resource;
import org.datacite.schema.kernel_4.Resource.Publisher;
import org.datacite.schema.kernel_4.Resource.Titles;
import org.datacite.schema.kernel_4.Resource.Titles.Title;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.stream.Collectors;


public class Transformer {
    public static final String COULD_NOT_GET_JAXB_CONTEXT = "Could not get JAXBContext";
    private final Resource resource;

    private static final JAXBContext jaxbContext = getContext();

    private final Marshaller marshaller;

    /**
     * Transforms a DynamoDB record to a Datacite Record.
     * @param dynamoRecordDto A DynamoDB record.
     * @throws JAXBException If the XML initialisation fails.
     */
    public Transformer(DynamoRecordDto dynamoRecordDto) throws JAXBException {
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        this.resource = new Resource();
        fromDynamoRecord(dynamoRecordDto);
    }

    private static JAXBContext getContext() {
        try {
            return JAXBContext.newInstance(Resource.class);
        } catch (JAXBException e) {
            throw new RuntimeException(COULD_NOT_GET_JAXB_CONTEXT);
        }
    }

    private void fromDynamoRecord(DynamoRecordDto dynamoRecordDto) {
        setResourceIdentifier(dynamoRecordDto);
        setResourceCreators(dynamoRecordDto);
        setResourceTitle(dynamoRecordDto);
        setPublisher(dynamoRecordDto);
        setPublicationYear(dynamoRecordDto);
        setResourceType(dynamoRecordDto);
    }

    private void setResourceType(DynamoRecordDto dynamoRecordDto) {
        resource.setResourceType(dynamoRecordDto.getResourceType().toResourceType());
    }

    private void setPublicationYear(DynamoRecordDto dynamoRecordDto) {
        resource.setPublicationYear(dynamoRecordDto.getPublicationYear());
    }

    private void setPublisher(DynamoRecordDto dynamoRecordDto) {
        Publisher publisher = new Publisher();
        publisher.setValue(dynamoRecordDto.getPublisher().getValue());
        resource.setPublisher(publisher);
    }

    private void setResourceTitle(DynamoRecordDto dynamoRecordDto) {
        Title title = new Title();
        title.setValue(dynamoRecordDto.getTitle().getValue());
        Titles titles = new Titles();
        titles.getTitle().add(title);
        resource.setTitles(titles);
    }

    private void setResourceIdentifier(DynamoRecordDto dynamoRecordDto) {
        resource.setIdentifier(dynamoRecordDto.getIdentifier().asIdentifier());
    }

    private void setResourceCreators(DynamoRecordDto dynamoRecordDto) {
        var creators = new Resource.Creators();
        dynamoRecordDto.getCreator().stream()
                .map(CreatorDto::toCreator)
                .collect(Collectors.toList())
                .forEach(creator -> creators.getCreator().add(creator));
        resource.setCreators(creators);
    }

    /**
     * Produces an XML string representation of the Datacite record.
     * @return String XML.
     * @throws JAXBException If the record cannot be produced.
     */
    public String asXml() throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(resource, stringWriter);
        return stringWriter.toString();
    }
}
