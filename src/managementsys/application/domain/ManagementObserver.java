package managementsys.application.domain;

public interface ManagementObserver {
    public void update();
    public boolean message(String s, boolean confirm);
}
