package flooring.controller;


import flooring.view.UserIO;
import flooring.view.UserIOConsoleImpl;

public class FlooringController {

    private UserIO io = new UserIOConsoleImpl();

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        while (keepGoing) {
            io.print("Main Menu");
            io.print("1. Display Orders");
            io.print("2. Add an Order");
            io.print("3. Edit an Order ");
            io.print("4. Remove an Order");
            io.print("5. Export all Data ");
            io.print("6. Exit");

            menuSelection = io.readInt("Please select from the"
                    + " above choices.", 1, 6);

            switch (menuSelection) {
                case 1:
                    io.print("DISPALU ORDER");
                    break;
                case 2:
                    io.print("ADD ORDER");
                    break;
                case 3:
                    io.print("EDIT ORDER");
                    break;
                case 4:
                    io.print("REMOVE ORDERT");
                    break;
                case 5:
                    io.print("EXPORT ALL DATA");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    io.print("UNKNOWN COMMAND");
            }

        }
        io.print("GOOD BYE");
    }
}