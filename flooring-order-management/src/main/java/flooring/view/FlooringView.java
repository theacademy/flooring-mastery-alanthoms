package flooring.view;

import flooring.dto.Order;
import flooring.dto.Product;
import flooring.dto.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlooringView {

    private UserIO io;

    public FlooringView(UserIO io) {
        this.io = io;
    }

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

    public String getCustomerName() {
        return io.readString("Please enter Customer Name");
    }
    public String getState() {
        return io.readString("Please enter State (abbreviation)");
    }
    public String getProductType() {
        return io.readString("Please enter Product Type");
    }

    public BigDecimal getArea() {
        return new BigDecimal(io.readString("Please enter Area"));
    }



    public void displayAddOrderBanner() {
        io.print("=== ADD ORDER ===");
    }
    public void displayAddOrderSuccessBanner() {
        io.readString(
                "Order successfully added.  Please hit enter to continue");
    }



    public void displayOrderList(List<Order> orders) {
        for (Order order : orders) {
            io.print(order.toString());
        }
    }

    public void displayOrder(Order order) {
        io.print(order.toString());
    }

    public void displayProductList(List<Product> products) {
        for (Product product : products) {
            io.print(product.toString());
        }
    }

    public void displayTaxList(List<Tax> taxes) {
        for (Tax tax : taxes) {
            io.print(tax.toString());
        }
    }


    public void displayDisplayAllBanner() {
        io.print("=== DISPLAY ALL ORDERS ===");
    }

    public void displayRemoveBanner() {
        io.print("=== REMOVE AN ORDER ===");
    }

    public void displayEditOrderBanner() {
        io.print("=== EDIT ORDER ===");
    }


    public void displayRemoveSuccessBanner() {
        io.readString(
                "Order successfully REMOVED.  Please hit enter to continue");
    }
    public void displaygetAllSuccessBanner() {
        io.readString(
                "All Orders for date printed.  Please hit enter to continue");
    }

    public void displayEditSuccessBanner() {
        io.readString("Order successfully EDITED.  Please hit enter to continue");
    }





    public int getOrderNo() {
        return io.readInt("Please enter the order no..");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
        io.readString("Press enter to continue");
    }

    public String printOptionAndGetDate() {
        return io.readString("Please enter the date you would like in MM/dd/yyyy format");
    }

    public String editCustomerName(String currentName) {
        return io.readString(
                "Enter customer name (" + currentName + "): "
        );
    }

    public String editProductType(String currentProductType) {
        return io.readString(
                "Enter product type  (" + currentProductType + "): "
        );
    }

    public String editState(String currentState) {
        return io.readString(
                "Enter state  (" + currentState + "): "
        );
    }

    public BigDecimal editArea(BigDecimal currentArea) {
        return new BigDecimal( io.readString(
                "Enter state  (" + currentArea + "): "
        )
        );
    }




    public void displayOrderNotFound() {
        io.print("Order not found");
    }

    public boolean confirmAdd() {

        String answer = io.readString(
                "Place this order? (Y/N): ");

        return answer.equalsIgnoreCase("Y");
    }




}
