package space.xrapid.domain;

import java.util.Arrays;

import static space.xrapid.domain.Currency.*;

public enum Exchange {

    BITSTAMP("bitstamp", USD,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
            "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
            "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD"),

    BITSO("bitso", MXN,
            "rG6FZ31hDHN1K5Dkbma3PSB5uVCuVVRzfn",
            "rHZaDC6tsGN2JWGeXhjKL6664RNCq5hu4B",
            "raXLsnnJVaLMDixEoXHXe56WQXKczbD8ub",
            "rGfGdVYLDSbji5mqfMvdpx4c8JyyfqVFgf",
            "rfEu1Wnr7LxStoFx8DBdzgr8M16FBUbH3K",
            "rLSn6Z3T8uCxbcd1oxwfGQN1Fdn5CyGujK"),


    SBI_VC("sbi_vc", null,
            "rHtqTcp3SvjxbhpTYMFTUWX5B1mny9KWeE",
            "rwB2UA47taZQCfi4po3tw8vwBpt6Y6eioZ",
            "rJLEjF9wbrHU2H5YSJKn6S7hyyq8gne7su",
            "rEFtdHuyxgUjDL4t3gBsesQwHtnDy2W8rC",
            "r4dYaiK2rcV9ypXZjwvwH6uLoeCTrUPceW",
            "rMEFmh7nvypDmk6xJ3xcuqhYhKQGtXEmyK",
            "rN5wo76mqvdNvShMbi5zwsnHWm2KVaqW4m",
            "rLzFjyD1gYJx3XQc1tEj9pNa4DD1SY6DeR",
            "r9x4D7c2nfa3UJefLR4fpN31zZQvByHSLQ"),

    COIN_PH("coin.ph", PHP, "rU2mEJSLqBRkYLVTv55rFTgQajkLTnT6mA"),


    BINANCE("binance", null, "rEb8TK3gBgk5auZkwc6sHnwrGVJH8DuaLh");

//    MCX("mcx", null, "rs2dgzYeqYqsk8bvkQR5YPyqsXYcA24MP2"),
//
//    KUCOIN("kucoin", null,
//            "rUW9toSjQkLY6EspdnBJP2paG4hWKmNbMh",
//            "r4pxv9txfQTyVkQUetNmyJqDy7s5fPPVfB",
//            "rhKrvXY8zRvVyDLT4nfXB86HyhqQyvcr4c"),
//
//
//    UPBIT("upbit", null,
//            "rNzT5xopUaJK8L7mHbThUGKaHoct62dNBC",
//            "rN9qNpgnBaZwqCg8CvUZRPqCcPPY7wfWep",
//            "rJYQKfsTi8XCLZ1vGSf9CxiL4prz5bvbtz",
//            "rBszWJzYpNoqoY4xKuGUpN23b6EBT41ocF"),
//
//
//    CATEX("catex", null, "rp953pKrFqc8NLa2z5dqcpgh6z4ZdBskPp"),
//
//    ZB("zb", null,
//            "r4hYwnBsNH764iTfhRSopu4M3Ct5gMkCGD",
//            "rKm6XqTFgJUpwwHhuXoPdGPtcbEhtj3Sza"),
//
//    BITRUE("bitrue", null, "raLPjTYeGezfdb6crXZzcC8RkLBEwbBHJ5"),
//
//    KRAKEN("kraken", null, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
//
//    BITREX("bitrex", null,
//            "rPVMhWBsfF9iMXYj3aAzJVkPDTFNSyWdKy"),
//
//    BITFINEX("bitfinex", null,
//            "rLW9gnQo7BQhU6igk5keqYnH3TVrCxGRzm",
//            "rE3hWEGquaixF2XwirNbA1ds4m55LxNZPk"),
//
//
//    COINBASE("coinbase", null,
//            "rLNaPoKeeBjZe2qs6x52yVPZpZ8td4dc6w",
//            "rw2ciyaNshpHe7bCHo4bRWq6pqqynnWKQg");

    private String name;
    private Currency localFiat;
    private String[] addresses;

    Exchange(String name, Currency localFiat, String... addresses) {
        this.name = name;
        this.addresses = addresses;
        this.localFiat = localFiat;
    }

    public static Exchange byAddress(String address) {
        return Arrays.stream(Exchange.values())
                .filter(adr -> Arrays.asList(adr.addresses).contains(address)).findAny().orElse(null);
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
}
