package space.xrapid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    public static List<String> combinations(List<String> motList, int Longeur) {
        List<String> Resultat = new ArrayList<String>();
        for (int i = 0; i < motList.size(); i++) {
            if (Longeur == 1)
                Resultat.add(motList.get(i));
            else {

                try  {
                    List<String> ListIntermediaire = motList.subList(i + 1, motList.size() - (i + 1));
                    List<String> CombiList = combinations(ListIntermediaire, Longeur - 1);
                    for (String s : CombiList) {
                        Resultat.add(motList.get(i) + s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return Resultat;
    }
}
