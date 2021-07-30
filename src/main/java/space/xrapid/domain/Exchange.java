package space.xrapid.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static space.xrapid.domain.Currency.*;

public enum Exchange {

    BITSTAMP("bitstamp", true, USD,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1", "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv", "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD","rnBbetfwFS7A7eb5HVyF5kxmvX5AxDUJZM",
            "rnSnmKhNKzGMjzFZDUR6KuZWLhUgJkCeCL", "r4fY8nAuV11zx4r8Fau9Fqbh3XXtymBHZA",
            "rnpGA8Lf5RLvZfVuo8b168n4RabQjyUuhK", "rUC8XGzh9W6uzn45GJVAoow7TxCMxmnLrq",
            "rafsKfCiaQ17AFiQYmgg1CRMHty8ge8tVB", "rG2bzZ2Q9JcpPeCyqXTQts6jHSYsX21G6a",
            "rnuDDzvYWTPqXTDVvwE9oLGLgxzV7Rpnpe", "r3JVRdvRWpM4c7N7dug4RTC7XD3cJF413R",
            "rK4CYpr34YzzuizUnEz3SAKuH66Wb2iLEr", "rPFWrwunGtQhVrcpn58D5XVyxwafjumwwt",
            "rEXmdJZRfjXN3XGVdz99dGSZpQyJqUeirE", "r3UfhuRaxjwGEibXZ9mRWaKMJqtSsWcP14"),

    BITSTAMP_EUR("bitstamp", true, EUR,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1", "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv", "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD","rnBbetfwFS7A7eb5HVyF5kxmvX5AxDUJZM",
            "rnSnmKhNKzGMjzFZDUR6KuZWLhUgJkCeCL", "r4fY8nAuV11zx4r8Fau9Fqbh3XXtymBHZA",
            "rnpGA8Lf5RLvZfVuo8b168n4RabQjyUuhK", "rUC8XGzh9W6uzn45GJVAoow7TxCMxmnLrq",
            "rafsKfCiaQ17AFiQYmgg1CRMHty8ge8tVB", "rG2bzZ2Q9JcpPeCyqXTQts6jHSYsX21G6a",
            "rnuDDzvYWTPqXTDVvwE9oLGLgxzV7Rpnpe", "r3JVRdvRWpM4c7N7dug4RTC7XD3cJF413R",
            "rK4CYpr34YzzuizUnEz3SAKuH66Wb2iLEr", "rPFWrwunGtQhVrcpn58D5XVyxwafjumwwt",
            "rEXmdJZRfjXN3XGVdz99dGSZpQyJqUeirE", "r3UfhuRaxjwGEibXZ9mRWaKMJqtSsWcP14"),

    BITSTAMP_GBP("bitstamp", true, GBP,
            "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1", "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
            "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv", "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
            "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD","rnBbetfwFS7A7eb5HVyF5kxmvX5AxDUJZM",
            "rnSnmKhNKzGMjzFZDUR6KuZWLhUgJkCeCL", "r4fY8nAuV11zx4r8Fau9Fqbh3XXtymBHZA",
            "rnpGA8Lf5RLvZfVuo8b168n4RabQjyUuhK", "rUC8XGzh9W6uzn45GJVAoow7TxCMxmnLrq",
            "rafsKfCiaQ17AFiQYmgg1CRMHty8ge8tVB", "rG2bzZ2Q9JcpPeCyqXTQts6jHSYsX21G6a",
            "rnuDDzvYWTPqXTDVvwE9oLGLgxzV7Rpnpe", "r3JVRdvRWpM4c7N7dug4RTC7XD3cJF413R",
            "rK4CYpr34YzzuizUnEz3SAKuH66Wb2iLEr", "rPFWrwunGtQhVrcpn58D5XVyxwafjumwwt",
            "rEXmdJZRfjXN3XGVdz99dGSZpQyJqUeirE", "r3UfhuRaxjwGEibXZ9mRWaKMJqtSsWcP14"),

