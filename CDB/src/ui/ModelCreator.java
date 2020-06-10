package ui;

@Deprecated
public class ModelCreator {
    private static final ModelCreator INSTANCE = new ModelCreator();

    public ModelCreator getInstance() {
        return INSTANCE;
    }

    private static final String resetNullMsg =
            "wrong input. Go back to menu (M)?" + " Use a null value (N)?"
                    + " Try again (anything else)?";

    private static final String resetMsg =
            "wrong input. Go back to menu (M)? " + "Try again (anything else)?";

}
