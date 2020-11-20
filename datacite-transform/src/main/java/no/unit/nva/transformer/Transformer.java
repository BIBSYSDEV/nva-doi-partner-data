package no.unit.nva.transformer;

import no.unit.nva.transformer.dto.CreatorDto;
import no.unit.nva.transformer.dto.DataCiteMetadataDto;
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
     * Transforms a DataCiteMetadataDto to a Datacite Record.
     * @param dataCiteMetadataDto A DataCiteMetadataDto instance.
     * @throws JAXBException If the XML initialisation fails.
     */
    public Transformer(DataCiteMetadataDto dataCiteMetadataDto) throws JAXBException {
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        this.resource = new Resource();
        fromDataCiteMetadataDto(dataCiteMetadataDto);
    }

    private static JAXBContext getContext() {
        try {
            return JAXBContext.newInstance(Resource.class);
        } catch (JAXBException e) {
            throw new RuntimeException(COULD_NOT_GET_JAXB_CONTEXT);
        }
    }

    private void fromDataCiteMetadataDto(DataCiteMetadataDto dataCiteMetadataDto) {
        setResourceIdentifier(dataCiteMetadataDto);
        setResourceCreators(dataCiteMetadataDto);
        setResourceTitle(dataCiteMetadataDto);
        setPublisher(dataCiteMetadataDto);
        setPublicationYear(dataCiteMetadataDto);
        setResourceType(dataCiteMetadataDto);
    }

    private void setResourceType(DataCiteMetadataDto dataCiteMetadataDto) {
        resource.setResourceType(dataCiteMetadataDto.getResourceType().toResourceType());
    }

    private void setPublicationYear(DataCiteMetadataDto dataCiteMetadataDto) {
        resource.setPublicationYear(dataCiteMetadataDto.getPublicationYear());
    }

    private void setPublisher(DataCiteMetadataDto dataCiteMetadataDto) {
        Publisher publisher = new Publisher();
        publisher.setValue(dataCiteMetadataDto.getPublisher().getValue());
        resource.setPublisher(publisher);
    }

    private void setResourceTitle(DataCiteMetadataDto dataCiteMetadataDto) {
        Title title = new Title();
        title.setValue(dataCiteMetadataDto.getTitle().getValue());
        Titles titles = new Titles();
        titles.getTitle().add(title);
        resource.setTitles(titles);
    }

    private void setResourceIdentifier(DataCiteMetadataDto dataCiteMetadataDto) {
        resource.setIdentifier(dataCiteMetadataDto.getIdentifier().asIdentifier());
    }

    private void setResourceCreators(DataCiteMetadataDto dataCiteMetadataDto) {
        var creators = new Resource.Creators();
        dataCiteMetadataDto.getCreator().stream()
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
