package main;

import controllers.NavigationController;

public class MainApp {

    public static void main(String[] args) {
        // All of the views are started here
        MainFrame start = new MainFrame();

        SalsaModel model = new SalsaModel();
        SalsaController control1 = new NavigationController(model, "navigation", start);
    }
}
