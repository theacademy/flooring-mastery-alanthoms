package flooring.controller;


import flooring.dao.OrderDao;
import flooring.dao.OrderDaoFileImpl;
import flooring.dto.Order;
import flooring.service.FlooringServiceLayer;
import flooring.service.OrderDaoDataValidationException;
import flooring.service.OrderDaoDuplicateIdException;
import flooring.service.OrderDaoPersistenceException;
import flooring.view.FlooringView;
import flooring.view.UserIO;
import flooring.view.UserIOConsoleImpl;

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
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        listOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        io.print("EDIT ORDER");
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

            }
            io.print("GOOD BYE");
        } catch (OrderDaoPersistenceException e) {

            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void addOrder() throws OrderDaoPersistenceException{
        view.displayAddOrderBanner();
        boolean hasErrors = false;
        do {
            Order currentOrder = view.getNewOrderInfo();
            try {
                service.addOrder(currentOrder);
                view.displayAddOrderBanner();
                hasErrors = false;
            } catch (OrderDaoDataValidationException | OrderDaoDuplicateIdException e){
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);


    }

    private void listOrders() throws OrderDaoPersistenceException {
        view.displayDisplayAllBanner();
        List<Order> orderList = service.getAllOrders();
        view.displayOrderList(orderList);
    }

    /**
    private void viewOrder() throws ClassRosterPersistenceException {
        view.displayDisplayStudentBanner();
        String studentId = view.getStudentIdChoice();
        Student student = service.getOrder(studentId);
        view.displayStudent(student);
    }*/

    private void removeOrder() throws OrderDaoPersistenceException {
        view.displayRemoveBanner();
        int orderNo = view.getOrderNo();
        service.removeOrder(orderNo);
        view.displayRemoveSuccessBanner();
    }
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
}