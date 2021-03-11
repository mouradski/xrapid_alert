package space.xrapid.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static space.xrapid.domain.Currency.*;
import static space.xrapid.domain.Currency.SGD;

public enum Exchange {

  BITSTAMP("bitstamp", true, USD, true,
          "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
          "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
          "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
          "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
          "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD"),

  BITSTAMP_EUR("bitstamp", true, EUR, false,
          "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
          "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
          "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
          "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
          "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD"),

  BITSTAMP_GBP("bitstamp", true, GBP, false,
          "rrpNnNLKrartuEqfJGpqyDwPj1AFPg9vn1",
          "rGFuMiw48HdbnrUbkRYuitXTmfrDBNTCnX",
          "rDsbeomae4FXwgQTJp9Rs64Qg9vDiTCdBv",
          "rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B",
          "rUobSiUpYH2S97Mgb4E7b7HuzQj2uzZ3aD"),

  BITSO("bitso", true, MXN, true,
          "rG6FZ31hDHN1K5Dkbma3PSB5uVCuVVRzfn",
          "rHZaDC6tsGN2JWGeXhjKL6664RNCq5hu4B",
          "raXLsnnJVaLMDixEoXHXe56WQXKczbD8ub",
          "rGfGdVYLDSbji5mqfMvdpx4c8JyyfqVFgf",
          "rfEu1Wnr7LxStoFx8DBdzgr8M16FBUbH3K",
          "rLSn6Z3T8uCxbcd1oxwfGQN1Fdn5CyGujK"),

  BITFINEX("bitfinex", true, USD, true, "rLW9gnQo7BQhU6igk5keqYnH3TVrCxGRzm",
          "rE3hWEGquaixF2XwirNbA1ds4m55LxNZPk"),
  BTC_TURK("btcturk", true, TRY, true, "rNEygqkMv4Vnj8M2eWnYT1TDnV1Sc1X5SN",
          "rGmmsmyspyPsuBT4L5pLvAqTjxYqaFq4U5"),

  BX_IN("bx.in.th", false, THB, false, "rp7Fq2NQVRJxQJvUZ4o8ZzsTSocvgYoBbs"),

  SBI_TRADE("sbi_trade", true, JPY, false, "rDDyH5nfvozKZQCwiBrWfcE528sWsBPWET",
          "rKcVYzVK1f4PhRFjLhWP7QmteG5FpPgRub"),

  CEX_IO("cex.io", false, USD, false, "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1"),
  CEX_IO_EUR("cex.io", false, EUR, false, "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1"),
  CEX_IO_GBP("cex.io", true, GBP, false, "rE1sdh25BJQ3qFwngiTBwaq3zPGGYcrjp1"),

  LUNO("luno", true, ZAR, false, "rsRy14FvipgqudiGmptJBhr1RtpsgfzKMM",
          "rsbfd5ZYWqy6XXf6hndPbRjDAzfmWc1CeQ"),


