package managementsys.application.domain;

public class Movie {

    private String name;
    private int year;
    private int duration;

    public Movie(String n, int y, int d) {
        name = n;
        year = y;
        duration = d;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getDuration() {
        return duration;
    }
}
