package org.enumus.samples.article.withenum;

import org.enumus.samples.article.IsoAlpha2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public enum Country {
    FRANCE("French Republic", null, "Emmanuel Macron", "Édouard Philippe", Collections.emptyList(), 33, IsoAlpha2.FR),
    USA("The United states of America", null, "Donald Trump", null, Arrays.asList(/*too long...*/), 1, IsoAlpha2.US),

    ENGLAND("England", "Elizabeth II", null, "Theresa May", Collections.emptyList(), 44, IsoAlpha2.GB),
    WELSH("England", "Elizabeth II", null, "Theresa May", Collections.emptyList(), 44, IsoAlpha2.GB),
    SCOTLAND("England", "Elizabeth II", null, "Nicola Sturgeon", Collections.emptyList(), 44, IsoAlpha2.GB),
    UNITED_KINDOM("The United Kingdom of Great Britain and Northern Ireland", "Elizabeth II", null, "Theresa May", Arrays.asList(ENGLAND, WELSH, SCOTLAND), 44, IsoAlpha2.GB),
    ;

    private final String name;
    private final String monarch;
    private final String president;
    private final String primeMinister;
    private final Collection<Country> federatedState;
    private final int countryCode;
    private final IsoAlpha2 iso;



    Country(String name, String monarch, String president, String primeMinister, Collection<Country> federatedState, int countryCode, IsoAlpha2 iso) {
        this.name = name;
        this.monarch = monarch;
        this.president = president;
        this.primeMinister = primeMinister;
        this.federatedState = federatedState;
        this.countryCode = countryCode;
        this.iso = iso;
    }
}
