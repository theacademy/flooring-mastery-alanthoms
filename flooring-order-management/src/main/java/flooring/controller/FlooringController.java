package flooring.controller;


import flooring.dao.OrderDao;
import flooring.dao.OrderDaoFileImpl;
import flooring.dto.Order;
import flooring.view.FlooringView;
import flooring.view.UserIO;
import flooring.view.UserIOConsoleImpl;

public class FlooringController {


    private FlooringView view = new FlooringView();
    private UserIO io = new UserIOConsoleImpl();
    private OrderDao orderDao = new OrderDaoFileImpl();




    int menuSelection = 0;
    public void run() {
        boolean keepGoing = true;
        while (keepGoing) {

            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    io.print("DISPLAY ORDER");
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    io.print("EDIT ORDER");
                    break;
                case 4:
                    io.print("REMOVE ORDER");
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

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void addOrder(){
        view.displayAddOrderBanner();
        Order newOrder = view.getNewOrderInfo();
        orderDao.addOrder(newOrder.getOrderNumber(), newOrder);
        view.displayAddOrderSuccessBanner();
    }
}