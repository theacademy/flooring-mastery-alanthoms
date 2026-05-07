import flooring.controller.FlooringController;
import flooring.dao.OrderDao;
import flooring.dao.OrderDaoFileImpl;
import flooring.service.FlooringServiceLayer;
import flooring.service.FlooringServiceLayerImpl;
import flooring.view.FlooringView;
import flooring.view.UserIO;
import flooring.view.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {
        UserIO io = new UserIOConsoleImpl();
        FlooringView myView = new FlooringView(io);
        OrderDao orderDao = new OrderDaoFileImpl();
        FlooringServiceLayer myService = new FlooringServiceLayerImpl(orderDao);

        FlooringController controller = new FlooringController(myService, myView);
        controller.run();
    }
}