    BITSO("bitso", true, MXN,
            "rG6FZ31hDHN1K5Dkbma3PSB5uVCuVVRzfn", "rwApWND4wwXXiYRBvTdrW981YssreQcRdw",
            "rHZaDC6tsGN2JWGeXhjKL6664RNCq5hu4B", "raXLsnnJVaLMDixEoXHXe56WQXKczbD8ub",
            "rGfGdVYLDSbji5mqfMvdpx4c8JyyfqVFgf", "rfEu1Wnr7LxStoFx8DBdzgr8M16FBUbH3K",
            "rLSn6Z3T8uCxbcd1oxwfGQN1Fdn5CyGujK"),

    BITSO_USD("bitso", true, USD,
            "rG6FZ31hDHN1K5Dkbma3PSB5uVCuVVRzfn", "rwApWND4wwXXiYRBvTdrW981YssreQcRdw",
            "rHZaDC6tsGN2JWGeXhjKL6664RNCq5hu4B", "raXLsnnJVaLMDixEoXHXe56WQXKczbD8ub",
            "rGfGdVYLDSbji5mqfMvdpx4c8JyyfqVFgf", "rfEu1Wnr7LxStoFx8DBdzgr8M16FBUbH3K",
            "rLSn6Z3T8uCxbcd1oxwfGQN1Fdn5CyGujK"),

    VALR_ZAR("valr", true, ZAR,
            "rfrnxmLBiXHj38a2ZUDNzbks3y6yd3wJnV", "rDseVXFK1SkWhFH65cqAxf3HmvHCF6b94t"),

    BITFINEX("bitfinex", true, USD, "rLW9gnQo7BQhU6igk5keqYnH3TVrCxGRzm",
            "rE3hWEGquaixF2XwirNbA1ds4m55LxNZPk", "rB3oRqA6r278XTxDrkfSu1UqBVJnTBqwoE"),

    BTC_TURK("btcturk", true, TRY,
            "rNEygqkMv4Vnj8M2eWnYT1TDnV1Sc1X5SN", "rGmmsmyspyPsuBT4L5pLvAqTjxYqaFq4U5",
            "ra81Ro5RX3Wu84j1v6UufevuzwJpQbGJR6"),

    BTC_TURK_USD("btcturk", true, USD,
            "rNEygqkMv4Vnj8M2eWnYT1TDnV1Sc1X5SN", "rGmmsmyspyPsuBT4L5pLvAqTjxYqaFq4U5",
            "ra81Ro5RX3Wu84j1v6UufevuzwJpQbGJR6"),

    BX_IN("bx.in.th", false, THB, "rp7Fq2NQVRJxQJvUZ4o8ZzsTSocvgYoBbs"),

    SBI_TRADE("sbi_trade", true, JPY,
            "rDDyH5nfvozKZQCwiBrWfcE528sWsBPWET", "rKcVYzVK1f4PhRFjLhWP7QmteG5FpPgRub",
            "rJLEjF9wbrHU2H5YSJKn6S7hyyq8gne7su", "rLzFjyD1gYJx3XQc1tEj9pNa4DD1SY6DeR",
            "rN5wo76mqvdNvShMbi5zwsnHWm2KVaqW4m", "rPuRWNv3V3NNUHjRmb2hjGzd18iJFiqqBQ",
            "r4dYaiK2rcV9ypXZjwvwH6uLoeCTrUPceW", "rwB2UA47taZQCfi4po3tw8vwBpt6Y6eioZ",
            "rhVdvJawQ9KfAkbgYJYCKJfYZAKgZ5JyFp", "rBaarjfTdR5SUetZJNP1CKBosgrei8FBq9",
            "r9x4D7c2nfa3UJefLR4fpN31zZQvByHSLQ", "rEFtdHuyxgUjDL4t3gBsesQwHtnDy2W8rC",
            "rFsU3oEky7kEkVybKamDDRwTGaibwe9Be", "rHtqTcp3SvjxbhpTYMFTUWX5B1mny9KWeE",
            "rMEFmh7nvypDmk6xJ3xcuqhYhKQGtXEmyK", "rHBC8fxeQm9e3HbUYxXRqHvbwmjVhKsTqv",
            "rLhfNZZBm621C6fmJFTbdJaRuYrQy1mMER"),

