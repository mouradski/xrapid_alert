package space.xrapid.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.xrapid.domain.GlobalStats;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
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

        sb.append("\uD83D\uDD25 New #ODL Daily All Time High Volume: ")
            .append(NumberFormat.getCurrencyInstance(Locale.US).format(usdValue))
            .append("\n\n")
            .append("https://utility-scan.com")
            .append("\n\n")
            .append("#XRP #XRPCommunity #ATH");
        try {
            twitter.updateStatus(sb.toString());
            Thread.sleep(30000);
        } catch (Exception e) {
            log.error("Unable to tweet new ATH", e);
        }
    }

public void dailySummary(GlobalStats globalStats) {
        StringBuilder sb = new StringBuilder();

        sb.append("#ODL daily summary\n\n");

        Map<String, Double> lastDayStats = globalStats.getVolumePerCorridor().entrySet().
                stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .findFirst().get().getValue();

        int corridorNbr = lastDayStats.size();

        sb.append(String.format("TOP %s Corridors : \n\n", corridorNbr));

        double lastDayVolume = lastDayStats.entrySet().stream()
                .mapToDouble(e -> e.getValue())
                .sum();

        if (lastDayVolume == globalStats.getDailyAth()) {
            newAth(lastDayVolume);
        }

        lastDayStats.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(corridorNbr > 5 ? 5 : corridorNbr).forEach(volumePerCorridor -> {

            sb.append("From ").append(volumePerCorridor.getKey().split("-")[0])
                    .append(" ").append("TO ").append(volumePerCorridor.getKey().split("-")[1])
                    .append(" :  ").append(NumberFormat.getCurrencyInstance(Locale.US).format(volumePerCorridor.getValue()).replaceAll("\\.[0-9]{2}", ""))
                    .append("\n");
        });

        sb.append("\n").append("ODL daily volume :  ").append(NumberFormat.getCurrencyInstance(Locale.US)
                .format(lastDayVolume).replaceAll("\\.[0-9]{2}", ""))
                .append("\n\n");

        sb.append("Last Daily ATH :  ").append(NumberFormat.getCurrencyInstance(Locale.US).format(globalStats.getDailyAth()).replaceAll("\\.[0-9]{2}", ""));

        sb.append("\n\nutility-scan.com");

        if (sb.toString().length() <= 260) {
            sb.append("\n\n").append("#XRP #XRPCommunity");
        }

        try {
            twitter.updateStatus(sb.toString());
        } catch (Exception e) {
            log.error("Unable to tweet summary", e);
        }
    }
}
