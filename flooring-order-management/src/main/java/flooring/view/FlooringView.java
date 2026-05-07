package flooring.view;

import flooring.dto.Order;

import java.math.BigDecimal;

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

    public Order getNewOrderInfo() {

        int orderNumber = io.readInt("Please enter Order Number");

        String customerName = io.readString("Please enter Customer Name");

        String state = io.readString("Please enter State (abbreviation)");

        String productType = io.readString("Please enter Product Type");

        BigDecimal area = new BigDecimal(
                io.readString("Please enter Area")
        );

        Order order = new Order(orderNumber);

        order.setCustomerName(customerName);
        order.setState(state);
        order.setProductType(productType);
        order.setArea(area);

        return order;
    }


    public void displayAddOrderBanner() {
        io.print("=== ADD ORDER ===");
    }
    public void displayAddOrderSuccessBanner() {
        io.readString(
                "Order successfully added.  Please hit enter to continue");
    }

}
