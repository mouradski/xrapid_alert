package space.xrapid.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.xrapid.domain.GlobalStats;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class TwitterService {

    @Value("${twitter.consumer.key}")
    private String consumerKey;
    @Value("${twitter.consumer.secret}")
    private String consumerSecret;
    @Value("${twitter.access.token.key}")
    private String accessToken;
    @Value("${twitter.access.token.secret}")
    private String accessTokenSecret;

    private Twitter twitter;

    @PostConstruct
    public void init() throws TwitterException {
        this.twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        AccessToken oauthAccessToken = new AccessToken(accessToken, accessTokenSecret);
        twitter.setOAuthAccessToken(oauthAccessToken);
    }

    public void newAth(Double usdValue) {
        StringBuilder sb = new StringBuilder();

        sb.append("New On-Demand-Liquidity Daily All Time High volume : ")
                .append(NumberFormat.getCurrencyInstance(Locale.US).format(usdValue))
                .append("\n\n")
                .append("https://utility-scan.com");
        try {
            twitter.updateStatus(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    #ODL daily summary  :
From USD TO MXN :  $9.233.333
From USD TO PHP :  $5.233.33
From AUD TO USD:
From AUD to PHP:
#ODL daily vol : $15.322.204
Last ATH :  $16,791,000.88
     */

    public void dailySummary(GlobalStats globalStats) {
        StringBuilder sb = new StringBuilder();


        try {
            twitter.updateStatus(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
