package org.enumus.samples.article.withoutenum;

import org.enumus.samples.article.IsoAlpha2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Country {
    private final String name;
    private final String monarch;
    private final String president;
    private final String primeMinister;
    private final Collection<Country> federatedState;
    private final int countryCode;
    private final IsoAlpha2 iso;


    public Country(String name, String monarch, String president, String primeMinister, Collection<Country> federatedState, int countryCode, IsoAlpha2 iso) {
        this.name = name;
        this.monarch = monarch;
        this.president = president;
        this.primeMinister = primeMinister;
        this.federatedState = federatedState;
        this.countryCode = countryCode;
        this.iso = iso;
    }

    public static  CountryBuilder builder() {
        return new CountryBuilder();
    }

    public static class CountryBuilder {
        private String name;
        private String monarch;
        private String president;
        private String primeMinister;
        private Collection<Country> federatedState = new ArrayList<>();
        private int countryCode;
        private IsoAlpha2 iso;

        public CountryBuilder named(String name) {
            this.name = name;
            return this;
        }

        public CountryBuilder ruledBy(String monarch) {
            this.monarch = monarch;
            return this;
        }

        public CountryBuilder leadBy(String president) {
            this.president = president;
            return this;
        }

        public CountryBuilder govermentHeadedBy(String primeMinister) {
            this.primeMinister = primeMinister;
            return this;
        }

        public CountryBuilder withPhoneCode(int countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public CountryBuilder federates(Country part) {
            federatedState.add(part);
            return this;
        }

        public CountryBuilder withIso(IsoAlpha2 iso) {
            this.iso = iso;
            return this;
        }

        public Country build(){
            return new Country(name, monarch, president,primeMinister, federatedState, countryCode, iso);
        }
    }


    public static void main(String[] args ) {

    }

    private static void callConstructor() {
        Country gb = new Country("The United Kingdom of Great Britain and Northern Ireland", "Elizabeth II", null, "Theresa May",
                Arrays.asList(
                        new Country("England", "Elizabeth II", null, "Theresa May", null, 44, IsoAlpha2.GB),
                        new Country("Welsh", "Elizabeth II", null, "Theresa May", null, 44, IsoAlpha2.GB),
                        new Country("Scotland", "Elizabeth II", null, "Nicola Sturgeon", null, 44, IsoAlpha2.GB)
                ),
                44, IsoAlpha2.GB
        );
        Country usa = new Country("The United states of America", null, "Donald Trump", null, Arrays.asList(/*too long...*/), 1, IsoAlpha2.US);
        Country france = new Country("French Republic", null, "Emmanuel Macron", "Édouard Philippe", Collections.emptyList(), 33, IsoAlpha2.FR);
    }

    private static void callBuilder() {
        Country gb = Country.builder().named("The United Kingdom of Great Britain and Northern Ireland").ruledBy("Elizabeth II").govermentHeadedBy("Theresa May")
                .federates(Country.builder().named("England").ruledBy("Elizabeth II").govermentHeadedBy("Theresa May").withPhoneCode(44).build())
                .federates(Country.builder().named("Welsh").ruledBy("Elizabeth II").govermentHeadedBy("Theresa May").withPhoneCode(44).build())
                .federates(Country.builder().named("Scotland").ruledBy("Elizabeth II").govermentHeadedBy("Nicola Sturgeon").withPhoneCode(44).build())
                .build();

        Country usa = Country.builder().named("The United states of America").leadBy("Donald Trump").withPhoneCode(1).build();
        Country france = Country.builder().named("The United states of America").govermentHeadedBy("Édouard Philippe").leadBy("Emmanuel Macron").withPhoneCode(33).build();
    }
}