    CEX_IO("cex.io", false, USD,
            "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1", "r3nhmcVpaP29ughoVFhrWpZ9Bqjzji4sct"),
    CEX_IO_EUR("cex.io", false, EUR,
            "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1", "r3nhmcVpaP29ughoVFhrWpZ9Bqjzji4sct"),
    CEX_IO_GBP("cex.io", true, GBP,
            "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1", "r3nhmcVpaP29ughoVFhrWpZ9Bqjzji4sct"),

    LUNO("luno", true, ZAR,
            "rsRy14FvipgqudiGmptJBhr1RtpsgfzKMM", "rsbfd5ZYWqy6XXf6hndPbRjDAzfmWc1CeQ"),

    LUNO_NGN("luno", true, NGN,
            "rsRy14FvipgqudiGmptJBhr1RtpsgfzKMM", "rsbfd5ZYWqy6XXf6hndPbRjDAzfmWc1CeQ"),

    LUNO_MYR("luno", true, MYR,
            "rsRy14FvipgqudiGmptJBhr1RtpsgfzKMM", "rsbfd5ZYWqy6XXf6hndPbRjDAzfmWc1CeQ"),


    COINFIELD_USD("coinfield", false, USD, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_GBP("coinfield", false, GBP, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_AED("coinfield", false, AED, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_JPY("coinfield", false, JPY, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_CAD("coinfield", false, CAD, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
    COINFIELD_EUR("coinfield", false, EUR, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),


    BINANCE_RUB("binance", false, RUB, "rEb8TK3gBgk5auZkwc6sHnwrGVJH8DuaLh",
            "rJb5KsHsDHF1YS5B5DU6QCkH5NsPaKQTcy", "rEy8TFcrAPvhpKrwyrscNYyqBGUkE9hKaJ"),
    BINANCE_US("binanceus", false, USD, "rEeEWeP88cpKUddKk37B2EZeiHBGiBXY3",
            "rMvYS27SYs5dXdFsUgpvv1CSrPsCz7ePF5"),

    WAZIRX("Wazirx", false, INR, "rwuAm7XdcP3SBwgJrVthCvCzU7kETJUUit",
            "rJXcrnAS8XoBwjvd5VrShrLMY8buPuiuC5"),

    KRAKEN_USD("kraken", false, USD, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_EUR("kraken", false, EUR, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_JPY("kraken", false, JPY, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
    KRAKEN_CAD("kraken", false, CAD, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),

    //TODO confirm addresses
    NOVADAX("novadax", true, BRL, "rE8aDxrPzx5Xqeppy2hgSXKppwNwpyEMbB",
            "rpfFaotCi4acV7HPFQsTfMJ4eE5ky8hucd", "rnKmNMJRFKckYEwaHYLVY9uaz5C313KjEh",
            "rHxtRRUMVAnPZUyRgXhRSyWZ2MrHx8AvVs"),

    COIN_PH("coin.ph", true, PHP,
            "rU2mEJSLqBRkYLVTv55rFTgQajkLTnT6mA", "rPCJsXSrKz2p8frowjJr9ezjCp6Txa5Pr4",
            "rskGqcqqvCz9xQt5TH7NXFV4tsXHntLcEC", "rHSvYuhn95wdr6u7n2yMjL6uaa9gTyaeHu"),

    COINBENE("coinbene", true, BRL, "r9CcrhpV7kMcTu1SosKaY8Pq9g5XiiHLvS"),
    
    FOXBIT("foxbit", true, BRL, "rLkhKQPc2Jrik1j5PSfBqoDDUTq9oX5mz1"),

    BITHUMB("bithumb", true, KRW, "rEtC4xAYJvtwDLJ9jZ4kHRHKbNoLLxSnfb",
            "rPMM1dRp7taeRkbT74Smx2a25kTAHdr4N5",
            "rNTkgxs5WG5mU5Sz26YoDVrHim5Y5ohC7", "r9hUMZBc3MWRc4YdsdZgNCW5Qef8wNSXpb",
            "rD7XQw67JWBXuo2WPX2gZRsGKNsDUGTbx5", "r9LHiNDZvpLoWPoKnbH2JWjFET8zoYT4Y5",
            "rBF3GWchod3QL8TEYicwSeMu6sjXqKFaQ2", "rDFCAUDVB5QhB61ETGT6tf8Rm1zBsj8LHU",
            "rDxfhNRgCDNDckm45zT5ayhKDC4Ljm7UoP", "rsdd66csGZkTzk42NDpJun7PNrGsC7WJHv",
            "rBfsWR2tsk7YJnAvFgzMr37Ww9xaGEBjmA", "rL6KHVDZmaTW7EApSn6tdNXA8pEy2uBXFi",
            "rJ1TAACKgTVue6xZhH6ayAzPWVPMp9rraa", "rDkxzjMX4ZVS5GutPJhhDdLpwMfMcBdmEk",
            "r4fPftbRJK92DmhGtu2kdGL6QzZaM6o1F6", "rsG1sNifXJxGS2nDQ9zHyoe1S5APrtwpjV",
            "rLdFvFyH1BpPargdaUn41srkyNcCgFKKX4", "rET6RAvEq1Bgsvg9GhYJGmSff8EZfmuPor",
            "r9nNfSyc9xxWPe96D6Jq7y4uA99SDA8VyU", "rQ96xxnrs9VuG9gD8GShW4ZYZziSzkcRh",
            "rLdfNmMmaAZS96DH4hAXTz3UvWs4L2JFLM", "rUqy9YvZai8ijf9X84NnGQpjvQ8QbRb8J1",
            "rMa2gKSPbDHanFefkaFabY9Ytk1U4a6hpZ", "rQ3BaZdPRbGa1p91sM8UKpwJ2rkUJDG9Ad",
            "rnArodsZcfbJoJ9WJ4fZEmBsjNX1889Kd7", "rJyEK6JdhEgiTJcvssLAizpq3nceBgV9oZ",
            "rZcBQae9iSJqFYBpNCfxGLXH7xuEzizxR", "rhRNxoEg4e1gpKoKqrSgHx6cP5iWif5zkK"),

    UPBIT("upbit", true, KRW, "rNzT5xopUaJK8L7mHbThUGKaHoct62dNBC",
            "rN9qNpgnBaZwqCg8CvUZRPqCcPPY7wfWep", "rJYQKfsTi8XCLZ1vGSf9CxiL4prz5bvbtz",
            "rBszWJzYpNoqoY4xKuGUpN23b6EBT41ocF"),

    INDEP_RESERVE("independent reserve", true, NZD, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),
    INDEP_RESERVE_AUD("independent reserve", true, AUD, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),
    INDEP_RESERVE_SGD("independent reserve", true, SGD, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),
    INDEP_RESERVE_USD("independent reserve", true, USD, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),


    MERCADO("mercado", true, BRL, "rnW8je5SsuFjkMSWkgfXvqZH3gLTpXxfFH",
            "rHLndqCyNeEKY2PoDmSvUf5hVX5mgUZteB"),

    BRAZILIEX("braziliex", false, BRL, "__UNKNOW__"),


    B2BX_USD("b2bx", true, USD, "rh1Qs81SBNLXzg1hQppVH53FDqfoE9NZFd"),

    B2BX_EUR("b2bx", true, EUR, "rh1Qs81SBNLXzg1hQppVH53FDqfoE9NZFd"),

    CURRENCY_COM("currency.com", true, BYN, "rpUJKigvytgfaM77qC9bNTNQiUZgGemSiB"),

    BITCOIN_TRADE("bitcoin_trade", true, BRL, "r4ZQiz7r4vnM6tAMLu1NhxcDa7TNMdFLGt"),

    LIQUID("liquid", true, JPY, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
            "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

    LIQUID_SGD("liquid", true, SGD, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
            "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

    LIQUID_USD("liquid", true, USD, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
            "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

    LIQUID_EUR("liquid", true, EUR, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
            "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

    COINONE("coinone", true, KRW,
            "rp2diYfVtpbgEMyaoWnuaWgFCAkqCAEg28", "rPsmHDMkheWZvbAkTA8A9bVnUdadPn7XBK",
            "rMksM39efoP4XyAqEjzFUEowwnVbQTh6KW", "rhuCPEoLFYbpbwyhXioSumPKrnfCi3AXJZ"),

    BITREX("bitrex", false, USD, "_TO_DELETE_"),

    EXMO("exmo", false, UAH, "rUocf1ixKzTuEe34kmVhRvGqNCofY1NJzV",
            "rUCjhpLHCcuwL1oyQfzPVeWHsjZHaZS6t2", "rsTv5cJK2EMJhYqUUni4sYBonVk7KqTxZg",
            "rLJPjRYGDVVEjv4VrJtouzqzyJ51YtdZKY"),

    BITTREX("bitrex", true, USD,
            "rPVMhWBsfF9iMXYj3aAzJVkPDTFNSyWdKy", "rEuvmJgjK7PbmiWfvG9DGSWi1GcRcsoxgR"),

    DCEX("dcex", false, USD, "r9W22DnkmktvdSdsdWS5CXJAxfWVRtbDD9",
            "rHXvKUCTzsu2CB8Y5tydaG7B2ABc4CCBYz"),

    BTC_MARKETS("btc_market", true, AUD, "r94JFtstbXmyG21h3RHKcNfkAHxAQ6HSGC",
            "rL3ggCUKaiR1iywkGW6PACbn3Y8g5edWiY", "rU7xJs7QmjbiyxpEozNYUFQxaRD5kueY7z", "rwWZxJQ8R2mvvtaFUJHhF6kfV64atBiPww"),

    BITBANK("bitbank", true, JPY,
            "rLbKbPyuvs4wc1h13BEPHgbFGsRXMeFGL6", "rw7m3CtVHwGSdhFjV4MyJozmZJv3DYQnsA",
            "rwggnsfxvCmDb3YP9Hs1TaGvrPR7ngrn7Z", "r97KeayHuEsDwyU1yPBVtMLLoQr79QcRFe"),

    BITKUB("bitkub", true, THB,
            "rpXTzCuXtjiPDFysxq8uNmtZBe9Xo97JbW", "rDnLSfDv8nG7D7P59QXi78XdnRJ6GHroFJ",
            "rE3Cc3i6163Qzo7oc6avFQAxQE4gyCWhGP", "rHN1mKrGtB5WmQfBjzD9hEbqm6XYh1cBHW"),

    INDODAX("indodax", false, IDR,
            "KUZ3ZFwzgaDGjKBysADByzxvohQ3C", "rDDrTcmnCxeTV1hycGdXiaEynYcU1QnSUg",
            "rB46Pb2mxdCk2zn68MNwZnFQ7Wv2Kjtddr", "rwWr7KUZ3ZFwzgaDGjKBysADByzxvohQ3C",
            "rwned6pcnAcJLZENGwWNMM7ccQsuqsfqqQ"),

    QUIDAX("quidax", false, NGN,
            "rMuC8SpD8GP5a1uXma2jZyHVY5wxeCK7bV", "rnNQs4WAKUHes7kJtqqRiU3Wq9q1pHuDEt");


    private String name;
    private Currency localFiat;
    private String[] addresses;
    private boolean confirmed;

    Exchange(String name, boolean confirmed, Currency localFiat,
             String... addresses) {
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

    public static List<Currency> currencies(String address) {
        return Arrays.stream(Exchange.values())
                .filter(exchange -> Arrays.asList(exchange.addresses).contains(address))
                .map(Exchange::getLocalFiat)
                .collect(Collectors.toList());

    }

    public static List<Currency> currencies(Exchange exchange) {
        String adr = exchange.addresses[0];
        return currencies(adr);
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

    public boolean isConfirmed() {
        return confirmed;
    }
}
