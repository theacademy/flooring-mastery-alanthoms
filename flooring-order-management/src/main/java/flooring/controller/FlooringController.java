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

    private void addOrder() throws OrderDaoPersistenceException{
        view.displayAddOrderBanner();
        String date = view.printOptionAndGetDate();
        boolean hasErrors = false;
        do {
            Order currentOrder = view.getNewOrderInfo();
            try {
                Order preppedOrder = service.prepareOrder(date, currentOrder);
                service.addOrder(date, preppedOrder);
                view.displayAddOrderBanner();
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
        } catch (OrderDaoPersistenceException e) {
            view.displayOrderNotFound();
            view.displayErrorMessage(e.getMessage());
        }

        String newName = view.editCustomerName(editOrder.getCustomerName());



        String newState = view.editState(editOrder.getState());



        String newProductType = view.editProductType(editOrder.getProductType());



        BigDecimal newArea = view.editArea(editOrder.getArea());


        editOrder = service.applyEdits(editOrder, newName, newState, newProductType, newArea);

        service.removeOrder(date, orderNo);
        Order preppedOrder = service.prepareOrder(date, editOrder);
        service.addOrder(date, preppedOrder);

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
        service.removeOrder(date, orderNo);
        view.displayRemoveSuccessBanner();
    }
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
}