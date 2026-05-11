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

    public  FlooringController( FlooringServiceLayer service, FlooringView view) {
        this.service = service;
        this.view = view;
    }




    int menuSelection = 0;
    public void run() {
        boolean keepGoing = true;
        while (keepGoing) {
            try {
                menuSelection = getMenuSelection();

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
                        io.print("EXPORT ALL DATA");
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        io.print("UNKNOWN COMMAND");
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

            } catch (OrderDaoDataValidationException | OrderDaoDuplicateIdException e){

                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);

    }

    private void listOrders() throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        view.displayDisplayAllBanner();
        String date = view.printOptionAndGetDate();
        List<Order> orderList = service.getAllOrders(date);
        view.displayOrderList(orderList);
        view.displaygetAllSuccessBanner();
    }

    private void editOrder() throws OrderDaoPersistenceException, OrderDaoDataValidationException, OrderDaoDuplicateIdException {

        view.displayEditOrderBanner();
        String date = view.printOptionAndGetDate();
        int orderNo = view.getOrderNo();
        Order editOrder = null;

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

        view.displayTaxList(service.getAllTaxes());
        String newState = view.editState(editOrder.getState());

        view.displayProductList(service.getAllProducts());
        String newProductType = view.editProductType(editOrder.getProductType());

        BigDecimal newArea = view.editArea(editOrder.getArea());

        //helper method, if values are null, leave previous value
        editOrder = service.applyEdits(editOrder, newName, newState, newProductType, newArea);


        Order preppedOrder = service.prepareOrder(date, editOrder);

        service.removeOrder(date, orderNo);

        //edit works by removing old order and adding new one

        view.displayOrder(preppedOrder);

        boolean confirm = view.confirmAdd();
        if (confirm) {
            service.addOrder(date, preppedOrder);
            view.displayEditSuccessBanner();
        } else {
            view.displayCancelBanner();
        }

    }

    /**
    private void viewOrder() throws ClassRosterPersistenceException {
        view.displayDisplayStudentBanner();
        String studentId = view.getStudentIdChoice();
        Student student = service.getOrder(studentId);
        view.displayStudent(student);
    }*/

    private void removeOrder() throws OrderDaoPersistenceException, OrderDaoDataValidationException {
        view.displayRemoveBanner();
        String date = view.printOptionAndGetDate();
        int orderNo = view.getOrderNo();
        view.displayOrder(service.getOrder(date, orderNo));
        boolean confirm =  view.confirmRemove();
        if (confirm) {
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
}