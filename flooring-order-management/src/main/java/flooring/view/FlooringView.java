package flooring.view;

public class FlooringView {
    private UserIO io = new UserIOConsoleImpl();

    public int printMenuAndGetSelection(){

        io.print("Main Menu");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order ");
        io.print("4. Remove an Order");
        io.print("5. Export all Data ");
        io.print("6. Exit");

        return io.readInt("Please select from the"
                        + " above choices.", 1, 6);
    }
}
