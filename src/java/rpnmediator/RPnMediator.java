package rpnmediator;

public class RPnMediator {

    private static RPnMediator instance_= null;

    protected void RPnMediator() {

    }

    public void sendCommand(String commandDesc) {

        System.out.println("Message sent with COMMAND : " + commandDesc);
    }

    public static RPnMediator instance() {

        if (instance_ == null) return new RPnMediator();
        else return instance_;
    }
}
