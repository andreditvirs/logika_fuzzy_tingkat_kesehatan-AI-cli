import java.util.ArrayList;

public class Defuzzification {
    Data data = new Data();

    float[] nilaiHimpunanF = new float[4];
    String[] namaHimpunanF = new String[4];

    private float nilaiMaxMethod;
    private String namaMaxMethod;
    private double crispDecisionIndex;
    private double nilaiFuzzyDecisionIndexBawah;
    private double nilaiFuzzyDecisionIndexAtas;
    private String namaFuzzyDecisionIndexBawah;
    private String namaFuzzyDecisionIndexAtas;

    public Defuzzification(ArrayList<Tinggi> listTinggi, ArrayList<Berat> listBerat, float[][] tabel, String[][] namaTabel){
        int indeksPertama = listBerat.get(0).getId()+1;
        int indeksKedua = listBerat.get(1).getId()+1;
        int indeksKetiga = listTinggi.get(0).getId()+1;
        int indeksKeempat = listTinggi.get(1).getId()+1;

        // Perbedaan letak nilai dan nama himpunan, tapi tidak masalah jadi f={AS, SS, TS, S}={0.7, 0.2, 0.3, 0.2}
        nilaiHimpunanF[0] = tabel[indeksKetiga][indeksPertama];
        nilaiHimpunanF[1] = tabel[indeksKetiga][indeksKedua];
        nilaiHimpunanF[2] = tabel[indeksKeempat][indeksPertama];
        nilaiHimpunanF[3] = tabel[indeksKeempat][indeksKedua];

        namaHimpunanF[0] = namaTabel[indeksKetiga][indeksPertama];
        namaHimpunanF[1] = namaTabel[indeksKetiga][indeksKedua];
        namaHimpunanF[2] = namaTabel[indeksKeempat][indeksPertama];
        namaHimpunanF[3] = namaTabel[indeksKeempat][indeksKedua];

        cariMaxMethod();
        cariCentroidMethod();
    }

    public void cariMaxMethod(){
        float tempNilai=0;
        String tempNama = "";
        for(int i = 0; i < 4; i++){
            if(nilaiHimpunanF[i]>=tempNilai){
                tempNilai = nilaiHimpunanF[i];
                tempNama = namaHimpunanF[i];
            }
        }
        nilaiMaxMethod = tempNilai;
        namaMaxMethod = tempNama;
    }

    public void cariCentroidMethod(){
        double[] iS = data.getIndexSehat();

        crispDecisionIndex = (nilaiHimpunanF[0]*iS[urutanDariHimpunanNama(namaHimpunanF[0])]
                + nilaiHimpunanF[1]*iS[urutanDariHimpunanNama(namaHimpunanF[1])]
                + nilaiHimpunanF[2]*iS[urutanDariHimpunanNama(namaHimpunanF[2])]
                + nilaiHimpunanF[3]*iS[urutanDariHimpunanNama(namaHimpunanF[3])])
                /
                (nilaiHimpunanF[0]+nilaiHimpunanF[1]+nilaiHimpunanF[2]+nilaiHimpunanF[3]);
        hitungFuzzyDecisionIndex();
    }

    public double[] hitungFuzzyDecisionIndex(){
        double[] hasil = new double[2]; // indeks array peratama untuk batas bawah, indeks array kedua untuk batas atas
        double[] iS = data.getIndexSehat();
        String[] jS = data.getJenisSehat();

        if(crispDecisionIndex <= iS[0] && crispDecisionIndex > iS[1]){ // INGAT! dari 0,8 indeks Sangat Sehat
            hasil = hitungNilaiFuzzyDecisionIndex(iS[1], iS[0]);
            namaFuzzyDecisionIndexBawah = jS[1];
            namaFuzzyDecisionIndexAtas = jS[0];
        }else if(crispDecisionIndex <= iS[1] && crispDecisionIndex > iS[2]){
            hasil = hitungNilaiFuzzyDecisionIndex(iS[2], iS[1]);
            namaFuzzyDecisionIndexBawah = jS[2];
            namaFuzzyDecisionIndexAtas = jS[1];
        }else if(crispDecisionIndex <= iS[2] && crispDecisionIndex > iS[3]){
            hasil = hitungNilaiFuzzyDecisionIndex(iS[3], iS[2]);
            namaFuzzyDecisionIndexBawah = jS[3];
            namaFuzzyDecisionIndexAtas = jS[2];
        }
        nilaiFuzzyDecisionIndexBawah = hasil[0]*100;
        nilaiFuzzyDecisionIndexAtas = hasil[1]*100;
        return hasil;
    }

    public double[] hitungNilaiFuzzyDecisionIndex(double batasBawah, double batasAtas){
        double hasilBawah = (crispDecisionIndex-batasBawah)/(batasAtas-batasBawah);
        double hasilAtas = (batasAtas- crispDecisionIndex)/(batasAtas-batasBawah);
        double[] hasil = {hasilBawah, hasilAtas};
        return hasil;
    }

    public int urutanDariHimpunanNama(String nama){
            if(nama.equals("SS")){
                return 0;
            }else if(nama.equals("S")){
                return 1;
            }else if(nama.equals("AS")){
                return 2;
            }else if(nama.equals("TS")){
                return 3;
            }
        return 4;
    }

    public float getNilaiMaxMethod(){
        return this.nilaiMaxMethod;
    }

    public String getNamaMaxMethod(){
        return this.namaMaxMethod;
    }

    public double getNilaiFuzzyDecisionIndexBawah() {
        return nilaiFuzzyDecisionIndexBawah;
    }

    public double getNilaiFuzzyDecisionIndexAtas() {
        return nilaiFuzzyDecisionIndexAtas;
    }

    public double getCrispDecisionIndex() {
        return crispDecisionIndex;
    }

    public String getNamaFuzzyDecisionIndexBawah() {
        return decodeNamaFuzzyDecisionIndex(namaFuzzyDecisionIndexBawah);
    }

    public String getNamaFuzzyDecisionIndexAtas() {
        return decodeNamaFuzzyDecisionIndex(namaFuzzyDecisionIndexAtas);
    }

    private String decodeNamaFuzzyDecisionIndex(String nama){
        if(nama.equals("SS")){
            return "Sangat sehat";
        }else if(nama.equals("S")){
            return "Sehat";
        }else if(nama.equals("AS")){
            return "Agak sehat";
        }else if(nama.equals("TS")){
            return "Tidak sehat";
        }
        return "Tidak terkategori";
    }
}