  COINFIELD_USD("coinfield", false, USD, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
  COINFIELD_GBP("coinfield", false, GBP, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
  COINFIELD_AED("coinfield", true, AED, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
  COINFIELD_JPY("coinfield", false, JPY, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
  COINFIELD_CAD("coinfield", false, CAD, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),
  COINFIELD_EUR("coinfield", false, EUR, false, "rK7D3QnTrYdkp1fGKKzHFNXZpqN8dUCfaf"),


  BINANCE_RUB("binance", false, RUB, false, "rEb8TK3gBgk5auZkwc6sHnwrGVJH8DuaLh",
          "rJb5KsHsDHF1YS5B5DU6QCkH5NsPaKQTcy", "rEy8TFcrAPvhpKrwyrscNYyqBGUkE9hKaJ"),
  BINANCE_US("binanceus", true, USD, false, "rEeEWeP88cpKUddKk37B2EZeiHBGiBXY3",
          "rMvYS27SYs5dXdFsUgpvv1CSrPsCz7ePF5"),

  WAZIRX("Wazirx", false, INR, false, "rwuAm7XdcP3SBwgJrVthCvCzU7kETJUUit",
          "rJXcrnAS8XoBwjvd5VrShrLMY8buPuiuC5"),

  KRAKEN_USD("kraken", true, USD, false, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
  KRAKEN_EUR("kraken", true, EUR, false, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
  KRAKEN_JPY("kraken", true, JPY, false, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),
  KRAKEN_CAD("kraken", true, CAD, false, "rLHzPsX6oXkzU2qL12kHCH8G8cnZv1rBJh"),

  //TODO confirm addresses
  NOVADAX("novadax", true, BRL, true, "rE8aDxrPzx5Xqeppy2hgSXKppwNwpyEMbB",
          "rpfFaotCi4acV7HPFQsTfMJ4eE5ky8hucd", "rnKmNMJRFKckYEwaHYLVY9uaz5C313KjEh",
          "rHxtRRUMVAnPZUyRgXhRSyWZ2MrHx8AvVs"),

  COIN_PH("coin.ph", true, PHP, false, "rU2mEJSLqBRkYLVTv55rFTgQajkLTnT6mA"),

  COINBENE("coinbene", true, BRL, false, "r9CcrhpV7kMcTu1SosKaY8Pq9g5XiiHLvS"),

  BITHUMB("bithumb", true, KRW, true, "rEtC4xAYJvtwDLJ9jZ4kHRHKbNoLLxSnfb",
          "rPMM1dRp7taeRkbT74Smx2a25kTAHdr4N5",
          "rNTkgxs5WG5mU5Sz26YoDVrHim5Y5ohC7", "r9hUMZBc3MWRc4YdsdZgNCW5Qef8wNSXpb",
          "rD7XQw67JWBXuo2WPX2gZRsGKNsDUGTbx5", "r9LHiNDZvpLoWPoKnbH2JWjFET8zoYT4Y5",
          "rBF3GWchod3QL8TEYicwSeMu6sjXqKFaQ2", "rDFCAUDVB5QhB61ETGT6tf8Rm1zBsj8LHU",
          "rDxfhNRgCDNDckm45zT5ayhKDC4Ljm7UoP", "rsdd66csGZkTzk42NDpJun7PNrGsC7WJHv"),

  UPBIT("upbit", true, KRW, true, "rNzT5xopUaJK8L7mHbThUGKaHoct62dNBC",
          "rN9qNpgnBaZwqCg8CvUZRPqCcPPY7wfWep", "rJYQKfsTi8XCLZ1vGSf9CxiL4prz5bvbtz",
          "rBszWJzYpNoqoY4xKuGUpN23b6EBT41ocF"),

  INDEP_RESERVE("independent reserve", true, NZD, false, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),

  INDEP_RESERVE_AUD("independent reserve", false, AUD, false, "r33hypJXDs47LVpmvta7hMW9pR8DYeBtkW"),

  MERCADO("mercado", true, BRL, false, "rnW8je5SsuFjkMSWkgfXvqZH3gLTpXxfFH",
          "rHLndqCyNeEKY2PoDmSvUf5hVX5mgUZteB"),

  BRAZILIEX("braziliex", false, BRL, false,
          "__UNKNOW__"),


  CURRENCY_COM("currency.com", true, BYN, false, "rpUJKigvytgfaM77qC9bNTNQiUZgGemSiB"),

  BITCOIN_TRADE("bitcoin_trade", true, BRL, false, "r4ZQiz7r4vnM6tAMLu1NhxcDa7TNMdFLGt"),

  LIQUID("liquid", true, JPY, true, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
          "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

  LIQUID_SGD("liquid", true, SGD, true, "rHQ6kEtVUPk6mK9XKnjRoudenoHzJ8ZL9p",
          "rMbWmirwEtRr7pNmhN4d4ysTMBvBxdvovs", "rENMoQvSHtb8sZwsxfefSGNZ7RQ89pd93H"),

  COINONE("coinone", true, KRW, true, "rp2diYfVtpbgEMyaoWnuaWgFCAkqCAEg28",
          "rPsmHDMkheWZvbAkTA8A9bVnUdadPn7XBK"),

  BITREX("bitrex", false, USD, false,
          "_TO_DELETE_"),

  EXMO("exmo", false, UAH, false, "rUocf1ixKzTuEe34kmVhRvGqNCofY1NJzV",
          "rUCjhpLHCcuwL1oyQfzPVeWHsjZHaZS6t2", "rsTv5cJK2EMJhYqUUni4sYBonVk7KqTxZg",
          "rLJPjRYGDVVEjv4VrJtouzqzyJ51YtdZKY"),

  BITTREX("bitrex", true, USD, false,
          "rPVMhWBsfF9iMXYj3aAzJVkPDTFNSyWdKy"),

  DCEX("dcex", false, USD, false, "r9W22DnkmktvdSdsdWS5CXJAxfWVRtbDD9",
          "rHXvKUCTzsu2CB8Y5tydaG7B2ABc4CCBYz"),

  BTC_MARKETS("btc_market", true, AUD, true, "r94JFtstbXmyG21h3RHKcNfkAHxAQ6HSGC",
          "rL3ggCUKaiR1iywkGW6PACbn3Y8g5edWiY", "rU7xJs7QmjbiyxpEozNYUFQxaRD5kueY7z"),

  BITBANK("bitbank", true, JPY, true, "rLbKbPyuvs4wc1h13BEPHgbFGsRXMeFGL6",
          "rw7m3CtVHwGSdhFjV4MyJozmZJv3DYQnsA", "rwggnsfxvCmDb3YP9Hs1TaGvrPR7ngrn7Z"),

  BITKUB("bitkub", true, THB, false, "rpXTzCuXtjiPDFysxq8uNmtZBe9Xo97JbW"),

  INDODAX("indodax", false, IDR, true, "KUZ3ZFwzgaDGjKBysADByzxvohQ3C",
          "rDDrTcmnCxeTV1hycGdXiaEynYcU1QnSUg", "rB46Pb2mxdCk2zn68MNwZnFQ7Wv2Kjtddr"),

  QUIDAX("quidax", false, NGN, true, "rMuC8SpD8GP5a1uXma2jZyHVY5wxeCK7bV",
          "rnNQs4WAKUHes7kJtqqRiU3Wq9q1pHuDEt");


  private String name;
  private Currency localFiat;
  private String[] addresses;
  private boolean confirmed;
  private boolean maxTolerence;

  Exchange(String name, boolean confirmed, Currency localFiat, boolean maxTolerence,
           String... addresses) {
    this.name = name;
    this.maxTolerence = maxTolerence;
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

  public boolean isMaxTolerence() {
    return maxTolerence;
  }
}
