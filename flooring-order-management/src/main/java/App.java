import flooring.controller.FlooringController;
import flooring.dao.*;
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
        TaxDao taxDao = new TaxDaoFileImpl();
        ProductDao productDao = new ProductDaoFileImpl();
        ExportDao exportDao = new ExportDaoFileImpl();

        FlooringServiceLayer myService = new FlooringServiceLayerImpl(orderDao,  productDao, taxDao, exportDao);

        FlooringController controller = new FlooringController(myService, myView);
        controller.run();
    }
}
