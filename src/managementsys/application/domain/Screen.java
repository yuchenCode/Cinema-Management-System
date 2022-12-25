package managementsys.application.domain;

public class Screen {
    private int number;
    private String screenName;
    private int capacity;

    public Screen(int no, String s, int c) {
        number = no;
        screenName = s;
        capacity = c;
    }

    public int getNumber(){
        return number;
    }

    public String getScreenName() {
        return screenName;
    }

    public int getCapacity() {
        return capacity;
    }
}
