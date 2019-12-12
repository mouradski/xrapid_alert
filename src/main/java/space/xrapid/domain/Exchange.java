package space.xrapid.domain;

import java.util.Arrays;

import static space.xrapid.domain.Currency.*;

public enum Exchange {

    BITSTAMP("bitstamp", true, USD,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
            "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
            "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD",
            "rPEPPER7kfTD9w2To4CQk6UCfuHM9c6GDY"),

    BITSTAMP_EUR("bitstamp", false, EUR,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
            "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
            "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD",
            "rPEPPER7kfTD9w2To4CQk6UCfuHM9c6GDY"),

    BITSO("bitso",true, MXN,
            "rG6FZ31hDHN1K5Dkbma3PSB5uVCuVVRzfn",
            "rHZaDC6tsGN2JWGeXhjKL6664RNCq5hu4B",
            "raXLsnnJVaLMDixEoXHXe56WQXKczbD8ub",
            "rGfGdVYLDSbji5mqfMvdpx4c8JyyfqVFgf",
            "rfEu1Wnr7LxStoFx8DBdzgr8M16FBUbH3K",
            "rLSn6Z3T8uCxbcd1oxwfGQN1Fdn5CyGujK"),

    BX_IN("bx.in.th", false, BAHT, "rp7Fq2NQVRJxQJvUZ4o8ZzsTSocvgYoBbs"),

    SBI_VC("sbi_vc", true, YEN,
            "rHtqTcp3SvjxbhpTYMFTUWX5B1mny9KWeE",
            "rwB2UA47taZQCfi4po3tw8vwBpt6Y6eioZ",
            "rJLEjF9wbrHU2H5YSJKn6S7hyyq8gne7su",
            "rEFtdHuyxgUjDL4t3gBsesQwHtnDy2W8rC",
            "r4dYaiK2rcV9ypXZjwvwH6uLoeCTrUPceW",
            "rMEFmh7nvypDmk6xJ3xcuqhYhKQGtXEmyK",
            "rN5wo76mqvdNvShMbi5zwsnHWm2KVaqW4m",
            "rLzFjyD1gYJx3XQc1tEj9pNa4DD1SY6DeR",
            "r9x4D7c2nfa3UJefLR4fpN31zZQvByHSLQ"),



    COINFIELD_USD("coinfield", false, USD, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_CAD("coinfield", false, CAD, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_JPY("coinfield", false, JPY, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_GBP("coinfield", false, GBP, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_EUR("coinfield", false, GBP, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_AED("coinfield", false, AED, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),

    BINANCE_RUB("binance", false, RUB, "rEb8TK3gBgk5auZkwc6sHnwrGVJH8DuaLh", "rJb5KsHsDHF1YS5B5DU6QCkH5NsPaKQTcy", "rEy8TFcrAPvhpKrwyrscNYyqBGUkE9hKaJ"),

    WAZIRX("Wazirx", false, INR, "rwuAm7XdcP3SBwgJrVthCvCzU7kETJUUit", "\trJXcrnAS8XoBwjvd5VrShrLMY8buPuiuC5"),

    KRAKEN_USD("kraken", false, USD, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_EUR("kraken", false, EUR, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_JPY("kraken", false, JPY, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_CAD("kraken", false, CAD, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),

    COIN_PH("coin.ph", true, PHP, "rU2mEJSLqBRkYLVTv55rFTgQajkLTnT6mA"),

    COINBENE("coinbene", true, BRL, "r9CcrhpV7kMcTu1SosKaY8Pq9g5XiiHLvS"),

    MERCADO("mercado", true, BRL, "rnW8je5SsuFjkMSWkgfXvqZH3gLTpXxfFH", "rHLndqCyNeEKY2PoDmSvUf5hVX5mgUZteB"),

    BRAZILIEX("braziliex", true, BRL,
            "__UNKNOW__"),

    BITCOIN_TRADE("bitcoin_trade", true, BRL, "r4ZQiz7r4vnM6tAMLu1NhxcDa7TNMdFLGt"),


    BITREX("bitrex", false, USD,
            "_TO_DELETE_"),

    EXMO("exmo", true, UAH, "rUocf1ixKzTuEe34kmVhRvGqNCofY1NJzV", "rUCjhpLHCcuwL1oyQfzPVeWHsjZHaZS6t2", "rsTv5cJK2EMJhYqUUni4sYBonVk7KqTxZg", "rLJPjRYGDVVEjv4VrJtouzqzyJ51YtdZKY"),

    BITTREX("bitrex", true, USD,
            "rPVMhWBsfF9iMXYj3aAzJVkPDTFNSyWdKy"),

    DCEX("dcex", false, USD, "r9W22DnkmktvdSdsdWS5CXJAxfWVRtbDD9", "rHXvKUCTzsu2CB8Y5tydaG7B2ABc4CCBYz"),

    BTC_MARKETS("btc_market", true, AUD, "r94JFtstbXmyG21h3RHKcNfkAHxAQ6HSGC", "rL3ggCUKaiR1iywkGW6PACbn3Y8g5edWiY", "rU7xJs7QmjbiyxpEozNYUFQxaRD5kueY7z"),

    BITKUB("bitkub", true, BAHT, "rpXTzCuXtjiPDFysxq8uNmtZBe9Xo97JbW");


    private String name;
    private Currency localFiat;
    private String[] addresses;
    private boolean confirmed;

    Exchange(String name, boolean confirmed, Currency localFiat, String... addresses) {
        this.name = name;
        this.addresses = addresses;
        this.localFiat = localFiat;
        this.confirmed = confirmed;
    }

    public static Exchange byAddress(String address) {
        return Arrays.stream(Exchange.values())
                .filter(adr -> Arrays.asList(adr.addresses).contains(address)).findAny().orElse(null);
    }

    public static Exchange byAddress(String address, Currency fiat) {
        if (fiat == null) {
            return byAddress(address);
        } else {
            return Arrays.stream(Exchange.values())
                    .filter(exchange -> Arrays.asList(exchange.addresses).contains(address))
                    .filter(exchange -> exchange.getLocalFiat().equals(fiat))
                    .findAny().orElse(null);
        }
    }

    public String getName() {
        return name;
    }

    public Currency getLocalFiat() {
        return localFiat;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public boolean isConfirmed() {return confirmed;}
}
