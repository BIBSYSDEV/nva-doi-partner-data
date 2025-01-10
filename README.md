# nva-doi-partner-data

A Java API for creation of Datacite metadata (DataciteXML).

## Usage

Add JitPack repository to your build, e.g. for gradle:

```
    repositories {
        maven { url 'https://jitpack.io' }
    }
```

Add the dependency to your code, e.g. for gradle:

```
    dependencies {
        implementation 'com.github.BIBSYSDEV:nva-doi-partner-data:Tag'
    }
```

Code usage:

```
    var creator = new CreatorDto.Builder()
                   .withCreatorName("Name Nameson")
                   .build();
    var identifier = new IdentifierDto.Builder().withType(IdentifierType.DOI).withValue("10.NNN/yyyyy).build();
    var alternateIdentifiers = List.of(new AlternateIdentifierDto.Builder()
                           .withValue("OtherIdentifier")
                           .build());
    var publisher = new PublisherDto.Builder().withValue("Some publisher name").build();
    var title = new TitleDto.Builder().withValue("Some title").build();
    var resourceType = new ResourceTypeDto.Builder()
                   .withValue("Some resource type")
                   .build();
    var xml = new DataCiteMetadataDto.Builder()
                   .withCreator(creator)
                   .withIdentifier(identifier)
                   .withAlternateIdentifiers(alternateIdentifiers)
                   .withPublicationYear("2022")
                   .withPublisher(publisher)
                   .withTitle(title)
                   .withResourceType(resourceType)
                   .build()
                   .asXml();
```

## Limitations

The ResourceType is mapped either to Text or Other based on the presence of a textual value (Text) or null (Other).
