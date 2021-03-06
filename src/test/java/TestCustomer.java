import com.service365.common.SelectDomain;
import com.service365.common.SelectDriver;
import com.service365.customerPage.*;
import com.service365.customerPage.MyOrderPage;
import com.service365.customerPage.OrderDetailPage;
import com.service365.customerPage.PlaceOrderPage;
import com.service365.customerPage.ServicePage;
import com.service365.readData.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.*;


public class TestCustomer {
    public Properties properties;
    WebDriver webDriver;
    HomePage homePage;
    RegisterPage registerPage;
    LoginPage loginPage;
    MePage mePage;
    EditProfilePage editProfilePage;
    ChangePasswordPage changePasswordPage;
    EditAddressPage editAddressPage;
    MyAddressPage myAddressPage;
    MyOrderPage myOrderPage;
    OrderDetailPage orderDetailPage;
    PlaceOrderPage placeOrderPage;
    ServicePage servicePage;
    SelectDomain selectDomain;
    SelectDriver selectDriver;

    @BeforeTest(groups = "basic")
    @Parameters("environment")
    public void setup(String environment) {
        System.out.println(environment);
        selectDomain = new SelectDomain();
        properties = selectDomain.selectDomain(environment);
    }

    @BeforeMethod()
    public void setHomePage() {
        selectDriver = new SelectDriver();
        webDriver = selectDriver.selectDriver("chrome");
        webDriver.get(properties.getProperty("homePageURL"));
        homePage = new HomePage(webDriver);
    }

    @AfterMethod()
    public void closeDriver() {
        webDriver.close();
    }

    @Test(priority = 1, groups = "register")
    public void testCustomerRegister() throws InterruptedException {
        homePage.clickRegister();
        registerPage = new RegisterPage(webDriver);
        registerPage.registerService365("3109272895@qq.com", "123456", "123456", true, true);
        Thread.sleep(5000);
//        测试是否进入home页面
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));
        mePage.myPageStatus();
        Reporter.log("customer register case pass");
    }

    @Test(priority = 1, groups = "login")
    public void testLoginPage() throws InterruptedException {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
//        测试是否进入mePage页面
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));
        System.out.println("customer login case pass");
    }


    @Test(priority = 2, groups = "changePicture")
    public void testChangeProfilePicture() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        String currentPicture = mePage.imageCheck();
        mePage.changePicture();
        String laterPicture = mePage.imageCheck();
        Assert.assertNotEquals(laterPicture, currentPicture);
    }

    @Test(priority = 3, groups = "editProfile")
    public void testEditProfile() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickEditProfile();
        editProfilePage = new EditProfilePage(webDriver);
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("editProfileURL"));
        editProfilePage.editProfile("chen", "99999", "Male", "I pretty!");
        Assert.assertEquals(mePage.nickNameCheck(), "chen");
        Assert.assertEquals(mePage.contactNumberCheck(), "99999");
        Assert.assertEquals(mePage.instructionCheck(), "I pretty!");
        Reporter.log("customer editProfile case pass");
        System.out.println("customer editProfile case pass");
    }

    @Test(priority = 4, groups = "changePassword", dataProvider = "dataProvider")
    public void testChangePassword(HashMap<String, String> data) {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickChangePassword();
        changePasswordPage = new ChangePasswordPage(webDriver);
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("changePasswordURL"));
        changePasswordPage.changePassword(data.get("Current password"), data.get("New password"), data.get("Confirm password"));
//        changePasswordPage.changePassword("123456q","123456","123456");
        webDriver.get(properties.getProperty("loginPageURL"));
        loginPage.loginToService365("hechenjuner@gmail.com", data.get("New password"));
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("mePageURL"));
        Reporter.log("customer changePassword case pass");
    }

    @Test(priority = 5, groups = "addAddress")
    public void testAddAddress() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickAddress();
        myAddressPage = new MyAddressPage(webDriver);
        myAddressPage.clickAddNewButton();
        editAddressPage = new EditAddressPage(webDriver);
        editAddressPage.addNewAddress("101", "rode", "csse", "chrischurch", "123", "I don't know");
        Assert.assertEquals(webDriver.getCurrentUrl(), properties.getProperty("myAddressURL"));
        myAddressPage.newAddressCheck("101", "rode", "csse", "chrischurch", "123", "I don't know");
        Reporter.log("customer addAddress case pass");
        System.out.println("customer addAddress case pass");

    }


    @DataProvider(name = "dataProvider")
    public Object[][] getDataFromDataprovider() {
        ReadExcelFile readExcelFile = new ReadExcelFile();
        String filePath = "data";
        Object[][] o = null;
        try {
            o = readExcelFile.readExcel(filePath, "asdf.xlsx", "asdf");
        } catch (IOException e) {

        }
        return o;

    }

    @Test(priority = 7, groups = "findPassword")
    public void testFindPassword() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.clickRecoverPassword();
        List listA;

        listA = webDriver.findElements(By.tagName("a"));
        Reporter.log("There are " + listA.size() + " links in this page!");
        Iterator iteratorA = listA.iterator();
        while (iteratorA.hasNext()) {
            WebElement tagA = (WebElement) iteratorA.next();
            String url = tagA.getAttribute("href");
            if (url == null || url.equals(null) || url.equals("")) {
                Reporter.log("current url is \"\"");
            } else {
                Reporter.log("current url " + url);
            }


        }

    }

    @Test(priority = 2, groups = "order")
    public void testBookService() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickService();
        servicePage = new ServicePage(webDriver);
        servicePage.editFilterList("hair");
        List list = servicePage.getBookNowButtons();
        String url = (String) list.get((int) (Math.random() * list.size()));
        webDriver.get(url);
        placeOrderPage = new PlaceOrderPage(webDriver);
        placeOrderPage.setAllElement();
        while (webDriver.getCurrentUrl().equals(url)) {
            placeOrderPage.checkPlaceOrderUrl();
        }
        orderDetailPage = new OrderDetailPage(webDriver);
//        检查各项信息是否正确
        orderDetailPage.orderStatus();
        Reporter.log("customers book service case pass");
        System.out.println("customers book service case pass");
    }

    @Test()
    public void testLeaveOrderMessage() {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickOrder();
        myOrderPage = new MyOrderPage(webDriver);
        myOrderPage.clickRandomOrder();
        orderDetailPage = new OrderDetailPage(webDriver);
        orderDetailPage.setMessageInput("123");
        orderDetailPage.clickMessageSubmit();
//        order顺序应该是倒序，case未完
    }

    @Test(priority = 2, groups = "cancelOrder")
    public void testCancelOrder() throws NoAlertPresentException {
        homePage.clickLogin();
        loginPage = new LoginPage(webDriver);
        loginPage.loginToService365("hechenjuner@gmail.com", "123456");
        mePage = new MePage(webDriver);
        mePage.clickOrder();
        myOrderPage = new MyOrderPage(webDriver);
        myOrderPage.clickRandomOrder();
        orderDetailPage = new OrderDetailPage(webDriver);
        orderDetailPage.clickcCancelButton();
        orderDetailPage.acceptAlert();
        Assert.assertEquals(orderDetailPage.orderStatus4(), "Order cancelled");
        Assert.assertEquals(orderDetailPage.orderWaiting(), "cancelled");
        Reporter.log("customer cancel order case pass");
        System.out.println("customer cancel order case pass");

    }

    @AfterTest()
    public void quit() {
        webDriver.quit();
    }
}
