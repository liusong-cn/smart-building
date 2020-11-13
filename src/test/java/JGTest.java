import com.SmartApplication;
import com.bz.service.JGAirDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:ls
 * @date: 2020/11/12 15:34
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartApplication.class)
public class JGTest {

    @Autowired
    private JGAirDataService airDataService;

    @Test
    public void run(){
        airDataService.httpAirData();
    }
}
