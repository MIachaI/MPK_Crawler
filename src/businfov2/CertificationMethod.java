package businfov2;

public enum CertificationMethod {
    LEED_v4(false),
    LEED_2009(true),
    BREEAM(false);

    private boolean implemented;
    CertificationMethod(boolean implemented){
        this.implemented = implemented;
    }
    public boolean isImplemented(){
        return this.implemented;
    }
    public String toString(){
        switch(this){
            case LEED_v4:
                return "LEED v4";
            case LEED_2009:
                return "LEED 2009";
            case BREEAM:
                return "BREEAM";
        }
        return null;
    }
}

/*
żeby linia była liczona,
to muszą być z danego przystanku przejazdy w obydwie strony,
 a wtedy liczy się tak jak mówisz mniejszą liczbę kursów,
 i to rozpatruje się osobno dla pon-pt (trzeba zliczyć dzień, w który jest najmniej kursów)
 i weekendów (trzeba zliczyć średnią liczbę kursów na dzień),
 i to wszystko to jest LEED v4;
 w LEED 2009 liczy się całkowitą liczbę kursów w obydwie strony;
  a w BREEAM liczy się jakoś w wyznaczonych dniach i godzinach
 */