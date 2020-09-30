package no.unit.nva.transformer;

import no.unit.nva.transformer.dto.CreatorDto;
import no.unit.nva.transformer.dto.DynamoRecordDto;
import no.unit.nva.transformer.dto.IdentifierDto;
import no.unit.nva.transformer.dto.PublisherDto;
import no.unit.nva.transformer.dto.ResourceTypeDto;
import no.unit.nva.transformer.dto.TitleDto;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.util.List;

import static no.unit.nva.transformer.dto.CreatorDto.SEPARATOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

class TransformerTest {

    public static final String ANY_URI = "https://example.org/123";
    public static final String ANY_NAME = "Wallace, Cornelius";
    public static final String ANY_TITLE = "A long, slow depressing march";
    public static final String ANY_YEAR = "2007";
    public static final String ANY_PUBLISHER = "Hubert's Æsopian university";
    public static final String ANY_RESOURCE_TYPE = "Article";
    public static final String RESOURCE_TYPE_OTHER = "Other";

    @Test
    void transformerTransformsDynamoRecordDtoWhenInputIsValid() throws JAXBException {
        var record = generateRecord(ANY_URI, ANY_NAME, ANY_TITLE, ANY_YEAR,
                ANY_PUBLISHER, ANY_RESOURCE_TYPE);
        var actual = new Transformer(record).asXml();

        assertThat(actual, containsString(ANY_URI));
        assertThat(actual, containsString(ANY_NAME));
        assertThat(actual, containsString(ANY_TITLE));
        assertThat(actual, containsString(ANY_YEAR));
        assertThat(actual, containsString(ANY_PUBLISHER));
        assertThat(actual, containsString(ANY_RESOURCE_TYPE));
    }

    @Test
    void transformerReturnsXmlWithSplitName() throws JAXBException {
        var surname = "Higgs";
        var forename = "Boson";
        var name = String.join(SEPARATOR, surname, forename);
        var record = generateRecord(ANY_URI, name, ANY_TITLE, ANY_YEAR,
                ANY_PUBLISHER, ANY_RESOURCE_TYPE);
        var actual = new Transformer(record).asXml();
        assertThat(actual, containsString(name));
        assertThat(actual, containsString(enclosedString(surname)));
        assertThat(actual, containsString(enclosedString(forename)));
    }

    @Test
    void transformerReturnsXmlWithoutSplitName() throws JAXBException {
        String rank = "Bosun";
        String surname = "Higgs";

        var name = rank + " " + surname;
        var record = generateRecord(ANY_URI, name, ANY_TITLE, ANY_YEAR,
                ANY_PUBLISHER, ANY_RESOURCE_TYPE);
        var actual = new Transformer(record).asXml();
        assertThat(actual, containsString(name));
        assertThat(actual, not(containsString(enclosedString(rank))));
        assertThat(actual, not(containsString(enclosedString(surname))));
    }

    @Test
    void transformerReturnsXmlWithResourceTypeGeneralOtherWhenResourceTypeIsNull() throws JAXBException {
        var record = generateRecord(ANY_URI, ANY_NAME, ANY_TITLE, ANY_YEAR,
                ANY_PUBLISHER, null);
        var actual = new Transformer(record).asXml();
        assertThat(actual, containsString(RESOURCE_TYPE_OTHER));
    }

    private String enclosedString(String string) {
        return ">" + string + "<";
    }

    private DynamoRecordDto generateRecord(String identifier,
                                           String creator,
                                           String title,
                                           String year,
                                           String publisher,
                                           String resourceType) {
        return new DynamoRecordDto.Builder()
                .withIdentifier(getIdentifier(identifier))
                .withCreator(getCreator(creator))
                .withTitle(getTitle(title))
                .withPublicationYear(year)
                .withPublisher(getPublisher(publisher))
                .withResourceType(getResourceType(resourceType))
                .build();
    }

    private ResourceTypeDto getResourceType(String resourceType) {
        return new ResourceTypeDto.Builder()
                .withValue(resourceType)
                .build();
    }

    private PublisherDto getPublisher(String publisher) {
        return new PublisherDto.Builder()
                .withValue(publisher)
                .build();
    }

    private TitleDto getTitle(String title) {
        return new TitleDto.Builder()
                .withValue(title)
                .build();
    }

    private IdentifierDto getIdentifier(String identifier) {
        return new IdentifierDto.Builder()
                .withValue(identifier)
                .build();
    }

    private List<CreatorDto> getCreator(String name) {
        return List.of(new CreatorDto.Builder()
                .withCreatorName(name).build());
    }
}