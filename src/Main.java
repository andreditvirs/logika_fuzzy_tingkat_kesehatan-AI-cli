import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Fuzzification f = new Fuzzification();
        Scanner inp = new Scanner(System.in);
        float t = inp.nextFloat();
        float b = inp.nextFloat();
        RulesEvaluation rE = new RulesEvaluation(f.getListTinggi(t), f.getListBerat(b), t, b);
        Defuzzification d = new Defuzzification(f.getListTinggi(t), f.getListBerat(b), rE.getTabelKaidah(), rE.getNamaTabelKadiah());
        System.out.println(d.getNamaFuzzyDecisionIndexAtas()+"---"+d.getNilaiFuzzyDecisionIndexAtas()
                +":"+d.getNilaiFuzzyDecisionIndexBawah()+"---"+d.getNamaFuzzyDecisionIndexBawah());
    }


}
