package flooring.controller;


import flooring.dto.Order;
import flooring.service.FlooringServiceLayer;
import flooring.service.OrderDaoDataValidationException;
import flooring.service.OrderDaoDuplicateIdException;
import flooring.service.OrderDaoPersistenceException;
import flooring.view.FlooringView;
import flooring.view.UserIO;
import flooring.view.UserIOConsoleImpl;

import java.math.BigDecimal;
import java.util.List;

public class FlooringController {


    private UserIO io = new UserIOConsoleImpl();

    private FlooringView view;
    private FlooringServiceLayer service;

    //injection with constructor
    public  FlooringController( FlooringServiceLayer service, FlooringView view) {

        this.service = service;
        this.view = view;

    }


    int menuSelection = 0;

    public void run() {

        //loop for menu
        boolean keepGoing = true;

        while (keepGoing) {

            try {

                // get menuSelection with input validation
                menuSelection = getMenuSelection();
                //switch case to handle choices
                switch (menuSelection) {
                    case 1:
                        listOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportOrders();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        view.displayUnknownCommandBanner();
                }

            } catch (OrderDaoPersistenceException | OrderDaoDataValidationException | OrderDaoDuplicateIdException e) {
                view.displayErrorMessage(e.getMessage());
            }

        }
        view.displayExitBanner();
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void addOrder() throws OrderDaoPersistenceException, OrderDaoDataValidationException {

        boolean hasErrors = false;
        do {

            view.displayAddOrderBanner();

            String date = view.printOptionAndGetDate();

            String customerName = view.getCustomerName();

            //display tax & state info
            view.displayTaxList(service.getAllTaxes());

            String state = view.getState();

            //display product info
            view.displayProductList(service.getAllProducts());

            String productType = view.getProductType();

            BigDecimal area = view.getArea();

            //generate orderNo from orders in file
            int orderNo = service.getNextOrderNumber(date);

            //create Order object and set values
            Order currentOrder = new Order(orderNo);

            currentOrder.setCustomerName(customerName);
            currentOrder.setState(state);
            currentOrder.setProductType(productType);
            currentOrder.setArea(area);

            try {
                //check if orderNo already exists
                service.orderNoCheck(date, currentOrder);
            } catch (OrderDaoDuplicateIdException e) {
                view.displayErrorMessage(e.getMessage());
                return;
            }


            try {
                //calculate into full preppedOrder
                Order preppedOrder = service.prepareOrder(date, currentOrder);
                //display full order
                view.displayOrder(preppedOrder);
                //user confirmation
                boolean confirm = view.confirmAdd();
                if (confirm) {
                    //if yes then add order
                    service.addOrder(date, preppedOrder);
                    view.displayAddOrderSuccessBanner();

                }
                else {
                    view.displayCancelBanner();
                }
                hasErrors = false;

                //catch errors and display appropriate error message
            } catch (OrderDaoDataValidationException | OrderDaoDuplicateIdException e){

                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

    }


    private void listOrders() throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        view.displayDisplayAllBanner();
        //Prompt user for date with validation
        String date = view.printOptionAndGetDate();
        List<Order> orderList = service.getAllOrders(date);
        view.displayOrderList(orderList);
        view.displaygetAllSuccessBanner();
    }

    private void editOrder() throws OrderDaoPersistenceException, OrderDaoDataValidationException, OrderDaoDuplicateIdException {


        view.displayEditOrderBanner();

        //Prompt user for date with validation
        String date = view.printOptionAndGetDate();
        int orderNo = view.getOrderNo();
        Order editOrder = null;

        //try and find order otherwise return not found message
        try {
            editOrder = service.getOrder(date, orderNo);
            view.displayOrder(editOrder);

        } catch (OrderDaoPersistenceException e) {
            view.displayOrderNotFound();
            view.displayErrorMessage(e.getMessage());
            return;
        }

        //View prints old value and asks for new one
        String newName = view.editCustomerName(editOrder.getCustomerName());

        //display all taxes to help user
        view.displayTaxList(service.getAllTaxes());
        String newState = view.editState(editOrder.getState());

        //display all products to help user
        view.displayProductList(service.getAllProducts());
        String newProductType = view.editProductType(editOrder.getProductType());

        BigDecimal newArea = view.editArea(editOrder.getArea());

        //helper method, if values are null, leave previous value
        editOrder = service.applyEdits(editOrder, newName, newState, newProductType, newArea);

        //calculate remaining values from product and taxes file
        Order preppedOrder = service.prepareOrder(date, editOrder);

        //edit works by removing old order and adding new one
        //inefficient should have been changed to use put
        //nevermind has been changed to use editOrder
        view.displayOrder(preppedOrder);

        boolean confirm = view.confirmAdd();
        if (confirm) {

            service.editOrder(date, orderNo, preppedOrder);
            view.displayEditSuccessBanner();
        } else {
            view.displayCancelBanner();
        }

    }


    private void removeOrder() throws OrderDaoPersistenceException, OrderDaoDataValidationException {

        view.displayRemoveBanner();

        String date = view.printOptionAndGetDate();
        int orderNo = view.getOrderNo();
        view.displayOrder(service.getOrder(date, orderNo));

        boolean confirm =  view.confirmRemove();

        if (confirm) {
            //calls remove order from service to simply remove
            service.removeOrder(date, orderNo);
            view.displayRemoveSuccessBanner();
        }
        else {
            view.displayCancelBanner();
        }
    }
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exportOrders() throws OrderDaoPersistenceException {
        view.displayExportOrdersBanner();
        //calls export all orders function
        service.exportAllOrders();
        view.displayExportSuccessBanner();

    }
}